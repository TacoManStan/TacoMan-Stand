package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.ImagePaintCommand;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import org.eclipse.persistence.annotations.ReadOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameObjectModel
        implements SpringableWrapper, GameComponent {
    
    private final GameObject owner;
    
    //
    
    private final ReadOnlyStringWrapper imageTypeProperty;
    private final ReadOnlyStringWrapper imageIdProperty;
    private final ObjectBinding<Image> imageBinding;
    
    private final ImagePaintCommand modelPaintCommand;
    
    private IntegerBinding xPaintPositionBinding;
    private IntegerBinding yPaintPositionBinding;
    
    public GameObjectModel(@NotNull GameObject owner) {
        this(owner, "unit", "taco");
    }
    
    public GameObjectModel(@NotNull GameObject owner, @Nullable String imageType, @Nullable String imageId) {
        this.owner = owner;
        
        //
        
        this.imageTypeProperty = new ReadOnlyStringWrapper();
        this.imageIdProperty = new ReadOnlyStringWrapper();
        this.imageBinding = BindingsSL.objBinding(() -> {
            final String imageTypeImpl = getImageType();
            final String imageIdImpl = getImageId();
            if (imageTypeImpl != null && imageIdImpl != null)
                return ResourcesSL.getGameImage(getImageType() + "/", getImageId());
            return ResourcesSL.getGameImage("units/", "taco");
        }, imageTypeProperty, imageIdProperty);
        
        this.modelPaintCommand = new ImagePaintCommand(this, null);
    }
    
    public GameObjectModel init() {
        this.xPaintPositionBinding = BindingsSL.intBinding(() -> (-getOwner().getGameMap().getModel().getCamera().getAggregateX() + getOwner().getLocationX(false)), getOwner().getGameMap().getModel().getCamera().xAggregateBinding(), getOwner().xLocationProperty());
        this.yPaintPositionBinding = BindingsSL.intBinding(() -> (-getOwner().getGameMap().getModel().getCamera().getAggregateY() + getOwner().getLocationY(false)), getOwner().getGameMap().getModel().getCamera().yAggregateBinding(), getOwner().yLocationProperty());
        
        this.modelPaintCommand.init();
        
        //        imageBinding.addListener((observable, oldValue, newValue) -> {
        //            if (newValue != null)
        //        });
        modelPaintCommand.imageProperty().bind(imageBinding);
        
        modelPaintCommand.boundsBinding().widthProperty().set(getGameMap().getTileSize());
        modelPaintCommand.boundsBinding().heightProperty().set(getGameMap().getTileSize());
        
        modelPaintCommand.boundsBinding().xProperty().bind(xPaintPositionBinding);
        modelPaintCommand.boundsBinding().yProperty().bind(yPaintPositionBinding);
        
        modelPaintCommand.setPaintPriority(1);
        
        getGameMap().getModel().getCanvas().addPaintable(modelPaintCommand);
        
        setImageData(null, null);
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    
    public final ImagePaintCommand getPaintCommand() { return modelPaintCommand; }
    
    public final ReadOnlyStringProperty readOnlyImageIdProperty() { return imageIdProperty.getReadOnlyProperty(); }
    public final String getImageId() { return imageIdProperty.get(); }
    public final String setImageId(@Nullable String newValue) { return PropertiesSL.setProperty(imageIdProperty, newValue != null ? newValue : "taco"); }
    
    public final ReadOnlyStringProperty readOnlyImageTypeProperty() { return imageTypeProperty.getReadOnlyProperty(); }
    public final String getImageType() { return imageTypeProperty.get(); }
    public final String setImageType(@Nullable String newValue) { return PropertiesSL.setProperty(imageTypeProperty, newValue != null ? newValue : "units"); }
    
    public final void setImageData(@Nullable String imageId, @Nullable String imageType) {
        setImageId(imageId);
        setImageType(imageType);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return owner; }
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    //</editor-fold>
}
