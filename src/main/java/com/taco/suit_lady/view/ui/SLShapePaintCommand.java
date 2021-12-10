package com.taco.suit_lady.view.ui;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainer;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.ui_util.Bounds2D;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class SLShapePaintCommand<S extends Shape> extends SLPaintCommand<S>
        implements ObservablePropertyContainer
{
    public SLShapePaintCommand(
            @Nullable ReentrantLock lock, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<S>> autoRemoveCondition,
            boolean scaleToParent, int priority,
            int x, int y, int width, int height)
    {
        super(lock, name, autoRemoveCondition, scaleToParent, priority);
    }
}
