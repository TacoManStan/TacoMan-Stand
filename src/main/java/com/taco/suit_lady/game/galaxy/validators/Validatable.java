package com.taco.suit_lady.game.galaxy.validators;

import com.taco.suit_lady.game.WrappedGameComponent;
import org.jetbrains.annotations.NotNull;

public interface Validatable<T extends Validatable<T>>
        extends WrappedGameComponent {
    
    @NotNull Validator<T> validator();
    
    //
    
    default boolean isValid() { return validator().isValid(); }
}
