package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.util.tools.TaskTools;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.components.RectanglePaintCommand;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.MandelbrotIterator.MandelbrotColor;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.SLMandelbrotContentController.MouseDragData;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class SLMandelbrotContent extends SLContent<SLMandelbrotContentData, SLMandelbrotContentController>
{
    private final ReentrantLock lock;
    
    private Task<Void> worker;
    private final MandelbrotDimensions dimensions; // This object is passed to every MandelbrotIterator as they are created
    private final ReadOnlyBooleanWrapper isGeneratingProperty;
    
    private final RectanglePaintCommand selectionBoxPaintCommand;
    
    private MandelbrotPage coverPage;
    
    public SLMandelbrotContent(@NotNull Springable springable)
    {
        super(springable);
        
        this.lock = new ReentrantLock();
        
        injectBookshelf("Mandelbrot Demo", new UIBook(
                weaver(), ctx(),
                "Mandelbrot Demo",
                "mandelbrot",
                uiBook -> TB.resources().get(
                        "pages",
                        uiBook.getUID(uiBook.getButtonID()),
                        () -> coverPage = new MandelbrotPage(uiBook, this)),
                null));
        
        this.worker = null;
        this.dimensions = MandelbrotDimensions.newDefaultInstance(getController().canvas().getWidth(), getController().canvas().getHeight());
        this.isGeneratingProperty = new ReadOnlyBooleanWrapper(false);
        
        getController().canvas().setCanvasListener(this::refreshCanvas);
        
        this.selectionBoxPaintCommand = new RectanglePaintCommand(false, lock);
        ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas().addPaintCommand(selectionBoxPaintCommand);
        
        getController().setDragConsumer(dragData -> zoom(dragData));
        getController().setMoveConsumer(dragData -> updateZoomBox(dragData));
    }
    
    public final ReadOnlyBooleanProperty isGeneratingProperty()
    {
        return isGeneratingProperty.getReadOnlyProperty();
    }
    
    public final boolean isGenerating()
    {
        return isGeneratingProperty.get();
    }
    
    protected final void setIsGenerating(boolean isGenerating)
    {
        isGeneratingProperty.set(isGenerating);
    }
    
    protected MandelbrotPage getCoverPage()
    {
        return coverPage;
    }
    
    private void refreshCanvas(BoundCanvas source, double newWidth, double newHeight)
    {
        FXTools.get().runFX(() -> TaskTools.sync(lock, () -> {
            FXTools.get().clearCanvasUnsafe(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas());
            
            if (worker != null)
                worker.cancel(false);
            
            dimensions.resizeTo(newWidth, newHeight);
            final MandelbrotIterator iterator = new MandelbrotIterator(new MandelbrotColor[(int) newWidth][(int) newHeight], dimensions, lock);
            worker = new Task<>()
            {
                @Override
                protected Void call()
                {
                    FXTools.get().runFX(() -> getCoverPage().getController().getProgressBar().setVisible(true), true);
                    while (!iterator.isComplete()) {
                        iterator.next();
                        if (iterator.getWorkProgress() % 10 == 0)
                            updateProgress(iterator.getWorkProgress(), iterator.getWorkTotal());
                        if (isCancelled())
                            return null;
                    }
                    redraw(iterator.getResult());
                    return null;
                }
            };
            getCoverPage().getController().getProgressBar().progressProperty().bind(worker.progressProperty());
            new Thread(worker).start(); // Use executor instead?
        }), true);
    }
    
    private void redraw(MandelbrotColor[][] colors)
    {
        FXTools.get().runFX(() -> TaskTools.sync(lock, () -> {
            getCoverPage().getController().getProgressBar().setVisible(false);
            for (int i = 0; i < colors.length; i++)
                for (int j = 0; j < colors[i].length; j++) {
                    final MandelbrotColor mandelbrotColor = colors[i][j];
                    final Color color = mandelbrotColor != null ? mandelbrotColor.getColor() : Color.BLACK;
                    getController().canvas().getGraphicsContext2D().getPixelWriter().setColor(i, j, color);
                }
        }), true);
    }
    
    private void zoom(MouseDragData dragData)
    {
        if (!dragData.isValid())
            throw ExceptionTools.ex("Drag Data is Invalid!");
        selectionBoxPaintCommand.setActive(false);
        dimensions.zoomTo(dragData.getStartX(), dragData.getStartY(), dragData.getEndX(), dragData.getEndY());
        refreshCanvas(getController().canvas(), getController().canvas().getWidth(), getController().canvas().getHeight());
    }
    
    private void updateZoomBox(MouseDragData moveData)
    {
        TaskTools.sync(lock, () -> {
            selectionBoxPaintCommand.setActive(true);
            selectionBoxPaintCommand.setRectangle(moveData.getAsPaintable());
        });
        //        final AppUI ui = ctx().getBean(AppUI.class);
        //        FXTools.get().drawRectangle(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas(), moveData.getAsPaintable(), true, false);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull SLMandelbrotContentData loadData()
    {
        return new SLMandelbrotContentData();
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
