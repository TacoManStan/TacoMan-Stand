package com.taco.suit_lady.view.ui.contents.mandelbrot;

import com.taco.suit_lady._to_sort._new.Debugger;
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
import com.taco.suit_lady.view.ui.contents.mandelbrot.MandelbrotIterator.MandelbrotColor;
import com.taco.suit_lady.view.ui.contents.mandelbrot.MandelbrotContentController.MouseDragData;
import com.taco.tacository.json.JFiles;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class MandelbrotContent extends Content<MandelbrotContentData, MandelbrotContentController> {
    
    private final ReentrantLock lock;
    
    private Task<Void> worker;
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
                this,
                "Mandelbrot Demo",
                "mandelbrot2",
                uiBook -> TB.resources().get(
                        "pages",
                        uiBook.getUID(uiBook.getButtonID()),
                        () -> coverPage = new MandelbrotPage(uiBook, this)),
                null));
        
        this.worker = null;
        this.isGeneratingProperty = new ReadOnlyBooleanWrapper(false);
        
        getController().canvas().setCanvasListener((source, newWidth, newHeight) -> refreshCanvas());
        getCoverPage().getController().getRegenerateButton().setOnAction(event -> refreshCanvas());
        
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
        
        initUIPage();
    }
    
    private void initUIPage() {
        //Bind the value properties of relevant JFX components to the matching MandelbrotData property bidirectionally
        getCoverPage().getController().getXMaxTextField().getFormatter().valueProperty().bindBidirectional(getData().xMaxProperty());
        getCoverPage().getController().getYMaxTextField().getFormatter().valueProperty().bindBidirectional(getData().yMaxProperty());
        getCoverPage().getController().getXMinTextField().getFormatter().valueProperty().bindBidirectional(getData().xMinProperty());
        getCoverPage().getController().getYMinTextField().getFormatter().valueProperty().bindBidirectional(getData().yMinProperty());
        
        getCoverPage().getController().getColorSchemeChoiceBox().valueProperty().bindBidirectional(getData().colorSchemeProperty());
//        getCoverPage().getController().getInvertColorSchemeImageButton().selectedProperty().bindBidirectional(data.invertColorSchemeProperty());
        getCoverPage().getController().getInvertColorSchemeCheckBox().selectedProperty().bindBidirectional(getData().invertColorSchemeProperty());
        
        getCoverPage().getController().getPauseAutoRegenerationImageButton().selectedProperty().bindBidirectional(getData().pauseAutoRegenerationProperty());
        
        
        // Refresh the generated image when an applicable MandelbrotData property changes
        getData().xMinProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().xMaxProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().yMinProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().yMaxProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
    
        getData().colorSchemeProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().invertColorSchemeProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
    
    
        // Bind the text properties of applicable labels to reflect relevant MandelbrotData calculated values (bindings)
        getCoverPage().getController().getWidthLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getWidth(), getData().widthBinding()));
        getCoverPage().getController().getHeightLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getHeight(), getData().heightBinding()));
        getCoverPage().getController().getWidthScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledWidth(), getData().scaledWidthBinding()));
        getCoverPage().getController().getHeightScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledHeight(), getData().scaledHeightBinding()));
        
        getCoverPage().getController().getXMinScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMinX(), getData().scaledXMinBinding()));
        getCoverPage().getController().getYMinScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMinY(), getData().scaledYMinBinding()));
        getCoverPage().getController().getXMaxScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMaxX(), getData().scaledXMaxBinding()));
        getCoverPage().getController().getYMaxScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMaxY(), getData().scaledYMaxBinding()));
        
        getCoverPage().getController().getCanvasWidthLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getCanvasWidth(), getData().canvasWidthProperty()));
        getCoverPage().getController().getCanvasHeightLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getCanvasHeight(), getData().canvasHeightProperty()));
        
        
        getCoverPage().getController().getSaveConfigButton().setOnAction(event -> JFiles.save(getData()));
        getCoverPage().getController().getLoadConfigButton().setOnAction(event -> JFiles.load(getData()));
    }
    
    protected MandelbrotPage getCoverPage() {
        return coverPage;
    }
    
    private void refreshCanvasChecked() {
        boolean autoRegenerationPaused = getData().isAutoRegenerationPaused();
        if (!autoRegenerationPaused)
            refreshCanvas();
    }
    
    private void refreshCanvas() {
        TaskTools.sync(lock, () -> FXTools.get().runFX(() -> {
            final BoundCanvas canvas = getController().canvas();
            final double newWidth = getController().canvas().getWidth();
            final double newHeight = getController().canvas().getHeight();
            debugger().print("In Refresh 1...");
            
            if (worker != null) {
                debugger().print("Cancelling Worker...");
                worker.cancel(false);
            }
            FXTools.get().clearCanvasUnsafe(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas());
            
            debugger().print("In Refresh 2...");
    
            getData().resizeTo(newWidth, newHeight);
            final MandelbrotIterator iterator = new MandelbrotIterator(
                    this, new MandelbrotColor[(int) newWidth][(int) newHeight], getData(), lock);
            worker = new Task<>() {
                @Override
                protected Void call() {
                    FXTools.get().runFX(() -> getCoverPage().getController().getProgressBar().setVisible(true), true);
                    debugger().print("In Refresh Task A...");
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
                
                @Override
                protected void succeeded() {
                    debugger().print("Generation Successful!");
                    worker = null;
                }
                
                @Override
                protected void cancelled() {
                    debugger().print(Debugger.WARN, "Generation Cancelled.");
                    worker = null;
                }
                
                @Override
                protected void failed() {
                    debugger().print(Debugger.ERROR, "Generation Failed!");
                    worker = null;
                }
            };
            debugger().print("Worker: " + worker);
            
            getCoverPage().getController().getProgressBar().progressProperty().bind(worker.progressProperty());
            new Thread(worker).start(); // Use executor instead?
        }, true), true);
    }
    
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
        selectionBoxPaintCommand.deactivate();
        selectionBoxPaintCommand2.deactivate();
        selectionCirclePaintCommand.deactivate();
    
        getData().zoomTo(dragData.getStartX(), dragData.getStartY(), dragData.getEndX(), dragData.getEndY());
    }
    
    private void updateZoomBox(MouseDragData moveData) {
        TaskTools.sync(lock, () -> {
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
        return MandelbrotContentData.newDefaultInstance(this);
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
    protected void onShutdown() {
        if (worker != null) {
            debugger().print("Cancelling Mandelbrot Worker Task");
            if (worker.cancel(true))
                debugger().print("Worker Cancellation Successful!");
            else
                debugger().print(Debugger.WARN, "Worker Cancellation Failed!");
        }
    }
    
    //</editor-fold>
}
