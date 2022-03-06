package com.taco.suit_lady.game.items;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>{@link Item} objects are game entities that are displayed via an inventory grid.</p>
 * <p>Items can be dropped, at which point they are no longer {@link Item} objects, but rather a {@link Entity} implementation that carries a reference to the {@link Item} that it represents.</p>
 * <br>
 * <p><b>UPDATE 1</b></p>
 * <ol>
 *     <li>{@link Item} now implements {@link Entity} directly, as {@link Entity} now defines universal game entity definitions, such as name & attributes.</li>
 * </ol>
 */
// TO-IMPROVE
public class Item
        implements Lockable, Entity {
    
    private final GameViewContent content;
    private final ReentrantLock lock;
    
    public Item(@NotNull GameViewContent content, @Nullable ReentrantLock lock) {
        this.content = content;
        this.lock = lock != null ? lock : new ReentrantLock();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //<editor-fold desc="--- GENERIC ---">
    
    @Override public @NotNull FxWeaver weaver() { return content.weaver(); }
    @Override public @NotNull ConfigurableApplicationContext ctx() { return content.ctx(); }
    
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //</editor-fold>
    
    //</editor-fold>
}
