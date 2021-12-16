package com.taco.suit_lady.view.ui.painting;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class SLShapePaintCommand<S extends Shape> extends SLPaintCommand<S>
        implements ObservablePropertyContainable
{
    public SLShapePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<S>> autoRemoveCondition, int priority)
    {
        super(lock, springable, name, autoRemoveCondition, priority);
    }
}
