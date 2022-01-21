package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.PropertyTools;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.concurrent.locks.ReentrantLock;

public class TextPainter extends ShapePainter {
    
    private final StringProperty textProperty;
    private final IntegerProperty maxWidthProperty;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public TextPainter(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this(springable, lock, null, 0);
    }
    
    public TextPainter(@NotNull Springable springable, @Nullable ReentrantLock lock, int maxWidth) {
        this(springable, lock, null, maxWidth);
    }
    
    public TextPainter(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String text) {
        this(springable, lock, text, 0);
    }
    
    
    public TextPainter(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String text, int maxWidth) {
        super(springable, lock);
        
        this.textProperty = new SimpleStringProperty(text);
        this.maxWidthProperty = new SimpleIntegerProperty(maxWidth);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final StringProperty textProperty() { return textProperty; }
    public final String getText() { return textProperty.get(); }
    public final String setText(@Nullable String newValue) { return PropertyTools.setProperty(textProperty, newValue); }
    
    public final IntegerProperty maxWidthProperty() { return maxWidthProperty; }
    public final int getMaxWidth() { return maxWidthProperty.get(); }
    public final int setMaxWidth(int newValue) { return PropertyTools.setProperty(maxWidthProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull TextPainter init() {
        maxWidthProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < 0)
                throw ExceptionTools.unsupported("Max Width must be non-negative (" + newValue.intValue() + ")");
        });
        
        return (TextPainter) super.init();
    }
    
    @Override protected void onPaint() {
        final Bounds bounds = getBounds();
        final String text = getText();
        if (text != null && isValidDimensions())
            if (isFill())
                getSurface().getGraphicsContext2D().fillText(text, bounds.getX(true), bounds.getY(true));
            else
                getSurface().getGraphicsContext2D().strokeText(text, bounds.getX(true), bounds.getY(true));
    }
    
    //</editor-fold>
}
