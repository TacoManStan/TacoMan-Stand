package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * <p>Provides tools for traversing a {@code matrix} (2D Array).</p>
 *
 * @param <T> The type of element in the {@code matrix}.
 */
public abstract class MatrixIterator<T>
        implements Springable, Lockable {
    
    private final StrictSpringable springable;
    private final Lock lock;
    
    private final ReadOnlyObjectWrapper<T[][]> matrixProperty;
    private final ProgressIndicator[] progressIndicators;
    
    private int iX;
    private int iY;
    private final ReadOnlyBooleanWrapper completeProperty;
    
    private boolean isShutdown;
    
    public MatrixIterator(@NotNull Springable springable, @Nullable Lock lock, @NotNull Object... params) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        Arrays.stream(params).map(o -> "Param " + ArrayTools.indexOf(o, params) + ": " + o).forEach(msg -> debugger().print(msg));
        
        this.progressIndicators = Arrays.stream(params)
                                        .filter(param -> param instanceof ProgressIndicator)
                                        .map(param -> (ProgressIndicator) param)
                                        .toArray(value -> new ProgressIndicator[value]);
        
        this.completeProperty = new ReadOnlyBooleanWrapper(false);
        this.completeProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                onComplete();
        });
        
        construct(params);
        this.matrixProperty = new ReadOnlyObjectWrapper<>();
        
        this.isShutdown = false;
        
        reset();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<T[][]> matrixProperty() {
        return matrixProperty.getReadOnlyProperty();
    }
    
    public final T[][] getMatrix() {
        return matrixProperty.get();
    }
    
    
    public final ReadOnlyBooleanProperty completeProperty() {
        return completeProperty.getReadOnlyProperty();
    }
    
    public final boolean isComplete() {
        return completeProperty.get();
    }
    
    private void setComplete(boolean newValue) {
        completeProperty.set(newValue);
    }
    
    
    public final boolean isShutdown() {
        return isShutdown;
    }
    
    //</editor-fold>
    
    public final int getWidth() {
        return getMatrix().length;
    }
    
    public final int getHeight() {
        return getMatrix()[0].length;
    }
    
    public final long getWorkTotal() {
        return (long) getWidth() * (long) getHeight();
    }
    
    public final long getWorkProgress() {
        return (long) iX + ((long) iY * (long) getWidth());
    }
    
    public final double getWorkPercent() {
        return (double) getWorkProgress() / (double) getWorkTotal();
    }
    
    //<editor-fold desc="--- ABSTRACT METHODS ---">
    
    protected abstract void construct(@NotNull Object... params);
    
    protected abstract @NotNull T[][] newMatrix();
    
    protected abstract T step(int i, int j);
    
    protected abstract void onComplete();
    
    protected abstract IteratorTask newTask();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Lock getLock() {
        return lock;
    }
    
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void next() {
        getMatrix()[iX][iY] = step(iX, iY);
        increment();
    }
    
    // Helper method that iterates the values of iX and iY
    private void increment() {
        iX++;
        if (iX == getWidth()) {
            iX = 0;
            iY++;
        }
        if (iY == getHeight())
            setComplete(true);
    }
    
    /**
     * <p>Resets the values of this {@link MatrixIterator} to default values</p>
     * <p>Required for reusable {@link MatrixIterator iterators}.</p>
     */
    private void reset() {
        iX = 0;
        iY = 0;
        setComplete(false);
        matrixProperty.set(newMatrix());
    }
    
    private void updateProgressOverlays(@NotNull Consumer<ProgressIndicator> action) {
        FXTools.runFX(() -> Arrays.stream(progressIndicators).forEach(action), true);
    }
    
    //</editor-fold>
    
    public void run() {
        sync(() -> {
            if (worker != null) {
                debugger().print("Cancelling Worker...");
                worker.cancel(false);
            }
            
            reset();
            worker = newTask();
            
            updateProgressOverlays(progressIndicator -> progressIndicator.progressProperty().bind(worker.progressProperty()));
            
            worker.onPreTask();
            new Thread(worker).start();
            worker.onPostTask();
        });
    }
    
    public final void shutdown() {
        sync(() -> {
            if (isShutdown)
                debugger().print(Debugger.WARN, "MatrixIterator has already been shut down!");
            isShutdown = true;
            
            preShutdown();
            if (worker != null) {
                debugger().print("Cancelling Mandelbrot Worker Task");
                if (worker.cancel(true))
                    debugger().print("Worker Cancellation Successful!");
                else
                    debugger().print(Debugger.WARN, "Worker Cancellation Failed!");
            }
            postShutdown();
        });
    }
    
    protected void preShutdown() { }
    
    protected void postShutdown() { }
    
    
    private IteratorTask worker;
    
    public abstract class IteratorTask extends Task<Void> {
        
        //<editor-fold desc="--- IMPLEMENTATIONS ---">
        
        @Override
        protected Void call() {
            if (isCancelled())
                return null;
            
            onTaskStart();
            updateProgressOverlays(progressIndicator -> progressIndicator.setVisible(true));
            
            
            while (!isComplete()) {
                if (isCancelled())
                    return null;
                next();
                if (getWorkProgress() % 10 == 0)
                    updateProgress(getWorkProgress(), getWorkTotal());
                if (isCancelled())
                    return null;
            }
            
            FXTools.runFX(() -> sync(() -> {
                updateProgressOverlays(progressIndicator -> progressIndicator.setVisible(false));
                onTaskEnd(getMatrix());
            }), true);
            
            return null;
        }
        
        
        @Override
        protected void succeeded() {
            onSucceeded();
            worker = null;
        }
        
        @Override
        protected void cancelled() {
            onCancelled();
            worker = null;
        }
        
        @Override
        protected void failed() {
            onFailed();
            worker = null;
        }
        
        //</editor-fold>
        
        //<editor-fold desc="--- ABSTRACT METHODS ---">
        
        protected abstract void onPreTask();
        
        protected abstract void onPostTask();
        
        protected abstract void onTaskStart();
        
        protected abstract void onTaskEnd(T[][] result);
        
        
        protected abstract void onSucceeded();
        
        protected abstract void onCancelled();
        
        protected abstract void onFailed();
        
        //</editor-fold>
    }
}
