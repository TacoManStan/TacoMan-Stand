package com.taco.suit_lady.game.commands;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public class CommandHub
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final GameObject owner;
    
    public CommandHub(@NotNull GameObject owner) {
        this.owner = owner;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull GameObject getOwner() { return owner; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @Nullable Lock getLock() { return owner.getLock(); }
    
    //</editor-fold>
}
