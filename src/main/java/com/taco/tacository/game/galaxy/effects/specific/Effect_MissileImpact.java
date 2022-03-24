package com.taco.tacository.game.galaxy.effects.specific;

import com.taco.tacository.game.galaxy.effects.Effect_Targeted;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.util.tools.printing.Printer;
import com.taco.tacository.util.values.Value2D;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Effect_MissileImpact extends Effect_Targeted {
    
    public Effect_MissileImpact(@NotNull GameObject source) {
        super(source);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean onTrigger(@NotNull Map<String, Object> params) {
        final GameObject target = (GameObject) params.get("target_obj");
        Printer.err("Shutting Down Target: " + target);
        target.taskManager().shutdown();
        return true;
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target_obj", GameObject.class));
    }
    
    //</editor-fold>
}
