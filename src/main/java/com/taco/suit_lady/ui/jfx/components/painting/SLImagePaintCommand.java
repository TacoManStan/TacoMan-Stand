package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.ui.jfx.util.Bounds2D;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ResourceTools;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class SLImagePaintCommand extends SLPaintCommand<ImageView> {
    
    private final ImageView imageView;
    
    public SLImagePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<ImageView>> autoRemoveCondition, int priority) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(ResourceTools.get().getImage("/", "Flork_of_Taco", "png"));
    }
    
    public SLImagePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<ImageView>> autoRemoveCondition, int priority,
            String imageID, String pathID) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(ResourceTools.get().getImage(pathID, imageID, "png"));
    }
    
    public SLImagePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<ImageView>> autoRemoveCondition, int priority,
            String url) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(url);
    }
    
    public SLImagePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<ImageView>> autoRemoveCondition, int priority,
            Image image) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(image);
    }
    
    @Override
    protected ImageView refreshNode() {
        return imageView;
    }
    
    @Override
    protected void syncBounds(@NotNull ImageView imageView) {
        imageView.setX(getX());
        imageView.setY(getY());
        imageView.setFitWidth(getWidthSafe());
        imageView.setFitHeight(getHeightSafe());
    }
    
    @Override
    protected void onAdded(@NotNull Overlay owner) { }
    
    @Override
    protected void onRemoved(@NotNull Overlay owner) { }
}
