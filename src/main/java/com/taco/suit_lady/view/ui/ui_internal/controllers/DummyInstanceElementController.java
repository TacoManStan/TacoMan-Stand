package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import com.taco.suit_lady.view.ui.ui_internal.contents.DummyInstance;
import com.taco.util.quick.ConsoleBB;
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
@FxmlView("/fxml/sidebar/client_instance_list/client_instance_element.fxml")
@Scope("prototype")
public final class DummyInstanceElementController extends CellController<DummyInstance>
{
    
    //<editor-fold desc="FXML">
    
    @FXML private AnchorPane root;
    
    @FXML private ImagePane iconImagePane;
    @FXML private ImagePane closeImagePane;
    
    @FXML private ImagePane playImagePane;
    @FXML private ImagePane stopImagePane;
    @FXML private ImagePane rerunImagePane;
    
    //</editor-fold>
    
    //<editor-fold desc="Buttons">
    
    private ImageButton iconButton;
    private ImageButton closeButton;
    
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton rerunButton;
    
    //</editor-fold>
    
    public DummyInstanceElementController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    public void initialize()
    {
        (this.iconButton = new ImageButton(iconImagePane, "logo", this::icon, false, null)).initialize();
        (this.closeButton = new ImageButton(closeImagePane, "close", this::close, false, new Point2D(15, 15))).initialize();
        
        (this.playButton = new ImageButton(playImagePane, "play", this::runScript, false, ImageButton.SMALL)).initialize();
        (this.stopButton = new ImageButton(stopImagePane, "stop", this::stopScript, false, ImageButton.SMALL)).initialize();
        (this.rerunButton = new ImageButton(rerunImagePane, "rerun", this::rerunScript, false, ImageButton.SMALL)).initialize();
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Button Actions                                                              *
     *                                                                             *
     * *************************************************************************** */
    
    private void icon() { }
    
    private void close()
    {
        getContents().shutdown();
    }
    
    //
    
    private void runScript()
    {
        //			instancesPage.loadScript(getContents());
        ConsoleBB.CONSOLE.dev("Run Script Pressed [" + getContents() + "]");
    }
    
    private void stopScript()
    {
        //			getContents().stopScript();
        ConsoleBB.CONSOLE.dev("Stop Script Pressed [" + getContents() + "]");
    }
    
    private void rerunScript()
    {
        ConsoleBB.CONSOLE.dev("Rerun Script Pressed [" + getContents() + "]");
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Implementation                                                              *
     *                                                                             *
     * *************************************************************************** */
    
    @Override
    protected void onContentChange(DummyInstance oldInstance, DummyInstance newInstance)
    {
        // CHANGE-HERE
        if (newInstance == null)
        {
            playButton.disabledProperty().unbind();
            playButton.setDisabled(true);
            stopButton.disabledProperty().unbind();
            stopButton.setDisabled(false);
        }
    }
}