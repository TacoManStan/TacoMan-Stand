package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.jfx.components.ContentPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasContentPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContentManager
        implements SpringableWrapper {
    
    private final StrictSpringable springable;
    
    //
    
    private final ContentPane contentBase;
    private final ReadOnlyObjectWrapper<Content<?, ?, ?>> contentProperty; // Add support for a list of overlapping Content, each overlapping on the Content Base StackPane?
    
    public ContentManager(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        
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
    
    public @NotNull ReadOnlyObjectProperty<Content<?, ?, ?>> contentProperty() {
        return contentProperty.getReadOnlyProperty();
    }
    
    public @Nullable Content<?, ?, ?> getContent() {
        return contentProperty.get();
    }
    
    public boolean setContent(@Nullable Content<?, ?, ?> newContent) {
        contentProperty.set(newContent);
        return true; // TODO - Add actual validity checks here
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    
    //</editor-fold>
    
    private void onChange(@Nullable Content<?, ?, ?> oldContent, @Nullable Content<?, ?, ?> newContent) {
        // TODO - Execute onRemoved() and onSet via a JavaFX Task implementation. For now, though, this will work.
        // When the above is completed, don't forget to update the onRemoved() and onSet() Javadocs as well.
        ToolsFX.runFX(() -> {
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
                
                logiCore().execute(oldContent::onRemovedInternal);
            }
            if (newContent != null) {
                ToolsFX.bindToParent(newContent.getController().root(), getContentPrimaryPane(), true);
                ToolsFX.bindToParent(newContent.getOverlayHandler().root(), getContentPrimaryPane(), true);
                
                logiCore().execute(newContent::onSetInternal);
            }
        }, true);
    }
    
    protected boolean submitKeyEvent(@NotNull KeyEvent keyEvent) {
        Content<?, ?, ?> content = getContent();
        if (content != null)
            return content.handleKeyEvent(keyEvent);
        return false;
    }
}
