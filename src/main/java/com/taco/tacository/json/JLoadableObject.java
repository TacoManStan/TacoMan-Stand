package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonObject;

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
