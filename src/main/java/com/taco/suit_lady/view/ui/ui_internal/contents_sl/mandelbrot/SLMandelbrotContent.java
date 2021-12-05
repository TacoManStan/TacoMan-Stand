package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.MandelbrotIterator.MandelbrotColor;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class SLMandelbrotContent extends SLContent<SLMandelbrotContentData, SLMandelbrotContentController>
{
    private final ReentrantLock lock;
    
    private Task<Void> worker;
    
    public SLMandelbrotContent(@NotNull Springable springable)
    {
        super(springable);
        
        this.lock = new ReentrantLock();
        
        this.worker = null;
        getController().canvas().setCanvasListener(this::resetCanvas);
    }
    
    private void resetCanvas(BoundCanvas source, double newWidth, double newHeight)
    {
        lock.lock();
        try {
            if (worker != null)
                worker.cancel(false);
    
            getData().widthProperty().set((int) newWidth);
            getData().heightProperty().set((int) newHeight);
    
            getData().reset();
    
            final MandelbrotIterator iterator = new MandelbrotIterator(new MandelbrotColor[(int) newWidth][(int) newHeight], 1.2, lock);
            worker = new Task<>()
            {
                @Override
                protected Void call()
                {
                    while (!iterator.isComplete()) {
                        iterator.next();
                        if (isCancelled())
                            return null;
                    }
                    redraw(iterator.getResult());
                    return null;
                }
            };
            new Thread(worker).start();
        } finally {
            lock.unlock();
        }
    }
    
    private void redraw(MandelbrotColor[][] colors) {
        FXTools.get().runFX(() -> {
            for (int i = 0; i < colors.length; i++)
                for (int j = 0; j < colors[i].length; j++) {
                    final MandelbrotColor mandelbrotColor = colors[i][j];
                    final Color color = mandelbrotColor != null ? mandelbrotColor.getColor() : Color.BLACK;
                    final int i2 = i;
                    final int j2 = j;
                    getController().canvas().getGraphicsContext2D().getPixelWriter().setColor(i2, j2, color);
                }
        }, true);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull SLMandelbrotContentData loadData()
    {
        return new SLMandelbrotContentData(-1, -1, 1.2);
    }
    
    @Override
    protected @NotNull Class<SLMandelbrotContentController> controllerDefinition()
    {
        return SLMandelbrotContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}
