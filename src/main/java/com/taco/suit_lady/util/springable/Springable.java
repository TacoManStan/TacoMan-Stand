package com.taco.suit_lady.util.springable;

import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.console.Console;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
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
    @NotNull FxWeaver weaver();
    
    /**
     * <p>Grants access to the appropriate {@link ConfigurableApplicationContext Application Context} for use in this {@link Springable} implementation.</p>
     *
     * @return The {@link ConfigurableApplicationContext Application Context} to be used by this {@link Springable} implementation when necessary.
     */
    @NotNull ConfigurableApplicationContext ctx();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    /**
     * <p>Returns the singleton {@link Console} instance stored and managed by the {@link SpringApplication Spring} framework.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #ctx()}<b>.</b>{@link ApplicationContext#getBean(Class) getBean}<b>(</b>{@link Console}<b>.</b>{@link #getClass() class}<b>)</b></code></i></blockquote>
     *
     * @return The singleton {@link Console} instance stored and managed by the {@link SpringApplication Spring} framework.
     */
    default @NotNull Console console()
    {
        return ctx().getBean(Console.class);
    }
    
    /**
     * <p>Returns the singleton {@link AppUI} instance stored and managed by the {@link SpringApplication Spring} framework.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #ctx()}<b>.</b>{@link ApplicationContext#getBean(Class) getBean}<b>(</b>{@link AppUI}<b>.</b>{@link #getClass() class}<b>)</b></code></i></blockquote>
     *
     * @return The singleton {@link AppUI} instance stored and managed by the {@link SpringApplication Spring} framework.
     */
    default @NotNull AppUI ui()
    {
        return ctx().getBean(AppUI.class);
    }
    
    /**
     * <p>Returns the {@link Sidebar} instance contained within the {@link AppUI} singleton stored and managed by the {@link SpringApplication Spring} framework.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #ui()}<b>.</b>{@link AppUI#getSidebar() sidebar()}</b></code></i></blockquote>
     *
     * @return The {@link Sidebar} instance contained within the {@link AppUI} singleton stored and managed by the {@link SpringApplication Spring} framework.
     */
    default @NotNull Sidebar sidebar()
    {
        return ui().getSidebar();
    }
    
    //
    
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
