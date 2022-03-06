package com.taco.suit_lady.game;

import com.taco.suit_lady.game.ui.GameUIData;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.game.ui.GameViewContentData;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public interface GameComponent {
    
    @NotNull GameViewContent getGame();
    
    //
    
    default @NotNull ObjectProperty<GameMap> gameMapProperty() { return getGame().gameMapProperty(); }
    default GameMap getGameMap() { return getGame().getGameMap(); }
    default GameMap setGameMap(@NotNull GameMap newValue) { return getGame().setGameMap(newValue); }
    
    default GameViewContentData getData() { return getGame().getData(); }
    default GameUIData getUIData() { return getData().getUIData(); }
    
    default @NotNull Camera getCamera() { return getGameMap().getModel().getCamera(); }
    
    //
    
    default @NotNull Num2D pixelToTile(@NotNull Point2D pixelValue) { return new Num2D(pixelToTile(pixelValue.getX()), pixelToTile(pixelValue.getY())); }
    default @NotNull Num2D pixelToTile(@NotNull NumExpr2D<?> pixelValue) { return pixelToTile(pixelValue.asPoint()); }
    default @NotNull Num2D pixelToTile(@NotNull Number x, @NotNull Number y) { return pixelToTile(new Num2D(x, y)); }
    
    default @NotNull Num2D tileToPixel(@NotNull Point2D tileValue) { return new Num2D(tileToPixel(tileValue.getX()), tileToPixel(tileValue.getY())); }
    default @NotNull Num2D tileToPixel(@NotNull NumExpr2D<?> tileValue) { return tileToPixel(tileValue.asPoint()); }
    default @NotNull Num2D tileToPixel(@NotNull Number x, @NotNull Number y) { return tileToPixel(new Num2D(x, y)); }
    
    
    default @NotNull Number pixelToTile(@NotNull Number pixelValue) { return pixelValue.doubleValue() / getTileSize(); }
    default @NotNull Number tileToPixel(@NotNull Number tileValue) { return tileValue.doubleValue() * getTileSize(); }
    
    //
    
    default int getTileSize() { return getGameMap().getTileSize(); }
}