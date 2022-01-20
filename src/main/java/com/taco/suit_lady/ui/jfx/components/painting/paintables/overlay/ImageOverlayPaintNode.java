package com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ResourceTools;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class ImageOverlayPaintNode extends OverlayPaintNode {
    
    private final ImageView imageView;
    
    public ImageOverlayPaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.imageView = new ImageView(ResourceTools.get().getDummyImage(ResourceTools.AVATAR));
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected Node refreshNode() { return imageView; }
    @Override protected Node syncBounds(@NotNull Node n) {
        imageView.setX(getX());
        imageView.setY(getY());
        imageView.setFitWidth(getWidth());
        imageView.setFitHeight(getHeight());
        return imageView;
    }
    
    //</editor-fold>
}
