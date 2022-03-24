package com.taco.tacository.ui.jfx.components.painting.surfaces.canvas;

import com.taco.tacository.ui.jfx.components.ContentPane;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.fx_tools.FX;
import org.jetbrains.annotations.NotNull;

public class CanvasContentPane extends ContentPane {
    
    private final CanvasPane overlayCanvasPane;
    private final CanvasPane backdropCanvasPane;
    
    public CanvasContentPane(@NotNull Springable springable) {
        super(springable);
        
        this.overlayCanvasPane = new CanvasPane(this);
        this.backdropCanvasPane = new CanvasPane(this);
        
        FX.bindToParent(overlayCanvasPane, this, false);
        FX.bindToParent(backdropCanvasPane, this, false);
        
        getChildren().add(overlayCanvasPane);
        getChildren().add(0, backdropCanvasPane);
        
        FX.togglePickOnBounds(this, false);
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
