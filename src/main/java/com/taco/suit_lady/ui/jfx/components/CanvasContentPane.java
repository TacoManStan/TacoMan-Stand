package com.taco.suit_lady.ui.jfx.components;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import org.jetbrains.annotations.NotNull;

public class CanvasContentPane extends ContentPane
{
    private final CanvasPane overlayCanvasPane;
    private final CanvasPane backdropCanvasPane;
    
    public CanvasContentPane(@NotNull Springable springable)
    {
        super(springable);
        
        this.overlayCanvasPane = new CanvasPane(this);
        this.backdropCanvasPane = new CanvasPane(this);
    
        FXTools.get().bindToParent(overlayCanvasPane, this, false);
        FXTools.get().bindToParent(backdropCanvasPane, this, false);

        getChildren().add(overlayCanvasPane);
        getChildren().add(0, backdropCanvasPane);
        
        FXTools.get().togglePickOnBounds(this, false);
    }
    
    protected final CanvasPane getOverlayCanvasPane()
    {
        return overlayCanvasPane;
    }
    
    protected final CanvasPane getBackdropCanvasPane()
    {
        return backdropCanvasPane;
    }
    
    public final BoundCanvas getOverlayCanvas()
    {
        return getOverlayCanvasPane().canvas();
    }
    
    public final BoundCanvas getBackdropCanvas()
    {
        return getBackdropCanvasPane().canvas();
    }
}
