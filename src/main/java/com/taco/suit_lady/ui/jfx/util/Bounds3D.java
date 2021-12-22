package com.taco.suit_lady.ui.jfx.util;

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
    
    public Bounds2D xyBounds2D()
    {
        return new Bounds2D(x, y, width, height);
    }
    
    public Bounds2D xzBounds2D()
    {
        return new Bounds2D(x, z, width, depth);
    }
    
    @SuppressWarnings("SuspiciousNameCombination")
    public Bounds2D yzBounds2D()
    {
        return new Bounds2D(y, z, height, depth);
    }
}
