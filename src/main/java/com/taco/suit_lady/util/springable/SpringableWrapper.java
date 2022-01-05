package com.taco.suit_lady.util.springable;

import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>Convenience implementation of {@link Springable} that provides {@code default} implementations of <i>{@link Springable#weaver()}</i> && <i>{@link Springable#ctx()}</i> bound to the {@link Springable} instance returned by <i>{@link SpringableWrapper#springable()}.</i></p>
 */
public interface SpringableWrapper
        extends Springable {
    
    @NotNull Springable springable();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    @Override
    default @NotNull FxWeaver weaver() {
        return springable().weaver();
    }
    
    @Override
    default @NotNull ConfigurableApplicationContext ctx() {
        return springable().ctx();
    }
    
    //</editor-fold>
}
