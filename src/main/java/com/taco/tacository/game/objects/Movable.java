package com.taco.tacository.game.objects;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.GameMap;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines implementing {@link Object Objects} as being {@link Movable} on a {@link GameMap}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@link Mover#move(NumExpr2D) Movement Logic} for a {@link Movable} implementation is defined by the {@link Mover} object returned by the abstract <i>{@link #mover()}</i> method.</li>
 * </ol>
 */
//TO-EXPAND
public interface Movable
        extends GameComponent {
    @NotNull Mover mover();
}
