package com.taco.suit_lady.logic.game.objects;

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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GObject
        implements Lockable, GAttributeContainable, GEntity {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    private final GAttributeContainer attributes;
    
    public GObject(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.attributes = new GAttributeContainer(this, lock, this);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull GAttributeContainer attributes() {
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
    public @NotNull Lock getLock() {
        return null;
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
