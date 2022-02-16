package com.taco.suit_lady.logic.legacy;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TickableMk1
        extends Springable {
    
    void tick(@NotNull LogiCore logiCore);
    
    //
    
    default boolean hasSubActions() { return false; }
    default @Nullable List<TickableMk1> subActions() { return null; }
    default boolean autoRemove() { return false; }
}
