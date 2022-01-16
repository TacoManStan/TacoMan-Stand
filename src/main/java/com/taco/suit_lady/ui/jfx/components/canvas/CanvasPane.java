package com.taco.suit_lady.ui.jfx.components.canvas;

import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public class CanvasPane extends AnchorPane
        implements Springable {
    
    private final Springable springable;
    
    private final BoundCanvas canvas;
    
    public CanvasPane(@NotNull Springable springable) {
        this(springable, 0, 0, 0, 0);
    }
    
    public CanvasPane(@NotNull Springable springable, double anchor) {
        this(springable, anchor, anchor, anchor, anchor);
    }
    
    public CanvasPane(@NotNull Springable springable, @NotNull Bounds bounds) {
        this(springable, bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY());
    }
    
    public CanvasPane(@NotNull Springable springable, double leftAnchor, double rightAnchor, double topAnchor, double bottomAnchor) {
        this.springable = springable.asStrict();
        
        this.canvas = new BoundCanvas(this);
//        this.canvas.setStyle("-fx-border-color: red");
//        this.setStyle("-fx-border-color: blue");
        
        getChildren().add(canvas);
        FXTools.setAnchors(canvas, leftAnchor, rightAnchor, topAnchor, bottomAnchor);
    }
    
    public final BoundCanvas canvas() {
        return canvas;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    //</editor-fold>
}
