package com.taco.suit_lady.util;

import com.taco.suit_lady.view.ui.jfx.lists.treehandler.TreeCellData;

/**
 * <p>TO-DOC</p>
 */
public interface UIDProcessable extends UID
{
    
    /**
     * <p>Returns the {@link UIDProcessor} instance for this {@link UIDProcessable} object.</p>
     * <br><hr><br>
     * <h4>Example Implementation</h4>
     * <pre>{@code @Override
     * public UIDProcessor getUIDProcessor()
     * {
     *     if (uIDContainer == null) // Lazy Initialization
     *         uIDContainer = new UIDProcessor("group-name");
     *     return uIDContainer;
     * }}</pre>
     * <p><b>Details</b></p>
     * <ol>
     * <li>In most cases, it is recommended that {@link UIDProcessor} objects are stored as lazy-initialized singleton field variables as shown in the example above.</li>
     * </ol>
     *
     * @return The {@link UIDProcessor} instance for this {@link UIDProcessable} object.
     */
    UIDProcessor getUIDProcessor();
    
    @Override
    default String getUID(Object... params)
    {
        return getUIDProcessor().getUID(params);
    }
    
    @Override
    default String getGroupID()
    {
        return getUIDProcessor().getGroupID();
    }
}
