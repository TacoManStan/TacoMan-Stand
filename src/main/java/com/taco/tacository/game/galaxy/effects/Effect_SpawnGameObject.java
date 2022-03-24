package com.taco.tacository.game.galaxy.effects;

import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Effect_SpawnGameObject extends Effect_Targeted {
    
    public Effect_SpawnGameObject(@NotNull GameObject source) {
        super(source);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean onTrigger(@NotNull Map<String, Object> params) {
        final Num2D target = (Num2D) params.get("target");
        final Supplier<GameObject> factory = (Supplier<GameObject>) params.get("factory");
        final GameObject spawnedObj = factory.get().init();
        spawnedObj.setLocation(target, true);
        spawnedObj.addToMap();
        return true;
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Num2D.class),
                             new Value2D<>("factory", Supplier.class));
    }
    
    //</editor-fold>
}
