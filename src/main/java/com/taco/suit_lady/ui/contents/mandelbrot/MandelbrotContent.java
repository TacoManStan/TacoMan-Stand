package com.taco.suit_lady.ui.contents.mandelbrot;

import com.taco.suit_lady.ui.AppUI;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContentController.MouseDragData;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotIterator.MandelbrotColor;
import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.ui.jfx.components.canvas.shapes.ArcPaintCommand;
import com.taco.suit_lady.ui.jfx.components.canvas.shapes.RectanglePaintCommand;
import com.taco.suit_lady.ui.pages.impl.content_selector.ListableContent;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentHandler;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentSelectorPage;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentSelectorPageController;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotElementController;
import com.taco.suit_lady.ui.jfx.components.painting.EllipseOverlayCommand;
import com.taco.suit_lady.ui.jfx.components.painting.ImageOverlayCommand;
import com.taco.suit_lady.ui.jfx.components.painting.RectangleOverlayCommand;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.util.tools.TaskTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.tacository.json.JFiles;
import javafx.beans.binding.Bindings;
import javafx.scene.paint.Color;
import org.docx4j.wml.R;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class MandelbrotContent extends ListableContent<
        MandelbrotContentData,
        MandelbrotContentController,
        MandelbrotContentSelectorPage,
        MandelbrotContentSelectorPageController,
        MandelbrotElementController,
        MandelbrotContentHandler,
        MandelbrotContent>
        implements UIDProcessable {
    
    private final ReentrantLock lock;
    
    private final MandelbrotIterator iterator;
    
    private final RectangleOverlayCommand selectionBoxPaintCommand;
    private final ImageOverlayCommand selectionBoxPaintCommand2;
    private final EllipseOverlayCommand selectionCirclePaintCommand;
    
    private final ArcPaintCommand testPaintCommand;
    private final RectanglePaintCommand testPaintCommand2;
    
    private MandelbrotPage coverPage;
    
    public MandelbrotContent(@NotNull MandelbrotContentHandler contentHandler) {
        super(contentHandler);
        
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
        
        getController().canvas().setCanvasListener((source, newWidth, newHeight) -> refreshCanvas());
        getCoverPage().getController().getRegenerateButton().setOnAction(event -> refreshCanvas());
        
        this.selectionBoxPaintCommand = new RectangleOverlayCommand(
                lock, this, "selection-box",
                null, 1,
                null, Color.BLACK);
        this.selectionBoxPaintCommand2 = new ImageOverlayCommand(
                lock, this, "selection-box2",
                null, 3);
        this.selectionCirclePaintCommand = new EllipseOverlayCommand(
                lock, this, "selection-circle",
                null, 2);
        
        this.testPaintCommand = new ArcPaintCommand(this, lock, 50, 120, null);
        this.testPaintCommand2 = new RectanglePaintCommand(this, lock);
//        this.testPaintCommand2.setIsFill(true);
        
        this.selectionBoxPaintCommand.deactivate();
        this.selectionBoxPaintCommand2.deactivate();
        this.selectionCirclePaintCommand.deactivate();
        
        
//        getOverlayHandler().getOverlay("default").addPaintCommand(selectionBoxPaintCommand);
//        ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas().addPaintCommand(testPaintCommand);
        ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas().init().addPaintable(testPaintCommand2.init());
//        getController().canvas().addPaintCommand(testPaintCommand);
        //        getOverlayHandler().getOverlay("default").addPaintCommand(selectionBoxPaintCommand2);
        //        getOverlayHandler().getOverlay("default").addPaintCommand(selectionCirclePaintCommand);
        
        //        this.selectionBoxPaintCommand = new RectanglePaintCommand(false, lock);
        //        ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas().addPaintCommand(selectionBoxPaintCommand);
        
        getController().setDragConsumer(dragData -> zoom(dragData));
        getController().setMoveConsumer(dragData -> updateZoomBox(dragData));
        
        iconImageProperty().bind(getController().canvas().imageProperty());
        initUIPage();
        
        this.iterator = new MandelbrotIterator(this, lock, getData(), getController().canvas(), getCoverPage().getController().getProgressBar());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void initUIPage() {
        //Bind the value properties of relevant JFX components to the matching MandelbrotData property bidirectionally
        getCoverPage().getController().getPrecisionTextField().getFormatter().valueProperty().bindBidirectional(getData().precisionProperty());
        
        getCoverPage().getController().getXMaxTextField().getFormatter().valueProperty().bindBidirectional(getData().xMaxProperty());
        getCoverPage().getController().getYMaxTextField().getFormatter().valueProperty().bindBidirectional(getData().yMaxProperty());
        getCoverPage().getController().getXMinTextField().getFormatter().valueProperty().bindBidirectional(getData().xMinProperty());
        getCoverPage().getController().getYMinTextField().getFormatter().valueProperty().bindBidirectional(getData().yMinProperty());
        
        getCoverPage().getController().getColorSchemeChoiceBox().valueProperty().bindBidirectional(getData().colorSchemeProperty());
        getCoverPage().getController().getInvertColorSchemeImageButton().selectedProperty().bindBidirectional(getData().invertColorSchemeProperty());
        
        getCoverPage().getController().getPauseAutoRegenerationImageButton().selectedProperty().bindBidirectional(getData().pauseAutoRegenerationProperty());
        
        
        // Refresh the generated image when an applicable MandelbrotData property changes
        getData().precisionProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        
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
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected MandelbrotPage getCoverPage() {
        return coverPage;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public boolean isNullableLock() {
        return true;
    }
    
    //
    
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
        iterator.shutdown();
    }
    
    //
    
    private UIDProcessor uidProcessor;
    
    @Override
    public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("mandelbrot_content");
        return uidProcessor;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void refreshCanvasChecked() {
        boolean autoRegenerationPaused = getData().isAutoRegenerationPaused();
        if (!autoRegenerationPaused)
            refreshCanvas();
    }
    
    private void refreshCanvas() {
        sync(() -> FXTools.runFX(() -> {
            final BoundCanvas canvas = getController().canvas();
            final double newWidth = getController().canvas().getWidth();
            final double newHeight = getController().canvas().getHeight();
            
            FXTools.clearCanvasUnsafe(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas());
            
            getData().resizeTo(newWidth, newHeight);
            
            iterator.run();
        }, true));
    }
    
    private void redraw(MandelbrotColor[][] colors) {
        FXTools.runFX(() -> TaskTools.sync(lock, () -> {
            getCoverPage().getController().getProgressBar().setVisible(false);
            for (int i = 0; i < colors.length; i++)
                for (int j = 0; j < colors[i].length; j++) {
                    final MandelbrotColor mandelbrotColor = colors[i][j];
                    final Color color = mandelbrotColor != null ? mandelbrotColor.getColor() : Color.BLACK;
                    getController().canvas().getGraphicsContext2D().getPixelWriter().setColor(i, j, color);
                }
        }), true);
    }
    
    private void zoom(@NotNull MouseDragData dragData) {
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
            
            testPaintCommand.boundsBinding().setBounds(moveData.getBounds());
            testPaintCommand2.boundsBinding().setBounds(moveData.getBounds());
        });
    }
    
    //</editor-fold>
}
