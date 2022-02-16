package com.taco.suit_lady.game.interfaces;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.legacy.TickableMk1;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public interface GameObjectComponent
        extends SpringableWrapper, Lockable, GameComponent, TickableMk1 {
    
    @NotNull GameObject getOwner();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default @Override @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    default @Override @Nullable Lock getLock() { return getOwner().getLock(); }
    default @Override @NotNull Springable springable() { return getOwner(); }
    
    //</editor-fold>
}
