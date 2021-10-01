package com.taco.suit_lady.view.ui.content;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.Pane;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ContentView<P extends Pane>
{
    
    private final ReentrantLock lock;
    private final ReadOnlyObjectWrapper<P> contentProperty;
    
    public ContentView()
    {
        this(null);
    }
    
    public ContentView(P content)
    {
        this.lock = new ReentrantLock();
        this.contentProperty = new ReadOnlyObjectWrapper<>(content);
    }
    
    //<editor-fold desc="Properties">
    
    public final ReadOnlyObjectProperty<P> contentProperty()
    {
        return contentProperty.getReadOnlyProperty();
    }
    
    public final P getContent()
    {
        return contentProperty.get();
    }
    
    public final void setContent(P content)
    {
        P _oldContent = getContent();
        contentProperty.set(content);
        onContentChange(_oldContent, content);
    }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="Abstract">
    
    protected abstract void onContentChange(P oldContent, P newContent);
    
    //</editor-fold>
}
