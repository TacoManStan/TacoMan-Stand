package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.galaxy.effects.specific.Effect_MissileImpact;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.objects.Mover;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.tools.printing.PrintData;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.values.Value2D;
import com.taco.suit_lady.util.values.numbers.N;
import com.taco.suit_lady.util.values.numbers.Num2D;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.geometry.Point2D;
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
        final Point2D target = (Point2D) params.get("target");
        final Supplier<GameObject> factory = (Supplier<GameObject>) params.get("factory");
        final GameObject spawnedObj = factory.get().init();
        spawnedObj.setLocation(target, true);
        spawnedObj.addToMap();
        return true;
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Point2D.class),
                             new Value2D<>("factory", Supplier.class));
    }
    
    //</editor-fold>
}
