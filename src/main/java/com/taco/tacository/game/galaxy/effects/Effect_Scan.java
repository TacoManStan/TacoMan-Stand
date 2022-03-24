package com.taco.tacository.game.galaxy.effects;

import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.util.tools.Props;
import com.taco.tacository.util.tools.list_tools.L;
import com.taco.tacository.util.tools.printing.Printer;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import com.taco.tacository.util.values.numbers.shapes.Circle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Effect_Scan extends Effect_Targeted {
    
    private final ReadOnlyObjectWrapper<Effect_Targeted> scanEffectProperty;
    
    public Effect_Scan(@NotNull GameObject source, @Nullable Effect_Targeted scanEffect) {
        super(source);
        this.scanEffectProperty = new ReadOnlyObjectWrapper<>(scanEffect);
    }
    
    public Effect_Scan(@NotNull GameObject source) { this(source, null); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Effect_Targeted> readOnlyScanEffectProperty() { return scanEffectProperty.getReadOnlyProperty(); }
    public final Effect_Targeted getScanEffect() { return scanEffectProperty.get(); }
    
    public final Effect_Targeted setScanEffect(@Nullable Effect_Targeted newValue) { return Props.setProperty(scanEffectProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean onTrigger(@NotNull Map<String, Object> params) {
        //TODO: Remove test implementation & make generic
        final GameObject missile = (GameObject) params.get("missile");
        final double radius = (double) params.get("radius");
        final Num2D impactLocation = (Num2D) params.get("impact_location");
        
        final Circle scanZone = new Circle(this, getLock());
        scanZone.setLocation(impactLocation);
        scanZone.setRadius(radius);
        
        Printer.print("Scanning: " + impactLocation + "  |  " + radius);
        
        final ArrayList<GameObject> scannedObjs = getGameMap().scan(
                gameObject -> !gameObject.equals(getSource()) &&
                              !gameObject.equals(missile) &&
                              gameObject.collidesWith(scanZone));
        scannedObjs.forEach(gameObject -> getScanEffect().trigger(L.map(new Value2D<>("target_obj", gameObject))));
        
        return true;
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Num2D.class),
                             new Value2D<>("missile", GameObject.class),
                             new Value2D<>("radius", Number.class),
                             new Value2D<>("impact_location", Num2D.class));
    }
    
    //</editor-fold>
}
