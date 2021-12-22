package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class ContentController extends Controller
{
    private final StackPane overlayStackPane;
    
    public ContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.overlayStackPane = new StackPane();
    }
    
    public final StackPane getOverlayPane()
    {
        return overlayStackPane;
    }
}
