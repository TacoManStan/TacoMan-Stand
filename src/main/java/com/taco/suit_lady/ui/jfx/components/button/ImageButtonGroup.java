package com.taco.suit_lady.ui.jfx.components.button;

import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ToolsSL;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Represents a collection of {@link ImageButton} objects that are linked together as mutually-exclusive selectable values.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>To add/remove an {@link ImageButton} to/from an {@link ImageButtonGroup}, use the <i>{@link ImageButton#setButtonGroup(ImageButtonGroup)}</i> method in the {@link ImageButton} class itself.</li>
 *     <li>The {@link BoundImageButtonGroup} implementation of {@link ImageButtonGroup this class} allows you to add objects directly so long as they implement the {@link ButtonViewable} interface.</li>
 * </ol>
 */
//TO-EXPAND
public class ImageButtonGroup {
    
    protected final ReentrantLock lock;
    
    protected final ListProperty<ImageButton> buttons;
    private final ReadOnlyObjectWrapper<ImageButton> selectedButtonProperty;
    
    public ImageButtonGroup() {
        this(null);
    }
    
    public ImageButtonGroup(ReentrantLock lock) {
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.buttons = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.selectedButtonProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.buttons.addListener((ListChangeListener<ImageButton>) change -> {
            this.lock.lock();
            try {
                while (change.next()) {
                    change.getAddedSubList().forEach(this::onButtonAdded);
                    change.getRemoved().forEach(this::onButtonRemoved);
                }
            } finally {
                this.lock.unlock();
            }
        });
        
        this.selectedButtonProperty().addListener((observable, oldButton, newButton) -> {
            if (oldButton != null)
                oldButton.setSelected(false);
            if (newButton != null)
                newButton.setSelected(true);
        });
    }
    
    private void onButtonAdded(ImageButton button) {
        button.setButtonGroup(this);
    }
    
    private void onButtonRemoved(ImageButton button) {
        button.setButtonGroup(null);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ListProperty} containing the {@link ImageButton buttons} in this {@link ImageButtonGroup}.</p>
     * <ol>
     *     <li>The {@link ListProperty list} returned by this method is <i>not</i> a copy</li>
     *     <li>All changes made to the {@link ListProperty list} returned by this method will be reflected in this {@link ImageButtonGroup}.</li>
     * </ol>
     *
     * @return The {@link ListProperty} containing the {@link ImageButton buttons} in this {@link ImageButtonGroup}.
     */
    protected ListProperty<ImageButton> buttons() {
        return buttons;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} representing the {@link #getSelectedButton() selected} {@link ImageButton button} in this {@link ImageButtonGroup}.</p>
     * <p><i>
     * <b>EDIT:</b> Now returns a writable {@link ObjectProperty} instead of a {@link ReadOnlyObjectProperty}.
     * <br>
     * Note that the returned value is in reality still an instance of {@link ReadOnlyObjectWrapper} so rolling back to {@link ReadOnlyObjectProperty} will be easy if necessary.
     * </i></p>
     *
     * @return The {@link ReadOnlyObjectProperty} representing the {@link #getSelectedButton() selected} {@link ImageButton button} in this {@link ImageButtonGroup}.
     */
    public ObjectProperty<ImageButton> selectedButtonProperty() {
        return selectedButtonProperty;
    }
    
    /**
     * <p>Returns the {@link #getSelectedButton() selected} {@link ImageButton button} in this {@link ImageButtonGroup}.</p>
     * <ol>
     *     <li><i>{@link #getSelectedButton()}</i> is identical to <i>{@link #selectedButtonProperty() selectedButtonProperty().get()}</i>.</li>
     * </ol>
     *
     * @return The {@link #getSelectedButton() selected} {@link ImageButton button} in this {@link ImageButtonGroup}.
     *
     * @see #selectedButtonProperty()
     * @see #setSelectedButton(ImageButton)
     * @see #isButtonSelected(ImageButton)
     */
    public ImageButton getSelectedButton() {
        return selectedButtonProperty.get();
    }
    
    /**
     * <p>Checks if the specified {@link ImageButton} is {@link #getSelectedButton() selected}.</p>
     * <ol>
     *     <li>If the specified {@link ImageButton} is {@code null}, this method returns false.</li>
     * </ol>
     *
     * @param button The {@link ImageButton} being checked.
     *
     * @return True if the specified {@link ImageButton} is {@link #getSelectedButton() selected}, false otherwise.
     *
     * @see #selectedButtonProperty()
     * @see #getSelectedButton()
     * @see #setSelectedButton(ImageButton)
     */
    public boolean isButtonSelected(ImageButton button) {
        return button != null && Objects.equals(button, getSelectedButton());
    }
    
    /**
     * <p>Sets the {@link #getSelectedButton() selected} {@link ImageButton button} to the specified value.</p>
     *
     * @param button The {@link ImageButton} to be {@link #getSelectedButton() selected}.
     *
     * @return The previously {@link #getSelectedButton() selected} {@link ImageButton button}.
     * <br>
     * If no {@link ImageButton button} was {@link #getSelectedButton() selected}, this method returns {@code null}.
     *
     * @see #selectedButtonProperty()
     * @see #getSelectedButton()
     * @see #isButtonSelected(ImageButton)
     */
    public ImageButton setSelectedButton(ImageButton button) {
        return PropertiesSL.setProperty(selectedButtonProperty, button);
    }
    
    /**
     * <p>If the {@link #getSelectedButton() selected} {@link ImageButton button} equals any of the specified values,
     * the {@link #getSelectedButton() selected} {@link ImageButton button} is cleared.</p>
     * <ol>
     *     <li>If the specified {@link ImageButton} {@code array} is {@code null}, a {@link NullPointerException} is thrown.</li>
     *     <li>If the specified {@link ImageButton} {@code array} is {@code empty}, the {@link #getSelectedButton() selected} {@link ImageButton button} is always cleared.</li>
     *     <li>
     *         {@link #clearSelection(ImageButton...) Clearing} the {@link #getSelectedButton() selected} {@link ImageButton button}
     *         is identical to <i>{@link #setSelectedButton(ImageButton) setSelectedButton(null)}</i>.
     *     </li>
     * </ol>
     *
     * @param buttons The {@code array} of {@link ImageButton buttons} to be compared for clearing.
     *
     * @return True if the selection was successfully cleared, false if it was not.
     *
     * @see #selectedButtonProperty()
     * @see #getSelectedButton()
     * @see #setSelectedButton(ImageButton)
     */
    public boolean clearSelection(ImageButton... buttons) {
        if (buttons == null)
            throw new NullPointerException("Button array cannot be null.");
        
        if (buttons.length == 0)
            setSelectedButton(null);
        
        if (ToolsSL.equalsAny(getSelectedButton(), (Object[]) buttons)) {
            setSelectedButton(null);
            return true;
        } else
            return false;
    }
    
    /**
     * <p>{@link #setSelectedButton(ImageButton) Selects} the {@code first} {@link ImageButton button} in this {@link ImageButtonGroup}, then returns the previous {@link #getSelectedButton() selection.}</p>
     * <ol>
     *     <li>If there are no {@link ImageButton buttons} in this {@link ImageButtonGroup}, return {@code null}.</li>
     * </ol>
     *
     * @return The previously selected {@link ImageButton button}, or {@code null} if there are no {@link ImageButton buttons} in this {@link ImageButtonGroup}.
     *
     * @see #selectLast()
     * @see #selectedButtonProperty()
     * @see #getSelectedButton()
     * @see #setSelectedButton(ImageButton)
     */
    public ImageButton selectFirst() {
        try {
            return setSelectedButton(buttons().get(0));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    /**
     * <p>{@link #setSelectedButton(ImageButton) Selects} the {@code last} {@link ImageButton button} in this {@link ImageButtonGroup}, then returns the previous {@link #getSelectedButton() selection.}</p>
     * <ol>
     *     <li>If there are no {@link ImageButton buttons} in this {@link ImageButtonGroup}, return {@code null}.</li>
     * </ol>
     *
     * @return The previously selected {@link ImageButton button}, or {@code null} if there are no {@link ImageButton buttons} in this {@link ImageButtonGroup}.
     *
     * @see #selectFirst()
     * @see #selectedButtonProperty()
     * @see #getSelectedButton()
     * @see #setSelectedButton(ImageButton)
     */
    public ImageButton selectLast() {
        try {
            return setSelectedButton(buttons().get(buttons().size() - 1));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    //</editor-fold>
}
