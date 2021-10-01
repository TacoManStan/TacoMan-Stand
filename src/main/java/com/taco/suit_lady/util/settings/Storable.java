package com.taco.suit_lady.util.settings;

public interface Storable
{
    default void onLoad(boolean successful) { }
    
    default void onSave(boolean successful) { }
}
