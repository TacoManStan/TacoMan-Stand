package com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas;

import com.taco.suit_lady.ui.jfx.components.ContentPane;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import org.jetbrains.annotations.NotNull;

public class CanvasContentPane extends ContentPane {
    
    private final CanvasPane overlayCanvasPane;
    private final CanvasPane backdropCanvasPane;
    
    public CanvasContentPane(@NotNull Springable springable) {
        super(springable);
        
        this.overlayCanvasPane = new CanvasPane(this);
        this.backdropCanvasPane = new CanvasPane(this);
        
        FXTools.bindToParent(overlayCanvasPane, this, false);
        FXTools.bindToParent(backdropCanvasPane, this, false);
        
        getChildren().add(overlayCanvasPane);
        getChildren().add(0, backdropCanvasPane);
        
        FXTools.togglePickOnBounds(this, false);
    }
    
    protected final CanvasPane getOverlayCanvasPane() {
        return overlayCanvasPane;
    }
    
    protected final CanvasPane getBackdropCanvasPane() {
        return backdropCanvasPane;
    }
    
    public final CanvasSurface getOverlayCanvas() {
        return getOverlayCanvasPane().canvas();
    }
    
    public final CanvasSurface getBackdropCanvas() {
        return getBackdropCanvasPane().canvas();
    }
}
