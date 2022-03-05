package com.taco.suit_lady.game.galaxy.effects.specific;

import com.taco.suit_lady.game.galaxy.effects.Effect_Targeted;
import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Effect_MissileImpact extends Effect_Targeted {
    
    public Effect_MissileImpact(@NotNull GameObject source) {
        super(source);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean trigger(@NotNull Map<String, Object> params) {
        final GameObject target = (GameObject) params.get("target");
        target.taskManager().shutdown();
        return true;
    }
    
    //</editor-fold>
}
