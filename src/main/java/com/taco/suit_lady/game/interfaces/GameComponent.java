package com.taco.suit_lady.game.interfaces;

import com.taco.suit_lady.game.GameMap;
import com.taco.suit_lady.game.ui.GameUIData;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.game.ui.GameViewContentData;
import javafx.beans.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;

public interface GameComponent {
    
    @NotNull GameViewContent getGame();
    
    //
    
    default @NotNull ObjectProperty<GameMap> gameMapProperty() { return getGame().gameMapProperty(); }
    default GameMap getGameMap() { return getGame().getGameMap(); }
    default GameMap setGameMap(@NotNull GameMap newValue) { return getGame().setGameMap(newValue); }
    
    default GameViewContentData getData() { return getGame().getData(); }
    default GameUIData getUIData() { return getData().getUIData(); }
}