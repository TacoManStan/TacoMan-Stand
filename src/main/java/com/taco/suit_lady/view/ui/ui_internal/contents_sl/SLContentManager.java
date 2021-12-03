package com.taco.suit_lady.view.ui.ui_internal.contents_sl;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
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
    
    private final StackPane contentBase;
    private final ReadOnlyObjectWrapper<SLContent<?, ?>> contentProperty; // Add support for a list of overlapping Content, each overlapping on the Content Base StackPane?
    
    public SLContentManager(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx, @NotNull StackPane contentBase)
    {
        this.weaver = ExceptionTools.nullCheck(weaver, "FxWeaver");
        this.ctx = ExceptionTools.nullCheck(ctx, "Application Context");
        
        this.contentBase = ExceptionTools.nullCheck(contentBase, "Content Base");
        this.contentProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.contentProperty.addListener((observable, oldValue, newValue) -> onChange(oldValue, newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public StackPane getContentBase()
    {
        return contentBase;
    }
    
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
                contentBase.getChildren().remove(oldContent.getController().root());
                
                oldContent.getController().root().prefWidthProperty().unbind();
                oldContent.getController().root().prefHeightProperty().unbind();
    
                ctx().getBean(LogiCore.class).execute(() -> oldContent.onRemovedInternal());
            }
            if (newContent != null) {
                contentBase.getChildren().add(newContent.getController().root());
                
                newContent.getController().root().prefWidthProperty().bind(contentBase.widthProperty());
                newContent.getController().root().prefHeightProperty().bind(contentBase.heightProperty());
    
                ctx().getBean(LogiCore.class).execute(() -> newContent.onSetInternal());
            }
        }, true);
    }
}
