package com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.util.tools.TaskTools;
import com.taco.suit_lady.view.ui.painting.SLEllipsePaintCommand;
import com.taco.suit_lady.view.ui.painting.SLImagePaintCommand;
import com.taco.suit_lady.view.ui.painting.SLRectanglePaintCommand;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.view.ui.jfx.components.RectanglePaintCommand;
import com.taco.suit_lady.view.ui.AppUI;
import com.taco.suit_lady.view.ui.Content;
import com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot.MandelbrotIterator.MandelbrotColor;
import com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot.MandelbrotContentController.MouseDragData;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class MandelbrotContent extends Content<MandelbrotContentData, MandelbrotContentController> {
    private final ReentrantLock lock;
    
    private Task<Void> worker;
    private final MandelbrotDimensions dimensions; // This object is passed to every MandelbrotIterator as they are created
    private final ReadOnlyBooleanWrapper isGeneratingProperty;
    
    private RectanglePaintCommand selectionBoxPaintCommandOld;
    private final SLRectanglePaintCommand selectionBoxPaintCommand;
    private final SLImagePaintCommand selectionBoxPaintCommand2;
    private final SLEllipsePaintCommand selectionCirclePaintCommand;
    
    private MandelbrotPage coverPage;
    
    public MandelbrotContent(@NotNull Springable springable) {
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
        getCoverPage().getController().getRegenerateButton().setOnAction(event -> regenerate(event));
        
        this.selectionBoxPaintCommand = new SLRectanglePaintCommand(
                lock, this, "selection-box",
                null, 1,
                null, Color.BLACK);
        this.selectionBoxPaintCommand2 = new SLImagePaintCommand(
                lock, this, "selection-box2",
                null, 3);
        this.selectionCirclePaintCommand = new SLEllipsePaintCommand(
                lock, this, "selection-circle",
                null, 2);
        
        this.selectionBoxPaintCommand.deactivate();
        this.selectionBoxPaintCommand2.deactivate();
        this.selectionCirclePaintCommand.deactivate();
        
        
        getOverlayHandler().getOverlay("default").addPaintCommand(selectionBoxPaintCommand);
//        getOverlayHandler().getOverlay("default").addPaintCommand(selectionBoxPaintCommand2);
//        getOverlayHandler().getOverlay("default").addPaintCommand(selectionCirclePaintCommand);
        
        //        this.selectionBoxPaintCommand = new RectanglePaintCommand(false, lock);
        //        ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas().addPaintCommand(selectionBoxPaintCommand);
        
        getController().setDragConsumer(dragData -> zoom(dragData));
        getController().setMoveConsumer(dragData -> updateZoomBox(dragData));
    }
    
    public final ReadOnlyBooleanProperty isGeneratingProperty() {
        return isGeneratingProperty.getReadOnlyProperty();
    }
    
    public final boolean isGenerating() {
        return isGeneratingProperty.get();
    }
    
    protected final void setIsGenerating(boolean isGenerating) {
        isGeneratingProperty.set(isGenerating);
    }
    
    protected MandelbrotPage getCoverPage() {
        return coverPage;
    }
    
    private void refreshCanvas(BoundCanvas source, double newWidth, double newHeight) {
        FXTools.get().runFX(() -> TaskTools.sync(lock, () -> {
            FXTools.get().clearCanvasUnsafe(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas());
            
            if (worker != null)
                worker.cancel(false);
            
            dimensions.resizeTo(newWidth, newHeight);
            final MandelbrotIterator iterator = new MandelbrotIterator(new MandelbrotColor[(int) newWidth][(int) newHeight], dimensions, lock);
            worker = new Task<>() {
                @Override
                protected Void call() {
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
            getCoverPage().getController().getWidthLabel().setText("" + dimensions.getWidth());
            getCoverPage().getController().getHeightLabel().setText("" + dimensions.getHeight());
            getCoverPage().getController().getCanvasWidthLabel().setText("" + dimensions.getCanvasWidth());
            getCoverPage().getController().getCanvasHeightLabel().setText("" + dimensions.getCanvasHeight());
            new Thread(worker).start(); // Use executor instead?
        }), true);
    }
    
    private void regenerate(ActionEvent event) { }
    
    private void redraw(MandelbrotColor[][] colors) {
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
    
    private void zoom(MouseDragData dragData) {
        if (!dragData.isValid())
            throw ExceptionTools.ex("Drag Data is Invalid!");
        //        selectionBoxPaintCommandOld.setActive(false);
        selectionBoxPaintCommand.deactivate();
        selectionBoxPaintCommand2.deactivate();
        selectionCirclePaintCommand.deactivate();
        dimensions.zoomTo(dragData.getStartX(), dragData.getStartY(), dragData.getEndX(), dragData.getEndY());
        refreshCanvas(getController().canvas(), getController().canvas().getWidth(), getController().canvas().getHeight());
    }
    
    private void updateZoomBox(MouseDragData moveData) {
        TaskTools.sync(lock, () -> {
            //            selectionBoxPaintCommandOld.setActive(true);
            //            selectionBoxPaintCommandOld.setRectangle(moveData.getAsPaintable());
            //            System.out.println("Updating zoom box... " + moveData.getBounds());
            selectionBoxPaintCommand.activate();
            selectionBoxPaintCommand2.activate();
            selectionCirclePaintCommand.activate();
            selectionBoxPaintCommand.setBounds(moveData.getBounds());
            selectionBoxPaintCommand2.setBounds(moveData.getBounds());
            selectionCirclePaintCommand.setBounds(moveData.getBounds());
        });
        //        final AppUI ui = ctx().getBean(AppUI.class);
        //        FXTools.get().drawRectangle(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas(), moveData.getAsPaintable(), true, false);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull MandelbrotContentData loadData() {
        return new MandelbrotContentData();
    }
    
    @Override
    protected @NotNull Class<MandelbrotContentController> controllerDefinition() {
        return MandelbrotContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}
