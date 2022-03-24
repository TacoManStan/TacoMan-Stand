package com.taco.tacository.util.values.params;

import com.taco.tacository.util.values.Value2D;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <p>Defines implementing {@link Object Objects} as requiring a {@link List} of {@link Object Parameter Objects}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>A {@link Paramable} must override <i>{@link #requiredParams()}</i> to define the {@link Object Object Parameters} required for this {@link Paramable} implementation.</li>
 *     <li>{@link Object Parameter Objects} are mapped using {@code keys} of generic type {@link K}.</li>
 *     <li>
 *         <i>{@link #requiredParams()}</i> returns a {@link List} of {@link Value2D} objects.
 *         <ul>
 *             <li>The {@link Value2D#a() First Parameter Value} defines the {@code key} of type {@link K} mapping the {@link Object Parameter Value} of a specified {@link Value2D} parameter.</li>
 *             <li>The {@link Value2D#b() Second Parameter Value} defines the required {@link Class Class Type} of the {@link Object Parameter Value} mapped by a specified {@link Value2D} parameter.</li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <K> The type of {@code key} used by <i>{@link #requiredParams()}</i> to access the {@link Value2D Parameter Value}.
 */
//TO-EXPAND
public interface Paramable<K> {
    @NotNull List<Value2D<K, Class<?>>> requiredParams();
}
