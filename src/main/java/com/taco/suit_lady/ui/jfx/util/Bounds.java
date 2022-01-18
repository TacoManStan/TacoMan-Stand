package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public record Bounds(int x, int y, int width, int height)
        implements Boundable {
    
    //<editor-fold desc="--- CLASS BODY ---">
    
    public int getMinX() { return x(); }
    public int getMinY() { return y(); }
    
    public int getMaxX() { return x() + width(); }
    public int getMaxY() { return y() + height(); }
    
    //</editor-fold>
    
    
    //<editor-fold desc="--- CONVERSION ---">
    
    public @NotNull java.awt.Rectangle asAWT() { return new java.awt.Rectangle(x, y, width, height); }
    public @NotNull javafx.scene.shape.Rectangle asFX() { return new javafx.scene.shape.Rectangle(x, y, width, height); }
    
    public @NotNull javafx.geometry.Bounds asBounds() { return new BoundingBox(x, y, width, height); }
    
    public @NotNull Point2D getLocation() { return new Point2D(x, y); }
    public @NotNull Point2D getDimensions() { return new Point2D(width, height); }
    
    public @NotNull Dimensions asDimensions() { return new Dimensions(width(), height()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC CONVERSION ---">
    
    public static @NotNull Bounds fromRectAWT(@NotNull java.awt.Rectangle rectangle) {
        ExceptionTools.nullCheck(rectangle, "AWT Rectangle");
        return new Bounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
    public static @NotNull Bounds fromRectFX(@NotNull javafx.scene.shape.Rectangle rectangle) {
        ExceptionTools.nullCheck(rectangle, "JFX Rectangle");
        return new Bounds((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }
    
    public static @NotNull Bounds fromPoints(@NotNull Point2D location, @NotNull Point2D dimensions) {
        ExceptionTools.nullCheck(location, "Location Point2D");
        ExceptionTools.nullCheck(dimensions, "Dimensions Point2D");
        
        return new Bounds((int) location.getX(), (int) location.getY(), (int) dimensions.getX(), (int) dimensions.getY());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public int getX() {
        return x();
    }
    
    @Override
    public int getY() {
        return y();
    }
    
    @Override
    public int getWidth() {
        return width();
    }
    
    @Override
    public int getHeight() {
        return height();
    }
    
    
    @Override
    public Bounds getBounds() {
        return this;
    }
    
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
