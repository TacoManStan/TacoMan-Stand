package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface JObject extends JElement {
    
    JElement[] jFields();
    
    @Override
    default JsonObject jValue() {
        return Arrays.stream(jFields()).collect(Collectors.toMap(
                JElement::jID,
                JElement::jValue,
                (a, b) -> b,
                JsonObject::new));
    }
}