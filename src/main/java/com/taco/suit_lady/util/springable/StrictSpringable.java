package com.taco.suit_lady.util.springable;

import com.taco.suit_lady.util.tools.Exc;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>A basic implementation of {@link Springable}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Identical to {@link SimpleSpringable}, except its contents cannot be {@code null}.</li>
 * </ol>
 *
 * @see Springable
 * @see SimpleSpringable
 */
public record StrictSpringable(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx)
        implements Springable {
    
    /**
     * <p>Constructs a new {@link StrictSpringable} instance containing the specified {@link FxWeaver} and {@link ConfigurableApplicationContext Context}.</p>
     *
     * @param weaver The {@link FxWeaver} to be wrapped by this {@link StrictSpringable} instance.
     * @param ctx    The {@link ConfigurableApplicationContext} to be wrapped by this {@link StrictSpringable} instance.
     *
     * @throws NullPointerException If the specified {@link FxWeaver} is {@code null}.
     * @throws NullPointerException If the specified {@link ConfigurableApplicationContext} is {@code null}.
     */
    public StrictSpringable(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx) {
        this.weaver = Exc.nullCheck(weaver, "FxWeaver");
        this.ctx = Exc.nullCheck(ctx, "Application Context");
    }
    
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
