package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.util.tools.fxtools.FXTools;
import javafx.scene.layout.AnchorPane;

public class CanvasPane extends AnchorPane
{
    private final BoundCanvas canvas;
    
    public CanvasPane()
    {
        this(0, 0, 0, 0);
    }
    
    public CanvasPane(double left, double right, double top, double bottom)
    {
        this.canvas = new BoundCanvas();
        
        getChildren().add(canvas);
        FXTools.get().setAnchors(canvas, left, right, top, bottom);
    }
    
    public final BoundCanvas canvas()
    {
        return canvas;
    }
}
