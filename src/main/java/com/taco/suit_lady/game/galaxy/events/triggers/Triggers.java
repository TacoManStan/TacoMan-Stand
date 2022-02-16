package com.taco.suit_lady.game.galaxy.events.triggers;

import com.taco.suit_lady.game.objects.GameObject;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public final class Triggers {
    private Triggers() { } //No Instance
    
    public static @NotNull Trigger newObjectAtPointTrigger(@NotNull GameObject source, @NotNull Point2D point, @NotNull Runnable action) {
        return new Trigger() {
            @Override public boolean test() { return source.isAtPoint(point); }
            @Override public void trigger() {
                action.run();
            }
        };
    }
}
