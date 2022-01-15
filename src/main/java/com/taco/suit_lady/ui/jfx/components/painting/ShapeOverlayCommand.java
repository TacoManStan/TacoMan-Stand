package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class ShapeOverlayCommand<S extends Shape> extends OverlayCommand<S>
        implements ObservablePropertyContainable
{
    public ShapeOverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<S>> autoRemoveCondition, int priority)
    {
        super(lock, springable, name, autoRemoveCondition, priority);
    }
}
