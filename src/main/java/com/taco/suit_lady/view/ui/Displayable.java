package com.taco.suit_lady.view.ui;

import javafx.scene.layout.Pane;

/**
 * <p>Contains a single method, {@link #getContent()}, that returns a {@link Pane} housing the UI contents of this {@link Displayable} object.</p>
 *
 * @see Displayer
 */
public interface Displayable
{
    /**
     * <p>Returns the {@link Pane} housing the UI contents of this {@link Displayable}.</p>
     *
     * @return The {@link Pane} housing the UI contents of this {@link Displayable}.
     */
    Pane getContent();
}
