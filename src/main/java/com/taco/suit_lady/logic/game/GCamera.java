package com.taco.suit_lady.logic.game;

/**
 * <p>An object that does not implement/extend any abstract game concept, but is rather a unique object that is assigned to a game window to define how the game is viewed.</p>
 * <p>All {@link GEntity} objects should be able to generate a new {@link GCamera} object that is bound to their location.</p>
 * <p>
 * {@link GCamera} objects should also permit an unbounded view, in which case the {@link GCamera} is not bound to the location of a {@link GEntity}, but rather follows a pre-defined set of rules.
 *     <ul>
 *         <li>For example, an unbounded {@link GCamera} could be used via a CameraTransition (not yet available) to pan/move/zoom/etc. to any location on the map given a set of movement instructions.</li>
 *     </ul>
 * </p>
 * <p>{@link GCamera} objects should be able to have tints, zoom values, vision radius, etc.</p>
 */
public class GCamera {
}
