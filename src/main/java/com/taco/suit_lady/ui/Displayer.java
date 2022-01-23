package com.taco.suit_lady.ui;

import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
    
    private final BooleanProperty hiddenProperty;
    private final BooleanBinding visibleBinding;
    
    /**
     * <p>Constructs a new {@link Displayer} that contains the logic and details pertaining to a particular {@link Displayable} implementation.</p>
     * <ol>
     *     <li><i>{@link #Displayer(StackPane) new Displayer()}</i> is identical to <i>{@link #Displayer(ReentrantLock, StackPane) new Displayer(null, null)}</i>.</li>
     * </ol>
     * <p>Refer to the {@link #Displayer(ReentrantLock, StackPane) fully parameterized constructor} for details.</p>
     *
     * @see #Displayer(ReentrantLock, StackPane)
     * @see #Displayer(StackPane)
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
     * @see #Displayer(ReentrantLock, StackPane)
     * @see #Displayer()
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
     *     <li>If the specified {@link ReentrantLock lock} is {@code null}, a new {@link ReentrantLock} instance will be automatically constructed.</li>
     *     <li>If the specified {@link StackPane display container} is {@code null}, a new {@link StackPane} instance will be automatically constructed.</li>
     * </ol>
     *
     * @param lock             The {@link ReentrantLock} object that will be used to {@link Lock#lock() synchronize} this {@link Displayer} instance. Can be {@code null}.
     * @param displayContainer A {@link StackPane} that the {@link Displayable#getContent() displayable content} will be displayed on. Can be {@code null}.
     * @see #Displayer(StackPane)
     * @see #Displayer()
     */
    public Displayer(ReentrantLock lock, StackPane displayContainer)
    {
        this.lock = lock == null ? new ReentrantLock() : lock;
        
        this.displayContainer = displayContainer == null ? new StackPane() : displayContainer;
        this.displayProperty = new ReadOnlyObjectWrapper<>();
        
        this.hiddenProperty = new SimpleBooleanProperty(false);
        this.visibleBinding = Bindings.createBooleanBinding(
                () -> !isHidden() && getDisplay() != null && this.displayContainer.isVisible(),
                this.hiddenProperty,
                this.displayProperty(),
                this.displayContainer.visibleProperty()
        );
        
        //
        
        this.displayProperty.addListener((observable, oldDisplay, newDisplay) -> onDisplaySwitch(oldDisplay, newDisplay));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link StackPane} instance that this {@link Displayer} is displaying its {@link #displayProperty() contents} on.</p>
     *
     * @return The {@link StackPane} instance that this {@link Displayer} is displaying its {@link #displayProperty() contents} on.
     */
    public StackPane getDisplayContainer()
    {
        return displayContainer;
    }
    
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
    
    /**
     * <p>A {@link BooleanProperty} defining whether this {@link Displayer} <i>should</i> display its contents.</p>
     * <ol>
     *     <li>Contrary to {@link #visibleBinding()}, the {@link #hiddenProperty()} only defines whether or not the {@link Displayer} <i>should</i> display its contents.</li>
     *     <li>In other words, if the {@link Displayable} is invalid or otherwise cannot be displayed for any reason, the value of {@link #hiddenProperty()} is irrelevant.</li>
     *     <li>In other <i>other</i> words, {@link #hiddenProperty()} defines the <i>intention</i>, whereas {@link #visibleBinding()} defines the actual display state.</li>
     * </ol>
     *
     * @return The {@link BooleanProperty} defining whether this {@link Displayer} <i>should</i> display its contents.
     * @see #isHidden()
     * @see #setHidden(boolean)
     * @see #visibleBinding()
     */
    public BooleanProperty hiddenProperty()
    {
        return hiddenProperty;
    }
    
    /**
     * <p>Returns the value of {@link #hiddenProperty()}.</p>
     * <ol>
     *     <li><i>{@link #isHidden()}</i> is identical to <i>{@link #hiddenProperty() hiddenProperty().get()}</i>.</li>
     * </ol>
     *
     * @return True if this {@link Displayer} instance is currently set to {@link #hiddenProperty() hide} its contents, false if it is not.
     * @see #hiddenProperty()
     * @see #setHidden(boolean)
     * @see #visibleBinding()
     */
    public boolean isHidden()
    {
        return hiddenProperty().get();
    }
    
    /**
     * <p>Tells this {@link Displayer} instance whether it should <i>attempt to</i> display its {@link #getDisplay() contents} or not.</p>
     * <ol>
     *     <li><i>{@link #setHidden(boolean) setHidden(hidden)}</i> is identical to <i>{@link #hiddenProperty() hiddenProperty().set(hidden)}</i>.</li>
     * </ol>
     *
     * @param hidden True if this {@link Displayer} instance should hide its {@link #getDisplay() contents}, false if it should not.
     * @see #hiddenProperty()
     * @see #isHidden()
     * @see #visibleBinding()
     */
    public void setHidden(boolean hidden)
    {
        hiddenProperty().set(hidden);
    }
    
    /**
     * <p>A {@link BooleanBinding} bound to the logic that determines if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents} or not.</p>
     * <ol>
     *     <li>Contrary to {@link #hiddenProperty()}, the {@link #visibleBinding()} is a calculated value that is bound to the actual display state of this {@link Displayer Displayer's} {@link #displayProperty() contents}.</li>
     * </ol>
     *
     * @return The {@link BooleanBinding} bound to the logic that determines if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents} or not.
     * @see #isVisible()
     * @see #hiddenProperty()
     */
    public BooleanBinding visibleBinding()
    {
        return visibleBinding;
    }
    
    /**
     * <p>Checks if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents} or not.</p>
     * <ol>
     *     <li><i>{@link #isVisible()}</i> is identical to <i>{@link #visibleBinding() visibleBinding().get()}</i>.</li>
     * </ol>
     *
     * @return True if this {@link Displayer} instance is currently displaying its {@link #getDisplay() contents}, false if it is not.
     * @see #visibleBinding()
     * @see #hiddenProperty()
     */
    public boolean isVisible()
    {
        return visibleBinding().get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    /**
     * <p>{@link ReadOnlyObjectWrapper#bind(ObservableValue) Binds} the {@link #displayProperty() Displayable} instance managed by this {@link Displayer} to the specified {@link ObservableValue}.</p>
     * <ol>
     *     <li>If the specified {@link ObservableValue} is a {@link Binding}, the {@link Binding} is {@link Binding#invalidate() invalidated} immediately upon being {@link ReadOnlyObjectWrapper#bind(ObservableValue) bound}.</li>
     *     <li>If the specified {@link ObservableValue} is {@code null}, a {@link NullPointerException} is thrown.</li>
     * </ol>
     *
     * @param observable The {@link ObservableValue} to define the {@link #displayProperty() Displayable} object managed by this {@link Displayer}
     *                   via a {@link ReadOnlyObjectWrapper#bind(ObservableValue) binding}.
     * @see #isBound()
     * @see #unbind()
     * @see ReadOnlyObjectWrapper#bind(ObservableValue)
     */
    public void bind(ObservableValue<T> observable)
    {
        SLExceptions.nullCheck(observable, "Observable cannot be null");
        lock.lock();
        try {
            displayProperty.bind(observable);
            if (observable instanceof Binding<T>)
                ((Binding<T>) observable).invalidate();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * <p>Checks if the {@link #displayProperty() contents} of this {@link Displayer} object is currently {@link ReadOnlyObjectWrapper#isBound() bound} or not.</p>
     * <ol>
     *     <li>Internally, <i>{@link #isBound()}</i> is identical to <i>{@link ReadOnlyObjectWrapper#isBound() displayProperty.isBound()}</i>.</li>
     *     <li>Note that the {@link #displayProperty} field is a {@link ReadOnlyObjectWrapper}, whereas {@link #displayProperty()} returns a {@link ReadOnlyObjectWrapper#getReadOnlyProperty() ReadOnlyObjectProperty}.</li>
     * </ol>
     *
     * @return True if the {@link #displayProperty() contents} of this {@link Displayer} object are currently {@link ReadOnlyObjectWrapper#isBound() bound}, false if they are not.
     * @see #bind(ObservableValue)
     * @see #unbind()
     * @see ReadOnlyObjectWrapper#isBound()
     */
    public boolean isBound()
    {
        return displayProperty.isBound();
    }
    
    /**
     * <p>{@link ReadOnlyObjectWrapper#unbind() Unbinds} the {@link #displayProperty() contents} of this {@link Displayer}.</p>
     * <ol>
     *     <li>If the {@link #displayProperty() contents} of this {@link Displayer} are not {@link #isBound() bound}, this method does nothing.</li>
     * </ol>
     *
     * @see #bind(ObservableValue)
     * @see #isBound()
     * @see ReadOnlyObjectWrapper#unbind()
     */
    public void unbind()
    {
        displayProperty.unbind();
    }
    
    /**
     * <p>An internal method that is called whenever the {@link #displayProperty() contents} of this {@link Displayer} are {@link ObjectPropertyBase#addListener(ChangeListener) changed}.</p>
     * <p><b>Functionality Details</b></p>
     * <ol>
     *     <li>
     *         If non-null, the {@link T new displayable} parameter is automatically {@link FXTools#bindToParent(Region, Region, boolean, boolean, ObservableDoubleValue, FXTools.BindOrientation, FXTools.BindType) bound} to the {@link #getDisplayContainer() display container}.
     *         <ol>
     *             <li>The binding {@link FXTools.BindOrientation orientation} is set to {@link FXTools.BindOrientation#BOTH BOTH}.</li>
     *             <li>The binding {@link FXTools.BindType type} is set to {@link FXTools.BindType#BOTH BOTH}.</li>
     *         </ol>
     *     </li>
     *     <li>If the {@link Displayable oldDisplayable} parameter is non-null but contains null {@link Displayable#getContent() contents}, a {@link NullPointerException} is thrown.</li>
     *     <li>If the {@link Displayable newDisplayable} parameter is non-null but contains null {@link Displayable#getContent() contents}, no special actions are taken and this method returns silently.</li>
     * </ol>
     * <p><b>Implementation and Usage Details</b></p>
     * <ol>
     *     <li>This method is called {@link #Displayer(ReentrantLock, StackPane) internally} via a {@link ObjectPropertyBase#addListener(ChangeListener) ChangeListener}.</li>
     *     <li>This method should <i>never</i> be called manually or otherwise outside of the {@link ObjectPropertyBase#addListener(ChangeListener) ChangeListener}.</li>
     *     <li>The {@link ObjectPropertyBase#addListener(ChangeListener) ChangeListener} is defined and added in the {@link #Displayer(ReentrantLock, StackPane) constructor}.</li>
     * </ol>
     *
     * @param oldDisplayable The previous {@link #displayProperty() contents} of this {@link Displayer}.
     *                       <br>Will be {@code null} if this {@link Displayer} had no previous {@link #displayProperty() contents}.
     * @param newDisplayable The new {@link #displayProperty() contents} of this {@link Displayer}.
     *                       <br>Will be {@code null} if the {@link #displayProperty() contents} of this {@link Displayer} are being removed.
     */
    //TODO - Synchronize actual displayContainer children
    private void onDisplaySwitch(T oldDisplayable, T newDisplayable)
    {
        lock.lock();
        try {
            //Remove old content
            if (oldDisplayable != null) {
                final Node oldContent = oldDisplayable.getContent();
                if (oldContent != null)
                    displayContainer.getChildren().remove(oldContent);
                else
                    throw new NullPointerException("Displayable contents (old) cannot be null.");
            }
            
            //Add new content
            if (newDisplayable != null) {
                final Pane newContent = newDisplayable.getContent();
                if (newContent != null) {
                    FXTools.bindToParent(newContent, displayContainer, FXTools.BindOrientation.BOTH, FXTools.BindType.BOTH, true);
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    //</editor-fold>
}
