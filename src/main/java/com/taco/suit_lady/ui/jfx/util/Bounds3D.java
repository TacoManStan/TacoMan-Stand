package com.taco.suit_lady.ui.jfx.util;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point3D;

public record Bounds3D(int x, int y, int z, int width, int height, int depth)
{
    public javafx.geometry.Bounds asBounds()
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
    
    public Bounds xyBounds2D()
    {
        return new Bounds(x, y, width, height);
    }
    
    public Bounds xzBounds2D()
    {
        return new Bounds(x, z, width, depth);
    }
    
    @SuppressWarnings("SuspiciousNameCombination")
    public Bounds yzBounds2D()
    {
        return new Bounds(y, z, height, depth);
    }
}
