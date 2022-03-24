package com.taco.tacository.ui.jfx.components;

import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.fx_tools.FX;
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
        this.springable = Exc.nullCheck(springable, "Springable Input").asStrict();
        
        this.foregroundPane = loadForegroundPane();
        this.contentPane = loadContentPane();
        this.backgroundPane = loadBackgroundPane();
        
        FX.bindToParent(foregroundPane, this, true);
        FX.bindToParent(contentPane, this, true);
        FX.bindToParent(backgroundPane, this, true);
        
        FX.togglePickOnBounds(this, false);
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
