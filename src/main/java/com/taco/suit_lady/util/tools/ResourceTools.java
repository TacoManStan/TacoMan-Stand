package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.UID;
import com.taco.tacository.quick.ConsoleBB;
import javafx.scene.image.Image;

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

public class ResourceTools
{
    public static ResourceTools get()
    {
        return TB.resources();
    }
    
    private final ReentrantLock lock;
    private final HashMap<String, HashMap<String, Object>> resources;
    
    ResourceTools()
    {
        this.lock = new ReentrantLock();
        this.resources = new HashMap<>();
    }
    
    //<editor-fold desc="Helpers">
    
    private String debugMessage(String typeKey, String lookupKey, String message)
    {
        return message + " [" + typeKey + " -> " + lookupKey + "]";
    }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="Get">
    
    // Resource Retrieval
    
    public <V> V get(String lookupKey)
    {
        return get(null, lookupKey);
    }
    
    public <V> V get(String groupKey, String lookupKey)
    {
        return get(groupKey, lookupKey, () ->
        {
            throw ExceptionTools.unsupported("Value \"" + groupKey + " -> " + lookupKey + "\" has not yet been defined");
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
     * @return The cached object retrieved using the specified cache reference keys.
     */
    public <V> V get(String groupKey, String lookupKey, Supplier<V> defaultValueSupplier)
    {
        groupKey = groupKey != null ? groupKey : "default";
        
        ExceptionTools.nullCheck(lookupKey, "UID Lookup Key");
        ExceptionTools.nullCheckMessage(defaultValueSupplier, "Default value returner is null for type \" + groupKey + \" using lookup key \" + lookupKey + \"");
        
        final String tempTypeKey = groupKey.toLowerCase();
        final String tempLookupKey = lookupKey.toLowerCase();
        
        HashMap<String, V> map = ExceptionTools.nullCheck(getMap(tempTypeKey), debugMessage(tempTypeKey, tempLookupKey, "Map"));
        
        V value;// A Supplier is used so resources are only used getting the new value if necessary.
        if (map.containsKey(tempLookupKey))
            value = map.get(tempLookupKey);
        else
        {
            value = defaultValueSupplier.get();
            map.put(tempLookupKey, value);
        }
        return value;
    }
    
    public <V> V get(UID uID, Object... params)
    {
        return get(uID, () -> null, params);
    }
    
    public <V> V get(UID uID, Supplier<V> defaultValueSupplier, Object... params)
    {
        ExceptionTools.nullCheck(uID, "UID");
        ExceptionTools.nullCheck(defaultValueSupplier, "Default Value Supplier");
        
        return get(uID.getGroupID(), uID.getUID(params), defaultValueSupplier);
    }
    
    // Get All
    
    public List<Object> getAll(String groupKey)
    {
        return getAll(groupKey, null);
    }
    
    public <V> List<Object> getAll(String groupKey, Class<V> typeReq)
    {
        ExceptionTools.nullCheck(groupKey, "Lookup Key");
        ArrayList<Object> returnList = new ArrayList<>();
        List<HashMap<String, Object>> maps = ExceptionTools.nullCheck(ArrayTools.getMapValues(resources), debugMessage(groupKey, "N/A", "Map Values"));
        maps.stream().filter(Objects::nonNull).forEach(map -> {
            Object element = map.get(groupKey);
            if (element != null && (typeReq == null || GeneralTools.get().instanceOf(element, typeReq)))
                returnList.add(element);
        });
        return returnList;
    }
    
    // Images
    
    private static final String image_type_key = "image";
    
    public Image getTestImage(String imageID)
    {
        return getImage("_tests/", imageID, "png");
    }
    
    public Image getImage(String pathID, String imageID)
    {
        return getImage(pathID, imageID, "png");
    }
    
    public Image getImage(String pathID, String imageID, String extension)
    {
        ExceptionTools.nullCheck(extension, "File Extension");
        
        String hashID = TB.strings().replaceSeparator((pathID != null ? pathID : "") + imageID);
        String filePath = TB.strings().replaceSeparator("images/" + hashID + "." + extension);
        
        //noinspection Duplicates
        return get(image_type_key, hashID, () ->
        {
            try (InputStream inputStream = getResourceStream(filePath))
            {
                if (inputStream != null)
                    return new Image(inputStream);
            }
            catch (IOException ignored) { } // Ignore because when an IOException is thrown, Missingno is loaded.
            
            ConsoleBB.CONSOLE.dev("WARNING: Input stream was null for image: " + filePath);
            return null;
        });
    }
    
    /**
     * @see #getImage(String, String, String)
     * @deprecated temporarily because the way themes are loaded will likely be changed.
     */
    @Deprecated
    public Image getImage(String pathID, String imageID, String extension, boolean isTheme)
    {
        ExceptionTools.nullCheck(extension, "File Extension");
        
        String theme = isTheme ? "themes/dark/" : ""; // TODO [S]: Load the theme from settings.
        String hashID = TB.strings().replaceSeparator((pathID != null ? pathID : "") + imageID);
        String filePath = TB.strings().replaceSeparator("images/" + theme + hashID + "." + extension);
        
        //noinspection Duplicates
        return get(image_type_key, hashID, () ->
        {
            try (InputStream inputStream = getResourceStream(filePath))
            {
                if (inputStream != null)
                    return new Image(inputStream);
            }
            catch (IOException ignored) { } // Ignore because when an IOException is thrown, Missingno is loaded.
            
            ConsoleBB.CONSOLE.dev("WARNING: Input stream was null for image: " + filePath);
            return null;
        });
    }
    
    public static final String AVATAR = "Avatar";
    public static final String MAP = "Map";
    
    public Image getDummyImage(String name) {
        return getImage("/", name, "png");
    }
    
    // Update
    
    public <V> V update(String groupKey, String lookupKey, V newValue)
    {
        ExceptionTools.nullCheck(groupKey, "Group Key");
        ExceptionTools.nullCheck(lookupKey, "Lookup Key");
        ExceptionTools.nullCheckMessage(newValue, debugMessage(groupKey, lookupKey, "New Value is null"));
        
        HashMap<String, V> map = ExceptionTools.nullCheck(getMap(groupKey), debugMessage(groupKey, lookupKey, "Map is null"));
        return map.put(lookupKey, newValue);
    }
    
    public <V> V update(UID uID, String lookupKey, V newValue, Object... params)
    {
        ExceptionTools.nullCheck(uID, "UID Type Key");
        ExceptionTools.nullCheck(lookupKey, "Lookup Key");
        ExceptionTools.nullCheckMessage(newValue, debugMessage(uID.getUID(params), lookupKey, "New Value is null"));
        
        return update(uID.getUID(params), lookupKey, newValue);
    }
    
    public <V> V updateLater(UID uID, String lookupKey, Supplier<V> valueSupplier, Object... params)
    {
        throw ExceptionTools.nyi();
    } // TODO
    
    // Get Map
    
    protected <V> HashMap<String, V> getMap(String typeKey)
    {
        ExceptionTools.nullCheck(typeKey, "Type Key");
        if (!resources.containsKey(typeKey))
            resources.put(typeKey, new HashMap<>());
        return (HashMap<String, V>) resources.get(typeKey);
    }
    
    //</editor-fold>
    
    public InputStream getResourceStream(String resource) throws IOException
    {
        ExceptionTools.nullCheck(resource, "Resource");
        return getClass().getResourceAsStream(TB.strings().replaceSeparator("/" + resource));
    } // TODO: Load from resource jar file
    
    public URL getResourceURL(String resource)
    {
        ExceptionTools.nullCheck(resource, "Resource");
        return getClass().getResource(TB.strings().replaceSeparator("/" + resource));
    } // TODO: Load from resource jar file
    
    public URI getResourceURI(String resource) {
        try {
            return getResourceURL(resource).toURI();
        } catch (URISyntaxException e) {
            throw ExceptionTools.ex(e);
        }
    }
}

/*
 * TODO LIST:
 * [S]: Create separate Resources instances for each ClientInstance and ScriptContext instance, in addition to the instance in App.
 * [S]: Write Javadocs.
 */