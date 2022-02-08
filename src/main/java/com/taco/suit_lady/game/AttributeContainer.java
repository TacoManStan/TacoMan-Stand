package com.taco.suit_lady.game;

import com.taco.suit_lady.game.interfaces.Attributable;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class AttributeContainer
        implements Springable, Lockable, Attributable {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final Attributable owner;
    
    private final MapProperty<String, Object> attributes;
    
    public AttributeContainer(@NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull Attributable owner) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.owner = owner;
        
        this.attributes = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Attributable getOwner() {
        return owner;
    }
    
    //<editor-fold desc="--- ATTRIBUTES ---">
    
    public final MapProperty<String, Object> attributeMap() {
        return attributes;
    }
    
    //
    
    @Override
    public final <V> V get(@NotNull String key, @Nullable V defaultValue) {
        return sync(() -> (V) attributes.getOrDefault(key, defaultValue));
    }
    
    
    @Override
    public final <V> V add(String key, V attribute, boolean replaceIfPresent) {
        return sync(() -> {
            if (attributes.containsKey(ExceptionsSL.nullCheck(key, "Property Key")))
                throw ExceptionsSL.ex("Property with name \"" + key + "\" already exists in this map (" + attribute + ")");
            if (attributes.containsValue(ExceptionsSL.nullCheck(attribute, "Property Value")))
                throw ExceptionsSL.ex("Property \"" + attribute + "\" already exists in this map.");
            
            return (V) attributes.put(key, attribute);
        });
    }
    
    
    @Override
    public final <V> V remove(@NotNull String key) {
        return sync(() -> (V) attributes.remove(key));
    }
    
    @Override
    public final <V> V remove(@NotNull String key, V attribute) {
        return (V) sync(() -> attributes.remove(key, attribute));
    }
    
    @Override
    public final <V> V removeIf(@NotNull String key, @Nullable V defaultValue, @NotNull Predicate<V> filter) {
        return sync(() -> {
            final V attribute = get(key);
            return attribute != null && filter.test(attribute) ? (V) attributes.remove(key) : null;
        });
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public @NotNull ReentrantLock getLock() {
        return lock;
    }
    
    //</editor-fold>
}
