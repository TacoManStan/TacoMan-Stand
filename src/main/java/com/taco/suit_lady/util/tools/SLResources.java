package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UID;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.tacository.quick.ConsoleBB;
import javafx.scene.image.Image;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Component
public class SLResources {
    
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
            throw SLExceptions.unsupported("Value \"" + groupKey + " -> " + lookupKey + "\" has not yet been defined");
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
        
        SLExceptions.nullCheck(lookupKey, "UID Lookup Key");
        SLExceptions.nullCheckMessage(defaultValueSupplier, "Default value returner is null for type \" + groupKey + \" using lookup key \" + lookupKey + \"");
        
        final String tempTypeKey = groupKey.toLowerCase();
        final String tempLookupKey = lookupKey.toLowerCase();
        
        HashMap<String, V> map = SLExceptions.nullCheck(getMap(tempTypeKey), debugMessage(tempTypeKey, tempLookupKey, "Map"));
        
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
        SLExceptions.nullCheck(uID, "UID");
        SLExceptions.nullCheck(defaultValueSupplier, "Default Value Supplier");
        
        return get(uID.getGroupID(), uID.getUID(params), defaultValueSupplier);
    }
    
    // Get All
    
    public static List<Object> getAll(String groupKey) {
        return getAll(groupKey, null);
    }
    
    public static <V> List<Object> getAll(String groupKey, Class<V> typeReq) {
        SLExceptions.nullCheck(groupKey, "Lookup Key");
        ArrayList<Object> returnList = new ArrayList<>();
        List<HashMap<String, Object>> maps = SLExceptions.nullCheck(SLArrays.getMapValues(resources), debugMessage(groupKey, "N/A", "Map Values"));
        maps.stream().filter(Objects::nonNull).forEach(map -> {
            Object element = map.get(groupKey);
            if (element != null && (typeReq == null || SLTools.instanceOf(element, typeReq)))
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
        SLExceptions.nullCheck(extension, "File Extension");
        
        String hashID = SLStrings.replaceSeparator((pathID != null ? pathID : "") + imageID);
        String filePath = SLStrings.replaceSeparator("images/" + hashID + "." + extension);
        
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
    
    /**
     * @see #getImage(String, String, String)
     * @deprecated temporarily because the way themes are loaded will likely be changed.
     */
    @Deprecated
    public static Image getImage(String pathID, String imageID, String extension, boolean isTheme) {
        SLExceptions.nullCheck(extension, "File Extension");
        
        String theme = isTheme ? "themes/dark/" : ""; // TODO [S]: Load the theme from settings.
        String hashID = SLStrings.replaceSeparator((pathID != null ? pathID : "") + imageID);
        String filePath = SLStrings.replaceSeparator("images/" + theme + hashID + "." + extension);
        
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
    public static final String MAP = "Map";
    
    public static Image getDummyImage(String name) {
        return getImage("/", name, "png");
    }
    
    // Update
    
    public static <V> V update(String groupKey, String lookupKey, V newValue) {
        SLExceptions.nullCheck(groupKey, "Group Key");
        SLExceptions.nullCheck(lookupKey, "Lookup Key");
        SLExceptions.nullCheckMessage(newValue, debugMessage(groupKey, lookupKey, "New Value is null"));
        
        HashMap<String, V> map = SLExceptions.nullCheck(getMap(groupKey), debugMessage(groupKey, lookupKey, "Map is null"));
        return map.put(lookupKey, newValue);
    }
    
    public static <V> V update(UID uID, String lookupKey, V newValue, Object... params) {
        SLExceptions.nullCheck(uID, "UID Type Key");
        SLExceptions.nullCheck(lookupKey, "Lookup Key");
        SLExceptions.nullCheckMessage(newValue, debugMessage(uID.getUID(params), lookupKey, "New Value is null"));
        
        return update(uID.getUID(params), lookupKey, newValue);
    }
    
    public static <V> V updateLater(UID uID, String lookupKey, Supplier<V> valueSupplier, Object... params) {
        throw SLExceptions.nyi();
    } // TODO
    
    // Get Map
    
    protected static <V> HashMap<String, V> getMap(String typeKey) {
        SLExceptions.nullCheck(typeKey, "Type Key");
        if (!resources.containsKey(typeKey))
            resources.put(typeKey, new HashMap<>());
        return (HashMap<String, V>) resources.get(typeKey);
    }
    
    
    public static InputStream getResourceStream(String resource) throws IOException {
        SLExceptions.nullCheck(resource, "Resource");
        return SLResources.class.getResourceAsStream(SLStrings.replaceSeparator("/" + resource));
    } // TODO: Load from resource jar file
    
    public static URL getResourceURL(String resource) {
        SLExceptions.nullCheck(resource, "Resource");
        return SLResources.class.getResource(SLStrings.replaceSeparator("/" + resource));
    } // TODO: Load from resource jar file
    
    public static URI getResourceURI(String resource) {
        try {
            return getResourceURL(resource).toURI();
        } catch (URISyntaxException e) {
            throw SLExceptions.ex(e);
        }
    }
    
    //</editor-fold>
}

/*
 * TODO LIST:
 * [S]: Create separate Resources instances for each ClientInstance and ScriptContext instance, in addition to the instance in App.
 * [S]: Write Javadocs.
 */