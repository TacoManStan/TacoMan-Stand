package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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
    public static <T> @NotNull JArray<T> createArray(String jID, T... elementArray) {
        return createArray(
                jID, e -> {
                    if (e instanceof JObject eObject)
                        return appendTo(eObject.getJValue(), create("jID", eObject.getJID()));
                    else if (e instanceof JElement eElement)
                        return eElement.getJValue();
                    return e;
                }, elementArray);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @SafeVarargs
    public static <T> @NotNull JArray<T> createArray(String jID, Function<T, Object> processor, T... elementArray) {
        return new JArray<>() {
            @Override public T[] jArrayElements() { return elementArray; }
            @Override public Object convertElement(T jArrayElement) { return processor.apply(jArrayElement); }
            @Override public String getJID() { return jID; }
        };
    }
    
    
    public static <T> @NotNull JMatrix<T> createMatrix(String jID, T[][] elementMatrix) {
        return createMatrix(jID, e -> {
            if (e instanceof JObject eObject)
                return appendTo(eObject.getJValue(), create("jID", eObject.getJID()));
            else if (e instanceof JElement eElement)
                return eElement.getJValue();
            return e;
        }, elementMatrix);
    }
    
    public static <T> @NotNull JMatrix<T> createMatrix(String jID, Function<T, Object> processor, T[][] elementMatrix) {
        return new JMatrix<>() {
            @Override public T[][] jMatrixElements() { return elementMatrix; }
            @Override public Object convertElement(T jMatrixElement) { return processor.apply(jMatrixElement); }
            @Override public String getJID() { return jID; }
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
        JsonArray jArr = (JsonArray) root.get(jID);
        
        if (jArr != null)
            for (Object o: jArr)
                ts.add(elementFactory.apply(o));
        
        return ts;
    }
    
    public static <T> @NotNull List<List<T>> loadMatrix(@NotNull JsonObject root, String jID, Function<Object, T> elementFactory) {
        final ArrayList<List<T>> columns = new ArrayList<>();
        for (Object o: ((JsonArray) root.get(jID))) {
            final JsonArray jsonColumn = (JsonArray) o;
            final ArrayList<T> column = new ArrayList<>();
            for (Object o2: jsonColumn)
                column.add(elementFactory.apply(o2));
            columns.add(column);
        }
        return columns;
    }
    
    //</editor-fold>
    
    static JsonObject appendTo(JsonObject jsonObject, JElement... jElements) {
        Arrays.stream(jElements).forEach(jElement -> jsonObject.put(jElement.getJID(), jElement.getJValue()));
        return jsonObject;
    }
}
