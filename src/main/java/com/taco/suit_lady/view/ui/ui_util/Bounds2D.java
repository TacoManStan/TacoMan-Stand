package com.taco.suit_lady.view.ui.ui_util;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

public record Bounds2D(int x, int y, int width, int height)
{
    public java.awt.Rectangle asAWT()
    {
        return new java.awt.Rectangle(x, y, width, height);
    }
    
    public javafx.scene.shape.Rectangle asFX()
    {
        return new javafx.scene.shape.Rectangle(x, y, width, height);
    }
    
    public Bounds asBounds()
    {
        return new BoundingBox(x, y, width, height);
    }
    
    public Point2D getLocation()
    {
        return new Point2D(x, y);
    }
    
    public Point2D getDimensions()
    {
        return new Point2D(width, height);
    }
}
