package com.taco.tacository.game.galaxy.abilities;

import com.taco.tacository.game.galaxy.effects.Effect;
import com.taco.tacository.game.objects.GameObject;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;


public abstract class Ability_InstantEffect
        extends Ability {
    
    private final ReadOnlyObjectWrapper<Effect> effectProperty;
    
    {
        this.effectProperty = new ReadOnlyObjectWrapper<>();
    }
    
    public Ability_InstantEffect(@NotNull GameObject source) {
        super(source);
    }
    public Ability_InstantEffect(@NotNull GameObject source, @Nullable Lock lock) {
        super(source, lock);
    }
    
    public final ReadOnlyObjectProperty<Effect> readOnlyEffectProperty() { return effectProperty.getReadOnlyProperty(); }
}
