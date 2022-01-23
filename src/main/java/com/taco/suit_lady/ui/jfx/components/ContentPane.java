package com.taco.suit_lady.ui.jfx.components;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public class ContentPane extends StackPane
        implements Springable {
    
    private final Springable springable;
    
    private final StackPane foregroundPane;
    private final StackPane contentPane;
    private final StackPane backgroundPane;
    
    public ContentPane(@NotNull Springable springable) {
        this.springable = SLExceptions.nullCheck(springable, "Springable Input").asStrict();
        
        this.foregroundPane = loadForegroundPane();
        this.contentPane = loadContentPane();
        this.backgroundPane = loadBackgroundPane();
        
        FXTools.bindToParent(foregroundPane, this, true);
        FXTools.bindToParent(contentPane, this, true);
        FXTools.bindToParent(backgroundPane, this, true);
        
        FXTools.togglePickOnBounds(this, false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull StackPane getForegroundPane() {
        return foregroundPane;
    }
    
    public final @NotNull StackPane getContentPane() {
        return contentPane;
    }
    
    public final @NotNull StackPane getBackgroundPane() {
        return backgroundPane;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected @NotNull StackPane loadForegroundPane() {
        return new StackPane();
    }
    
    protected @NotNull StackPane loadContentPane() {
        return new StackPane();
    }
    
    protected @NotNull StackPane loadBackgroundPane() {
        return new StackPane();
    }
    
    //</editor-fold>
    
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
