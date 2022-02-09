package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameUIData
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final GameViewContentData parent;
    
    private final ReadOnlyObjectWrapper<GameTile> selectedTileProperty;
    
    protected GameUIData(@NotNull GameViewContentData parent) {
        this.selectedTileProperty = new ReadOnlyObjectWrapper<>();
        this.selectedTileProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.getModel().setBorderShowing(false);
            if (newValue != null)
                newValue.getModel().setBorderShowing(true);
        });
        
        this.parent = parent;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final ReadOnlyObjectWrapper<GameTile> selectedTileProperty() { return selectedTileProperty; }
    public final ReadOnlyObjectProperty<GameTile> readOnlySelectedTileProperty() { return selectedTileProperty.getReadOnlyProperty(); }
    public final GameTile getSelectedTile() { return selectedTileProperty.get(); }
    protected final GameTile setSelectedTile(GameTile newValue) { return PropertiesSL.setProperty(selectedTileProperty, newValue); }
    
    //</editor-fold>
    
    @Override public @NotNull Springable springable() { return parent; }
    @Override public @NotNull Lock getLock() { return parent.getLock(); }
    
    //
    
    @Override public @NotNull GameViewContent getGame() { return parent.getGame(); }
    
    @Override public @NotNull GameViewContentData getData() { return parent; }
    @Override public @NotNull GameUIData getUIData() { return this; }
}
