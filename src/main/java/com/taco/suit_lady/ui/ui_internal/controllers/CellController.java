package com.taco.suit_lady.ui.ui_internal.controllers;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

public abstract class CellController<T> extends Controller
{
    private final ReadOnlyObjectWrapper<T> contentsProperty;
    
    public CellController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.contentsProperty = new ReadOnlyObjectWrapper<>();
        this.contentsProperty.addListener((observable, oldInstance, newInstance) -> {
            if (!Objects.equals(oldInstance, newInstance))
                onContentChange(oldInstance, newInstance);
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<T> contentsProperty()
    {
        return contentsProperty.getReadOnlyProperty();
    }
    
    public final T getContents()
    {
        return contentsProperty.get();
    }
    
    public final void setContents(T cellContents)
    {
        contentsProperty.set(cellContents);
    }
    
    //</editor-fold>
    
    protected abstract void onContentChange(T oldCellContents, T newCellContents);
}
