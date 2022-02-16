package com.taco.suit_lady.game.galaxy.abilities;

import com.taco.suit_lady.game.galaxy.effects.Effect;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;

public class Ability_InstantEffect extends Ability {
    
    private final GameObject owner;
    
    private final ReadOnlyObjectWrapper<Effect> effectProperty;
    
    public Ability_InstantEffect(@NotNull GameObject owner) {
        this.owner = owner;
        
        this.effectProperty = new ReadOnlyObjectWrapper<>();
    }
    
    public final ReadOnlyObjectProperty<Effect> readOnlyEffectProperty() { return effectProperty.getReadOnlyProperty(); }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameObject getOwner() { return owner; }
    
    @Override public void tick(@NotNull LogiCore logiCore) {
    
    }
    
    //</editor-fold>
}
