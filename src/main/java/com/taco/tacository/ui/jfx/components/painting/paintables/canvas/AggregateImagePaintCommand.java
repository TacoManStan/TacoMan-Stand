package com.taco.tacository.ui.jfx.components.painting.paintables.canvas;

import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.Props;
import com.taco.tacository.util.tools.fx_tools.FX;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class AggregateImagePaintCommand extends PaintCommand {
    
    private final ReadOnlyListWrapper<Image> imageListProperty;
    private final BooleanProperty scaledProperty;
    
    public AggregateImagePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        
        this.imageListProperty = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.scaledProperty = new SimpleBooleanProperty(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final BooleanProperty scaledProperty() { return scaledProperty; }
    public final boolean isScaled() { return scaledProperty.get(); }
    public final boolean setScaled(boolean newValue) { return Props.setProperty(scaledProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull AggregateImagePaintCommand init() {
        scaledProperty.addListener((observable, oldValue, newValue) -> repaintSurface());
        return (AggregateImagePaintCommand) super.init();
    }
    
    @Override protected void onPaint() {
        if (isValidDimensions())
            for (Image image: imageListProperty) {
                if (image != null) {
                    if (isScaled())
                        FX.drawImageScaled(getSurface(), image, getBounds(), 1, 1, false);
                    else
                        FX.drawImage(getSurface(), getBounds(), image, false, false);
                }
            }
    }
    
    //</editor-fold>
    
    public final void addImages(@NotNull List<Image> images) {
        for (Image image: images) {
            if (image != null)
                imageListProperty.add(image);
        }
        repaintSurface();
    }
    
    public final boolean addImage(@NotNull Image image, boolean silent) {
        final boolean added = imageListProperty.add(image);
        if (!silent)
            repaintSurface();
        return added;
    }
    
    public final boolean removeImage(@NotNull Image image, boolean silent) {
        final boolean removed = imageListProperty.remove(image);
        if (!silent)
            repaintSurface();
        return removed;
    }
    
    public final void clearImageList(boolean silent) {
        sync(() -> {
            imageListProperty.clear();
            if (!silent)
                repaintSurface();
        });
    }
}
