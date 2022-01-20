package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ResourceTools;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class ImageOverlayCommand extends OverlayCommand<ImageView> {
    
    private final ImageView imageView;
    
    public ImageOverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<ImageView>> autoRemoveCondition, int priority) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(ResourceTools.get().getDummyImage(ResourceTools.AVATAR));
    }
    
    public ImageOverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<ImageView>> autoRemoveCondition, int priority,
            String imageID, String pathID) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(ResourceTools.get().getImage(pathID, imageID, "png"));
    }
    
    public ImageOverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<ImageView>> autoRemoveCondition, int priority,
            String url) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(url);
    }
    
    public ImageOverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<ImageView>> autoRemoveCondition, int priority,
            Image image) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.imageView = new ImageView(image);
    }
    
    protected ImageView refreshNode() {
        return imageView;
    }
    
    @Override
    protected void syncBounds(@NotNull ImageView imageView) {
        imageView.setX(getX());
        imageView.setY(getY());
        //TODO
//        imageView.setFitWidth(getWidthSafe());
//        imageView.setFitHeight(getHeightSafe());
    }
}
