package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.taco.suit_lady.util.tools.ExceptionTools;
import org.bson.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class JUtil {
    private JUtil() { } //No Instance
    
    //<editor-fold desc="--- CREATE (SAVE) ---">
    
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull JElement create(String jID, Object jValue) {
        return new JElement() {
            @Override
            public String getJID() {
                return jID;
            }
            
            @Override
            public Object getJValue() {
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
            public String getJID() {
                return jID;
            }
        };
    }
    
    @Contract("_, _ -> new")
    public static @NotNull JObject createObject(String jID, @NotNull JObject jObject) {
        return createObject(jID, jObject.jFields());
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @SafeVarargs
    public static <T> @NotNull JArray<T> createArray(String jID, T... jElements) {
        return createArray(
                jID, e -> {
                    if (e instanceof JObject)
                        return appendTo(((JObject) e).getJValue(), create("jID", ((JObject) e).getJID()));
                    else if (e instanceof JElement)
                        return ((JElement) e).getJValue();
                    return e;
                }, jElements);
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
            public String getJID() {
                return jID;
            }
        };
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOAD ---">
    
    @Contract("_, _, _ -> param3")
    public static <T extends JLoadable> @NotNull T loadObject(@NotNull JsonObject root, String jID, @NotNull T jLoadable) {
        JsonObject self = (JsonObject) root.get(jID);
        jLoadable.load(self);
        return jLoadable;
    }
    
    public static <T extends JLoadable> @NotNull T loadObject(@NotNull JsonObject root, @NotNull T jLoadable) {
        return loadObject(root, jLoadable.getJID(), jLoadable);
    }
    
    public static int loadInt(Object o) {
        if (o instanceof BigInteger)
            return ((BigInteger) o).intValue();
        else if (o instanceof BigDecimal)
            return ((BigDecimal) o).intValue();
        return (int) o;
    }
    
    public static int loadInt(@NotNull JsonObject root, String jID) {
        return loadInt(root.get(jID));
    }
    
    public static double loadDouble(Object o) {
        if (o instanceof BigInteger)
            return ((BigInteger) o).doubleValue();
        else if (o instanceof BigDecimal)
            return ((BigDecimal) o).doubleValue();
        return (double) o;
    }
    
    public static double loadDouble(@NotNull JsonObject root, String jID) {
        return loadDouble(root.get(jID));
    }
    
    public static boolean loadBoolean(@NotNull JsonObject root, String jID) {
        return (boolean) root.get(jID);
    }
    
    public static String loadString(@NotNull JsonObject root, String jID) {
        return (String) root.get(jID);
    }
    
    public static <T> @NotNull List<T> loadArray(@NotNull JsonObject root, String jID, Function<Object, T> elementFactory) {
        ArrayList<T> ts = new ArrayList<>();
        for (Object o: ((JsonArray) root.get(jID))) {
            T t = elementFactory.apply(o);
            ts.add(t);
        }
        return ts;
    }
    
    //</editor-fold>
    
    static JsonObject appendTo(JsonObject jsonObject, JElement... jElements) {
        Arrays.stream(jElements).forEach(jElement -> jsonObject.put(jElement.getJID(), jElement.getJValue()));
        return jsonObject;
    }
}
