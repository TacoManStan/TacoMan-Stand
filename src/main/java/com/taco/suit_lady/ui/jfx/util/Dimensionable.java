package com.taco.suit_lady.ui.jfx.util;

import org.jetbrains.annotations.NotNull;

public interface Dimensionable {
    
    int getWidth();
    int getHeight();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default @NotNull Dimensions dimensions() { return new Dimensions(getWidth(), getHeight()); }
    
    //</editor-fold>
}
