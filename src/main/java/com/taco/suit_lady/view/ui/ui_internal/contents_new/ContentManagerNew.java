package com.taco.suit_lady.view.ui.ui_internal.contents_new;

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
    
    public ReadOnlyObjectProperty<ContentNew> contentProperty()
    {
        return contentProperty.getReadOnlyProperty();
    }
    
    public ContentNew getContent()
    {
        return contentProperty.get();
    }
    
    public StackPane getContentBase()
    {
        return contentBase;
    }
    
    //</editor-fold>
    
    //
    
    private void onChange(@Nullable ContentNew oldContent, @Nullable ContentNew newContent)
    {
        if (oldContent != null) {
            contentBase.getChildren().remove(oldContent.getContentRoot());
        }
        if (newContent != null) {
            contentBase.getChildren().add(newContent.getContentRoot());
        }
    }
}
