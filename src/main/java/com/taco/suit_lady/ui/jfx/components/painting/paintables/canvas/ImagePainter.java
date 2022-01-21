package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertyTools;
import com.taco.suit_lady.util.tools.ResourceTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class ImagePainter extends CanvasPainter {
    
    private final ObjectProperty<Image> imageProperty;
    
    public ImagePainter(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.imageProperty = new SimpleObjectProperty<>();
    }
    
    @Override public @NotNull CanvasPainter init() {
        imageProperty.addListener((observable, oldValue, newValue) -> repaintSurface());
        imageProperty.set(ResourceTools.get().getDummyImage(ResourceTools.MAP));
        
        return super.init();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectProperty<Image> imageProperty() { return imageProperty; }
    public final Image getImage() { return imageProperty.get(); }
    public final Image setImage(Image newValue) { return PropertyTools.setProperty(imageProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void onPaint() {
        Image image = getImage();
//        System.out.println("Image: " + getImage());
//        System.out.println("Image Dimensions: " + getImage().getWidth() + ", " + getImage().getHeight());
//        System.out.println("Is Valid Dimensions: " + isValidDimensions());
        Bounds bounds = getBounds();
//        System.out.println("Bounds: " + bounds);
        if (image != null && isValidDimensions())
            FXTools.drawImage(getSurface(), bounds, image, true, false);
    }
    
    //</editor-fold>
}
