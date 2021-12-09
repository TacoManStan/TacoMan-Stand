package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public class CanvasPane extends AnchorPane
        implements Springable
{
    private final Springable springable;
    
    private final BoundCanvas canvas;
    
    public CanvasPane(Springable springable)
    {
        this(springable, 0, 0, 0, 0);
    }
    
    public CanvasPane(Springable springable, double left, double right, double top, double bottom)
    {
        this.springable = springable.asStrict();
        
        this.canvas = new BoundCanvas(this);
        
        getChildren().add(canvas);
        FXTools.get().setAnchors(canvas, left, right, top, bottom);
    }
    
    public final BoundCanvas canvas()
    {
        return canvas;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return null;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return null;
    }
    
    //</editor-fold>
}
