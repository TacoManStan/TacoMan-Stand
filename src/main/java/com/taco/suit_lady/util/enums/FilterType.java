package com.taco.suit_lady.util.enums;

import org.jetbrains.annotations.NotNull;

public enum FilterType
        implements DefaultableEnum<FilterType> {
    
    ANY, ALL, ONE, NONE;
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull FilterType defaultValue() { return FilterType.ALL; }
    
    //</editor-fold>
}
