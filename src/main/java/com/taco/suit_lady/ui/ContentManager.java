package com.taco.suit_lady.ui;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasContentPane;
import com.taco.suit_lady.ui.jfx.components.ContentPane;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

public class ContentManager
        implements Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ContentPane contentBase;
    private final ReadOnlyObjectWrapper<Content<?, ?>> contentProperty; // Add support for a list of overlapping Content, each overlapping on the Content Base StackPane?
    
    public ContentManager(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx) {
        this.weaver = SLExceptions.nullCheck(weaver, "FxWeaver");
        this.ctx = SLExceptions.nullCheck(ctx, "Application Context");
        
        this.contentBase = new ContentPane(this) {
            @Override
            protected @NotNull StackPane loadForegroundPane() {
                return new CanvasContentPane(this);
            }
            
            @Override
            protected @NotNull StackPane loadContentPane() {
                return new CanvasContentPane(this);
            }
            
            @Override
            protected @NotNull StackPane loadBackgroundPane() {
                return new CanvasContentPane(this);
            }
        };
        this.contentProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.contentProperty.addListener((observable, oldValue, newValue) -> onChange(oldValue, newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="--- INTERNAL UI PROPERTIES ---">
    
    protected ContentPane getContentBasePane() {
        return contentBase;
    }
    
    //
    
    protected final @NotNull CanvasContentPane getInternalForegroundBasePane() {
        return (CanvasContentPane) getContentBasePane().getForegroundPane();
    }
    
    protected final @NotNull CanvasContentPane getInternalContentBasePane() {
        return (CanvasContentPane) getContentBasePane().getContentPane();
    }
    
    protected final @NotNull CanvasContentPane getInternalBackgroundBasePane() {
        return (CanvasContentPane) getContentBasePane().getBackgroundPane();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CONTENT UI PROPERTIES ---">
    
    // NODE STACK ORDER: background pane -> backdrop canvas -> primary pane -> overlay canvas -> foreground pane
    
    public final @NotNull StackPane getContentForegroundPane() {
        return getInternalContentBasePane().getForegroundPane();
    }
    
    public final @NotNull StackPane getContentPrimaryPane() {
        return getInternalContentBasePane().getContentPane();
    }
    
    public final @NotNull StackPane getContentBackgroundPane() {
        return getInternalContentBasePane().getBackgroundPane();
    }
    
    //
    
    public final @NotNull CanvasSurface getContentOverlayCanvas() {
        return getInternalContentBasePane().getOverlayCanvas();
    }
    
    public final @NotNull CanvasSurface getContentBackdropCanvas() {
        return getInternalContentBasePane().getBackdropCanvas();
    }
    
    //</editor-fold>
    
    public @NotNull ReadOnlyObjectProperty<Content<?, ?>> contentProperty() {
        return contentProperty.getReadOnlyProperty();
    }
    
    public @Nullable Content<?, ?> getContent() {
        return contentProperty.get();
    }
    
    public boolean setContent(@Nullable Content<?, ?> newContent) {
        contentProperty.set(newContent);
        return true; // TODO - Add actual validity checks here
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return ctx;
    }
    
    //</editor-fold>
    
    private void onChange(@Nullable Content<?, ?> oldContent, @Nullable Content<?, ?> newContent) {
        // TODO - Execute onRemoved() and onSet via a JavaFX Task implementation. For now, though, this will work.
        // When the above is completed, don't forget to update the onRemoved() and onSet() Javadocs as well.
        FXTools.runFX(() -> {
            if (oldContent != null) {
                getContentPrimaryPane().getChildren().remove(oldContent.getController().root());
                oldContent.getController().root().prefWidthProperty().unbind();
                oldContent.getController().root().prefHeightProperty().unbind();
                oldContent.getController().root().maxWidthProperty().unbind();
                oldContent.getController().root().maxHeightProperty().unbind();
                
                getContentPrimaryPane().getChildren().remove(oldContent.getOverlayHandler().root());
                oldContent.getOverlayHandler().root().prefWidthProperty().unbind();
                oldContent.getOverlayHandler().root().prefHeightProperty().unbind();
                oldContent.getOverlayHandler().root().maxWidthProperty().unbind();
                oldContent.getOverlayHandler().root().maxHeightProperty().unbind();
                
                ctx().getBean(LogiCore.class).executor().execute(() -> oldContent.onRemovedInternal());
            }
            if (newContent != null) {
                FXTools.bindToParent(newContent.getController().root(), getContentPrimaryPane(), true);
                FXTools.bindToParent(newContent.getOverlayHandler().root(), getContentPrimaryPane(), true);
                
                //                final List<Overlay> contentOverlays = newContent.getOverlayHandler().overlays().getCopy();
                //                for (Overlay overlay: contentOverlays) {
                //                    System.out.println("Binding overlay... " + overlay.getName());
                //                    FXTools.bindToParent(overlay.root(), getContentForegroundPane(), true);
                //                }
                
                ctx().getBean(LogiCore.class).executor().execute(() -> newContent.onSetInternal());
            }
        }, true);
    }
}
