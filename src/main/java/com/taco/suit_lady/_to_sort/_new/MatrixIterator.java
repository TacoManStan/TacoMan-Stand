package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    
    //
    
    private int iX;
    private int iY;
    
    private boolean complete;
    
    public MatrixIterator(@NotNull Springable springable, @Nullable Lock lock, @NotNull Object... params) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
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
    
    protected abstract T step(int i, int j);
    
    protected abstract void onComplete();
    
    protected abstract @NotNull T[][] initMatrix(@NotNull Object... params);
    
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
}
