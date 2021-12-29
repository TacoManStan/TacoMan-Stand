package com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_list_page;

import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.suit_lady.ui.jfx.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@FxmlView("/fxml/sidebar/pages/mandelbrot/mandelbrot_list/mandelbrot_data_element.fxml")
@Scope("prototype")
public class MandelbrotContentElementController extends CellController<MandelbrotContent> {
    
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
    
    public MandelbrotContentElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected void onContentChange(MandelbrotContent oldCellContents, MandelbrotContent newCellContents) {
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
        this.iconButton = new ImageButton(this, iconImagePane, "logo", this::icon, null, false, new Point2D(50, 50)).initialize();
        this.closeButton = new ImageButton(this, closeImagePane, "close", this::close, null, false, new Point2D(15, 15)).initialize();
        
        this.playButton = new ImageButton(this, playImagePane, "play", this::runScript, null, false, ImageButton.SMALL).initialize();
        this.stopButton = new ImageButton(this, stopImagePane, "stop", this::stopScript, null, false, ImageButton.SMALL).initialize();
        this.rerunButton = new ImageButton(this, rerunImagePane, "rerun", this::rerunScript, null, false, ImageButton.SMALL).initialize();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    private void icon() {
        debugger().print("Logo Pressed");
    }
    
    private void close() {
        debugger().print("Close Pressed");
        getContents().shutdown();
    }
    
    private void runScript() {
        debugger().print("Run Script Pressed");
//        getContents().setIsRunning(true);
    }
    
    private void stopScript() {
        debugger().print("Stop Script Pressed");
//        getContents().setIsRunning(false);
    }
    
    private void rerunScript() {
        debugger().print("Re-Run Script Pressed");
    }
    
    //</editor-fold>
}
