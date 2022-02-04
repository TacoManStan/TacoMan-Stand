package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonArray;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface JArray<T> extends JElement {
    
    T[] jArrayElements();
    
    Object convertElement(T jArrayElement);
    
    @Override
    default Object getJValue() {
        final JsonArray jsonArray = new JsonArray();
        for (T t: jArrayElements())
            jsonArray.add(convertElement(t));
        return jsonArray;
    }
}
