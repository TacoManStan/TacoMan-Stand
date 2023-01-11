package com.taco.tacository.util.springable;

import com.taco.tacository._to_sort._new.Debugger;
import com.taco.tacository.logic.LogiCore;
import com.taco.tacository.logic.triggers.TriggerEventManager;
import com.taco.tacository.ui.ContentManager;
import com.taco.tacository.ui.Sidebar;
import com.taco.tacository.ui.console.Console;
import com.taco.tacository.ui.AppUI;
import com.taco.tacository.util.tools.printing.Printer;
import com.taco.tacository.util.tools.Exe;
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
public interface Springable {
    
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
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #ctx()}<b>.</b>{@link ApplicationContext#getBean(Class) getBean}<b>(</b>{@link Console}<b>.</b>{@code class}<b>)</b></code></i></blockquote>
     *
     * @return The singleton {@link Console} instance stored and managed by the {@link SpringApplication Spring} framework.
     */
    default @NotNull Console console() { return getSafely(Console.class); }
    
    /**
     * <p>Returns the singleton {@link AppUI} instance stored and managed by the {@link SpringApplication Spring} framework.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #ctx()}<b>.</b>{@link ApplicationContext#getBean(Class) getBean}<b>(</b>{@link AppUI}<b>.</b>{@code class}<b>)</b></code></i></blockquote>
     *
     * @return The singleton {@link AppUI} instance stored and managed by the {@link SpringApplication Spring} framework.
     */
    default @NotNull AppUI ui() { return getSafely(AppUI.class); }
    
    default @NotNull ContentManager manager() { return ui().getContentManager(); }
    
    /**
     * <p>Returns the {@link Sidebar} instance contained within the {@link AppUI} singleton stored and managed by the {@link SpringApplication Spring} framework.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #ui()}<b>.</b>{@link AppUI#getSidebar() sidebar()}</code></i></blockquote>
     *
     * @return The {@link Sidebar} instance contained within the {@link AppUI} singleton stored and managed by the {@link SpringApplication Spring} framework.
     */
    default @NotNull Sidebar sidebar() { return ui().getSidebar(); }
    
    // TO-DOC
    default @NotNull Debugger debugger() { return getSafely(Debugger.class); }
    default @NotNull Printer printer() { return getSafely(Printer.class); }
    
    // TO-DOC
    default @NotNull LogiCore logiCore() { return getSafely(LogiCore.class); }
    default @NotNull TriggerEventManager triggers() { return logiCore().triggers(); }
    
    //
    
    /**
     * <p>Constructs a new {@link SimpleSpringable} instance wrapping the contents of this {@link Springable}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>By passing the value of {@link #asSimple() this method} wherever a {@link Springable} is required permits this {@link Springable} instance to be garbage collected when no longer used.</li>
     *     <li>This prevents the possibility of an unnecessary reference to this {@link Springable} instance from accidentally being stored as a {@link Springable}, preventing garbage collection.</li>
     *     <li>If the specified {@link Springable} is already an instance of {@link SimpleSpringable}, a reference to {@code this} {@link Springable} instance is returned instead.</li>
     * </ol>
     *
     * @return A new {@link SimpleSpringable} instance wrapping the contents of this {@link Springable}.
     */
    default @NotNull SimpleSpringable asSimple() {
        if (this instanceof SimpleSpringable simpleSpringable)
            return simpleSpringable;
        return new SimpleSpringable(weaver(), ctx());
    }
    
    /**
     * <p>Identical to {@link #asSimple()}, except the {@link Springable} returned by {@link #asStrict() this method} is {@link StrictSpringable strict}.</p>
     * <blockquote>Refer to <code><i>{@link #asSimple()}</i></code> for additional information.</blockquote>
     *
     * @return A new {@link StrictSpringable} instance wrapping the contents of this {@link Springable}.
     */
    default @NotNull StrictSpringable asStrict() {
        if (this instanceof StrictSpringable strictSpringable)
            return strictSpringable;
        return new StrictSpringable(weaver(), ctx());
    }
    
    //</editor-fold>
    
    default <T> @Nullable T getSafely(@NotNull Class<T> c) {
        if (ctx().isActive() && ctx().isRunning()) {
            return ctx().getBean(c);
        } else {
            System.err.println("Cannot retrieve Spring element as context is not active [" + c.getName() + "]");
            Exe.printThread();
            return null;
        }
    }
}
