package com.taco.suit_lady.logic;

import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Tickable
        extends Springable {
    
    void tick();
    
    //
    
    default boolean hasSubActions() { return false; }
    default @Nullable List<Tickable> subActions() { return null; }
}
