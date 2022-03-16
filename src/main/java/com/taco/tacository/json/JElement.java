package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import javafx.scene.paint.Color;

import java.util.function.Function;

/**
 * <p>Indicates that an {@code implementing} class can construct a {@link JsonObject} or similar class that can then be used to save this {@link JElement} as a {@link JFiles JSON File} using the {@link JUtil JSON Framework}.</p>
 * <p><b>Standard Implementations</b></p>
 * <ol>
 *     <li><b>{@link JObject}:</b> Defines a single {@link JFiles#save(JObject) saveable} {@link Object} value.</li>
 *     <li><b>{@link JArray}:</b> Defines an {@code Array} of {@link JFiles#save(JObject) saveable} {@link Object} values.</li>
 *     <li><b>{@link JMatrix}:</b> Defines a {@code 2D Array} — i.e., {@code Matrix} — of {@link JFiles#save(JObject) saveable} {@link Object} values.</li>
 *     <li><b>{@link NonJObjectFieldWrapper}:</b> Defines {@code JSON Data} for an {@link Object} that is not a {@link JElement} implementation — e.g., default classes such as {@link Color}..</li>
 * </ol>
 */
public interface JElement {
    
    /**
     * <p>Returns the {@code JSON ID} of this {@link JElement} instance.</p>
     *
     * @return The {@code JSON ID} of this {@link JElement} instance.
     */
    //TO-EXPAND
    String getJID();
    
    /**
     * <p>Returns an {@link Object} that can be {@code processed} by the {@link JUtil JSON Framework}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>In most contexts, the {@link Object} returned by {@link #getJValue() this method} will be a {@link Jsonable} object, most commonly {@link JsonObject} or {@link JsonArray}.</li>
     *     <li>
     *         In other contexts, the {@link Object} returned by {@link #getJValue() this method} can be processed by specifying a {@link Function Processor Function}, such as with the following static utility functions:
     *         <ul>
     *             <li><i>{@link JUtil#createArray(String, Function, Object[])}</i></li>
     *             <li><i>{@link JUtil#createMatrix(String, Function, Object[][])}</i></li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @return An {@link Object} that can be used to process and {@link JFiles#save(JObject) save} the contents of this {@link JElement} instance using the {@link JUtil JSON Framework}.
     */
    Object getJValue();
}
