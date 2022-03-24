package com.taco.tacository.logic;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.logic.triggers.Galaxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>A {@link GameTask} implementation designed to {@link #execute() Execute} only {@code once}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>To construct a new {@link OneTimeTask} instance, use any of the available {@link OneTimeTask#OneTimeTask(GameComponent, Tickable) Constructors} or applicable {@link Galaxy} {@link Galaxy#newOneTimeTask(GameComponent, Tickable, Runnable) Factory Methods}.</li>
 * </ol>
 * <p><i>See {@link TaskManager} for additional information.</i></p>
 *
 * @param <E> The type of {@link Tickable} implementation assigned to this {@link OneTimeTask} object.
 *
 * @see GameTask
 * @see TaskManager
 * @see Galaxy
 * @see Galaxy#newOneTimeTask(GameComponent, Tickable, Runnable)
 * @see Galaxy#newOneTimeTask(Tickable, Runnable)
 */
//TO-EXPAND: Examples mainly
public abstract class OneTimeTask<E extends Tickable<E>> extends GameTask<E> {
    
    public OneTimeTask(@NotNull E owner) { super(owner); }
    public OneTimeTask(@Nullable GameComponent gameComponent, @NotNull E owner) {
        super(gameComponent, owner);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void shutdown() { }
    @Override protected boolean isDone() { return getTickCount() >= 1; }
    
    //</editor-fold>
}
