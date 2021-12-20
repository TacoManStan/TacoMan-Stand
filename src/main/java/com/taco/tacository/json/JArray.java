package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonArray;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface JArray<T> extends JElement {
    
    T[] jArrayElements();
    
    Object convertElement(T jArrayElement);
    
    @Override
    default Object jValue() {
        return Arrays.stream(jArrayElements()).map(this::convertElement).collect(Collectors.toCollection(JsonArray::new));
    }
}
