package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>Provides tools for traversing a {@code matrix} (2D Array).</p>
 *
 * @param <T> The type of element in the {@code matrix}.
 */
public abstract class MatrixIterator<T>
        implements Springable, Lockable {
    
    private final StrictSpringable springable;
    private final Lock lock;
    
    private final T[][] values;
    private final ProgressIndicator[] progressIndicators;
    
    private int iX;
    private int iY;
    private boolean complete;
    
    public MatrixIterator(@NotNull Springable springable, @Nullable Lock lock, @NotNull Object... params) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.progressIndicators = Arrays.stream(params)
                                        .filter(param -> param instanceof ProgressIndicator)
                                        .map(param -> (ProgressIndicator) param)
                                        .toArray(value -> new ProgressIndicator[value]);
        
        System.out.println("Printing Indicators");
        for (ProgressIndicator indicator: progressIndicators)
            System.out.println("Indicator: " + indicator);
        this.values = initMatrix(params);
    }
    
    public final void next() {
        sync(() -> {
            values[iX][iY] = step(iX, iY);
            increment();
        });
    }
    
    // Helper method that iterates the values of iX and iY
    private void increment() {
        iX++;
        if (iX == getWidth()) {
            iX = 0;
            iY++;
        }
        if (iY == getHeight())
            complete();
    }
    
    
    public final T[][] getResult() {
        return values;
    }
    
    public final int getWidth() {
        return values.length;
    }
    
    public final int getHeight() {
        return values[0].length;
    }
    
    private void complete() {
        complete = true;
        onComplete();
    }
    
    public final boolean isComplete() {
        return complete;
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
    
    protected abstract @NotNull T[][] initMatrix(@NotNull Object... params);
    
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
    
    
    public void runTask() {
        sync(() -> {
            if (worker != null) {
                debugger().print("Cancelling Worker...");
                worker.cancel(false);
            }
            worker = newTask();
            
            updateProgressOverlays(progressIndicator -> progressIndicator.progressProperty().bind(worker.progressProperty()));
            
            worker.onPreTask();
            new Thread(worker).start();
            worker.onPostTask();
        });
    }
    
    private void updateProgressOverlays(@NotNull Consumer<ProgressIndicator> action) {
        FXTools.runFX(() -> Arrays.stream(progressIndicators).forEach(action), true);
    }
    
    private IteratorTask worker;
    
    public abstract class IteratorTask extends Task<Void> {
        
        //<editor-fold desc="--- IMPLEMENTATIONS ---">
        
        @Override
        protected Void call() {
            onTaskStart();
            updateProgressOverlays(progressIndicator -> progressIndicator.setVisible(true));
            
            while (!isComplete()) {
                next();
                if (getWorkProgress() % 10 == 0)
                    updateProgress(getWorkProgress(), getWorkTotal());
                if (isCancelled())
                    return null;
            }
            
            updateProgressOverlays(progressIndicator -> progressIndicator.setVisible(false));
            onTaskEnd(getResult());
            
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
