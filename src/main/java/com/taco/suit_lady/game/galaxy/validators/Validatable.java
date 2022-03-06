package com.taco.suit_lady.game.galaxy.validators;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.values.ValuePair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface Validatable<T extends Validatable<T>>
        extends SpringableWrapper, Lockable, GameComponent {
    
    @NotNull Validator<T> validator();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean isValid() { return validator().isValid(); }
    
    default boolean revalidate(@NotNull Map<String, Object> params) { return validator().revalidate(params); }
    default boolean revalidate(@NotNull ValuePair<String, Object>... params) { return validator().revalidate(params); }
    
    //</editor-fold>
}
