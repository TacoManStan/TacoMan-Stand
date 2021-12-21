package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.taco.suit_lady.util.tools.ExceptionTools;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class JUtil {
    private JUtil() { } //No Instance
    
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
    
    public static @NotNull JObject createObject(String jID, JObject jObject) {
        return createObject(jID, jObject.jFields());
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @SafeVarargs
    public static <T> @NotNull JArray<T> createArray(String jID, T... jElements) {
        return createArray(jID,
                           e -> {
                               if (e instanceof JObject)
                                   return appendTo(((JObject) e).getJValue(), create("jID", ((JObject) e).getJID()));
                               else if (e instanceof JElement)
                                   return ((JElement) e).getJValue();
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
            public String getJID() {
                return jID;
            }
        };
    }
    
    private static JsonObject appendTo(JsonObject jsonObject, JElement... jElements) {
        Arrays.stream(jElements).forEach(jElement -> jsonObject.put(jElement.getJID(), jElement.getJValue()));
        return jsonObject;
    }
    
    //    private static Object convert(Object o) {
    //        if (o instanceof Color)
    //            return new JObject() {
    //                @Override
    //                public JElement[] jFields() {
    //                    Color color = (Color) o;
    //                    return new JElement[]{
    //                            JUtil.create("red", color.getRed()),
    //                            JUtil.create("green", color.getGreen()),
    //                            JUtil.create("blue", color.getBlue()),
    //                            JUtil.create("alpha", color.getOpacity())
    //                    };
    //                }
    //
    //                @Override
    //                public String jID() {
    //                    return "color";
    //                }
    //            };
    //        return o;
    //    }
    
    //
    
    private static final String pathPrefix = "src/main/resources/json/";
    
    public static void save(@NotNull JObject root) {
        System.out.println("Saving " + root.getJID() + "...");
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(pathPrefix + root.getJID() + ".json"));
            Jsoner.serialize(root.getJValue(), writer);
            writer.close();
        } catch (IOException e) {
            throw ExceptionTools.ex(e);
        }
    }
    
    @Contract("_ -> param1")
    public static <T extends JLoadable> @NotNull T load(@NotNull T jLoadable) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(pathPrefix + jLoadable.getJID() + ".json"));
            JsonObject root = (JsonObject) Jsoner.deserialize(reader);
            jLoadable.load(root);
            return jLoadable;
        } catch (IOException | JsonException e) {
            throw ExceptionTools.ex(e);
        }
    }
    
    public static <T extends JLoadableObject> @NotNull T load(String jID, @NotNull T jLoadableObject) {
        jLoadableObject.setJID(jID);
        return load(jLoadableObject);
    }
    
    public static <T extends JLoadable> @NotNull T load(String jID, @NotNull Supplier<T> factory) {
        return JUtil.load(factory.get());
    }
    
    public static <T extends JLoadable> @NotNull T loadObject(@NotNull JsonObject root, String jID, T jLoadable) {
        JsonObject self = (JsonObject) root.get(jID);
        jLoadable.load(self);
        return jLoadable;
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
    
    public static <T> List<T> loadArray(@NotNull JsonObject root, String jID, Function<Object, T> elementFactory) {
        ArrayList<T> ts = new ArrayList<>();
        for (Object o: ((JsonArray) root.get(jID))) {
            T t = elementFactory.apply(o);
            ts.add(t);
        }
        return ts;
    }
}
