package com.taco.suit_lady.game.galaxy.abilities;

import com.taco.suit_lady.game.galaxy.effects.Effect;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.util.tools.Props;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Ability_TargetEffect<E extends Effect> extends Ability {
    
    private final ReadOnlyObjectWrapper<E> effectProperty;
    
    public Ability_TargetEffect(@NotNull GameObject source) {
        super(source);
        
        this.effectProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<E> readOnlyEffectProperty() { return effectProperty.getReadOnlyProperty(); }
    public final E getEffect() { return effectProperty.get(); }
    public final E setEffect(@Nullable E newValue) { return Props.setProperty(effectProperty, newValue); }
    
    //</editor-fold>
}
