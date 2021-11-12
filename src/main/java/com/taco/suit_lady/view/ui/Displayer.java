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

//TODO - Make read-only variation
public class Displayer<T extends Displayable>
{
    
    private final ReentrantLock lock;
    
    private final StackPane displayContainer;
    private final ReadOnlyObjectWrapper<T> displayProperty;
    
    private final BooleanProperty showingProperty;
    private final BooleanBinding visibleBinding;
    
    public Displayer(StackPane displayContainer)
    {
        this(null, displayContainer);
    }
    
    public Displayer(ReentrantLock lock, StackPane displayContainer)
    {
        this.lock = lock == null ? new ReentrantLock() : lock;
        
        this.displayContainer = displayContainer == null ? new StackPane() : displayContainer;
        this.displayProperty = new ReadOnlyObjectWrapper<>();
        
        this.showingProperty = new SimpleBooleanProperty(true);
        this.visibleBinding = Bindings.createBooleanBinding(
                () -> isShowing() && getDisplay() != null && this.displayContainer.isVisible(),
                this.showingProperty,
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
    
    public BooleanProperty showingProperty()
    {
        return showingProperty;
    }
    
    public boolean isShowing()
    {
        return showingProperty.get();
    }
    
    public void setShowing(boolean showing)
    {
        showingProperty.set(showing);
    }
    
    //
    
    public BooleanBinding visibleBinding()
    {
        return visibleBinding;
    }
    
    public boolean isVisible()
    {
        return visibleBinding.get();
    }
    
    //</editor-fold>
    
    //
    
    public void bind(ObservableValue<T> observable)
    {
        bind(observable, false);
    }
    
    public void bindAndInvalidate(ObservableValue<T> observable)
    {
        bind(observable, true);
    }
    
    private void bind(ObservableValue<T> observable, boolean invalidate)
    {
        ExceptionTools.nullCheck(observable, "Observable cannot be null");
        
        lock.lock();
        try
        {
            displayProperty.bind(observable);
            if (invalidate)
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
        //		Printing.dev("On Display Switch (" + GeneralTools.getSimpleName(oldDisplayable) + " -> " + GeneralTools.getSimpleName(newDisplayable) + ")");
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
}
