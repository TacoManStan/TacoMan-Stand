package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <b>--- To Format ---</b>
 * <br><br>
 * My best guess atm is that the Displayer class is meant to add basic functionality to a Displayable that defines how to display it.
 * To me, it seems like this completely defeats the purpose of making Displayable in the first place, as it is an interface.
 * It also doesn't make sense for Displayer to be typed, as it should be able to display any type of Displayable... right? I think? Maybe not...
 * At the very least, the Displayable/Displayer system is going to need to be analyzed and documented at the very least, more likely heavily reworked.
 */
public class Displayer<T extends Displayable>
{
    private final ReentrantLock lock;
    
    private final StackPane displayContainer;
    private final ReadOnlyObjectWrapper<T> displayProperty;
    
    private final BooleanProperty shouldShowProperty;
    private final BooleanBinding showingBinding;
    
    public Displayer(StackPane displayContainer)
    {
        this(null, displayContainer);
    }
    
    public Displayer(ReentrantLock lock, StackPane displayContainer)
    {
        this.lock = lock == null ? new ReentrantLock() : lock;
        
        this.displayContainer = displayContainer == null ? new StackPane() : displayContainer;
        this.displayProperty = new ReadOnlyObjectWrapper<>();
        
        this.shouldShowProperty = new SimpleBooleanProperty(true);
        this.showingBinding = Bindings.createBooleanBinding(
                () -> shouldShow() && getDisplay() != null && this.displayContainer.isVisible(),
                this.shouldShowProperty,
                this.displayProperty(),
                this.displayContainer.visibleProperty()
        );
        
        //
        
        this.displayProperty.addListener((observable, oldDisplay, newDisplay) -> onDisplaySwitch(oldDisplay, newDisplay));
    }
    
    //<editor-fold desc="Properties">
    
    public StackPane getDisplayContainer()
    {
        return displayContainer;
    }
    
    //
    
    public ReadOnlyObjectProperty<T> displayProperty()
    {
        return displayProperty.getReadOnlyProperty();
    }
    
    public T getDisplay()
    {
        return displayProperty.get();
    }
    
    public void setDisplay(T displayable)
    {
        displayProperty.set(displayable);
    }
    
    //
    
    /**
     * <p>A {@link BooleanProperty} defining whether this {@link Displayer} <i>should</i> display its contents.</p>
     * <p>
     * Contrary to {@link #showingBinding() showingBinding}, the {@link #shouldShowProperty() shouldShowProperty} only defines whether or not the {@link Displayer}
     * <i>should</i> display its contents.
     * </p>
     * <p>In other words, if the {@link Displayable} is invalid or otherwise cannot be displayed, the value of {@link #shouldShowProperty() shouldShowProperty} is irrelevant.</p>
     *
     * @return The {@link BooleanProperty} defining whether this {@link Displayer} <i>should</i> display its contents.
     * @see #showingBinding()
     */
    public BooleanProperty shouldShowProperty()
    {
        return shouldShowProperty;
    }
    
    public boolean shouldShow()
    {
        return shouldShowProperty.get();
    }
    
    public void setShouldShow(boolean showing)
    {
        shouldShowProperty.set(showing);
    }
    
    //
    
    /**
     * <p>A {@link BooleanBinding} defining whether this {@link Displayer} is currently displaying its contents.</p>
     * <p>
     * Contrary to {@link #shouldShowProperty() shouldShowProperty}, the {@link #showingBinding() showingBinding} is a calculated value that is bound to the actual display status of
     * this {@link Displayer}.
     * </p>
     *
     * @return The {@link BooleanBinding} defining whether this {@link Displayer} <i>is</i> displaying its contents.
     */
    public BooleanBinding showingBinding()
    {
        return showingBinding;
    }
    
    public boolean isShowing()
    {
        return showingBinding.get();
    }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="--- INTERNAL ---">
    
    public void bind(ObservableValue<T> observable)
    {
        ExceptionTools.nullCheck(observable, "Observable cannot be null");
        
        lock.lock();
        try
        {
            displayProperty.bind(observable);
            if (observable instanceof Binding<T>)
                ((Binding<T>) observable).invalidate();
            else
                throw ExceptionTools.unsupported("Observable must be instance of Binding to invalidate.");
        }
        finally
        {
            lock.unlock();
        }
    }
    
    //TODO - Synchronize actual displayContainer children
    private void onDisplaySwitch(T oldDisplayable, T newDisplayable)
    {
        lock.lock();
        try
        {
            //Remove old content
            if (oldDisplayable != null)
            {
                Node oldContent = oldDisplayable.getContent();
                if (oldContent != null)
                    displayContainer.getChildren().remove(oldContent);
            }
            
            //Add new content
            if (newDisplayable != null)
            {
                Pane newContent = newDisplayable.getContent();
                if (newContent != null)
                {
                    FXTools.get().bindToParent(newContent, displayContainer, FXTools.BindOrientation.BOTH, FXTools.BindType.BOTH);
                    displayContainer.getChildren().add(newContent);
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    //</editor-fold>
}
