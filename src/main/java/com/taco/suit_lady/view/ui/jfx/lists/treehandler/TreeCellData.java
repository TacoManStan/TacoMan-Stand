package com.taco.suit_lady.view.ui.jfx.lists.treehandler;

import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TreeView;

import java.io.Serializable;

/**
 * <p>A wrapper class that contains an {@link #wrappedObjectProperty() ObjectProperty} containing an object of type {@link T} to be displayed in a {@link TreeView}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link TreeCellData} is immutable.</li>
 * </ol>
 *
 * @param <T>
 */
// TO-EXPAND
public abstract class TreeCellData<T>
        implements Nameable, Serializable, UIDProcessable
{
    private final String name;
    private final String parentName;
    private final boolean isFolder;
    
    private final ReadOnlyObjectWrapper<T> wrappedObjectProperty;
    
    protected TreeCellData(String name, String parentName, boolean isFolder)
    {
        this.name = name;
        this.parentName = parentName;
        this.isFolder = isFolder;
        
        this.wrappedObjectProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link String name} of this {@link TreeCellData}.</p>
     *
     * @return The {@link String name} of this {@link TreeCellData}.
     */
    @Override
    public final String getName()
    {
        return name;
    }
    
    public final String getParentName()
    {
        return parentName;
    }
    
    public final boolean isFolder()
    {
        return isFolder;
    }
    
    public final ReadOnlyObjectProperty<T> wrappedObjectProperty()
    {
        return wrappedObjectProperty.getReadOnlyProperty();
    }
    
    public final T getWrappedObject()
    {
        return wrappedObjectProperty.get();
    }
    
    protected final void setWrappedObject(T wrappedObject)
    {
        wrappedObjectProperty.set(wrappedObject);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Implementation">
    
    private UIDProcessor uIDContainer;
    
    @Override
    public UIDProcessor getUIDProcessor()
    {
        if (uIDContainer == null) // Lazy initialization
            uIDContainer = new UIDProcessor("tree-cell-data");
        return uIDContainer;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Abstract">
    
    public abstract T createWrappedInstance(Object... creationParams);
    
    //</editor-fold>
}