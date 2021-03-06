package com.taco.tacository.ui.jfx.components.painting.paintables.overlay;

import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.Stuff;
import com.taco.tacository.util.values.numbers.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class ImagePaintNode extends PaintNode {
    
    private final ImageView imageView;
    
    public ImagePaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.imageView = new ImageView(Stuff.getGameImage("units/", "taco"));
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected Node refreshNode() { return imageView; }
    @Override protected Node syncBounds(@NotNull Node n) {
        final Bounds constraints = boundsPosDim();
        
        imageView.setX(constraints.xD());
        imageView.setY(constraints.yD());
        imageView.setFitWidth(constraints.wD());
        imageView.setFitHeight(constraints.hD());
        
        return imageView;
    }
    
    //</editor-fold>
}
