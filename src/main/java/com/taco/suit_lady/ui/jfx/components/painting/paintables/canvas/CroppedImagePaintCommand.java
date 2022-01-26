package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class CroppedImagePaintCommand extends ImagePaintCommandBase {
    
    private final BoundsBinding croppingBoundsBinding;
    
    
    public CroppedImagePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.croppingBoundsBinding = new BoundsBinding();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull BoundsBinding croppingBoundsBinding() { return croppingBoundsBinding; }
    public final @NotNull Bounds getCroppingBounds() { return croppingBoundsBinding.getBounds(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull CroppedImagePaintCommand init() {
        return (CroppedImagePaintCommand) super.init();
    }
    
    @Override protected void drawImage(@NotNull Image image, @NotNull CanvasSurface surface, @NotNull Bounds bounds) {
        ToolsFX.drawImage(surface, getCroppingBounds(), bounds, image, true, false);
    }
    
    //</editor-fold>
}
