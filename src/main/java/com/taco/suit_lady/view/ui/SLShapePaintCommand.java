package com.taco.suit_lady.view.ui;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainer;
import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class SLShapePaintCommand<S extends Shape> extends SLPaintCommand<S>
        implements ObservablePropertyContainer
{
    public SLShapePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<S>> autoRemoveCondition,
            boolean scaleToParent, int priority,
            int x, int y, int width, int height)
    {
        super(lock, springable, name, autoRemoveCondition, scaleToParent, priority);
    }
}
