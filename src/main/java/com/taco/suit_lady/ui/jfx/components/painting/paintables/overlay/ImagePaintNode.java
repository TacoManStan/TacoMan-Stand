package com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ResourcesSL;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class ImagePaintNode extends PaintNode {
    
    private final ImageView imageView;
    
    public ImagePaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.imageView = new ImageView(ResourcesSL.getDummyImage(ResourcesSL.AVATAR));
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected Node refreshNode() { return imageView; }
    @Override protected Node syncBounds(@NotNull Node n) {
        imageView.setX(getX(true));
        imageView.setY(getY(true));
        imageView.setFitWidth(getWidth(true));
        imageView.setFitHeight(getHeight(true));
        return imageView;
    }
    
    //</editor-fold>
}
