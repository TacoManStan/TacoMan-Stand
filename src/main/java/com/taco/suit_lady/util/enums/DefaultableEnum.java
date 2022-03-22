package com.taco.suit_lady.util.enums;

import com.taco.suit_lady.util.tools.Enu;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines an {@link Enumable} with a {@link #defaultValue() Default Value}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>
 *         The {@link #defaultValue() Default Value} is used primarily by the static {@link Enu} utility class:
 *         <ul>
 *             <li><i>{@link Enu#get(Enum, Class)}</i></li>
 *             <li><i>{@link Enu#get(Class)}</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <E> The {@link Enum} {@link Class Type} of this {@link DefaultableEnum} implementation.
 */
public interface DefaultableEnum<E extends Enum<E>>
        extends Enumable<E> {
    
    @NotNull E defaultValue();
}
