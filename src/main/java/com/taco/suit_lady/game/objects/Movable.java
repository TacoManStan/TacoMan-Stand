package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.WrappedGameComponent;
import org.jetbrains.annotations.NotNull;

public interface Movable
        extends WrappedGameComponent {
    
    @NotNull Mover mover();
    
    //
    
//    default @NotNull Point2D move(@NotNull Point2D targetPoint) { return mover().move(targetPoint); }
//    default @NotNull Point2D unbindAndMove(@NotNull Point2D targetPoint) { return mover().unbindAndMove(targetPoint); }
//    default @NotNull Point2D moveAndBind(@NotNull ObservableValue<? extends Number> observableTargetX, @NotNull ObservableValue<? extends Number> observableTargetY) {
//        return mover().moveAndBind(observableTargetX, observableTargetY);
//    }
//
//    default @NotNull Point2D getTarget() { return mover().getTarget(); }
}
