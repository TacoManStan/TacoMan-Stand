package com.taco.suit_lady.ui.jfx.util;

public interface Boundable {
    
    int getX();
    int getY();
    
    int getWidth();
    int getHeight();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default Bounds2D getBounds() {
        return new Bounds2D(getX(), getY(), getWidth(), getHeight());
    }
    
    //</editor-fold>
}
