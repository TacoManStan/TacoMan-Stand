package com.taco.tacository.util.tools.printing;

import com.taco.tacository.util.enums.DefaultableEnum;
import org.jetbrains.annotations.NotNull;

public enum AbsentDef
        implements DefaultableEnum<AbsentDef> {
    
    DO_NOTHING, CREATE_NEW, USE_GLOBAL, THROW_EXCEPTION;
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull AbsentDef defaultValue() { return AbsentDef.CREATE_NEW; }
    
    //</editor-fold>
}
