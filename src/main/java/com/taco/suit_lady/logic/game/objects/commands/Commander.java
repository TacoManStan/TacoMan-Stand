package com.taco.suit_lady.logic.game.objects.commands;

import com.taco.suit_lady.logic.game.execution.AutoManagedTickable;
import com.taco.suit_lady.logic.game.execution.WrappedTickable;
import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>Container class for containing, managing, executing, adding/removing, etc. {@link Command commands}.</p>
 */
public class Commander
        implements SpringableWrapper, WrappedTickable<Commander> {
    
    private final AutoManagedTickable<Commander> tickable;
    
    private final GameObject owner;
    private final LinkedBlockingQueue<Command> commandQueue;
    
    public Commander(@NotNull GameObject owner) {
        this.tickable = new AutoManagedTickable<>(this) {
            @Override
            protected void step() {
                getCommandQueue().forEach(command -> command.tick());
            }
        };
        
        this.owner = owner;
        this.commandQueue = new LinkedBlockingQueue<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() {
        return owner;
    }
    
    public final LinkedBlockingQueue<Command> getCommandQueue() {
        return commandQueue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return owner;
    }
    
    @Override
    public @NotNull AutoManagedTickable<Commander> tickable() {
        return tickable;
    }
    
    //</editor-fold>
}
