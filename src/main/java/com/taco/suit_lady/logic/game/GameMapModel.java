package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;

public class GameMapModel
        implements SpringableWrapper {
    
    private final GameMap owner;
    
    public GameMapModel(@NotNull GameMap owner) {
        this.owner = owner;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameMap getOwner() {
        return owner;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return owner;
    }
    
    //</editor-fold>
}
