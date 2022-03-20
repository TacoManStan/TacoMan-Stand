package com.taco.suit_lady.game.galaxy.validators;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.values.Value2D;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * <p>Defines an {@link Object} as {@link Validatable}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link Validatable} is used to define an {@link Object} as having a {@link List} of {@link ValidationFilter conditions} that must be met for the {@link Validatable} to be {@link #isValid() valid}.</li>
 *     <li>{@link Validatable} defines its {@link #revalidate(Map) validation logic} by customizing the {@link Validator} instance returned by the {@code abstract} <i>{@link #validator()}</i> method.</li>
 *     <li>
 *         Passthrough Methods
 *         <ul>
 *             <li><i><b>{@link #isValid()}:</b> {@link #validator()}.{@link Validator#isValid() isValid()}</i></li>
 *             <li><i><b>{@link #revalidate(Map)}:</b> {@link #validator()}.{@link Validator#revalidate(Map) revalidate(paramMap)}</i></li>
 *             <li><i><b>{@link #revalidate(Value2D[])}:</b> {@link #validator()}.{@link Validator#revalidate(Value2D[]) revalidate(params)}</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T> The {@link Class type} of this {@link Validatable} instance.
 */
//TO-EXPAND
@SuppressWarnings("unchecked")
public interface Validatable<T extends Validatable<T>>
        extends SpringableWrapper, Lockable, GameComponent {
    
    @NotNull Validator<T> validator();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean isValid() { return validator().isValid(); }
    
    default boolean revalidate(@NotNull Map<String, Object> params) { return validator().revalidate(params); }
    default boolean revalidate(@NotNull Value2D<String, Object>... params) { return validator().revalidate(params); }
    
    //</editor-fold>
}
