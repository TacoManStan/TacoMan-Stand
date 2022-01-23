package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.SLProperties;
import com.taco.suit_lady.util.tools.SLResources;
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
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectProperty<Image> imageProperty() { return imageProperty; }
    public final Image getImage() { return imageProperty.get(); }
    public final Image setImage(Image newValue) { return SLProperties.setProperty(imageProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull ImagePainter init() {
        imageProperty.addListener((observable, oldValue, newValue) -> repaintSurface());
        imageProperty.set(SLResources.get().getDummyImage(SLResources.MAP));
        
        return (ImagePainter) super.init();
    }
    
    @Override protected void onPaint() {
        Image image = getImage();
        Bounds bounds = getBounds();
        if (image != null && isValidDimensions())
            FXTools.drawImage(getSurface(), bounds, image, true, false);
        
        // Below is example use case for both source & target Bounds.
        // The example clips the image to show only the top left quadrant of the image
        // FXTools.drawImage(getSurface(), new Bounds(0, 0, (int) (image.getWidth() / 2), (int) (image.getHeight() / 2)), bounds, image, true, false);
    }
    
    //</editor-fold>
}
