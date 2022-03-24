package com.taco.tacository._to_sort.json;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.io.Reader;
import java.util.function.Function;

/**
 * <p>Indicates that all implementing classes can be {@link #load(JsonObject) loaded} using the {@link JUtil JSON Framework}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@link JsonObject} parameter passed to the <i>{@link #load(JsonObject)}</i> method will always be the {@link JsonObject} representation of this {@link JLoadable} instance.</li>
 *     <li>
 *         Use any of the {@code static utility methods} available in the {@link JUtil} class to pull data from the {@link JsonObject} parameter:
 *         <ul>
 *             <li>{@link JUtil#loadObject(JsonObject, JLoadable)}</li>
 *             <li>{@link JUtil#loadObject(JsonObject, String, JLoadable)}</li>
 *             <li>{@link JUtil#loadInt(JsonObject, String)}</li>
 *             <li>{@link JUtil#loadDouble(JsonObject, String)}</li>
 *             <li>{@link JUtil#loadBoolean(JsonObject, String)}</li>
 *             <li>{@link JUtil#loadString(JsonObject, String)}</li>
 *             <li>{@link JUtil#loadArray(JsonObject, String, Function)}</li>
 *             <li>{@link JUtil#loadMatrix(JsonObject, String, Function)}</li>
 *         </ul>
 *         <i>Additional information on each {@code JUtil Load Method} can be accessed by clicking on any of the {@code links} shown above.</i>
 *     </li>
 *     <li>
 *         To load an existing {@code JSON File}, use any of the {@code static utility methods} available in the {@link JFiles} class:
 *         <ul>
 *             <li>{@link JFiles#load(JLoadable)}</li>
 *             <li>{@link JFiles#load(String, JLoadableObject)}</li>
 *             <li>{@link JFiles#load(JLoadable, Reader)}</li>
 *             <li>{@link JFiles#loadFromString(JLoadable, String)}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         The {@link JLoadableObject} interface can be used as an alternative to {@link JLoadable}
 *         — the two are near-identical, except {@link JLoadableObject} automatically {@link JLoadableObject#setJID(String) sets} its {@link JLoadable#getJID() JSON ID} when {@link #load(JsonObject) loading}.
 *         <ul>
 *             <li><i>Note that to function properly, the {@link #getJID() JSON ID} must be {@link JFiles#save(JObject) saved} as a {@code JSON Element} mapped to key \"jId\"</i></li>
 *             <li><i>Additionally, because the {@link JLoadableObject#setJID(String)} method accepts only an exact {@link String} value, dynamic {@link #getJID() JSON IDs} are not supported.</i></li>
 *             <li><i>In most contexts, {@link JLoadable} is preferred over {@link JLoadableObject}.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 */
//TO-EXPAND: Examples
public interface JLoadable {
    
    String getJID();
    void load(JsonObject parent);
}
