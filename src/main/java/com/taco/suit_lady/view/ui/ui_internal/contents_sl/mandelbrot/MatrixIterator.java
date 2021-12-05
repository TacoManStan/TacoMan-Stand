package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.tools.ExceptionTools;

import java.util.concurrent.locks.ReentrantLock;

public abstract class MatrixIterator<T>
{
    private final ReentrantLock lock;
    
    private final int width;
    private final int height;
    private final Object[][] values;
    private final boolean isTyped;
    
    private int iX;
    private int iY;
    
    private boolean complete;
    
    public MatrixIterator(T[][] targetArray, ReentrantLock lock)
    {
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.width = targetArray.length;
        this.height = targetArray[0].length;
        this.values = targetArray;
        
        this.isTyped = true;
        
        this.init();
    }
    
    public MatrixIterator(int width, int height, ReentrantLock lock)
    {
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.width = width;
        this.height = height;
        this.values = new Object[this.width][this.height];
        
        this.isTyped = false;
        
        this.init();
    }
    
    private void init()
    {
        this.iX = 0;
        this.iY = 0;
        
        this.complete = false;
    }
    
    public final void next()
    {
        lock.lock();
        try {
            values[iX][iY] = step(iX, iY);
            increment();
        } finally {
            lock.unlock();
        }
    }
    
    // Helper method that iterates the values of iX and iY
    private void increment()
    {
        iX++;
        if (iX == width) {
            iX = 0;
            iY++;
        }
        if (iY == height)
            complete();
    }
    
    //
    
    public final T[][] getResult()
    {
        if (!isTyped)
            throw ExceptionTools.ex("Matrix Iterator is not typed! Use getResultGeneric() instead.");
        return (T[][]) values;
    }
    
    public final Object[][] getResultGeneric()
    {
        return values;
    }
    
    public final int getWidth()
    {
        return width;
    }
    
    public final int getHeight()
    {
        return height;
    }
    
    private void complete()
    {
        complete = true;
    }
    
    public final boolean isComplete()
    {
        return complete;
    }
    
    public final long getWorkTotal()
    {
        return (long) getWidth() * (long) getHeight();
    }
    
    public final long getWorkProgress()
    {
        return (long) iX + ((long) iY * (long) getWidth());
    }
    
    public final double getWorkPercent()
    {
        return (double) getWorkProgress() / (double) getWorkTotal();
    }
    
    //
    
    protected abstract T step(int i, int j);
}
