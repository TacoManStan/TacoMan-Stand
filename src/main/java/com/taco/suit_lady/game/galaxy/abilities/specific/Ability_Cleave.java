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

public class Ability_Cleave extends Ability_TargetEffect<Effect_Cleave> {
    
    private final ReadOnlyDoubleWrapper cleaveAngleProperty;
    private final ReadOnlyDoubleWrapper cleaveRangeProperty;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    {
        this.cleaveAngleProperty = new ReadOnlyDoubleWrapper(45);
        this.cleaveRangeProperty = new ReadOnlyDoubleWrapper(100);
    }
    
    public Ability_Cleave(@NotNull GameObject source) {
        super(source);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean execute(@NotNull Map<String, Object> params) {
        params.put("cleave_size", cleaveAngleProperty.get());
        params.put("cleave_range", cleaveRangeProperty.get());
        return Effect_Cleave.newInstance(getSource()).trigger(params);
    }
    
    //</editor-fold>
}
