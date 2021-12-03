package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.view.ui.jfx.components.ImagePane;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/content/mandelbrot/mandelbrot_content.fxml")
@Scope("prototype")
public class SLMandelbrotContentController extends SLContentController
{
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    
    @FXML private BorderPane borderPaneRoot;
    @FXML private Label titleLabel;
    
    @FXML private ImagePane canvasImagePane;
    
    //</editor-fold>
    
    public SLMandelbrotContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    protected ImagePane canvas()
    {
        return canvasImagePane;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    public void initialize() { }
    
    //</editor-fold>
}
