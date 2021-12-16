package com.taco.suit_lady.view.ui.overlay.paint_commands;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ResourceTools;
import com.taco.suit_lady.view.ui.jfx.components.ImagePane;
import com.taco.suit_lady.view.ui.overlay.Overlay;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class SLImagePaintCommand extends SLPaintCommand<ImagePane> {
    
    private final ImagePane imagePane;
    
    public SLImagePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<ImagePane>> autoRemoveCondition,
            boolean scaleToParent, int priority,
            Image image) {
        super(lock, springable, name, autoRemoveCondition, scaleToParent, priority);
        
        this.imagePane = new ImagePane(ResourceTools.get().getImage("/", "Flork_of_Taco", "png"));
    }
    
    // TODO: Add additional painting properties, probably to the parent intermediary SLShapePaintCommand class
    @Override
    protected ImagePane refreshNode() {
        return sync(() -> {
            imagePane.resizeRelocate(getX(), getY(),
                                     getWidth() > 0 ? getWidth() : 1,
                                     getHeight() > 0 ? getHeight() : 1);
            return imagePane;
        });
    }
    
    @Override
    protected void onAdded(@NotNull Overlay owner) { }
    
    @Override
    protected void onRemoved(@NotNull Overlay owner) { }
}
