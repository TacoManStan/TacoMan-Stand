package com.taco.suit_lady.view.ui.ui_internal;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.components.ContentPane;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

public class SLContentManager
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ContentPane contentBase;
    private final ReadOnlyObjectWrapper<SLContent<?, ?>> contentProperty; // Add support for a list of overlapping Content, each overlapping on the Content Base StackPane?
    
    public SLContentManager(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx)
    {
        this.weaver = ExceptionTools.nullCheck(weaver, "FxWeaver");
        this.ctx = ExceptionTools.nullCheck(ctx, "Application Context");
        
        this.contentBase =
                new ContentPane()
                {
                    @Override
                    protected @NotNull StackPane loadForegroundPane()
                    {
                        return new ContentPane();
                    }
                    
                    @Override
                    protected @NotNull StackPane loadContentPane()
                    {
                        return new ContentPane();
                    }
                    
                    @Override
                    protected @NotNull StackPane loadBackgroundPane()
                    {
                        return new ContentPane();
                    }
                };
        this.contentProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.contentProperty.addListener((observable, oldValue, newValue) -> onChange(oldValue, newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected ContentPane getContentBasePane()
    {
        return contentBase;
    }
    
    //
    
    protected final @NotNull ContentPane getInternalForegroundBasePane()
    {
        return (ContentPane) getContentBasePane().getForegroundPane();
    }
    
    protected final @NotNull ContentPane getInternalContentBasePane()
    {
        return (ContentPane) getContentBasePane().getContentPane();
    }
    
    protected final @NotNull ContentPane getInternalBackgroundBasePane()
    {
        return (ContentPane) getContentBasePane().getBackgroundPane();
    }
    
    //
    
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
    
    public @NotNull ReadOnlyObjectProperty<SLContent<?, ?>> contentProperty()
    {
        return contentProperty.getReadOnlyProperty();
    }
    
    public @Nullable SLContent<?, ?> getContent()
    {
        return contentProperty.get();
    }
    
    public boolean setContent(@Nullable SLContent<?, ?> newContent)
    {
        contentProperty.set(newContent);
        return true; // TODO - Add actual validity checks here
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
    
    private void onChange(@Nullable SLContent<?, ?> oldContent, @Nullable SLContent<?, ?> newContent)
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
