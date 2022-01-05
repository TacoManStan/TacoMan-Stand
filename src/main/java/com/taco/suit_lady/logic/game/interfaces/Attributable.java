package com.taco.suit_lady.logic.game.interfaces;

import java.util.function.Predicate;

public interface Attributable {
    
    default <V> V get(String key) {
        return get(key, null);
    }
    
    <V> V get(String key, V defaultValue);
    
    
    <V> V add(String key, V attribute, boolean replaceIfPresent);
    
    default <V> V addIf(String key, V attribute, Predicate<V> filter, boolean replaceIfPresent) {
        return filter.test(attribute) ? add(key, attribute, replaceIfPresent) : null;
    }
    
    
    <V> V remove(String key);
    
    <V> V remove(String key, V attribute);
    
    default <V> V removeIf(String key, Predicate<V> filter) {
        return removeIf(key, null, filter);
    }
    
    <V> V removeIf(String key, V defaultValue, Predicate<V> filter);
}
