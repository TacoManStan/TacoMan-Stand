package com.taco.tacository.util.springable;

import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>A basic implementation of {@link Springable}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Stores an {@link FxWeaver} and an {@link ConfigurableApplicationContext Application Context} as fields.</li>
 *     <li>Both {@link FxWeaver} and {@link ConfigurableApplicationContext Application Context} are permitted to be {@code null}.</li>
 *     <li>To construct a {@link SimpleSpringable} that does not permit {@code null} elements, refer to {@link StrictSpringable}.</li>
 *     <li>Neither fields wrapped by a {@link SimpleSpringable} can be modified.</li>
 * </ol>
 *
 * @see Springable
 * @see StrictSpringable
 */
public record SimpleSpringable(@Nullable FxWeaver weaver, @Nullable ConfigurableApplicationContext ctx)
        implements Springable {
    
    /**
     * <p>Constructs a new {@link SimpleSpringable} wrapping the specified {@link FxWeaver} and {@link ConfigurableApplicationContext Application Context}.</p>
     *
     * @param weaver The {@link FxWeaver} instance to be wrapped by this {@link SimpleSpringable}.
     * @param ctx    The {@link ConfigurableApplicationContext} instance to be wrapped by this {@link SimpleSpringable}.
     */
    public SimpleSpringable { }
    
    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public @NotNull FxWeaver weaver() { return weaver; }
    
    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public @NotNull ConfigurableApplicationContext ctx() { return ctx; }
}
