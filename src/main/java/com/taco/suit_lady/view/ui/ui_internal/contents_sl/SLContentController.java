package com.taco.suit_lady.view.ui.ui_internal.contents_sl;

import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class SLContentController extends Controller
{
    private final StackPane overlayStackPane;
    
    public SLContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.overlayStackPane = new StackPane();
    }
    
    public final StackPane getOverlayPane()
    {
        return overlayStackPane;
    }
}
