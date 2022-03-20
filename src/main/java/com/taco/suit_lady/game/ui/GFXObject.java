package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.util.tools.fx_tools.FX;

/**
 * <p>A specific implementation of {@link Tickable} that defines the {@link Tickable} as a {@link GFXObject}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>A {@link GFXObject} is an extension of {@link Tickable}.</li>
 *     <li>Override the abstract <i>{@link #onGfxUpdate()}</i> method to define all {@code operations} that must be {@link FX#runFX(Runnable, boolean) executed} on the {@link FX#isFXThread() JavaFX Applicatino Thread}.</li>
 *     <li>The {@link #taskManager() TaskManager} for a {@link GFXObject} instance automatically handles the {@link #onGfxUpdate() Graphics Operations} for the {@link GFXObject} instance.</li>
 *     <li>To eliminate redundant {@link FX#runFX(Runnable, boolean) Graphics Operations}, <i>{@link #onGfxUpdate()}</i> is only processed when <i>{@link #needsGfxUpdate()}</i> returns {@code true}.</li>
 *     <li>The optional <i>{@link #onGfxUpdateAlways()}</i> method can be overridden to define any {@link FX#runFX(Runnable, boolean Graphics Operations)} to be executed every {@link LogiCore#tick() Game Tick}, regardless of the value returned by <i>{@link #needsGfxUpdate()}</i>.</li>
 *     <li>
 *         All {@link GFXObject} implementations are {@code executed} by calling the <i>{@link #updateGfx()}</i> method.
 *         <ul>
 *             <li>By {@code default}, <i>{@link #updateGfx()}</i> executes <i>{@link #onGfxUpdateAlways()}</i>, checks the value returned by <i>{@link #needsGfxUpdate()}</i>, and then executes <i>{@link #onGfxUpdate()}</i> if <i>{@link #needsGfxUpdate()}</i> returns {@code true}.</li>
 *             <li>In the majority of cases, the {@code default} implementation of <i>{@link #updateGfx()}</i> is sufficient.</li>
 *         </ul>
 *         <p><i>See {@link ContentController#updateGfx()} for a {@link #updateGfx()} implementation example.</i></p>
 *     </li>
 * </ol>
 *
 * @param <E> The type of this {@link GFXObject} implementation.
 */
public interface GFXObject<E extends Tickable<E>>
        extends Tickable<E> {
    
    boolean needsGfxUpdate();
    void onGfxUpdate();
    
    //
    
    default void onGfxUpdateAlways() { }
    default void updateGfx() {
        onGfxUpdateAlways();
        if (needsGfxUpdate())
            onGfxUpdate();
    }
}
