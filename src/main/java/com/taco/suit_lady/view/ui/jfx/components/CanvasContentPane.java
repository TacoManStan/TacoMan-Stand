package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.util.tools.fxtools.FXTools;

public class CanvasContentPane extends ContentPane
{
    private final CanvasPane overlayCanvasPane;
    private final CanvasPane backdropCanvasPane;
    
    public CanvasContentPane()
    {
        this.overlayCanvasPane = new CanvasPane();
        this.backdropCanvasPane = new CanvasPane();
    
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
