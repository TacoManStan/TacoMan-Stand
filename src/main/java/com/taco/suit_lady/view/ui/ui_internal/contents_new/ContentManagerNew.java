package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContentManagerNew
{
    private final StackPane contentBase;
    
    private final ReadOnlyObjectWrapper<ContentNew> contentProperty;
    
    public ContentManagerNew(@NotNull StackPane contentBase)
    {
        this.contentBase = contentBase;
        
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
    
    //
    
    private void onChange(@Nullable ContentNew oldContent, @Nullable ContentNew newContent)
    {
        // TODO - Execute onRemoved() and onSet via a JavaFX Task implementation. For now, though, this will work.
        // When the above is completed, don't forget to update the onRemoved() and onSet() Javadocs as well.
        FXTools.get().runFX(() -> {
            if (oldContent != null) {
                contentBase.getChildren().remove(oldContent.getContentRoot());
                
                oldContent.getContentRoot().prefWidthProperty().unbind();
                oldContent.getContentRoot().prefHeightProperty().unbind();
    
                TB.executor().execute(() -> oldContent.onRemoved());
            }
            if (newContent != null) {
                contentBase.getChildren().add(newContent.getContentRoot());
                
                newContent.getContentRoot().prefWidthProperty().bind(contentBase.widthProperty());
                newContent.getContentRoot().prefHeightProperty().bind(contentBase.heightProperty());
                
                TB.executor().execute(() -> newContent.onSet());
            }
        }, true);
    }
}
