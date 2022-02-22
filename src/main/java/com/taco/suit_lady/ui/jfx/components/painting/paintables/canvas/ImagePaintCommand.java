package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
        
        ToolsFX.drawImage(getSurface(), bounds, image, false, false);
    }
    
    //</editor-fold>
}
