package com.taco.suit_lady.logic.game.items;

import com.taco.suit_lady.logic.game.AttributeContainer;
import com.taco.suit_lady.logic.game.Entity;
import com.taco.suit_lady.logic.game.interfaces.AttributeContainable;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
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
        implements Lockable, AttributeContainable, Entity {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    private final AttributeContainer attributes;
    
    public Item(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.attributes = new AttributeContainer(this, lock, this);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull AttributeContainer attributes() {
        return attributes;
    }
    
    //<editor-fold desc="--- GENERIC ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public @NotNull ReentrantLock getLock() {
        return lock;
    }
    
    @Override public void tick() {
        //TODO
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
