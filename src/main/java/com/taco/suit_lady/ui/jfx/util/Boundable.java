package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.ArraysSL;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Boundable {
    
    int x();
    int y();
    
    int width();
    int height();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default Bounds getBounds() { return new Bounds(x(), y(), width(), height()); }
    default Point2D getDimensions() { return new Point2D(width(), height()); }
    
    default int getMinX(boolean... safe) { return getX(safe); }
    default int getMaxX(boolean... safe) { return getMinX(safe) + getWidth(safe); }
    default int getMinY(boolean... safe) { return getY(safe); }
    default int getMaxY(boolean... safe) { return getMinY(safe) + getHeight(safe); }
    default double getMidX(boolean... safe) { return getMinX(safe) + (getWidth(safe) / 2d); }
    default double getMidY(boolean... safe) { return getMinY(safe) + (getHeight(safe) / 2d); }
    
    
    default @NotNull java.awt.Rectangle asAWT(boolean... safe) { return new java.awt.Rectangle(x(), y(), width(), height()); }
    default @NotNull javafx.scene.shape.Rectangle asJFX(boolean... safe) { return new javafx.scene.shape.Rectangle(x(), y(), width(), height()); }
    
    default @NotNull java.awt.Dimension asDimensionsAWT(boolean... safe) { return new java.awt.Dimension(width(), height()); }
    default @NotNull javafx.geometry.Dimension2D asDimensionsJFX(boolean... safe) { return new javafx.geometry.Dimension2D(width(), height()); }
    
    default @NotNull BoundingBox asBoundingBox(boolean... safe) { return new BoundingBox(x(), y(), width(), height()); }
    
    
    default double getCrossDistance(boolean... safe) {
        return getLocation(LocationDefinition.TOP_LEFT, safe)
                .distance(getLocation(LocationDefinition.BOTTOM_LEFT, safe));
    }
    default double getArea(boolean... safe) { return getWidth(safe) * getHeight(safe); }
    
    
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
    default Point2D getLocation(@Nullable LocationDefinition locationDefinition, boolean... safe) {
        locationDefinition = locationDefinition != null ? locationDefinition : LocationDefinition.getDefault();
        return switch (locationDefinition) {
            default -> new Point2D(getMinX(safe), getMinY(safe));
            
            case TOP_RIGHT -> new Point2D(getMaxX(safe), getMinY(safe));
            case BOTTOM_LEFT -> new Point2D(getMinX(safe), getMaxY(safe));
            case BOTTOM_RIGHT -> new Point2D(getMaxX(safe), getMaxY(safe));
            
            case CENTER -> new Point2D(getMidX(safe), getMidY(safe));
            case CENTER_LEFT -> new Point2D(getMinX(safe), getMidY(safe));
            case CENTER_RIGHT -> new Point2D(getMaxX(safe), getMidY(safe));
            case CENTER_TOP -> new Point2D(getMidX(safe), getMinY(safe));
            case CENTER_BOTTOM -> new Point2D(getMidX(safe), getMaxY(safe));
        };
    }
    
    //<editor-fold desc="> Safe/Fallback Accessors">
    
    default int getX(boolean... safe) {
        int x = x();
        return vargs(safe) ? (x > 0 ? x : fallbackX()) : x;
    }
    default int getY(boolean... safe) {
        int y = y();
        return vargs(safe) ? (y > 0 ? y : fallbackY()) : y;
    }
    default int getWidth(boolean... safe) {
        int width = width();
        return vargs(safe) ? (width > 0 ? width : fallbackWidth()) : width;
    }
    default int getHeight(boolean... safe) {
        int height = height();
        return vargs(safe) ? (height > 0 ? height : fallbackHeight()) : height;
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
     *     <li>Used in calculation methods such as <i>{@link #getLocation(LocationDefinition, boolean...) getLocation(LocationType)}</i>.</li>
     * </ol>
     */
    enum LocationDefinition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        CENTER, CENTER_LEFT, CENTER_RIGHT, CENTER_TOP, CENTER_BOTTOM;
        
        public static LocationDefinition getDefault() { return LocationDefinition.TOP_LEFT; }
    }
    
    /**
     * <p>A helper method that converts an {@code array} of {@code booleans} to a single {@code boolean} value.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code array} is {@code null}, return {@code false}.</li>
     *     <li>If the specified {@code array} is {@code empty} (length of {@code 0}, return {@code false}.</li>
     *     <li>In all other cases, return the {@code first} element in the {@code array}, regardless of its length.</li>
     * </ol>
     *
     * @param varArgs The {@code varargs} of {@code booleans} to be converted into a single {@code boolean} primitive.
     *
     * @return A single {@code boolean} primitive based on the specified {@code varargs array} and the rules listed above.
     */
    private boolean vargs(boolean... varArgs) {
        return varArgs != null && !ArraysSL.isEmpty(varArgs) && varArgs[0];
    }
}
