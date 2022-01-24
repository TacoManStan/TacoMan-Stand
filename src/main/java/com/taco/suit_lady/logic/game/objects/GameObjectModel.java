package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

public class GameObjectModel
        implements SpringableWrapper {
    
    private final GameObject owner;
    
    //
    
    private final ObjectProperty<String> imageIdProperty;
    private final ObjectBinding<Image> imageBinding;
    
    public GameObjectModel(@NotNull GameObject owner) {
        this.owner = owner;
        
        //
        
        this.imageIdProperty = new SimpleObjectProperty<>();
        this.imageBinding = BindingsSL.constObjBinding(ResourcesSL.getDummyImage(ResourcesSL.AVATAR));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() {
        return owner;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return owner;
    }
    
    //</editor-fold>
}
