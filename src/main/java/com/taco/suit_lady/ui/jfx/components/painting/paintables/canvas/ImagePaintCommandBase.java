package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ImagePaintCommandBase extends PaintCommand {
    
    private final ObjectProperty<Image> imageProperty;
    
    public ImagePaintCommandBase(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.imageProperty = new SimpleObjectProperty<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectProperty<Image> imageProperty() { return imageProperty; }
    public final Image getImage() { return imageProperty.get(); }
    public final Image setImage(Image newValue) { return PropertiesSL.setProperty(imageProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void drawImage(@NotNull Image image, @NotNull CanvasSurface surface, @NotNull Bounds bounds);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull ImagePaintCommandBase init() {
        imageProperty.addListener((observable, oldValue, newValue) -> repaintSurface());
        imageProperty.set(ResourcesSL.getDummyImage(ResourcesSL.MAP));
        
        return (ImagePaintCommandBase) super.init();
    }
    
    @Override protected void onPaint() {
        sync(() -> {
            Image image = getImage();
            if (image != null && isValidDimensions())
                drawImage(image, getSurface(), getBounds());
        });
        
        // Below is example use case for both source & target Bounds.
        // The example clips the image to show only the top left quadrant of the image
        // FXTools.drawImage(getSurface(), new Bounds(0, 0, (int) (image.getWidth() / 2), (int) (image.getHeight() / 2)), bounds, image, true, false);
    }
    
    //</editor-fold>
}
