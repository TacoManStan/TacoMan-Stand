package com.taco.suit_lady.game.galaxy.abilities.specific;

import com.taco.suit_lady.game.galaxy.abilities.Ability_InstantEffect;
import com.taco.suit_lady.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.suit_lady.game.galaxy.effects.Effect_Blink;
import com.taco.suit_lady.game.galaxy.effects.Effect_LaunchMissile;
import com.taco.suit_lady.game.galaxy.effects.specific.Effect_Cleave;
import com.taco.suit_lady.game.objects.GameObject;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public class Ability_Cleave extends Ability_InstantEffect {
    
    private final ReadOnlyDoubleWrapper cleaveAngleProperty;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    {
        this.cleaveAngleProperty = new ReadOnlyDoubleWrapper(45);
    }
    
    public Ability_Cleave(@NotNull GameObject source) {
        super(source);
    }
    
    public Ability_Cleave(@NotNull GameObject source, @Nullable Lock lock) {
        super(source, lock);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean execute(@NotNull Map<String, Object> params) {
        params.put("target_angle", cleaveAngleProperty.get());
        return Effect_Cleave.newInstance(getSource()).trigger(params);
    }
    
    //</editor-fold>
}
