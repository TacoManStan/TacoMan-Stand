package com.taco.tacository.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository._to_sort.json.JElement;
import com.taco.tacository._to_sort.json.JLoadableObject;
import com.taco.tacository._to_sort.json.JObject;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class GameObjectModelDefinitions
        implements Springable, JObject, JLoadableObject {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private String jID;
    
    //
    
    private final MapProperty<String, GameObjectModelDefinition> definitions;
    
    protected GameObjectModelDefinitions(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.definitions = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }
    
    
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> JSON">
    
    @Override public String getJID() { return jID; }
    @Override public void setJID(String jID) { this.jID = jID; }
    
    
    @Override public void doLoad(JsonObject parent) {
    
    }
    
    @Override public JElement[] jFields() {
        return new JElement[0];
    }
    
    //</editor-fold>
    
    @Override public @NotNull FxWeaver weaver() { return weaver; }
    @Override public @NotNull ConfigurableApplicationContext ctx() { return ctx; }
    
    //</editor-fold>
}
