package com.taco.suit_lady.ui.jfx.util;

import javafx.geometry.Point2D;
import org.docx4j.wml.P;

public interface Boundable {
    
    int getX();
    int getY();
    
    int getWidth();
    int getHeight();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default Bounds getBounds() { return new Bounds(getX(), getY(), getWidth(), getHeight()); }
    
    default Point2D getDimensions() { return new Point2D(getWidth(), getHeight()); }
    default Point2D getLocation() { return new Point2D(getX(), getY()); }
    
    //<editor-fold desc="> Safe/Fallback Accessors">
    
    default int getSafeX() {
        int x = getX();
        return x > 0 ? x : fallbackX();
    }
    default int getSafeY() {
        int y = getY();
        return y > 0 ? y : fallbackY();
    }
    
    default int getSafeWidth() {
        int width = getWidth();
        return width > 0 ? width : fallbackWidth();
    }
    default int getSafeHeight() {
        int height = getHeight();
        return height > 0 ? height : fallbackHeight();
    }
    
    //
    
    default int fallbackX() { return 1; }
    default int fallbackY() { return 1; }
    
    default int fallbackWidth() { return 1; }
    default int fallbackHeight() { return 1; }
    
    //</editor-fold>
    
    //</editor-fold>
}
