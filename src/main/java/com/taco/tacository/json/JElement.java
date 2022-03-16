package com.taco.tacository.json;

import javafx.scene.paint.Color;

/**
 * <p>Indicates that an {@code implementing} class contains a {@link Object value} that can be saved to a {@link JFiles JSON File} using the {@link JUtil JSON Framework}.</p>
 * <p><b>Standard Implementations</b></p>
 * <ol>
 *     <li><b>{@link JObject}:</b> Defines a single {@link JFiles#save(JObject) saveable} {@link Object} value.</li>
 *     <li><b>{@link JArray}:</b> Defines an {@code Array} of {@link JFiles#save(JObject) saveable} {@link Object} values.</li>
 *     <li><b>{@link JMatrix}:</b> Defines a {@code 2D Array} — i.e., {@code Matrix} — of {@link JFiles#save(JObject) saveable} {@link Object} values.</li>
 *     <li><b>{@link NonJObjectFieldWrapper}:</b> Defines {@code JSON Data} for an {@link Object} that is not a {@link JElement} implementation — e.g., default classes such as {@link Color}..</li>
 * </ol>
 */
public interface JElement {
    String getJID();
    Object getJValue();
}
