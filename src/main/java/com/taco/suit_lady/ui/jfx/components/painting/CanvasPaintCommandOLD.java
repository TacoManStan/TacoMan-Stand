package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.ui.jfx.components.canvas.CanvasUICommand;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertyTools;
import com.taco.suit_lady.util.tools.ResourceTools;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

public abstract class CanvasPaintCommandOLD
        implements SpringableWrapper, CanvasUICommand {
    
    private final ObjectProperty<CanvasOverlay> overlayProperty;
    
    private final IntegerProperty viewportWidthProperty;
    private final IntegerProperty viewportHeightProperty;
    private final IntegerProperty fullImageWidthProperty;
    private final IntegerProperty fullImageHeightProperty;
    private final IntegerProperty xLocationProperty;
    private final IntegerProperty yLocationProperty;
    
    private final ObjectProperty<Image> imageProperty;
    
    public CanvasPaintCommandOLD(@NotNull CanvasOverlay overlayOwner) {
        this.overlayProperty = new SimpleObjectProperty<>(overlayOwner);
        
        this.viewportWidthProperty = new SimpleIntegerProperty();
        this.viewportHeightProperty = new SimpleIntegerProperty();
        this.fullImageWidthProperty = new SimpleIntegerProperty();
        this.fullImageHeightProperty = new SimpleIntegerProperty();
        
        this.xLocationProperty = new SimpleIntegerProperty();
        this.yLocationProperty = new SimpleIntegerProperty();
        
        this.imageProperty = new SimpleObjectProperty<>(ResourceTools.get().getDummyImage(ResourceTools.MAP));
    }
    
    public CanvasPaintCommandOLD init() {
        viewportWidthProperty.bind(getOverlay().getCanvas().widthProperty());
        viewportHeightProperty.bind(getOverlay().getCanvas().heightProperty());
        
        fullImageWidthProperty.bind(getImage().widthProperty());
        fullImageHeightProperty.bind(getImage().heightProperty());
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectProperty<CanvasOverlay> ownerProperty() {
        return overlayProperty;
    }
    
    public final CanvasOverlay getOverlay() {
        return overlayProperty.get();
    }
    
    public final CanvasOverlay setOverlay(CanvasOverlay newValue) {
        CanvasOverlay oldValue = getOverlay();
        overlayProperty.set(newValue);
        return oldValue;
    }
    
    
    public final ObjectProperty<Image> imageProperty() {
        return imageProperty;
    }
    
    public final Image getImage() {
        return imageProperty.get();
    }
    
    public final Image setImage(Image newValue) {
        return PropertyTools.setProperty(imageProperty, newValue);
    }
    
    
    public final IntegerProperty viewportWidthProperty() {
        return viewportWidthProperty;
    }
    
    public final int getViewportWidth() {
        return viewportWidthProperty.get();
    }
    
    public final int setViewportWidth(int newValue) {
        return PropertyTools.setProperty(viewportWidthProperty, newValue);
    }
    
    
    public final IntegerProperty viewportHeightProperty() {
        return viewportHeightProperty;
    }
    
    public final int getViewportHeight() {
        return viewportHeightProperty.get();
    }
    
    public final int setViewportHeight(int newValue) {
        return PropertyTools.setProperty(viewportHeightProperty, newValue);
    }
    
    
    public final IntegerProperty fullImageWidthProperty() {
        return fullImageWidthProperty;
    }
    
    public final int getFullImageWidth() {
        return fullImageWidthProperty.get();
    }
    
    public final int setFullImageWidth(int newValue) {
        return PropertyTools.setProperty(fullImageWidthProperty, newValue);
    }
    
    
    public final IntegerProperty fullImageHeightProperty() {
        return fullImageHeightProperty;
    }
    
    public final int getFullImageHeight() {
        return fullImageHeightProperty.get();
    }
    
    public final int setFullImageHeight(int newValue) {
        return PropertyTools.setProperty(fullImageHeightProperty, newValue);
    }
    
    
    public final IntegerProperty xLocationProperty() {
        return xLocationProperty;
    }
    
    public final int getXLocation() {
        return xLocationProperty.get();
    }
    
    public final int setXLocation(int newValue) {
        return PropertyTools.setProperty(xLocationProperty, newValue);
    }
    
    
    public final IntegerProperty yLocationProperty() {
        return yLocationProperty;
    }
    
    public final int getYLocation() {
        return yLocationProperty.get();
    }
    
    public final int setYLocation(int newValue) {
        return PropertyTools.setProperty(yLocationProperty, newValue);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return overlayProperty.get();
    }
    
    //</editor-fold>
    
    public void paint() {
        getOverlay().getCanvas().getGraphicsContext2D().drawImage(
                getImage(),
                getXLocation(), getYLocation(),
                getViewportWidth(), getViewportHeight(),
                0, 0,
                getViewportWidth(), getViewportHeight());
    }
}
