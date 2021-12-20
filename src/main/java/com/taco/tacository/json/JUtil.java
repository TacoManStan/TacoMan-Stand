package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.taco.suit_lady.util.tools.ExceptionTools;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;

public final class JUtil {
    private JUtil() { } //No Instance
    
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull JElement create(String jID, Object jValue) {
        return new JElement() {
            @Override
            public String jID() {
                return jID;
            }
            
            @Override
            public Object jValue() {
                return jValue;
            }
        };
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull JObject createObject(String jID, JElement... jFields) {
        return new JObject() {
            @Override
            public JElement[] jFields() {
                return jFields;
            }
            
            @Override
            public String jID() {
                return jID;
            }
        };
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @SafeVarargs
    public static <T> @NotNull JArray<T> createArray(String jID, T... jElements) {
        return createArray(jID,
                           e -> {
                               if (e instanceof JObject)
                                   return appendTo(((JObject) e).jValue(), create("jID", ((JObject) e).jID()));
                               else if (e instanceof JElement)
                                   return ((JElement) e).jValue();
                               else
                                   return e;
                           },
                           jElements);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @SafeVarargs
    public static <T> @NotNull JArray<T> createArray(String jID, Function<T, Object> processor, T... jElements) {
        return new JArray<>() {
            @Override
            public T[] jArrayElements() {
                return jElements;
            }
            
            @Override
            public Object convertElement(T jArrayElement) {
                return processor.apply(jArrayElement);
            }
            
            @Override
            public String jID() {
                return jID;
            }
        };
    }
    
    private static JsonObject appendTo(JsonObject jsonObject, JElement... jElements) {
        Arrays.stream(jElements).forEach(jElement -> jsonObject.put(jElement.jID(), jElement.jValue()));
        return jsonObject;
    }
    
    //
    
    public static void save(@NotNull JObject root) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/json/" + root.jID() + ".json"));
            Jsoner.serialize(root.jValue(), writer);
            writer.close();
        } catch (IOException e) {
            throw ExceptionTools.ex(e);
        }
    }
}
