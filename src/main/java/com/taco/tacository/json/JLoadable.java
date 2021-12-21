package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonObject;

public interface JLoadable {
    
    String getJID();
    
    void load(JsonObject parent);
}
