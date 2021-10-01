package com.taco.suit_lady.view.ui.jfx.lists.treehandler;

/**
 * A class that contains all of the necessary information for loading an {@link Object} into the GUI.
 *
 * @param <T> The type of object this {@link WrappingTreeCellData} is to create.
 */
public class WrappingTreeCellData<T> extends TreeCellData<T>
{
    
    /**
     * Constructs a new WrappingTreeCellData with the specified name, parent name, and folder value.
     *
     * @param name       The name.
     * @param parentName The parent name.
     * @param folder     True if the constructed WrappingTreeCellData should be a folder, false otherwise.
     */
    protected WrappingTreeCellData(String name, String parentName, boolean folder, T wrappedObject)
    {
        super(name, parentName, folder);
        setWrappedObject(wrappedObject);
    }
    
    //<editor-fold desc="Implementation">
    
    @Override
    public T createWrappedInstance(Object... creationParams)
    {
        return getWrappedObject();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Static Creation">
    
    public static <T> WrappingTreeCellData<T> create(T wrappedObject)
    {
        return new WrappingTreeCellData<>("NULL", null, false, wrappedObject);
    }
    
    public static <T> WrappingTreeCellData<T> create(String name, T wrappedObject)
    {
        return new WrappingTreeCellData<>(name, null, false, wrappedObject);
    }
    
    public static <T> WrappingTreeCellData<T> create(String name, String parentName, T wrappedObject)
    {
        return new WrappingTreeCellData<>(name, parentName, false, wrappedObject);
    }
    
    //
    
    public static <T> WrappingTreeCellData<T> createFolder(String name, String parentName)
    {
        return new WrappingTreeCellData<>(name, parentName, true, null);
    }
    
    public static <T> WrappingTreeCellData<T> createFolder(String name)
    {
        return new WrappingTreeCellData<>(name, null, true, null);
    }
    
    //</editor-fold>
}
