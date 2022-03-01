package com.taco.suit_lady.util.tools.printer;

import org.jetbrains.annotations.Nullable;

public enum OnAbsentDefinition {
    
    DO_NOTHING, CREATE_NEW, USE_GLOBAL, THROW_EXCEPTION;
    
    public static OnAbsentDefinition get(@Nullable OnAbsentDefinition def) {
        return def != null ? def : OnAbsentDefinition.DO_NOTHING;
    }
}
