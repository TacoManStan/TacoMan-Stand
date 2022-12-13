package com.taco.tacository.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.ui.GFXObject;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.logic.TaskManager;
import com.taco.tacository.ui.jfx.components.painting.paintables.canvas.ImagePaintCommand;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.tools.Bind;
import com.taco.tacository.util.tools.Stuff;
import com.taco.tacository._to_sort.json.JElement;
import com.taco.tacository._to_sort.json.JLoadable;
import com.taco.tacository._to_sort.json.JObject;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

/**
 * <p>Defines the {@code Graphics Definition} for a {@link GameObject} {@link #getOwner() Owner}.</p>
 */
//TO-EXPAND: Lots.
public class GameObjectModel
        implements SpringableWrapper, Lockable, GameComponent, GFXObject<GameObjectModel>, JObject, JLoadable {
    
    private final GameObject owner;
    private final GameObjectModelDefinition modelDefinition;
    private final TaskManager<GameObjectModel> taskManager;
    
    //
    
    private ObjectBinding<Image> imageBinding;
    private final ImagePaintCommand modelPaintCommand;
    
    private boolean needsUpdate;
    
    public GameObjectModel(@NotNull GameObject owner) {
        this(owner, null, null);
    }
    
    public GameObjectModel(@NotNull GameObject owner, @Nullable String imageType, @Nullable String imageId) {
        this.owner = owner;
        this.modelDefinition = new GameObjectModelDefinition(this, imageType, imageId);
        
        //
        
        this.modelPaintCommand = new ImagePaintCommand(this, null);
        
        this.needsUpdate = false;
        this.taskManager = new TaskManager<>(this).init();
    }
    
    public GameObjectModel init() {
        this.getDefinition().init();
        
        this.imageBinding = Bind.objBinding(
                () -> Stuff.getGameImage(getDefinition().getImageType() + "/", getDefinition().getImageId()),
                getDefinition().readOnlyImageTypeProperty(), getDefinition().readOnlyImageIdProperty());
        
        if (getOwner().isTestObject1())
            printer().get().print("Image: " + getImage());
        
        this.modelPaintCommand.init();
        
        getDefinition().boundsBinding().addListener((observable, oldValue, newValue) -> {
            //            if (getOwner().isTestObject1())
            //                printer().get().print("Model Def Bounds Changed: [" + oldValue + " --> " + newValue + "]");
            needsUpdate = true;
        });
        imageBinding.addListener((observable, oldValue, newValue) -> {
            if (getOwner().isTestObject1())
                printer().get().print("Image Changed: [" + oldValue + " --> " + newValue + "]");
            needsUpdate = true;
        });
        
        modelPaintCommand.setPaintPriority(1);
        
        getGameMap().getModel().getCanvas().addPaintable(modelPaintCommand);
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    public final GameObjectModelDefinition getDefinition() { return modelDefinition; }
    
    
    public final ImagePaintCommand getPaintCommand() { return modelPaintCommand; }
    
    
    public final ObjectBinding<Image> imageBinding() { return imageBinding; }
    public final Image getImage() { return imageBinding.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @NotNull Lock getLock() { return owner.getLock(); }
    
    @Override public @NotNull GameViewContent getContent() { return getOwner().getGame(); }
    
    //
    
    @Override public void onGfxUpdate() {
        if (getOwner().isTestObject1())
            printer().get().print("Setting Gfx Bounds: " + getDefinition().getBounds());
        
        //        printer().get(getClass()).print("Updating GameObject Gfx: " + getDefinition().getBounds());
        
        
        modelPaintCommand.boundsBinding().setBounds(getDefinition().getBounds());
        
        if (getOwner().isTestObject1())
            printer().get().print("Gfx Bounds Set: " + modelPaintCommand.getBounds());
        
        modelPaintCommand.setImage(getImage());
        
        needsUpdate = false;
    }
    @Override public boolean needsGfxUpdate() { return needsUpdate; }
    
    //
    
    @Override public String getJID() {
        return "game-object-model";
    }
    @Override public void load(JsonObject parent) {
        //        setImageId(JUtil.loadString(parent, "image-id"));
        //        setImageType(JUtil.loadString(parent, "image-type"));
    }
    @Override public JElement[] jFields() {
        return new JElement[]{
                //                JUtil.create("image-id", getImageId()),
                //                JUtil.create("image-type", getImageType())
        };
    }
    
    //
    
    @Override public @NotNull TaskManager<GameObjectModel> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    void shutdown() {
        getGameMap().getModel().getCanvas().removePaintable(modelPaintCommand);
    }
}
