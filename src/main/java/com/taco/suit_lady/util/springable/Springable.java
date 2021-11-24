package com.taco.suit_lady.util.springable;

import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>Guarantees that all implementing classes will have direct access to...</p>
 * <ol>
 *     <li>The appropriate {@link ConfigurableApplicationContext} instance, ideally global scope if available.</li>
 *     <li>An {@link FxWeaver} instance.
 * </ol>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>By default, both <code><i>{@link #weaver()}</i></code> and <code><i>{@link #ctx()}</i></code> methods are permitted to return {@code null}.</li>
 *     <li>Annotating implemented methods as either {@link Nullable} or {@link NotNull} to reflect nullity is strongly recommended.</li>
 * </ol>
 */
public interface Springable
{
    /**
     * <p>Grants access to the appropriate {@link FxWeaver} instance for use in this {@link Springable} implementation.</p>
     *
     * @return The {@link FxWeaver} to be used by this {@link Springable} implementation when necessary.
     */
    FxWeaver weaver();
    
    /**
     * <p>Grants access to the appropriate {@link ConfigurableApplicationContext Application Context} for use in this {@link Springable} implementation.</p>
     *
     * @return The {@link ConfigurableApplicationContext Application Context} to be used by this {@link Springable} implementation when necessary.
     */
    ConfigurableApplicationContext ctx();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    /**
     * <p>Constructs a new {@link SimpleSpringable} instance wrapping the contents of this {@link Springable}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>By passing the value of {@link #asSimple() this method} wherever a {@link Springable} is required permits this {@link Springable} instance to be garbage collected when no longer used.</li>
     *     <li>This prevents the possibility of an unnecessary reference to this {@link Springable} instance from accidentally being stored as a {@link Springable}, preventing garbage collection.</li>
     * </ol>
     *
     * @return A new {@link SimpleSpringable} instance wrapping the contents of this {@link Springable}.
     */
    default @NotNull SimpleSpringable asSimple()
    {
        return new SimpleSpringable(weaver(), ctx());
    }
    
    /**
     * <p>Identical to {@link #asSimple()}, except the {@link Springable} returned by {@link #asStrict() this method} is {@link StrictSpringable strict}.</p>
     * <blockquote>Refer to <code><i>{@link #asSimple()}</i></code> for additional information.</blockquote>
     *
     * @return A new {@link StrictSpringable} instance wrapping the contents of this {@link Springable}.
     */
    default @NotNull StrictSpringable asStrict()
    {
        return new StrictSpringable(weaver(), ctx());
    }
    
    //</editor-fold>
}
