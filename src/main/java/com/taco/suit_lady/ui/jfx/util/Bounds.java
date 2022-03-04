package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.Exc;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public record Bounds(int x, int y, int width, int height)
        implements Boundable {
    
    //<editor-fold desc="--- STATIC CONVERSION ---">
    
    public static @NotNull Bounds fromRectAWT(@NotNull java.awt.Rectangle rectangle) {
        Exc.nullCheck(rectangle, "AWT Rectangle");
        return new Bounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
    public static @NotNull Bounds fromRectFX(@NotNull javafx.scene.shape.Rectangle rectangle) {
        Exc.nullCheck(rectangle, "JFX Rectangle");
        return new Bounds((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }
    
    public static @NotNull Bounds fromPoints(@NotNull Point2D location, @NotNull Point2D dimensions) {
        Exc.nullCheck(location, "Location Point2D");
        Exc.nullCheck(dimensions, "Dimensions Point2D");
        
        return new Bounds((int) location.getX(), (int) location.getY(), (int) dimensions.getX(), (int) dimensions.getY());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Bounds getBounds() { return this; }
    
    //</editor-fold>
    
    
    //<editor-fold desc="--- FOUNDATIONAL ---">
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Bounds bounds))
            return false;
        
        if (x != bounds.x)
            return false;
        if (y != bounds.y)
            return false;
        if (width != bounds.width)
            return false;
        return height == bounds.height;
    }
    
    @Override
    public int hashCode() {
        int result = x;
        
        result = 31 * result + y;
        result = 31 * result + width;
        result = 31 * result + height;
        
        return result;
    }
    
    @Override
    public String toString() {
        return "Bounds2D{" +
               "x=" + x +
               ", y=" + y +
               ", width=" + width +
               ", height=" + height +
               '}';
    }
    
    //</editor-fold>
}
