package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class Effect
        implements WrappedGameComponent {
    
    private final GameObject source;
    
    public Effect(@NotNull GameObject source) {
        this.source = source;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getSource() { return source; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    public abstract boolean trigger(@NotNull Map<String, ?> params);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return source.getGame(); }
    
    //</editor-fold>
}
