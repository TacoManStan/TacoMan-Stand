package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonObject;

// Identical to JLoadable except JLoadableObject automatically handles saving/loading the jID as a field in the JSON object itself.
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
