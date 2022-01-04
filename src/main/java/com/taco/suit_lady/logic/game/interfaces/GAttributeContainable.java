package com.taco.suit_lady.logic.game.interfaces;

import com.taco.suit_lady.logic.game.GAttributeContainer;
import com.taco.suit_lady.util.Lockable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface GAttributeContainable
        extends GAttributable, Lockable {
    
    @NotNull GAttributeContainer attributes();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    @Override
    default <V> V get(String key, V defaultValue) {
        return attributes().get(key, defaultValue);
    }
    
    
    @Override
    default <V> V add(String key, V attribute, boolean replaceIfPresent) {
        return attributes().add(key, attribute, replaceIfPresent);
    }
    
    
    @Override
    default <V> V remove(String key) {
        return attributes().remove(key);
    }
    
    @Override
    default <V> V remove(String key, V attribute) {
        return attributes().remove(key, attribute);
    }
    
    @Override
    default <V> V removeIf(String key, V defaultValue, Predicate<V> filter) {
        return attributes().removeIf(key, defaultValue, filter);
    }
    
    //</editor-fold>
}
