package com.taco.tacository._to_sort.json;

import com.github.cliftonlabs.json_simple.JsonArray;

public interface JArray<T> extends JElement {
    
    T[] jArrayElements();
    
    Object convertElement(T jArrayElement);
    
    @Override
    default JsonArray getJValue() {
        final JsonArray jsonArray = new JsonArray();
        for (T t: jArrayElements())
            jsonArray.add(convertElement(t));
        return jsonArray;
    }
}
