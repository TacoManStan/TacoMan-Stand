package com.taco.suit_lady.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.ImagePaintCommand;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public class GameObjectModel
        implements SpringableWrapper, Lockable, GameComponent, GFXObject<GameObjectModel>, JObject, JLoadable {
    
    private final GameObject owner;
    private final TaskManager<GameObjectModel> taskManager;
    
    //
    
    private final ReadOnlyStringWrapper imageTypeProperty;
    private final ReadOnlyStringWrapper imageIdProperty;
    private final ObjectBinding<Image> imageBinding;
    
    private final ImagePaintCommand modelPaintCommand;
    
    private IntegerBinding xPaintPositionBinding;
    private IntegerBinding yPaintPositionBinding;
    
    private ObjectBinding<Bounds> paintBoundsBinding;
    
    private boolean needsUpdate;
    
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
        
        this.needsUpdate = false;
        this.taskManager = new TaskManager<>(this).init();
    }
    
    public GameObjectModel init() {
        this.xPaintPositionBinding = BindingsSL.intBinding(() -> (-getOwner().getGameMap().getModel().getCamera().getAggregateX() + getOwner().getLocationX(false)), getOwner().getGameMap().getModel().getCamera().xAggregateBinding(), getOwner().xLocationProperty());
        this.yPaintPositionBinding = BindingsSL.intBinding(() -> (-getOwner().getGameMap().getModel().getCamera().getAggregateY() + getOwner().getLocationY(false)), getOwner().getGameMap().getModel().getCamera().yAggregateBinding(), getOwner().yLocationProperty());
        
        this.paintBoundsBinding = BindingsSL.objBinding(
                () -> new Bounds(getPaintPositionX(), getPaintPositionY(), getGameMap().getTileSize(), getGameMap().getTileSize()),
                xPaintPositionBinding, yPaintPositionBinding);
        
        this.modelPaintCommand.init();
        
        //        imageBinding.addListener((observable, oldValue, newValue) -> {
        //            if (newValue != null)
        //        });
        //        modelPaintCommand.imageProperty().bind(imageBinding);
        
        modelPaintCommand.boundsBinding().widthProperty().set(getGameMap().getTileSize());
        modelPaintCommand.boundsBinding().heightProperty().set(getGameMap().getTileSize());
        
        //        modelPaintCommand.boundsBinding().xProperty().bind(xPaintPositionBinding);
        //        modelPaintCommand.boundsBinding().yProperty().bind(yPaintPositionBinding);
        
        paintBoundsBinding.addListener((observable, oldValue, newValue) -> needsUpdate = true);
        imageBinding.addListener((observable, oldValue, newValue) -> needsUpdate = true);
        
        modelPaintCommand.setPaintPriority(1);
        
        getGameMap().getModel().getCanvas().addPaintable(modelPaintCommand);
        
        setImageData(null, null);
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    
    public final ImagePaintCommand getPaintCommand() { return modelPaintCommand; }
    
    
    private IntegerBinding xPaintPositionBinding() { return xPaintPositionBinding; }
    private int getPaintPositionX() { return xPaintPositionBinding.get(); }
    
    private IntegerBinding yPaintPositionBinding() { return yPaintPositionBinding; }
    private int getPaintPositionY() { return yPaintPositionBinding.get(); }
    
    private ObjectBinding<Bounds> paintBoundsBinding() { return paintBoundsBinding; }
    private Bounds getPaintBounds() { return paintBoundsBinding.get(); }
    
    
    public final ObjectBinding<Image> imageBinding() { return imageBinding; }
    public final Image getImage() { return imageBinding.get(); }
    
    
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
    @Override public @NotNull Lock getLock() { return owner.getLock(); }
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    //
    
    @Override public void onGfxUpdate() {
        modelPaintCommand.boundsBinding().setBounds(getPaintBounds());
        modelPaintCommand.setImage(getImage());
        
        needsUpdate = false;
    }
    @Override public boolean needsGfxUpdate() { return needsUpdate; }
    
    //
    
    @Override public String getJID() {
        return "game-object-model";
    }
    @Override public void load(JsonObject parent) {
        setImageId(JUtil.loadString(parent, "image-id"));
        setImageType(JUtil.loadString(parent, "image-type"));
    }
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("image-id", getImageId()),
                JUtil.create("image-type", getImageType())
        };
    }
    
    //
    
    @Override public @NotNull TaskManager<GameObjectModel> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    void shutdown() {
        getGameMap().getModel().getCanvas().removePaintable(modelPaintCommand);
    }
}
