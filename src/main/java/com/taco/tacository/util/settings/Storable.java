package com.taco.tacository.util.settings;

public interface Storable
{
    default void onLoad(boolean successful) { }
    
    default void onSave(boolean successful) { }
}
