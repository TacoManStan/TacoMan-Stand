package com.taco.tacository.ui.jfx.components.painting.paintables.canvas;

import com.taco.tacository.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.tacository.util.values.numbers.Bounds;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.fx_tools.FX;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class ImagePaintCommand extends ImagePaintCommandBase {
    
    public ImagePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull ImagePaintCommand init() {
        return (ImagePaintCommand) super.init();
    }
    
    @Override protected void drawImage(@NotNull Image image, @NotNull CanvasSurface surface, @NotNull Bounds bounds) {
//        System.out.println("Image Width: " + image.getWidth());
//        System.out.println("Image Height: " + image.getHeight());
        
        FX.drawImage(getSurface(), bounds, image, false, false);
    }
    
    //</editor-fold>
}
