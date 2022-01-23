package com.taco.suit_lady.ui.contents.mandelbrot;

import com.taco.suit_lady.ui.AppUI;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContentController.MouseDragData;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotIterator.MandelbrotColor;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.Paintable;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.*;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay.BoxPaintNode;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay.ImagePaintNode;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.pages.impl.content_selector.ListableContent;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentHandler;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentSelectorPage;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentSelectorPageController;
import com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotElementController;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.tools.SLResources;
import com.taco.suit_lady.util.tools.SLTasks;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import com.taco.tacository.json.JFiles;
import javafx.beans.binding.Bindings;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
    
    private final BoxPaintCommand boxPainter;
    private final OvalPaintCommand ovalPainter;
    private final ArcPaintCommand arcPainter;
    private final ImagePaintCommand imagePainter;
    private final TextPaintCommand textPainter;
    
    private final BoxPaintNode boxOverlayPainter;
    private final ImagePaintNode imageOverlayPainter;
    
    private final Paintable<?, ?>[] paintables;
    
    private MandelbrotPage coverPage;
    
    public MandelbrotContent(@NotNull MandelbrotContentHandler contentHandler) {
        super(contentHandler);
        
        this.lock = new ReentrantLock();
        
        injectBookshelf("Mandelbrot Demo", new UIBook(
                this,
                "Mandelbrot Demo",
                "mandelbrot2",
                uiBook -> SLResources.get(
                        "pages",
                        uiBook.getUID(uiBook.getButtonID()),
                        () -> coverPage = new MandelbrotPage(uiBook, this)),
                null));
        
        getController().canvas().setCanvasListener((source, newWidth, newHeight) -> refreshCanvas());
        getCoverPage().getController().getRegenerateButton().setOnAction(event -> refreshCanvas());
        
        //
        
        this.boxPainter = new BoxPaintCommand(this, lock);
        this.ovalPainter = new OvalPaintCommand(this, lock);
        this.arcPainter = new ArcPaintCommand(this, lock, 30, 30, ArcType.ROUND);
        this.imagePainter = new ImagePaintCommand(this, lock);
        this.textPainter = new TextPaintCommand(this, lock, "Test Text");
        
        this.boxOverlayPainter = new BoxPaintNode(this, lock);
        this.imageOverlayPainter = new ImagePaintNode(this, lock);
        
        Arrays.stream(paintables = new Paintable[]{ovalPainter, imagePainter, boxOverlayPainter})
              .forEach(paintable -> getOverlayHandler().getDefaultOverlay().add(paintable.init()));
        
        //
        
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
        sync(() -> FX.runFX(() -> {
            final CanvasSurface canvas = getController().canvas();
            final double newWidth = getController().canvas().getWidth();
            final double newHeight = getController().canvas().getHeight();
            
            FX.clearCanvasUnsafe(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas());
            
            getData().resizeTo(newWidth, newHeight);
            
            iterator.run();
        }, true));
    }
    
    private void redraw(MandelbrotColor[][] colors) {
        FX.runFX(() -> SLTasks.sync(lock, () -> {
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
            throw SLExceptions.ex("Drag Data is Invalid!");
        
        Arrays.stream(paintables).forEach(paintable -> paintable.setPaused(true));
        getData().zoomTo(dragData.getStartX(), dragData.getStartY(), dragData.getEndX(), dragData.getEndY());
    }
    
    private void updateZoomBox(MouseDragData moveData) {
        SLTasks.sync(lock, () -> {
            Arrays.stream(paintables).forEach(paintable -> paintable.setPaused(false));
            Arrays.stream(paintables).forEach(paintable -> paintable.boundsBinding().setBounds(moveData.getBounds()));
        });
    }
    
    //</editor-fold>
}
