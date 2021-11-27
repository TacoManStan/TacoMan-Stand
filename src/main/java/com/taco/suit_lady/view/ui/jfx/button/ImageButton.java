package com.taco.suit_lady.view.ui.jfx.button;

import com.taco.suit_lady.util.tools.BindingTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.ResourceTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

// TO-DOC
public class ImageButton
        implements Nameable
{
    //<editor-fold desc="--- FIELDS ---">
    
    private ApplicationContext ctx;
    
    private final ImagePane imagePane;
    
    private final StringBinding nameBinding;
    private final ObjectProperty<ImageButtonGroup> buttonGroupProperty;
    
    private final ReadOnlyObjectWrapper<Image> standardImageProperty;
    private final ReadOnlyObjectWrapper<Image> hoveredImageProperty;
    private final ReadOnlyObjectWrapper<Image> pressedImageProperty;
    private final ReadOnlyObjectWrapper<Image> disabledImageProperty;
    
    private final ObjectProperty<Runnable> actionResponderProperty;
    private final ObjectProperty<Runnable> actionResponderFXProperty;
    
    private final BooleanBinding hoveredBinding;
    private final BooleanBinding pressedBinding;
    
    private final ReadOnlyBooleanWrapper selectedProperty;
    private final BooleanProperty disabledProperty;
    
    private final boolean toggleable;
    
    //</editor-fold>
    
    /**
     * <p>Refer to {@link #ImageButton(ImagePane, ObservableStringValue, Runnable, Runnable, boolean, Point2D) Fully-Parameterized Constructor} for details.</p>
     * <p><b>Identical to...</b></p>
     * <blockquote><code>new ImageButton(imagePane, <u>BindingTools.createStringBinding(name)</u>, actionResponder, actionResponderFX, toggleable, size)</code></blockquote>
     */
    public ImageButton(
            @Nullable ImagePane imagePane,
            @Nullable String name,
            @Nullable Runnable actionResponder,
            @Nullable Runnable actionResponderFX,
            boolean toggleable,
            @Nullable Point2D size)
    {
        this(imagePane, BindingTools.createStringBinding(name), actionResponder, actionResponderFX, toggleable, size);
    }
    
    /**
     * <p><b>Fully-Parameterized Constructor</b></p>
     * <p><hr>
     * <p><b>Parameter Details</b></p>
     * <ol>
     *     <li>
     *         <p><b>Image Pane:</b> {@literal ImagePane}</p>
     *         <blockquote><i>Refer to <code>{@link #getImagePane()}</code> for additional information.</i></blockquote>
     *         <ol>
     *             <li>If the specified {@link ImagePane} is {@code null}, a new {@link ImagePane} is constructed.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Name Binding:</b> {@literal ObservableStringValue}
     *         <blockquote><i>Refer to <code><i>{@link #nameBinding()}</i></code> for additional information.</i></blockquote>
     *         <ol>
     *             <li>If the specified {@link ObservableStringValue nameBinding} is {@code null}, a {@link NullPointerException} is thrown.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Action Responder:</b> {@literal Runnable}
     *         <blockquote><i>Refer to <code><i>{@link #actionResponderProperty()}</i></code> for additional information.</i></blockquote>
     *     </li>
     *     <li>
     *         <b>FX Action Responder:</b> Runnable
     *         <blockquote><i>Refer to <code><i>{@link #actionResponderFXProperty()}</i></code> for additional information.</i></blockquote>
     *     </li>
     *     <li>
     *         <b>Toggleable:</b> boolean
     *         <blockquote><i>Refer to <code><i>{@link #isToggleable()}</i></code> for additional information.</i></blockquote>
     *     </li>
     *     <li>
     *         <b>Size</b>
     *         <ol>
     *             <li>Defines the dimensions of this {@link ImageButton}.</li>
     *             <li>More specifically, {@link Point2D size} defines the dimensions of the {@link #getImagePane() Image Pane} containing this {@link ImageButton}.</li>
     *             <li>If the specified value is {@code null}, no size adjustments will be made to the {@link #getImagePane() Image Pane}, which could result in a distorted image display.</li>
     *             <li>In the future, the {@link Point2D size} will also be used to load the {@link #getImage() Image} of the correct {@link #getSize() size} using the corresponding {@link #getSizeID() size ID}.</li>
     *             <li>After being used to set the size of the {@link ImagePane}, the {@link Point2D size} is not stored or otherwise used again.</li>
     *             <li>The <code><i>{@link #getSize()}</i></code> method returns the current dimensions of the {@link #getImagePane() Image Pane} containing this {@link ImageButton}.</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     * @param imagePane         The {@link ImagePane} this {@link ImageButton} will be displayed on.
     * @param nameBinding       The {@link ObservableStringValue} containing the {@link String name} of this {@link ImageButton}.
     * @param actionResponder   The {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     * @param actionResponderFX The {@link Runnable} that is executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     * @param toggleable        True if this {@link ImageButton} is {@link #isToggleable() toggleable}, false if it is not.
     * @param size              A {@link Point2D} representing the {@link ImagePane#widthProperty() width} and {@link ImagePane#heightProperty() height} of this {@link ImageButton}.
     */
    public ImageButton(
            @Nullable ImagePane imagePane,
            @NotNull ObservableStringValue nameBinding,
            @Nullable Runnable actionResponder,
            @Nullable Runnable actionResponderFX,
            boolean toggleable,
            @Nullable Point2D size)
    {
        this.imagePane = imagePane != null ? imagePane : new ImagePane();
    
        this.nameBinding = Bindings.createStringBinding(() -> {
            final String name = nameBinding.get();
            return name != null ? name : "missingno";
        }, nameBinding);
        
//        ConsoleBB.CONSOLE.print("Size for Button [" + getName() + "]: " + size);
        if (size != null) {
            this.imagePane.setPrefSize(size.getX(), size.getY());
            this.imagePane.setMaxSize(size.getX(), size.getY());
        }
//        ConsoleBB.CONSOLE.print("Actual Size for Button \"" + getName() + "\": " + "[" + this.imagePane.getWidth() + ", " + this.imagePane.getHeight() + "]");
        
        this.buttonGroupProperty = new ReadOnlyObjectWrapper<>();
        
        this.standardImageProperty = new ReadOnlyObjectWrapper<>();
        this.hoveredImageProperty = new ReadOnlyObjectWrapper<>();
        this.pressedImageProperty = new ReadOnlyObjectWrapper<>();
        this.disabledImageProperty = new ReadOnlyObjectWrapper<>();
        
        this.actionResponderProperty = new SimpleObjectProperty<>(actionResponder);
        this.actionResponderFXProperty = new SimpleObjectProperty<>(actionResponderFX);
        
        this.selectedProperty = new ReadOnlyBooleanWrapper();
        this.disabledProperty = new SimpleBooleanProperty();
        
        this.toggleable = toggleable;
        
        //
        
        this.buttonGroupProperty.addListener((observable, oldButtonGroup, newButtonGroup) -> {
            if (!Objects.equals(oldButtonGroup, newButtonGroup)) {
                selectedProperty.unbind();
                // TODO - Issue here where old listeners are not removed if the button group changes multiple times.
                if (oldButtonGroup != null)
                    oldButtonGroup.buttons().remove(this);
                if (newButtonGroup != null) {
                    newButtonGroup.buttons().add(this);
                    selectedProperty.addListener((observable1, oldValue, selected) -> {
                        if (selected)
                            newButtonGroup.setSelectedButton(this);
                        else
                            newButtonGroup.clearSelection(this); // Only clear the selection if the selection is this ImageButton
                    });
                }
            }
        });
        
        this.standardImageProperty.bind(createImageBinding(""));
        this.hoveredImageProperty.bind(createImageBinding("_hovered"));
        this.pressedImageProperty.bind(createImageBinding("_pressed"));
        this.disabledImageProperty.bind(createImageBinding("_disabled"));
        
        this.hoveredBinding = BindingTools.createBooleanBinding(this.imagePane.hoverProperty());
        this.pressedBinding = BindingTools.createBooleanBinding(this.imagePane.pressedProperty());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public void initialize()
    {
        // Note: Is NOT called automatically
        // Note: MUST be called manually after an ImageButton is constructed.
        Objects.requireNonNull(standardImageProperty, "Standard property cannot be null");
        
        initializeImagePane();
    }
    
    private void initializeImagePane()
    {
        Objects.requireNonNull(imagePane, "Image view cannot be null");
        
        ArrayList<Observable> observables = new ArrayList<>(Arrays.asList(
                nameBinding,
                hoveredBinding,
                pressedBinding,
                selectedProperty,
                disabledProperty,
                standardImageProperty
        ));
        
        imagePane.imageProperty().bind(Bindings.createObjectBinding(
                () -> getImage(), observables.toArray(new Observable[0])));
        
        imagePane.setPickOnBounds(true);
        imagePane.setOnMouseClicked(Event::consume);
        imagePane.setOnMousePressed(Event::consume);
        imagePane.setOnMouseReleased(event -> {
            if (Objects.equals(event.getSource(), imagePane) && FXTools.get().isMouseOnEventSource(event))
                // Ignore event if this ImageButton is disabled
                if (!isDisabled()) {
                    toggle();
                    onAction();
                }
            event.consume();
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="Action Responding Properties">
    
    /**
     * <p>Returns the {@link ObjectProperty} containing the {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     * <p><b>Execution Details</b></p>
     * <ol>
     *     <li>The {@link Runnable} is executed in a {@link Task Background Task} by the core {@link TB#executor() Executor} for the entire application instance.</li>
     *     <li>The {@link Task} {@link ThreadPoolExecutor#execute(Runnable) execution} is non-blocking.</li>
     *     <li>To execute a {@link Runnable} on the {@link FXTools#isFXThread() JavaFX Thread}, refer to <code><i>{@link #actionResponderFXProperty()}</i></code>.</li>
     *     <li>If the {@link Runnable value} contained by the {@link ObjectProperty} is {@code null}, no action response will be executed.</li>
     * </ol>
     *
     * @return The {@link ObjectProperty} containing the {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderFXProperty()
     */
    public @NotNull ObjectProperty<Runnable> actionResponderProperty()
    {
        return actionResponderProperty;
    }
    
    /**
     * <p>Returns the {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @return The {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderProperty()
     */
    public @Nullable Runnable getActionResponder()
    {
        return actionResponderProperty.get();
    }
    
    /**
     * <p>Sets the {@link Runnable} to be executed when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @param actionResponder The {@link Runnable} to be executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderProperty()
     */
    public void setActionResponder(@Nullable Runnable actionResponder)
    {
        actionResponderProperty.set(actionResponder);
    }
    
    /**
     * <p>Returns the {@link ObjectProperty} containing the {@link Runnable} that is executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     * <p><b>Execution Details</b></p>
     * <ol>
     *     <li>The {@link Runnable} is executed on the {@link FXTools#isFXThread() JavaFX Thread}.</li>
     *     <li>The {@link Task} {@link FXTools#runFX(Runnable, boolean) execution} is blocking.</li>
     *     <li>To execute a {@link Runnable} in a {@link Task Background Task}, refer to <code><i>{@link #actionResponderProperty()}</i></code>.</li>
     *     <li>If the {@link Runnable value} contained by the {@link ObjectProperty} is {@code null}, no action response will be executed.</li>
     * </ol>
     *
     * @return The {@link ObjectProperty} containing the {@link Runnable} that is executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderProperty()
     */
    public @NotNull ObjectProperty<Runnable> actionResponderFXProperty()
    {
        return actionResponderFXProperty;
    }
    
    /**
     * <p>Returns the {@link Runnable} that is executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @return The {@link Runnable} that is executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderFXProperty()
     */
    public @Nullable Runnable getActionResponderFX()
    {
        return actionResponderFXProperty.get();
    }
    
    /**
     * <p>Sets the {@link Runnable} to be executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @param actionResponder The {@link Runnable} to be executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderFXProperty()
     */
    public void setActionResponderFX(@Nullable Runnable actionResponder)
    {
        actionResponderFXProperty.set(actionResponder);
    }
    
    /**
     * <p>Executes the {@link #actionResponderProperty() Background Action Responder} and {@link #actionResponderFXProperty() FX Action Responder} {@link Runnable Runnables}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Executed automatically when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</li>
     *     <li>
     *         <b>Background Execution</b>
     *         <ol>
     *             <li>If the {@link #actionResponderProperty() Background Responder} for this {@link ImageButton} is {@code non-null}, it is {@link ThreadPoolExecutor#execute(Runnable) executed} in a {@link Task Background Task}.</li>
     *             <li>The {@link #actionResponderProperty() Background Responder} execution is <u>non-blocking</u>.</li>
     *             <li>Refer to <code><i>{@link #actionResponderProperty()}</i></code> for additional information.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>FX Execution</b>
     *         <ol>
     *             <li>If the {@link #actionResponderFXProperty() FX Responder} for this {@link ImageButton} is {@code non-null}, it is {@link FXTools#runFX(Runnable, boolean) executed} on the {@link FXTools#isFXThread() JavaFX Thread}.</li>
     *             <li>The {@link #actionResponderFXProperty() FX Responder} execution is <u>blocking</u>.</li>
     *             <li>Refer to <code><i>{@link #actionResponderFXProperty()}</i></code> for additional information.</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     * @see #actionResponderProperty()
     * @see #actionResponderFXProperty()
     */
    private void onAction()
    {
        final Runnable actionResponder = getActionResponder();
        if (actionResponder != null) {
            TB.executor().execute(new Task<>()
            {
                @Override
                protected Object call()
                {
                    actionResponder.run();
                    return null;
                }
            });
        }
        final Runnable actionResponderFX = getActionResponderFX();
        if (actionResponderFX != null)
            FXTools.get().runFX(actionResponderFX, false);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Image Properties">
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#STANDARD} state.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#STANDARD} state.
     */
    public ReadOnlyObjectProperty<Image> standardImageProperty()
    {
        return standardImageProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#STANDARD} state.</p>
     *
     * @return The {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#STANDARD} state.
     *
     * @see #standardImageProperty()
     */
    public Image getStandardImage()
    {
        return standardImageProperty.get();
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#HOVERED} state.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#HOVERED} state.
     */
    public ReadOnlyObjectProperty<Image> hoveredImageProperty()
    {
        return hoveredImageProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#HOVERED} state.</p>
     *
     * @return The {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#HOVERED} state.
     *
     * @see #hoveredImageProperty()
     */
    public Image getHoveredImage()
    {
        return hoveredImageProperty.get();
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#PRESSED} state.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#PRESSED} state.
     */
    public ReadOnlyObjectProperty<Image> pressedImageProperty()
    {
        return pressedImageProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#PRESSED} state.</p>
     *
     * @return The {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#PRESSED} state.
     *
     * @see #pressedImageProperty()
     */
    public Image getPressedImage()
    {
        return pressedImageProperty.get();
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#DISABLED} state.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#DISABLED} state.
     */
    public ReadOnlyObjectProperty<Image> disabledImageProperty()
    {
        return disabledImageProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#DISABLED} state.</p>
     *
     * @return The {@link Image} to be displayed when this {@link ImageButton} is in a {@link ButtonState#DISABLED} state.
     *
     * @see #disabledImageProperty()
     */
    public Image getDisabledImage()
    {
        return disabledImageProperty.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Button Status Properties">
    
    /**
     * <p>Returns a {@link BooleanBinding binding} that reflects if this {@link ImageButton} is in a {@link ButtonState#HOVERED} state.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Specifically, the returned {@link BooleanBinding binding} is bound to <code><i>{@link #getImagePane()}<b>.</b>{@link ImagePane#hoverProperty() hoverProperty()}.</i></code></li>
     * </ol>
     *
     * @return A {@link BooleanBinding binding} bound to reflect if this {@link ImageButton} is in a {@link ButtonState#HOVERED HOVERED} state.
     */
    public BooleanBinding hoveredBinding()
    {
        return hoveredBinding;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is in a {@link ButtonState#HOVERED HOVERED} state.</p>
     *
     * @return True if this {@link ImageButton} is in a {@link ButtonState#HOVERED HOVERED} state, false if it is in any other {@link ButtonState ButtonState}.
     *
     * @see #hoveredBinding()
     */
    public boolean isHovered()
    {
        return hoveredBinding.get();
    }
    
    /**
     * <p>Returns a {@link BooleanBinding binding} that reflects if this {@link ImageButton} is in a {@link ButtonState#PRESSED} state.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Specifically, the returned {@link BooleanBinding binding} is bound to <code><i>{@link #getImagePane()}<b>.</b>{@link ImagePane#pressedProperty() pressedProperty()}.</i></code></li>
     * </ol>
     *
     * @return A {@link BooleanBinding binding} bound to reflect if this {@link ImageButton} is in a {@link ButtonState#PRESSED PRESSED} state.
     */
    public BooleanBinding pressedBinding()
    {
        return pressedBinding;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is toggleable or not.</p>
     *
     * @return True if this {@link ImageButton} is toggleable, false if it is not.
     */
    public boolean isToggleable()
    {
        return toggleable;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is in a {@link ButtonState#PRESSED PRESSED} state.</p>
     *
     * @return True if this {@link ImageButton} is in a {@link ButtonState#PRESSED PRESSED} state, false if it is in any other {@link ButtonState ButtonState}.
     *
     * @see #pressedBinding()
     */
    public boolean isPressed()
    {
        return pressedBinding.get();
    }
    
    /**
     * <p>Returns the {@link ReadOnlyBooleanProperty property} containing a boolean that reflects if this {@link ImageButton} is currently selected or not.</p>
     * <p><b>Changing the Value</b></p>
     * <ol>
     *     <li>To modify the value of the returned {@link ReadOnlyBooleanProperty property}, refer to {@link #setSelected(boolean)}.</li>
     *     <li>Modifications to the returned {@link ReadOnlyBooleanProperty property} can only be modified via the {@link #setSelected(boolean)} method because the value can only be changed if this {@link ImageButton} is {@link #isToggleable() toggleable}.</li>
     *     <li>Refer to <code><i>{@link #setSelected(boolean)}</i></code> documentation for additional information.</li>
     *     <li>The {@link #getButtonGroup() ImageButtonGroup} {@link ImageButtonGroup#selectedButtonProperty() selection} is automatically updated via listener when this value is changed.</li>
     * </ol>
     *
     * @return The {@link ReadOnlyBooleanProperty property} containing a boolean that reflects if this {@link ImageButton} is currently selected or not.
     */
    public final ReadOnlyBooleanProperty selectedProperty()
    {
        return selectedProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is currently {@link #selectedProperty() selected} or not.</p>
     *
     * @return If this {@link ImageButton} is currently {@link #selectedProperty() selected} or not.
     *
     * @see #selectedProperty()
     */
    public final boolean isSelected()
    {
        return selectedProperty.get();
    }
    
    /**
     * <p>Set the {@link #selectedProperty() selection} state of this {@link ImageButton} to the specified value.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>
     *         The {@link BooleanProperty property} returned by <code><i>{@link #selectedProperty()}</i></code> is {@code read-only}.
     *         Therefore, the {@link #selectedProperty() selection} state of this {@link ImageButton} can only be changed via this method.
     *         <ol>
     *             <li>This is because the {@link #selectedProperty() selection} state can only be modified if the {@link ImageButton} is {@link #isToggleable() toggleable}.</li>
     *         </ol>
     *     </li>
     *     <li>If this method is called but this {@link ImageButton} is not {@link #isToggleable() toggleable}, {@link #setSelected(boolean) this method} silently returns false.</li>
     *     <li>Refer to <code><i>{@link #selectedProperty()}</i></code> for additional information.</li>
     * </ol>
     *
     * @param selected True if this {@link ImageButton} is being selected, false if it is being deselected.
     *
     * @return True if the {@link #selectedProperty() selection} state of this {@link ImageButton} was successfully changed to the specified value, false if it was not.
     */
    public final boolean setSelected(boolean selected)
    {
        if (isToggleable()) {
            selectedProperty.set(selected);
            return true;
        }
        return false;
    }
    
    /**
     * <p>Toggles the {@link #selectedProperty() selection} state of this {@link ImageButton} by setting it to the opposite of its {@link #isSelected() current} state.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@code Passthrough Definition:} <i><code>{@link #setSelected(boolean) setSelected}<b>(!</b>{@link #isSelected()}<b>)</b></code></i></li>
     * </ol>
     *
     * @return True if the {@link #setSelected(boolean) toggle} operation was successful, false if it was not.
     *
     * @see #setSelected(boolean)
     */
    public boolean toggle()
    {
        return setSelected(!isSelected());
    }
    
    /**
     * <p>Returns the {@link BooleanProperty} reflecting if this {@link ImageButton} is {@link ButtonState#DISABLED disabled} or not.</p>
     *
     * @return The {@link BooleanProperty} reflecting if this {@link ImageButton} is {@link ButtonState#DISABLED disabled} or not.
     *
     * @see ButtonState#DISABLED
     */
    public final BooleanProperty disabledProperty()
    {
        return disabledProperty;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is currently in a {@link #disabledProperty() disabled} {@link ButtonState#DISABLED state} or not.</p>
     *
     * @return True if this {@link ImageButton} is currently in a {@link #disabledProperty() disabled} {@link ButtonState#DISABLED state}, false if it is not (i.e., if it is enabled).
     *
     * @see #disabledProperty()
     */
    public final boolean isDisabled()
    {
        return disabledProperty.get();
    }
    
    /**
     * <p>Sets the {@link #disabledProperty() disabled} {@link ButtonState#DISABLED state} of this {@link ImageButton} to the specified value.</p>
     *
     * @param disabled True to disable this {@link ImageButton}, false to enable it.
     *
     * @see #disabledProperty()
     */
    public final void setDisabled(boolean disabled)
    {
        disabledProperty.set(disabled);
    }
    
    //</editor-fold>
    
    /**
     * <p>Returns the {@link ImagePane} object containing this {@link ImageButton}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Required because {@link ImageButton ImageButtons} are not actually {@link Node UI elements} (*cough* yet *cough*).</li>
     * </ol>
     *
     * @return The {@link ImagePane} object containing this {@link ImageButton}.
     */
    public @NotNull ImagePane getImagePane()
    {
        return imagePane;
    }
    
    /**
     * <p>Returns the {@link StringBinding} bound to the {@link #getName() name} of this {@link ImageButton}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link #getName() name} is used to load references to the {@link ResourceTools#getImage(String, String, String) Cached} {@link Image Images}.</li>
     *     <li>Each {@link ButtonState state} results in a different variation of the same {@link Image} referenced by the {@link ImageButton} {@link #nameBinding() name}.</li>
     *     <li>The <code><i>{@link #getImage()}</i></code> method returns the current {@link Image} variation displayed by this {@link ImageButton} based on its current {@link #getState() state}.</li>
     *     <li>
     *         The {@link Image} variations are essentially each a different styling of the same original image.
     *         // TODO - Eventually have styling be done via code automatically.
     *     </li>
     *     <li>The {@link StringBinding} returned by this method can never return {@code null} and will instead return {@code "missingno"} if the {@link #nameBinding() name} is undefined.</li>
     * </ol>
     * <p><b>Cache ID</b></p>
     * <ol>
     *     <li>To get the cache ID, refer to the <code><i>{@link #getID()}</i></code> method.</li>
     *     <li>To get the full cache path ID, refer to the <code><i>{@link #getPathID()}</i></code> method.</li>
     * </ol>
     *
     * @return The {@link StringBinding} bound to the {@link #getName() name} of this {@link ImageButton}.
     *
     * @see #getName()
     * @see #getState()
     * @see #getImage()
     * @see #getID()
     * @see #getPathID()
     */
    public @NotNull StringBinding nameBinding()
    {
        // TODO - Dunno how and it isn't necessary yet but you should somehow make it possible to change the name after construction
        return nameBinding;
    }
    
    /**
     * <p>Returns the {@link #nameBinding() name} of this {@link ImageButton}.</p>
     * <blockquote>Refer to <code><i>{@link #nameBinding()}</i></code> for additional information.</blockquote>
     *
     * @return The {@link #nameBinding() name} of this {@link ImageButton}.
     *
     * @see #nameBinding()
     */
    @Override
    public @NotNull String getName()
    {
        return nameBinding.get();
    }
    
    /**
     * <p>Returns the {@link ButtonState#STANDARD vanilla} cache ID for this {@link ImageButton}.</p>
     * <blockquote>Refer to the <code><i>{@link #nameBinding()}</i></code> docs for additional information.</blockquote>
     *
     * @return The {@link ButtonState#STANDARD vanilla} cache ID for this {@link ImageButton}.
     *
     * @see #nameBinding()
     * @see #getPathID()
     * @see ResourceTools#getImage(String, String, String)
     */
    private @NotNull String getID()
    {
        return getName().replace(" ", "_").toLowerCase();
    }
    
    /**
     * <p>Returns the full {@link ButtonState#STANDARD vanilla} cache path ID for this {@link ImageButton}.</p>
     * <blockquote>Refer to the <code><i>{@link #nameBinding()}</i></code> docs for additional information.</blockquote>
     *
     * @return The full {@link ButtonState#STANDARD vanilla} cache path ID for this {@link ImageButton}.
     *
     * @see #nameBinding()
     * @see #getID()
     * @see ResourceTools#getImage(String, String, String)
     */
    private @NotNull String getPathID()
    {
        return "buttons/" + getID() + "/";
    }
    
    /**
     * <P>Returns the {@link ObjectProperty} containing the {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.</P>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All logic related to adding or removing an {@link ImageButton} from a {@link ImageButtonGroup} is done via a {@link ChangeListener} that observes the {@link ObjectProperty} returned by {@link #buttonGroupProperty() this method}.</li>
     *     <li>The aforementioned {@link ChangeListener} is configured in the {@link ImageButton} {@link #ImageButton(ImagePane, ObservableStringValue, Runnable, Runnable, boolean, Point2D) constructor}.</li>
     * </ol>
     *
     * @return The {@link ObjectProperty} containing the {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.
     */
    public @NotNull ObjectProperty<ImageButtonGroup> buttonGroupProperty()
    {
        return buttonGroupProperty;
    }
    
    /**
     * <p>Returns the {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.</p>
     *
     * @return The {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.
     */
    public @Nullable ImageButtonGroup getButtonGroup()
    {
        return buttonGroupProperty.get();
    }
    
    /**
     * <p>Sets the {@link ImageButtonGroup} that this {@link ImageButton} is in to the specified {@code value}.</p>
     *
     * @param buttonGroup The {@link ImageButtonGroup} that this {@link ImageButton} is being added to.
     */
    public void setButtonGroup(@Nullable ImageButtonGroup buttonGroup)
    {
        buttonGroupProperty.set(buttonGroup);
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is in a {@link ImageButtonGroup} or not.</p>
     *
     * @return True  if this {@link ImageButton} is in a {@link ImageButtonGroup}, false if it is not.
     */
    public boolean isInButtonGroup()
    {
        return getButtonGroup() != null;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- BUTTON STATE ---">
    
    /**
     * An {@code enum} defining the current {@code state} of this {@link ImageButton}.
     */
    public enum ButtonState
    {
        /**
         * <p>Represents the standard {@link ImageButton} state.</p>
         * <p><b>Details</b></p>
         * <ol>
         *     <li>If the {@link ImageButton} is not {@link #disabledProperty() disabled} or otherwise being interacted with, the {@link ImageButton} state will be {@link ButtonState#STANDARD}.</li>
         *     <li>
         *         An {@link ImageButton} is in a {@code standard} state if...
         *         <ol>
         *             <li>The mouse cursor is not {@link #hoveredBinding() hovering} over the {@link ImageButton}.</li>
         *             <li>The {@link ImageButton} is not {@link #disabledProperty() disabled}.</li>
         *         </ol>
         *     </li>
         *     <li>Analogous to default/vanilla.</li>
         * </ol>
         */
        STANDARD,
        
        /**
         * <p>Represents the {@link #hoveredBinding() hovered} {@link ImageButton} state.</p>
         * <p><b>Details</b></p>
         * <ol>
         *     <li>
         *         An {@link ImageButton} is in a {@link #hoveredBinding() hovered} state if...
         *         <ol>
         *             <li>The mouse cursor is {@link #hoveredBinding() hovering} over the {@link ImageButton}.</li>
         *             <li>The {@link ImageButton} is not {@link #disabledProperty() disabled}.</li>
         *         </ol>
         *     </li>
         * </ol>
         */
        HOVERED,
        
        /**
         * <p>Represents the {@link #pressedBinding() pressed} {@link ImageButton} state.</p>
         * <p><b>Details</b></p>
         * <ol>
         *     <li>
         *         An {@link ImageButton} is in a {@link #pressedBinding() pressed} state if...
         *         <ol>
         *             <li>The mouse cursor is currently pressed.</li>
         *             <li>The mouse cursor is currently within the bounds of the {@link ImageButton}.</li>
         *             <li>The {@link ImageButton} is not {@link #disabledProperty() disabled}.</li>
         *             <li>The {@link ImageButton} is {@link #isToggleable() toggleable} and has been {@link #toggle() toggled} on.</li>
         *         </ol>
         *     </li>
         *     <li>If the mouse cursor is pressed while {@link #hoveredBinding() hovering} over the {@link ImageButton} but is moved outside the {@link ImageButton} bounds, the {@link ButtonState ButtonState} reverts to {@link ButtonState#STANDARD}.</li>
         * </ol>
         */
        PRESSED,
        
        /**
         * <p>Represents the {@link #disabledProperty() disabled} {@link ImageButton} state.</p>
         * <p><b>Details</b></p>
         * <ol>
         *     <li>
         *         An {@link ImageButton} is in a {@link #disabledProperty() disabled} state if...
         *         <ol>
         *             <li>The {@link ImageButton} is {@link #disabledProperty() disabled}.</li>
         *             <li>In other words, if an {@link ImageButton} is {@link #disabledProperty() disabled}, the {@link ButtonState ButtonState} will always be {@link ButtonState#DISABLED}.</li>
         *         </ol>
         *     </li>
         * </ol>
         */
        DISABLED
    }
    
    /**
     * <p>Returns the current {@link ButtonState Button State} of this {@link ImageButton}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link ButtonState Button State} defines the {@link Image} that is currently displayed by this {@link ImageButton}.</li>
     * </ol>
     *
     * @return The current {@link ButtonState state} of this {@link ImageButton}.
     *
     * @see ButtonState
     */
    public ButtonState getState()
    {
        final Image standardImage = getStandardImage();
        final Image pressedImage = getPressedImage();
        final Image hoveredImage = getHoveredImage();
        final Image disabledImage = getDisabledImage();
        
        if (disabledImage != null && isDisabled())
            return ButtonState.DISABLED;
        else if (pressedImage != null && isHovered() && isPressed())
            return ButtonState.PRESSED;
        else if (hoveredImage != null && isHovered())
            return ButtonState.HOVERED;
        else if (pressedImage != null && isToggleable() && isSelected())
            return ButtonState.PRESSED;
        return ButtonState.STANDARD;
    }
    
    /**
     * <p>Returns the {@link Image} matching the current {@link ButtonState state} of this {@link ImageButton}.</p>
     *
     * @return The {@link Image} matching the current {@link ButtonState state} of this {@link ImageButton}.
     */
    public Image getImage()
    {
        return switch (getState()) {
            case HOVERED -> getHoveredImage();
            case PRESSED -> getPressedImage();
            case DISABLED -> getDisabledImage();
            default -> getStandardImage();
        };
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- SIZE  :  [ UNUSED ] ---">
    
    public static final Point2D VERY_SMALL = new Point2D(15.0, 12.5);
    public static final Point2D VERY_SMALL_BOX = new Point2D(15.0, 15.0);
    
    public static final Point2D SMALL = new Point2D(30.0, 25.0);
    public static final Point2D SMALL_BOX = new Point2D(30.0, 30.0);
    
    public static final Point2D MEDIUM_SMALL = new Point2D(45.0, 45.0);
    public static final Point2D MEDIUM_SMALL_BOX = new Point2D(45.0, 37.5);
    
    public static final Point2D MEDIUM = new Point2D(60.0, 60.0);
    public static final Point2D MEDIUM_BOX = new Point2D(60.0, 50.0);
    
    public static final Point2D MEDIUM_LARGE = new Point2D(90.0, 75.0);
    public static final Point2D MEDIUM_LARGE_BOX = new Point2D(90.0, 90.0);
    
    public static final Point2D LARGE = new Point2D(120.0, 100.0);
    public static final Point2D LARGE_BOX = new Point2D(120.0, 120.0);
    
    public static final Point2D VERY_LARGE = new Point2D(150.0, 125.0);
    public static final Point2D VERY_LARGE_BOX = new Point2D(150.0, 150.0);
    
    public Point2D getSize()
    {
        return new Point2D(getImagePane().getWidth(), getImagePane().getHeight());
    }
    
    /**
     * <p>Returns the {@link String Size ID} for the current {@link #getSize() size} of this {@link ImageButton}.</p>
     *
     * @return The {@link String Size ID} for the current {@link #getSize() size} of this {@link ImageButton}.
     *
     * @see #getSize()
     */
    public String getSizeID()
    {
        throw ExceptionTools.nyi();
        //        // TODO - Eventually, different size parameters of the same image will be created, and this will allow access to the correct image.
        //        // The purpose of multiple identical image files of different sizes will provide additional size options that are guaranteed to provide a non-warped image.
        //        // This is because images can (and usually do) become warped/fuzzy when they are automatically resized.
        //        // e.g., displaying a 60x60 image as 30x30, or worse, a 30x30 image as 60x60, or even worse than that, a 30x30 image as 30x25 (yikes).
        //        // For now though, this is not a priority.
        //        Point2D size = getSize();
        //        if (size.equals(SMALL))
        //            return "small";
        //        else if (size.equals(SMALL_BOX))
        //            return "small_box";
        //        else if (size.equals(MEDIUM))
        //            return "medium";
        //        else if (size.equals(MEDIUM_BOX))
        //            return "medium_box";
        //        else if (size.equals(LARGE))
        //            return "large";
        //        else if (size.equals(LARGE_BOX))
        //            return "large_box";
        //        throw ExceptionTools.ex("Invalid Image Size: [" + ((int) size.getX()) + ", " + ((int) size.getY()) + "]");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    /**
     * <p>Constructs a new {@link ObjectBinding} bound to the {@link Image} variation represented by the specified {@link String suffix}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link Image} bound to the returned {@link ObjectBinding} is automatically updated when the {@link #nameBinding() name} of this {@link ImageButton} changes.</li>
     *     <li>If the {@link Image} variation with the specified {@link String suffix} does not exist at the file location defined by the {@link Image} {@link #getPathID() mapping} for this {@link ImageButton}, {@link #missingno(String) missingno} is used instead.</li>
     * </ol>
     * <p><b>Missingno</b></p>
     * <ol>
     *     <li>The {@link #missingno(String) missingno} {@link Image images} are universal files displaying an icon indicating the intended {@link Image} could not be found.</li>
     *     <li>The name {@link #missingno(String) "missingno"} comes from the bugged Pokemon that would appear in the 1st Generation games due to a bug where in specific circumstances, a player could encounter a null Pokemon.</li>
     *     <li>{@link #missingno(String) "missingno"} stands for {@code Missing Number}.</li>
     * </ol>
     *
     * @param suffix The {@link String} representing the {@link Image} variation to be bound to the {@link ObjectProperty} returned by {@link #createImageBinding(String) this method}.
     *
     * @return A new {@link ObjectBinding} bound to the {@link Image} variation represented by the specified {@link String suffix}.
     *
     * @throws NullPointerException If the specified {@link String suffix} is {@code null}.
     * @see #missingno(String)
     */
    private @NotNull ObjectBinding<Image> createImageBinding(@NotNull String suffix)
    {
        ExceptionTools.nullCheck(suffix, "Suffix");
        
        return Bindings.createObjectBinding(() -> {
            final Image image = ResourceTools.get().getImage(getPathID(), getID() + suffix, "png");
            return image != null ? image : missingno(suffix);
        }, nameBinding());
    }
    
    /**
     * <p>Returns the {@code missingno} {@link Image variation} based on the specified {@link String suffix}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Refer to <code><i>{@link #createImageBinding(String)}</i></code> for additional information.</li>
     * </ol>
     *
     * @param suffix The {@link String} representing the {@link Image} variation to be bound to the {@link ObjectProperty} returned by {@link #missingno(String) this method}.
     *
     * @return The {@code missingno} {@link Image} variation based on the specified {@link String suffix}.
     *
     * @see #createImageBinding(String)
     */
    private Image missingno(String suffix)
    {
        return ResourceTools.get().getImage("buttons/missingno/", "missingno" + suffix, "png");
    }
    
    //</editor-fold>
}
