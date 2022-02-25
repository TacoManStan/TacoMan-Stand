package com.taco.suit_lady.game.attributes;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

public abstract class AttributeModel<T, R extends Region>
        implements WrappedGameComponent {
    
    private final Attribute<T> owner;
    private final R attributeDisplay;
    
    public AttributeModel(@NotNull Attribute<T> owner) {
        this.owner = owner;
        this.attributeDisplay = constructDisplay();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Attribute<T> getOwner() { return owner; }
    
    public final R getDisplay() { return attributeDisplay; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract R constructDisplay();
    protected abstract R refreshBindings();
    protected abstract R pauseBindings();
    
    protected void shutdown() { }
    
    //</editor-fold>
}
