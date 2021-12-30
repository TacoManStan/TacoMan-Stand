package com.taco.suit_lady.ui.pages.impl.content_selector;

import com.taco.suit_lady._to_sort._new.Debugger;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.ui.jfx.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public class ContentElementController<
        D extends ContentData,
        P extends ContentSelectorPage<D, P, SC, EC, H, T>,
        SC extends ContentSelectorPageController<D, P, SC, EC, H, T>,
        EC extends ContentElementController<D, P, SC, EC, H, T>,
        H extends ContentHandler<D, P, SC, EC, H, T>,
        T extends ListableContent<D, ?, P, SC, EC, H, T>>
        extends CellController<T> {
    
    @FXML private AnchorPane root;
    
    @FXML private ImagePane playImagePane;
    @FXML private ImagePane stopImagePane;
    @FXML private ImagePane rerunImagePane;
    @FXML private ImagePane closeImagePane;
    @FXML private ImagePane iconImagePane;
    
    private ImageButton iconButton;
    private ImageButton closeButton;
    
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton rerunButton;
    
    public ContentElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected void onContentChange(T oldCellContents, T newCellContents) {
//        if (newCellContents != null) {
//            playButton.disabledProperty().bind(newCellContents.runningProperty());
//            stopButton.disabledProperty().bind(Bindings.not(newCellContents.runningProperty()));
//        } else {
//            playButton.disabledProperty().unbind();
//            playButton.setDisabled(true);
//            stopButton.disabledProperty().unbind();
//            stopButton.setDisabled(false);
//        }
    }
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing ContentElementController");
        
        System.out.println("Icon Image Pane: " + iconImagePane);
        
        this.iconButton = new ImageButton(this, iconImagePane, "logo", this::icon, null, false, new Point2D(50, 50)).initialize();
        this.closeButton = new ImageButton(this, closeImagePane, "close", this::close, null, false, new Point2D(15, 15)).initialize();
        
        this.playButton = new ImageButton(this, playImagePane, "play", this::runScript, null, false, ImageButton.SMALL).initialize();
        this.stopButton = new ImageButton(this, stopImagePane, "stop", this::stopScript, null, false, ImageButton.SMALL).initialize();
        this.rerunButton = new ImageButton(this, rerunImagePane, "rerun", this::rerunScript, null, false, ImageButton.SMALL).initialize();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    private void icon() {
        debugger().print(Debugger.DEBUG, "Logo Pressed");
    }
    
    private void close() {
        debugger().print(Debugger.DEBUG, "Close Pressed");
        getContents().shutdown();
    }
    
    private void runScript() {
        debugger().print(Debugger.DEBUG, "Run Script Pressed");
//        getContents().setIsRunning(true);
    }
    
    private void stopScript() {
        debugger().print(Debugger.DEBUG, "Stop Script Pressed");
//        getContents().setIsRunning(false);
    }
    
    private void rerunScript() {
        debugger().print(Debugger.DEBUG, "Re-Run Script Pressed");
    }
    
    //</editor-fold>
}
