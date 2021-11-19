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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Contains logic for easily displaying {@link Displayable} objects.</p>
 *
 * @param <T> The type of {@link Displayable} displayed by this {@link Displayer}.
 * @see Displayable
 */
public class Displayer<T extends Displayable>
{
    private final ReentrantLock lock;
    
    private final StackPane displayContainer;
    private final ReadOnlyObjectWrapper<T> displayProperty;
    
    private final BooleanProperty shouldShowProperty;
    private final BooleanBinding showingBinding;
    
    /**
     * <p>Constructs a new {@link Displayer} that contains the logic and details pertaining to a particular {@link Displayable} implementation.</p>
     * <ol>
     *     <li><i>{@link #Displayer(StackPane) new Displayer()}</i> is identical to <i>{@link #Displayer(ReentrantLock, StackPane) new Displayer(null, null)}</i>.</li>
     * </ol>
     * <p>Refer to the {@link #Displayer(ReentrantLock, StackPane) fully parameterized constructor} for details.</p>
     */
    public Displayer()
    {
        this(null, null);
    }
    
    /**
     * <p>Constructs a new {@link Displayer} that contains the logic and details pertaining to a particular {@link Displayable} implementation.</p>
     * <ol>
     *     <li><i>{@link #Displayer(StackPane) new Displayer(displayContainer)}</i> is identical to <i>{@link #Displayer(ReentrantLock, StackPane) new Displayer(null, displayContainer)}</i>.</li>
     * </ol>
     * <p>Refer to the {@link #Displayer(ReentrantLock, StackPane) fully parameterized constructor} for details.</p>
     *
     * @param displayContainer A {@link StackPane} that the {@link Displayable#getContent() displayable content} will be displayed on.
     */
    public Displayer(StackPane displayContainer)
    {
        this(null, displayContainer);
    }
    
    /**
     * <p>Constructs a new {@link Displayer} that contains the logic and details pertaining to a particular {@link Displayable} implementation.</p>
     * <ol>
     *     <li>The {@link Displayable} is added to the specified {@link StackPane}.</li>
     *     <li>The {@link Displayable Displayable's} dimensions are automatically bound to the {@link StackPane StackPane's} dimensions.</li>
     * </ol>
     *
     * @param lock             The {@link ReentrantLock} object that will be used to {@link Lock#lock() synchronize} this {@link Displayer} instance.
     *                         <br>
     *                         If the specified {@link ReentrantLock lock} is null, a new {@link ReentrantLock lock} instance will be created instead.
     * @param displayContainer A {@link StackPane} that the {@link Displayable#getContent() displayable content} will be displayed on.
     *                         <br>
     *                         If the specified {@link StackPane} is null, a new {@link StackPane} instance will be created instead.
     */
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
    
    /**
     * <p>Returns the {@link StackPane} instance that this {@link Displayer} is displaying its {@link #displayProperty() contents} on.</p>
     *
     * @return The {@link StackPane} instance that this {@link Displayer} is displaying its {@link #displayProperty() contents} on.
     */
    public StackPane getDisplayContainer()
    {
        return displayContainer;
    }
    
    //
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty Property} containing the {@link Displayable} object managed by this {@link Displayer} instance.</p>
     *
     * @return The {@link ReadOnlyObjectProperty Property} containing the {@link Displayable} object managed by this {@link Displayer} instance.
     * @see #getDisplay()
     * @see #setDisplay(Displayable)
     */
    public ReadOnlyObjectProperty<T> displayProperty()
    {
        return displayProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Displayable} object managed by this {@link Displayer} instance.</p>
     *
     * @return The {@link Displayable} object managed by this {@link Displayer} instance.
     * @see #displayProperty()
     * @see #setDisplay(Displayable)
     */
    public T getDisplay()
    {
        return displayProperty.get();
    }
    
    /**
     * <p>Sets the {@link Displayable} object managed by this {@link Displayer} instance to the specified value.</p>
     *
     * @param displayable The {@link Displayable} object to be managed by this {@link Displayer} instance.
     * @see #displayProperty()
     * @see #getDisplay()
     */
    public void setDisplay(T displayable)
    {
        displayProperty.set(displayable);
    }
    
    //
    
    /**
     * <p>A {@link BooleanProperty} defining whether this {@link Displayer} <i>should</i> display its contents.</p>
     * <ol>
     *     <li>Contrary to {@link #showingBinding()}, the {@link #shouldShowProperty()} only defines whether or not the {@link Displayer} <i>should</i> display its contents.</li>
     *     <li>In other words, if the {@link Displayable} is invalid or otherwise cannot be displayed, the value of {@link #shouldShowProperty()} is irrelevant.</li>
     * </ol>
     *
     * @return The {@link BooleanProperty} defining whether this {@link Displayer} <i>should</i> display its contents.
     * @see #shouldShow()
     * @see #setShouldShow(boolean)
     * @see #showingBinding()
     */
    public BooleanProperty shouldShowProperty()
    {
        return shouldShowProperty;
    }
    
    /**
     * <p>Returns the value of {@link #shouldShowProperty()}.</p>
     * <ol>
     *     <li><i>{@link #shouldShow()}</i> is identical to <i>{@link #shouldShowProperty() shouldShowProperty().get()}</i>.</li>
     * </ol>
     *
     * @return True if this {@link Displayer} instance is currently set to be {@link #shouldShowProperty() shown}, false if it is not.
     * @see #shouldShowProperty()
     * @see #setShouldShow(boolean)
     * @see #showingBinding()
     */
    public boolean shouldShow()
    {
        return shouldShowProperty.get();
    }
    
    /**
     * <p>Tells this {@link Displayer} instance whether it should display its {@link #getDisplay() contents} or not.</p>
     *
     * @param showing True if this {@link Displayer} instance should show its {@link #getDisplay() contents}, false if it should not.
     * @see #shouldShowProperty()
     * @see #shouldShow()
     * @see #showingBinding()
     */
    public void setShouldShow(boolean showing)
    {
        shouldShowProperty.set(showing);
    }
    
    //
    
    /**
     * <p>A {@link BooleanBinding} bound to the logic that determines if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents} or not.</p>
     * <p>
     * Contrary to {@link #shouldShowProperty()}, {@link #showingBinding()} is a calculated value that is bound to the actual display state of this {@link Displayer} instance.
     * </p>
     *
     * @return The {@link BooleanBinding} bound to the logic that determines if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents} or not.
     * @see #isShowing()
     * @see #shouldShowProperty()
     */
    public BooleanBinding showingBinding()
    {
        return showingBinding;
    }
    
    /**
     * <p>Checks if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents} or not.</p>
     * <ol>
     *     <li><i>{@link #isShowing()}</i> is identical to <i>{@link #showingBinding() showingBinding().get()}</i>.</li>
     * </ol>
     *
     * @return True if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents}, false if it is not.
     * @see #showingBinding()
     * @see #shouldShowProperty()
     */
    public boolean isShowing()
    {
        return showingBinding().get();
    }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="--- INTERNAL ---">
    
    /**
     * <p>Binds the {@link #displayProperty() Displayable} managed by this {@link Displayer} to the specified {@link ObservableValue}.</p>
     *
     * @param observable The {@link ObservableValue} to define the {@link #displayProperty() Displayable} object managed by this {@link Displayer}
     *                   via a {@link ReadOnlyObjectWrapper#bind(ObservableValue) binding}.
     */
    public void bind(ObservableValue<T> observable)
    {
        ExceptionTools.nullCheck(observable, "Observable cannot be null");
        
        lock.lock();
        try {
            displayProperty.bind(observable);
            if (observable instanceof Binding<T>)
                ((Binding<T>) observable).invalidate();
            else
                throw ExceptionTools.unsupported("Observable must be instance of Binding to invalidate.");
        } finally {
            lock.unlock();
        }
    }
    
    //TODO - Synchronize actual displayContainer children
    private void onDisplaySwitch(T oldDisplayable, T newDisplayable)
    {
        lock.lock();
        try {
            //Remove old content
            if (oldDisplayable != null) {
                Node oldContent = oldDisplayable.getContent();
                if (oldContent != null)
                    displayContainer.getChildren().remove(oldContent);
            }
            
            //Add new content
            if (newDisplayable != null) {
                Pane newContent = newDisplayable.getContent();
                if (newContent != null) {
                    FXTools.get().bindToParent(newContent, displayContainer, FXTools.BindOrientation.BOTH, FXTools.BindType.BOTH);
                    displayContainer.getChildren().add(newContent);
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    //</editor-fold>
}
