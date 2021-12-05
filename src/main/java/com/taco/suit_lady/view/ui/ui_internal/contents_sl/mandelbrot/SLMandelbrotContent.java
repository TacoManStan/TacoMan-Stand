package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.MandelbrotIterator.MandelbrotColor;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.SLMandelbrotContentController.MouseDragData;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class SLMandelbrotContent extends SLContent<SLMandelbrotContentData, SLMandelbrotContentController>
{
    private final ReentrantLock lock;
    
    private Task<Void> worker;
    private MandelbrotDimensions dimensions; // Used to keep track of dimensions for zoom support
    
    public SLMandelbrotContent(@NotNull Springable springable)
    {
        super(springable);
        
        this.lock = new ReentrantLock();
        
        this.worker = null;
        this.dimensions = MandelbrotDimensions.newDefaultInstance(getController().canvas().getWidth(), getController().canvas().getHeight());
        
        getController().setDragConsumer(dragData -> zoom(dragData));
        getController().canvas().setCanvasListener(this::refreshCanvas);
    }
    
    private void refreshCanvas(BoundCanvas source, double newWidth, double newHeight)
    {
        FXTools.get().runFX(() -> {
            if (worker != null)
                worker.cancel(false);
            
            dimensions.resizeTo(newWidth, newHeight);
            final MandelbrotIterator iterator = new MandelbrotIterator(new MandelbrotColor[(int) newWidth][(int) newHeight], dimensions, lock);
            worker = new Task<>()
            {
                @Override
                protected Void call()
                {
                    FXTools.get().runFX(() -> getController().getProgressBar().setVisible(true), true);
                    while (!iterator.isComplete()) {
                        iterator.next();
                        if (iterator.getWorkProgress() % 10 == 0)
                            updateProgress(iterator.getWorkProgress(), iterator.getWorkTotal());
                        if (isCancelled())
                            return null;
                    }
                    //                ConsoleBB.CONSOLE.print("Redrawing...");
                    redraw(iterator.getResult());
                    //                getController().getProgressBar().progressProperty().unbind();
                    return null;
                }
            };
            getController().getProgressBar().progressProperty().bind(worker.progressProperty());
            new Thread(worker).start();
        }, true);
    }
    
    private void redraw(MandelbrotColor[][] colors)
    {
        FXTools.get().runFX(() -> {
            getController().getProgressBar().setVisible(false);
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
    
    private void zoom(MouseDragData dragData)
    {
        if (!dragData.isValid())
            throw ExceptionTools.ex("Drag Data is Invalid!");
        
        System.out.println("Zooming...  " + dragData);
        
        dimensions.zoomTo(dragData.getStartX(), dragData.getStartY(), dragData.getEndX(), dragData.getEndY());
        // Use width and height values from dimensions variable instead of canvas?
        refreshCanvas(getController().canvas(), getController().canvas().getWidth(), getController().canvas().getHeight());
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
