package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.util.quick.ConsoleBB;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

public class ContentManagerNew
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final StackPane contentBase;
    private final ReadOnlyObjectWrapper<ContentNew> contentProperty;
    
    public ContentManagerNew(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx, @NotNull StackPane contentBase)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
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
    
    //
    
    public @NotNull ReadOnlyObjectProperty<ContentNew> contentProperty()
    {
        return contentProperty.getReadOnlyProperty();
    }
    
    public @Nullable ContentNew getContent()
    {
        return contentProperty.get();
    }
    
    public boolean setContent(@Nullable ContentNew newContent)
    {
        contentProperty.set(newContent);
        return true; // TODO - Add actual validity checks here
    }
    
    //</editor-fold>
    
    private void onChange(@Nullable ContentNew oldContent, @Nullable ContentNew newContent)
    {
        // TODO - Execute onRemoved() and onSet via a JavaFX Task implementation. For now, though, this will work.
        // When the above is completed, don't forget to update the onRemoved() and onSet() Javadocs as well.
        FXTools.get().runFX(() -> {
            if (oldContent != null) {
                contentBase.getChildren().remove(oldContent.getRoot());
                
                oldContent.getRoot().prefWidthProperty().unbind();
                oldContent.getRoot().prefHeightProperty().unbind();
    
                ctx().getBean(LogiCore.class).execute(() -> oldContent.onRemoved());
            }
            if (newContent != null) {
                ConsoleBB.CONSOLE.print("Adding new content");
                contentBase.getChildren().add(newContent.getRoot());
                
                newContent.getRoot().prefWidthProperty().bind(contentBase.widthProperty());
                newContent.getRoot().prefHeightProperty().bind(contentBase.heightProperty());
    
                ctx().getBean(LogiCore.class).execute(() -> newContent.onSet());
            }
        }, true);
    }
    
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
}
