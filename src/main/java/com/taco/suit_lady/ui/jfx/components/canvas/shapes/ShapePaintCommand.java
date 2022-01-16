package com.taco.suit_lady.ui.jfx.components.canvas.shapes;

import com.taco.suit_lady.ui.jfx.components.canvas.PaintCommand;
import com.taco.suit_lady.ui.jfx.util.Boundable;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertyTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ShapePaintCommand extends PaintCommand
        implements Boundable {
    
    private final BoundsBinding boundsBinding;
    private final BooleanProperty isFillProperty;
    
    public ShapePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        
        this.boundsBinding = new BoundsBinding();
        this.isFillProperty = new SimpleBooleanProperty(false);
        
        isFillProperty.addListener((observable, oldValue, newValue) -> repaintOwners());
        boundsBinding.addListener((observable, oldValue, newValue) -> repaintOwners());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final BoundsBinding boundsBinding() {
        return boundsBinding;
    }
    
    
    public final BooleanProperty isFillProperty() {
        return isFillProperty;
    }
    
    public final boolean isFill() {
        return isFillProperty.get();
    }
    
    public final boolean setIsFill(boolean newValue) {
        return PropertyTools.setProperty(isFillProperty, newValue);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public int getX() {
        return boundsBinding().getX();
    }
    
    @Override
    public int getY() {
        return boundsBinding().getY();
    }
    
    @Override
    public int getWidth() {
        return boundsBinding().getWidth();
    }
    
    @Override
    public int getHeight() {
        return boundsBinding().getHeight();
    }
    
    //</editor-fold>
    
    /**
     * <p>Checks if the dimensions represented by the {@link BoundsBinding bounds} of this {@link ShapePaintCommand} are valid.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link BoundsBinding bounds} location values are ignored.</li>
     *     <li>This may change in the future, but, for now, dimensions are valid if both the {@link BoundsBinding#getWidth() width} and {@link BoundsBinding#getHeight() height} are greater than 0.</li>
     * </ol>
     *
     * @return True if the dimensions are valid, false if they are not.
     */
    public boolean isValidDimensions() {
        return getBounds().getWidth() > 0 && getBounds().getHeight() > 0;
    }
}
