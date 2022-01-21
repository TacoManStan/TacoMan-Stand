package com.taco.suit_lady.ui.jfx.util;

import javafx.geometry.Point2D;
import org.jetbrains.annotations.Nullable;

public interface Boundable {
    
    int getX();
    int getY();
    
    int getWidth();
    int getHeight();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default Bounds getBounds() { return new Bounds(getX(), getY(), getWidth(), getHeight()); }
    default Point2D getDimensions() { return new Point2D(getWidth(), getHeight()); }
    
    
    default int getMinX() { return getX(); }
    default int getMaxX() { return getMinX() + getWidth(); }
    default int getMinY() { return getY(); }
    default int getMaxY() { return getMinY() + getHeight(); }
    default double getMidX() { return getMinX() + (getWidth() / 2d); }
    default double getMidY() { return getMinY() + (getHeight() / 2d); }
    
    
    /**
     * <p>Returns a {@link Point2D} object representing a coordinate point on this {@link Boundable} instance, defined by the specified {@link LocationDefinition LocationDefinition}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@link LocationDefinition LocationDefinition} is {@code null}, the {@link LocationDefinition#getDefault() default} value — <i>{@link LocationDefinition#TOP_LEFT LocationDefinition.TOP_LEFT}</i>, as defined by <i>{@link LocationDefinition#getDefault() LocationDefinition.getDefault()}</i> — is used instead.</li>
     * </ol>
     *
     * @param locationDefinition The {@link LocationDefinition LocationDefinition} enum value representing which {@link Point2D point} on this {@link Boundable} should be returned.
     *
     * @return A {@link Point2D} object representing a coordinate point on this {@link Boundable} instance, defined by the specified {@link LocationDefinition LocationDefinition}.
     */
    default Point2D getLocation(@Nullable LocationDefinition locationDefinition) {
        locationDefinition = locationDefinition != null ? locationDefinition : LocationDefinition.getDefault();
        switch (locationDefinition) {
            default -> new Point2D(getMinX(), getMinY());
            
            case TOP_RIGHT -> new Point2D(getMaxX(), getMinY());
            case BOTTOM_LEFT -> new Point2D(getMinX(), getMaxY());
            case BOTTOM_RIGHT -> new Point2D(getMaxX(), getMaxY());
            
            case CENTER -> new Point2D(getMidX(), getMidY());
            case CENTER_LEFT -> new Point2D(getMinX(), getMidY());
            case CENTER_RIGHT -> new Point2D(getMaxX(), getMidY());
            case CENTER_TOP -> new Point2D(getMidX(), getMinY());
            case CENTER_BOTTOM -> new Point2D(getMidX(), getMaxY());
        }
    }
    
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
    
    /**
     * <p>An {@code enum} that defines a {@link Point2D point} on this {@link Boundable}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Used in calculation methods such as <i>{@link #getLocation(LocationDefinition) getLocation(LocationType)}</i>.</li>
     * </ol>
     */
    enum LocationDefinition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        CENTER, CENTER_LEFT, CENTER_RIGHT, CENTER_TOP, CENTER_BOTTOM
        
        public static LocationDefinition getDefault() { return LocationDefinition.TOP_LEFT; }
        
        }
}
