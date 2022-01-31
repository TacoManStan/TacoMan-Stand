package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.CroppedImagePaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.ImagePaintCommand;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

public class GameObjectModel
        implements SpringableWrapper, GameComponent {
    
    private final GameObject owner;
    
    //
    
    private final ObjectProperty<String> imageIdProperty;
    private final ObjectBinding<Image> imageBinding;
    
    private final ImagePaintCommand modelPaintCommand;
    
    private IntegerBinding xPaintPositionBinding;
    private IntegerBinding yPaintPositionBinding;
    
    public GameObjectModel(@NotNull GameObject owner) {
        this.owner = owner;
        
        //
        
        this.imageIdProperty = new SimpleObjectProperty<>();
        this.imageBinding = BindingsSL.constObjBinding(ResourcesSL.getDummyImage(ResourcesSL.AVATAR_16));
        
        this.modelPaintCommand = new ImagePaintCommand(this, null);
    }
    
    public GameObjectModel init() {
        this.xPaintPositionBinding = BindingsSL.intBinding(() -> -getOwner().getGameMap().getModel().getCamera().getAggregateX() + getOwner().getXLocation(), getOwner().getGameMap().getModel().getCamera().xAggregateBinding(), getOwner().xLocProperty());
        this.yPaintPositionBinding = BindingsSL.intBinding(() -> -getOwner().getGameMap().getModel().getCamera().getAggregateY() + getOwner().getYLocation(), getOwner().getGameMap().getModel().getCamera().yAggregateBinding(), getOwner().yLocProperty());
        
        this.modelPaintCommand.init();
        
        modelPaintCommand.imageProperty().bind(imageBinding);
        
        modelPaintCommand.boundsBinding().widthProperty().set(16);
        modelPaintCommand.boundsBinding().heightProperty().set(16);
        
        modelPaintCommand.boundsBinding().xProperty().bind(xPaintPositionBinding);
        modelPaintCommand.boundsBinding().yProperty().bind(yPaintPositionBinding);
        
        modelPaintCommand.setPaintPriority(1);
        
        getGameMap().getModel().getCanvas().addPaintable(modelPaintCommand);
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    public final ImagePaintCommand getPaintCommand() { return modelPaintCommand; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return owner; }
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    //</editor-fold>
}
