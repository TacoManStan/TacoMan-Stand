package com.taco.tacository.game;

import com.taco.tacository.game.items.GameItem;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.objects.tiles.GameTile;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;

import java.io.Serializable;

/**
 * <p>The {@code foundational} interface definition, implemented by all {@code objects} that represent a tangible {@link GameViewContent Game Component}.</p>
 * <p><b>Example Implementations</b></p>
 * <ol>
 *     <li>{@link GameObject}</li>
 *     <li>{@link GameTile}</li>
 *     <li>{@link GameItem}</li>
 * </ol>
 */
public interface Entity
        extends Springable, Lockable, Serializable, GameComponent {
}
