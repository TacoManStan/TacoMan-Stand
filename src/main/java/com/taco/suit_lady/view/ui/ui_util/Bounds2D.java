package com.taco.suit_lady.view.ui.ui_util;

import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public record Bounds2D(int x, int y, int width, int height)
{
    //<editor-fold desc="--- CONVERSION ---">
    
    public @NotNull java.awt.Rectangle asAWT()
    {
        return new java.awt.Rectangle(x, y, width, height);
    }
    
    public @NotNull javafx.scene.shape.Rectangle asFX()
    {
        return new javafx.scene.shape.Rectangle(x, y, width, height);
    }
    
    public @NotNull Bounds asBounds()
    {
        return new BoundingBox(x, y, width, height);
    }
    
    public @NotNull Point2D getLocation()
    {
        return new Point2D(x, y);
    }
    
    public @NotNull Point2D getDimensions()
    {
        return new Point2D(width, height);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC CONVERSION ---">
    
    public static @NotNull Bounds2D fromRectAWT(@NotNull java.awt.Rectangle rectangle)
    {
        ExceptionTools.nullCheck(rectangle, "AWT Rectangle");
        return new Bounds2D(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
    public static @NotNull Bounds2D fromRectFX(@NotNull javafx.scene.shape.Rectangle rectangle)
    {
        ExceptionTools.nullCheck(rectangle, "JFX Rectangle");
        return new Bounds2D((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }
    
    public static @NotNull Bounds2D fromPoints(@NotNull Point2D location, @NotNull Point2D dimensions)
    {
        ExceptionTools.nullCheck(location, "Location Point2D");
        ExceptionTools.nullCheck(dimensions, "Dimensions Point2D");
        
        return new Bounds2D((int) location.getX(), (int) location.getY(), (int) dimensions.getX(), (int) dimensions.getY());
    }
    
    //</editor-fold>
}
