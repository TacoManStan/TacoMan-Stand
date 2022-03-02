package com.taco.suit_lady.util.tools.printer;

import com.taco.suit_lady.util.enums.DefaultableEnum;
import org.jetbrains.annotations.NotNull;

public enum OnAbsentDefinition
        implements DefaultableEnum<OnAbsentDefinition> {
    
    DO_NOTHING, CREATE_NEW, USE_GLOBAL, THROW_EXCEPTION;
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull OnAbsentDefinition defaultValue() { return OnAbsentDefinition.DO_NOTHING; }
    
    //</editor-fold>
}
