package com.taco.suit_lady.view.ui.jfx.button;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <b>--- To Format ---</b>
 * <br><br>
 * <p>{@link BoundImageButtonGroup} is identical to its parent, {@link ImageButtonGroup}, except...</p>
 * <ol>
 *     <li>{@link BoundImageButtonGroup} contains an {@link #buttonViewables() List} of {@link ButtonViewable ButtonViewables} rather than the parent {@link #buttons() List} of {@link ImageButton ImageButtons}.</li>
 *     <li>The parent {@link #buttons() contents} are bound to the {@link #buttonViewables() ButtonViewables} in this {@link BoundImageButtonGroup}.</li>
 * </ol>
 */
public class BoundImageButtonGroup<T extends ButtonViewable> extends ImageButtonGroup
{
    private final ObservableList<T> buttonViewables;
    
    public BoundImageButtonGroup()
    {
        this(null, null);
    }
    
    public BoundImageButtonGroup(ObservableList<T> buttonViewables)
    {
        this(buttonViewables, null);
    }
    
    public BoundImageButtonGroup(ObservableList<T> buttonViewables, ReentrantLock lock)
    {
        super(lock);
        this.buttonViewables = buttonViewables != null ? buttonViewables : FXCollections.observableArrayList();
        
        //
        
        // ListChangeListener binding the buttonViewables list to the parent buttons list.
        this.buttonViewables.addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(buttonViewable -> onViewableAdded(buttonViewable));
                change.getRemoved().forEach(buttonViewable -> onViewableRemoved(buttonViewable));
            }
        });
    }
    
    private void onViewableAdded(T buttonViewable)
    {
        if (buttonViewable != null) {
            final ImageButton imageButton = buttonViewable.getButtonView();
            if (imageButton != null)
                buttons.add(imageButton);
        }
    }
    
    private void onViewableRemoved(T buttonViewable)
    {
        if (buttonViewable != null) {
            final ImageButton imageButton = buttonViewable.getButtonView();
            if (imageButton != null)
                buttons.remove(imageButton);
        }
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ObservableList} containing all {@link ButtonViewable ButtonViewables} in this {@link BoundImageButtonGroup}.</p>
     * <ol>
     *     <li>Changes made to the returned {@link ObservableList} are reflected in the parent {@link ImageButton} {@link #buttons() List}.</li>
     *     <li>Currently, the internal {@link ListChangeListener binding} linking <i>{@link #buttonViewables()}</i> to the parent <i>{@link #buttons()}</i> is <i>not</i> synchronized, and is therefore <i>not</i> thread safe.</li>
     * </ol>
     *
     * @return The {@link ObservableList} containing all {@link ButtonViewable ButtonViewables} in this {@link BoundImageButtonGroup}.
     */
    public ObservableList<T> buttonViewables()
    {
        return buttonViewables;
    }
    
    //</editor-fold>
    
    /**
     * <p>Returns the {@link ButtonViewable} object the specified {@link ImageButton} is assigned to.</p>
     * <ol>
     *     <li>If the specified {@link ImageButton button} is {@code null}, return {@code null}.</li>
     *     <li>If the specified {@link ImageButton button} has not been assigned to any of the {@link ButtonViewable ButtonViewables} in this {@link BoundImageButtonGroup group}, return {@code null}.</li>
     *     <li>This method is necessary because {@link ImageButton} objects are unaware if they are assigned to a {@link ButtonViewable}.</li>
     * </ol>
     *
     * @param button The {@link ImageButton} being used as the {@code lookup key} to retrieve the desired {@link ButtonViewable} instance.
     * @return The {@link ButtonViewable} object the specified {@link ImageButton} is assigned to.
     */
    public T getViewableByButton(ImageButton button)
    {
        if (button != null) {
            lock.lock();
            try {
                // TODO - Ensure the specified ImageButton is assigned to only one of the given ButtonViewables
                final List<T> buttonViewables = buttonViewables();
                for (T buttonViewable: buttonViewables)
                    if (Objects.equals(button, buttonViewable.getButtonView()))
                        return buttonViewable;
            } finally {
                lock.unlock();
            }
        }
        return null;
    }
}
