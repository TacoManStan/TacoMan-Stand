package com.taco.tacository.util.tools;

import com.taco.tacository.util.UID;
import com.taco.tacository.util.tools.list_tools.A;
import com.taco.tacository.ui.console.ConsoleBB;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Component
public class Stuff {
    
    private static final ReentrantLock lock;
    private static final HashMap<String, HashMap<String, Object>> resources;
    
    static {
        lock = new ReentrantLock();
        resources = new HashMap<>();
    }
    
    //<editor-fold desc="Helpers">
    
    private static String debugMessage(String typeKey, String lookupKey, String message) {
        return message + " [" + typeKey + " -> " + lookupKey + "]";
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Get">
    
    // Resource Retrieval
    
    public static <V> V get(String lookupKey) {
        return get(null, lookupKey);
    }
    
    public static <V> V get(String groupKey, String lookupKey) {
        return get(groupKey, lookupKey, () ->
        {
            throw Exc.unsupported("Value \"" + groupKey + " -> " + lookupKey + "\" has not yet been defined");
        });
    }
    
    /**
     * <p>Returns the cached object of type {@link V} from the specified {@code group} using the specified {@code UID}.</p>
     * <p>
     * This method is designed to work as an internal singleton lookup.
     * Therefore, shortcomings of the singleton pattern apply and should always be considered.
     * Memory leaks and stack/heap pollution are two problems that immediately come to mind.
     * </p>
     *
     * @param groupKey             The cache group being accessed. If the {@code group key} is null, the {@code 'default'} group is used.
     * @param lookupKey            The UID of the object being accessed.
     * @param defaultValueSupplier A {@link Supplier} function that is run if no object exists given the specified {@code group key} and {@code uid key}.
     * @param <V>                  The type of object being retrieved.
     *
     * @return The cached object retrieved using the specified cache reference keys.
     */
    public static <V> V get(String groupKey, String lookupKey, Supplier<V> defaultValueSupplier) {
        groupKey = groupKey != null ? groupKey : "default";
        
        Exc.nullCheck(lookupKey, "UID Lookup Key");
        Exc.nullCheckMessage(defaultValueSupplier, "Default value returner is null for type \" + groupKey + \" using lookup key \" + lookupKey + \"");
        
        final String tempTypeKey = groupKey.toLowerCase();
        final String tempLookupKey = lookupKey.toLowerCase();
        
        HashMap<String, V> map = Exc.nullCheck(getMap(tempTypeKey), debugMessage(tempTypeKey, tempLookupKey, "Map"));
        
        V value;// A Supplier is used so resources are only used getting the new value if necessary.
        if (map.containsKey(tempLookupKey))
            value = map.get(tempLookupKey);
        else {
            value = defaultValueSupplier.get();
            map.put(tempLookupKey, value);
        }
        return value;
    }
    
    public static <V> V get(UID uID, Object... params) {
        return get(uID, () -> null, params);
    }
    
    public static <V> V get(UID uID, Supplier<V> defaultValueSupplier, Object... params) {
        Exc.nullCheck(uID, "UID");
        Exc.nullCheck(defaultValueSupplier, "Default Value Supplier");
        
        return get(uID.getGroupID(), uID.getUID(params), defaultValueSupplier);
    }
    
    // Get All
    
    public static List<Object> getAll(String groupKey) {
        return getAll(groupKey, null);
    }
    
    public static <V> List<Object> getAll(String groupKey, Class<V> typeReq) {
        Exc.nullCheck(groupKey, "Lookup Key");
        ArrayList<Object> returnList = new ArrayList<>();
        List<HashMap<String, Object>> maps = Exc.nullCheck(A.getMapValues(resources), debugMessage(groupKey, "N/A", "Map Values"));
        maps.stream().filter(Objects::nonNull).forEach(map -> {
            Object element = map.get(groupKey);
            if (element != null && (typeReq == null || TB.instanceOf(element, typeReq)))
                returnList.add(element);
        });
        return returnList;
    }
    
    // Images
    
    private static final String image_type_key = "image";
    
    public static Image getTestImage(String imageID) {
        return getImage("_tests/", imageID, "png");
    }
    
    public static Image getImage(String pathID, String imageID) {
        return getImage(pathID, imageID, "png");
    }
    
    public static Image getImage(String pathID, String imageID, String extension) {
        Exc.nullCheck(extension, "File Extension");
        
        String hashID = Str.replaceSeparator((pathID != null ? pathID : "") + imageID);
        String filePath = Str.replaceSeparator("images/" + hashID + "." + extension);
        
        //noinspection Duplicates
        return get(image_type_key, hashID, () ->
        {
            try (InputStream inputStream = getResourceStream(filePath)) {
                if (inputStream != null) {
                    return new Image(inputStream);
                }
            } catch (IOException ignored) { } // Ignore because when an IOException is thrown, Missingno is loaded.
            
            ConsoleBB.CONSOLE.dev("WARNING: Input stream was null for image: " + filePath);
            return null;
        });
    }
    
    public static final String SIZE_16x16 = "16x16";
    public static final String SIZE_32x32 = "32x32";
    
    public static Image getGameImage(@NotNull String name) { return getGameImage(null, null, name); }
    public static Image getGameImage(@NotNull String pathID, @NotNull String name) { return getGameImage(pathID, null, name); }
    public static Image getGameImage(@Nullable String pathID, @Nullable String sizeID, @NotNull String name) { return getGameImage(pathID, sizeID, name, "png"); }
    public static Image getGameImage(@Nullable String pathID, @Nullable String sizeID, @NotNull String name, @NotNull String extension) {
        pathID = pathID != null ? pathID : "";
        sizeID = sizeID != null ? sizeID : SIZE_32x32;
        
        return switch (sizeID) {
            case SIZE_16x16 -> {
                yield getImage("game/" + sizeID + "/" + pathID, name, extension);
            }
            case SIZE_32x32 -> {
                yield getImage("game/" + sizeID + "/" + pathID, name, extension);
            }
            
            default -> throw Exc.unsupported("Unsupported Size ID: " + sizeID);
        };
    }
    
    /**
     * @see #getImage(String, String, String)
     * @deprecated temporarily because the way themes are loaded will likely be changed.
     */
    @Deprecated
    public static Image getImage(String pathID, String imageID, String extension, boolean isTheme) {
        Exc.nullCheck(extension, "File Extension");
        
        String theme = isTheme ? "themes/dark/" : ""; // TODO [S]: Load the theme from settings.
        String hashID = Str.replaceSeparator((pathID != null ? pathID : "") + imageID);
        String filePath = Str.replaceSeparator("images/" + theme + hashID + "." + extension);
        
        //noinspection Duplicates
        return get(image_type_key, hashID, () ->
        {
            try (InputStream inputStream = getResourceStream(filePath)) {
                if (inputStream != null)
                    return new Image(inputStream);
            } catch (IOException ignored) { } // Ignore because when an IOException is thrown, Missingno is loaded.
            
            ConsoleBB.CONSOLE.dev("WARNING: Input stream was null for image: " + filePath);
            return null;
        });
    }
    
    public static final String AVATAR = "Avatar";
    public static final String AVATAR_16 = "Avatar 16";
    public static final String AVATAR_32 = "Avatar 32";
    public static final String MAP = "Map";
    public static final String MAP_XL = "Map XL";
    
    public static Image getDummyImage(String name) {
        return getImage("/", name, "png");
    }
    
    // Update
    
    public static <V> V update(String groupKey, String lookupKey, V newValue) {
        Exc.nullCheck(groupKey, "Group Key");
        Exc.nullCheck(lookupKey, "Lookup Key");
        Exc.nullCheckMessage(newValue, debugMessage(groupKey, lookupKey, "New Value is null"));
        
        HashMap<String, V> map = Exc.nullCheck(getMap(groupKey), debugMessage(groupKey, lookupKey, "Map is null"));
        return map.put(lookupKey, newValue);
    }
    
    public static <V> V update(UID uID, String lookupKey, V newValue, Object... params) {
        Exc.nullCheck(uID, "UID Type Key");
        Exc.nullCheck(lookupKey, "Lookup Key");
        Exc.nullCheckMessage(newValue, debugMessage(uID.getUID(params), lookupKey, "New Value is null"));
        
        return update(uID.getUID(params), lookupKey, newValue);
    }
    
    public static <V> V updateLater(UID uID, String lookupKey, Supplier<V> valueSupplier, Object... params) {
        throw Exc.nyi();
    } // TODO
    
    // Get Map
    
    protected static <V> HashMap<String, V> getMap(String typeKey) {
        Exc.nullCheck(typeKey, "Type Key");
        if (!resources.containsKey(typeKey))
            resources.put(typeKey, new HashMap<>());
        return (HashMap<String, V>) resources.get(typeKey);
    }
    
    
    public static InputStream getResourceStream(String resource) throws IOException {
        Exc.nullCheck(resource, "Resource");
        return Stuff.class.getResourceAsStream(Str.replaceSeparator("/" + resource));
    } // TODO: Load from resource jar file
    
    public static URL getResourceURL(String resource) {
        Exc.nullCheck(resource, "Resource");
        return Stuff.class.getResource(Str.replaceSeparator("/" + resource));
    } // TODO: Load from resource jar file
    
    public static URI getResourceURI(String resource) {
        try {
            return getResourceURL(resource).toURI();
        } catch (URISyntaxException e) {
            throw Exc.ex(e);
        }
    }
    
    //</editor-fold>
}

/*
 * TODO LIST:
 * [S]: Create separate Resources instances for each ClientInstance and ScriptContext instance, in addition to the instance in App.
 * [S]: Write Javadocs.
 */