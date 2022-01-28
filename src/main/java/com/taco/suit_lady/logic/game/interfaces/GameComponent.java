package com.taco.suit_lady.logic.game.interfaces;

import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import javafx.beans.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;

public interface GameComponent {
    
    @NotNull GameViewContent getGame();
    
    //
    
    default @NotNull ObjectProperty<GameMap> gameMapProperty() { return getGame().gameMapProperty(); }
    default GameMap getGameMap() { return getGame().getGameMap(); }
    default GameMap setGameMap(@NotNull GameMap newValue) { return getGame().setGameMap(newValue); }
}