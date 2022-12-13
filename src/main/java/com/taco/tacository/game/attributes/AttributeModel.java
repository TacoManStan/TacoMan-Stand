package com.taco.tacository.game.attributes;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public abstract class AttributeModel<T, R extends Region>
        implements SpringableWrapper, Lockable, GameComponent {
    
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
    
    @Override public @NotNull GameViewContent getContent() { return getOwner().getGame(); }
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @Nullable Lock getLock() { return getOwner().getLock(); }
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract R constructDisplay();
    protected abstract R refreshBindings();
    protected abstract R pauseBindings();
    
    protected void shutdown() { }
    
    //</editor-fold>
}
