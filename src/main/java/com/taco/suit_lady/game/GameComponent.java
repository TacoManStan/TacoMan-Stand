package com.taco.suit_lady.game;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameUIData;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.game.ui.GameViewContentData;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines an {@link Object} as being a part of the {@link GameViewContent} virtual environment.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The primary purpose of {@link GameComponent} is to provide the abstract <i>{@link #getGame()}</i> method used to access the {@link GameViewContent} this {@link GameComponent} is a member of.</li>
 *     <li>
 *         {@link GameComponent} also offers a number of convenient {@code default methods} for accessing commonly-referenced {@link GameViewContent} properties. Examples include:
 *         <ul>
 *             <li>
 *                 Game Map Methods
 *                 <ul>
 *                     <li>{@link #gameMapProperty()}</li>
 *                     <li>{@link #getGameMap()}</li>
 *                     <li>{@link #setGameMap(GameMap)}</li>
 *                 </ul>
 *             </li>
 *             <li>{@link #getData()}</li>
 *             <li>{@link #getUIData()}</li>
 *             <li>{@link #getCamera()}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         In addition, {@link GameComponent} offers a variety of convenient {@code default methods} providing commonly-needed operations and calculations. Examples include:
 *         <ul>
 *             <li>
 *                 Pixel to Tile Methods
 *                 <ul>
 *                     <li>{@link #pixelToTile(Point2D)}</li>
 *                     <li>{@link #pixelToTile(NumExpr2D)}</li>
 *                     <li>{@link #pixelToTile(Number, Number)}</li>
 *                     <li>{@link #pixelToTile(Number)}</li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 Tile to Pixel Methods
 *                 <ul>
 *                     <li>{@link #tileToPixel(Point2D)}</li>
 *                     <li>{@link #tileToPixel(NumExpr2D)}</li>
 *                     <li>{@link #tileToPixel(Number, Number)}</li>
 *                     <li>{@link #tileToPixel(Number)}</li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 *     <li>The {@link GameObjectComponent} implementation of {@link GameComponent} defines an {@link Object} as being a member/property of a {@link GameObject}.</li>
 * </ol>
 */
//TO-DO: Examples?
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