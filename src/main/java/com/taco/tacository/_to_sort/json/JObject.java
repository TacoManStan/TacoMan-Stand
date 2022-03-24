package com.taco.tacository._to_sort.json;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.Arrays;

public interface JObject extends JElement {
    
    JElement[] jFields();
    
    @Override
    default JsonObject getJValue() {
        JsonObject map = new JsonObject();
        map.put("jID", getJID());
        Arrays.stream(jFields()).forEach(jElement -> map.put(jElement.getJID(), jElement.getJValue()));
        return map;
    }
}