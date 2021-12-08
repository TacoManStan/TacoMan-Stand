package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.util.tools.fxtools.FXTools;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

public class ContentPane extends StackPane
{
    private final StackPane foregroundPane;
    private final StackPane contentPane;
    private final StackPane backgroundPane;
    
    private final BoundCanvas overlayCanvas;
    private final BoundCanvas backgroundCanvas;
    
    public ContentPane()
    {
        this.foregroundPane = loadForegroundPane();
        this.contentPane = loadContentPane();
        this.backgroundPane = loadBackgroundPane();
        
        FXTools.get().bindToParent(foregroundPane, this, true);
        FXTools.get().bindToParent(contentPane, this, true);
        FXTools.get().bindToParent(backgroundPane, this, true);
        
        this.overlayCanvas = new BoundCanvas();
        this.backgroundCanvas = new BoundCanvas();
        
        FXTools.get().bindToParent(overlayCanvas, foregroundPane, true);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull StackPane getForegroundPane()
    {
        return foregroundPane;
    }
    
    public final @NotNull StackPane getContentPane()
    {
        return contentPane;
    }
    
    public final @NotNull StackPane getBackgroundPane()
    {
        return backgroundPane;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected @NotNull StackPane loadForegroundPane()
    {
        return new StackPane();
    }
    
    protected @NotNull StackPane loadContentPane()
    {
        return new StackPane();
    }
    
    protected @NotNull StackPane loadBackgroundPane()
    {
        return new StackPane();
    }
    
    //</editor-fold>
}
