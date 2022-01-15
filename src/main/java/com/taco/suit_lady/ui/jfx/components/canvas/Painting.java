package com.taco.suit_lady.ui.jfx.components.canvas;

import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public final class Painting {
    private Painting() { } //No Instance
    
    @Contract("_, _ -> new")
    public static @NotNull PaintCommand newCommand(@NotNull Springable springable, @NotNull Consumer<BoundCanvas> paintAction) {
        return newCommand(springable, null, paintAction);
    }
    
    @Contract("_, _, _ -> new")
    public static @NotNull PaintCommand newCommand(@NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull Consumer<BoundCanvas> paintAction) {
        return new PaintCommand(springable, lock) {
            @Override
            protected void onPaint(BoundCanvas canvas) {
                paintAction.accept(canvas);
            }
        };
    }
}
