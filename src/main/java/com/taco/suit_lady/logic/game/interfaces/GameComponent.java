package com.taco.suit_lady.logic.game.interfaces;

import com.taco.suit_lady.logic.game.Camera;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import javafx.beans.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;

public interface GameComponent {
    
    @NotNull GameViewContent game();
    
    //
    
    default @NotNull ObjectProperty<GameMap> gameMapProperty() { return game().gameMapProperty(); }
    default GameMap getGameMap() { return game().getGameMap(); }
    default GameMap setGameMap(@NotNull GameMap newValue) { return game().setGameMap(newValue); }
}