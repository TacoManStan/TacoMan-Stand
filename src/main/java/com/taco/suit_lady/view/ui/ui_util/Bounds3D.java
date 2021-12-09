package com.taco.suit_lady.view.ui.ui_util;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;

public record Bounds3D(int x, int y, int z, int width, int height, int depth)
{
    public Bounds asBounds()
    {
        return new BoundingBox(x, y, z, width, height, depth);
    }
    
    public Point3D getLocation()
    {
        return new Point3D(x, y, z);
    }
    
    public Point3D getDimensions()
    {
        return new Point3D(width, height, depth);
    }
}
