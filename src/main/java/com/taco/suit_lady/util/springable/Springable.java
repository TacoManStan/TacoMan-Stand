package com.taco.suit_lady.util.springable;

import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <b>--- To Format ---</b>
 * <br>
 * <p>An interface that guarantees all implementations will have direct and public access to...</p>
 * <ol>
 *     <li>Application Context Instance</li>
 *     <li>FxWeaver Instance
 * </ol>
 */
public interface Springable
{
    FxWeaver weaver();
    
    ConfigurableApplicationContext ctx();
    
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
}
