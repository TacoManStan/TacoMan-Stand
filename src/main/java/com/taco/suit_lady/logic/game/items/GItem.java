package com.taco.suit_lady.logic.game.items;

import com.taco.suit_lady.logic.game.GAttributeContainer;
import com.taco.suit_lady.logic.game.GEntity;
import com.taco.suit_lady.logic.game.interfaces.GAttributeContainable;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>{@link GItem} objects are game entities that are displayed via an inventory grid.</p>
 * <p>Items can be dropped, at which point they are no longer {@link GItem} objects, but rather a {@link GEntity} implementation that carries a reference to the {@link GItem} that it represents.</p>
 * <br>
 * <p><b>UPDATE 1</b></p>
 * <ol>
 *     <li>{@link GItem} now implements {@link GEntity} directly, as {@link GEntity} now defines universal game entity definitions, such as name & attributes.</li>
 * </ol>
 */
// TO-IMPROVE
public class GItem
        implements Lockable, GAttributeContainable, GEntity {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    private final GAttributeContainer attributes;
    
    public GItem(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.attributes = new GAttributeContainer(this, lock, this);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull GAttributeContainer attributes() {
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
    
    //</editor-fold>
    
    //</editor-fold>
}
