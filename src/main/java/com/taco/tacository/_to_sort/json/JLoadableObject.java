package com.taco.tacository._to_sort.json;

import com.github.cliftonlabs.json_simple.JsonObject;

/**
 * Identical to {@link JLoadable} except {@link JLoadableObject} automatically {@link JFiles#save(JObject) Saves} and {@link JFiles#load(JLoadable) Loads} the {@link #getJID() JID} as a {@code JSON} field in the object itself.
 *
 * @see JLoadable
 * @see JUtil
 * @see JFiles
 */
//TO-EXPAND: Examples
public interface JLoadableObject
        extends JLoadable {
    
    void setJID(String jID);
    
    void doLoad(JsonObject parent);
    
    @Override
    default void load(JsonObject parent) {
        setJID(JUtil.loadString(parent, "jID"));
        doLoad(parent);
    }
}
