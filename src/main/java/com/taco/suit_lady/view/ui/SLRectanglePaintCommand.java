package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.components.PaintCommandable;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class SLRectanglePaintCommand extends SLShapePaintCommand<Rectangle>
{
    public SLRectanglePaintCommand(
            @Nullable ReentrantLock lock, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<Rectangle>> autoRemoveCondition,
            boolean scaleToParent, int priority,
            int x, int y, int width, int height)
    {
        super(lock, name, autoRemoveCondition, scaleToParent, priority, x, y, width, height);
    }
    
    // TODO: Add additional painting properties, probably to the parent intermediary SLShapePaintCommand class
    @Override
    protected Rectangle regenerateNode()
    {
        return getBounds().asFX();
    }
    
    @Override
    protected void onAdded(@NotNull Overlay owner) { }
    
    @Override
    protected void onRemoved(@NotNull Overlay owner) { }
}
