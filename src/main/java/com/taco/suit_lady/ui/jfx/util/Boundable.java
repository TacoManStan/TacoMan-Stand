package com.taco.suit_lady.ui.jfx.util;

public interface Boundable {
    
    int getX();
    int getY();
    
    int getWidth();
    int getHeight();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default Bounds getBounds() {
        return new Bounds(getX(), getY(), getWidth(), getHeight());
    }
    
    //</editor-fold>
}
