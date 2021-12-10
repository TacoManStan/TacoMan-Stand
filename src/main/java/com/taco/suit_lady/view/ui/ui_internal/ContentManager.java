package com.taco.suit_lady.view.ui.ui_internal;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.view.ui.jfx.components.CanvasContentPane;
import com.taco.suit_lady.view.ui.jfx.components.ContentPane;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

public class ContentManager
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ContentPane contentBase;
    private final ReadOnlyObjectWrapper<Content<?, ?>> contentProperty; // Add support for a list of overlapping Content, each overlapping on the Content Base StackPane?
    
    public ContentManager(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx)
    {
        this.weaver = ExceptionTools.nullCheck(weaver, "FxWeaver");
        this.ctx = ExceptionTools.nullCheck(ctx, "Application Context");
        
        this.contentBase = new ContentPane(this)
        {
            @Override
            protected @NotNull StackPane loadForegroundPane()
            {
                return new CanvasContentPane(this);
            }
            
            @Override
            protected @NotNull StackPane loadContentPane()
            {
                return new CanvasContentPane(this);
            }
            
            @Override
            protected @NotNull StackPane loadBackgroundPane()
            {
                return new CanvasContentPane(this);
            }
        };
        this.contentProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.contentProperty.addListener((observable, oldValue, newValue) -> onChange(oldValue, newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="--- INTERNAL UI PROPERTIES ---">
    
    protected ContentPane getContentBasePane()
    {
        return contentBase;
    }
    
    //
    
    protected final @NotNull CanvasContentPane getInternalForegroundBasePane()
    {
        return (CanvasContentPane) getContentBasePane().getForegroundPane();
    }
    
    protected final @NotNull CanvasContentPane getInternalContentBasePane()
    {
        return (CanvasContentPane) getContentBasePane().getContentPane();
    }
    
    protected final @NotNull CanvasContentPane getInternalBackgroundBasePane()
    {
        return (CanvasContentPane) getContentBasePane().getBackgroundPane();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CONTENT UI PROPERTIES ---">
    
    // NODE STACK ORDER: background pane -> backdrop canvas -> primary pane -> overlay canvas -> foreground pane
    
    public final @NotNull StackPane getContentForegroundPane()
    {
        return getInternalContentBasePane().getForegroundPane();
    }
    
    public final @NotNull StackPane getContentPrimaryPane()
    {
        return getInternalContentBasePane().getContentPane();
    }
    
    public final @NotNull StackPane getContentBackgroundPane()
    {
        return getInternalContentBasePane().getBackgroundPane();
    }
    
    //
    
    public final @NotNull BoundCanvas getContentOverlayCanvas()
    {
        return getInternalContentBasePane().getOverlayCanvas();
    }
    
    public final @NotNull BoundCanvas getContentBackdropCanvas()
    {
        return getInternalContentBasePane().getBackdropCanvas();
    }
    
    //</editor-fold>
    
    public @NotNull ReadOnlyObjectProperty<Content<?, ?>> contentProperty()
    {
        return contentProperty.getReadOnlyProperty();
    }
    
    public @Nullable Content<?, ?> getContent()
    {
        return contentProperty.get();
    }
    
    public boolean setContent(@Nullable Content<?, ?> newContent)
    {
        contentProperty.set(newContent);
        return true; // TODO - Add actual validity checks here
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
    
    private void onChange(@Nullable Content<?, ?> oldContent, @Nullable Content<?, ?> newContent)
    {
        // TODO - Execute onRemoved() and onSet via a JavaFX Task implementation. For now, though, this will work.
        // When the above is completed, don't forget to update the onRemoved() and onSet() Javadocs as well.
        FXTools.get().runFX(() -> {
            if (oldContent != null) {
                getContentPrimaryPane().getChildren().remove(oldContent.getController().root());
                
                oldContent.getController().root().prefWidthProperty().unbind();
                oldContent.getController().root().prefHeightProperty().unbind();
                oldContent.getController().root().maxWidthProperty().unbind();
                oldContent.getController().root().maxHeightProperty().unbind();
                
                ctx().getBean(LogiCore.class).execute(() -> oldContent.onRemovedInternal());
            }
            if (newContent != null) {
                FXTools.get().bindToParent(newContent.getController().root(), getContentPrimaryPane(), FXTools.BindOrientation.BOTH, FXTools.BindType.BOTH, true);
                
                ctx().getBean(LogiCore.class).execute(() -> newContent.onSetInternal());
            }
        }, true);
    }
}
