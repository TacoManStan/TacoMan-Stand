package com.taco.suit_lady.view.ui.jfx.button;

import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.BindingTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.ResourceTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.view.ui.jfx.components.ImagePane;
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
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

// TO-DOC
public class ImageButton
        implements Nameable, Springable {
    //<editor-fold desc="--- FIELDS ---">
    
    private final StrictSpringable springable;
    
    private final ImagePane imagePane;
    
    private final StringBinding nameBinding;
    private final ObjectProperty<ImageButtonGroup> buttonGroupProperty;
    
    private final PaintData paintData;
    private final PaintData hoveredPaintData;
    private final PaintData pressedPaintData;
    private final PaintData disabledPaintData;
    private final PaintData selectedPaintData;
    private final PaintData selectedHoveredPaintData;
    private final PaintData selectedPressedPaintData;
    
    private final ObjectProperty<Runnable> actionResponderProperty;
    private final ObjectProperty<Runnable> actionResponderFXProperty;
    
    private final BooleanBinding hoveredBinding;
    private final BooleanBinding pressedBinding;
    
    private final BooleanProperty selectedProperty;
    private final BooleanProperty disabledProperty;
    
    private final boolean toggleable;
    
    //</editor-fold>
    
    /**
     * <p>Refer to {@link #ImageButton(Springable, ImagePane, ObservableStringValue, Runnable, Runnable, boolean, Point2D) Fully-Parameterized Constructor} for details.</p>
     * <p><b>Identical to...</b></p>
     * <blockquote><code>new ImageButton(imagePane, <u>BindingTools.createStringBinding(name)</u>, actionResponder, actionResponderFX, toggleable, size)</code></blockquote>
     */
    public ImageButton(
            @NotNull Springable springable,
            @Nullable ImagePane imagePane,
            @Nullable String name,
            @Nullable Runnable actionResponder,
            @Nullable Runnable actionResponderFX,
            boolean toggleable,
            @Nullable Point2D size) {
        this(springable, imagePane,
             BindingTools.createStringBinding(name), actionResponder, actionResponderFX, toggleable, size);
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
            @NotNull Springable springable,
            @Nullable ImagePane imagePane,
            @NotNull ObservableStringValue nameBinding,
            @Nullable Runnable actionResponder,
            @Nullable Runnable actionResponderFX,
            boolean toggleable,
            @Nullable Point2D size) {
        this.springable = springable.asStrict();
        
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
        
        this.actionResponderProperty = new SimpleObjectProperty<>(actionResponder);
        this.actionResponderFXProperty = new SimpleObjectProperty<>(actionResponderFX);
        
        this.selectedProperty = new SimpleBooleanProperty();
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
        
        
        this.hoveredBinding = BindingTools.createBooleanBinding(this.imagePane.hoverProperty());
        this.pressedBinding = BindingTools.createBooleanBinding(this.imagePane.pressedProperty());
        
        
        this.selectedProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue && !isToggleable())
                throw ExceptionTools.unsupported("Image Button \"" + getName() + "\" is not toggleable and can therefore not be selected.");
        });
        
        
        this.paintData = new PaintData("");
        this.hoveredPaintData = new PaintData("_hovered");
        this.pressedPaintData = new PaintData("_pressed");
        this.disabledPaintData = new PaintData("_disabled");
        this.selectedPaintData = new PaintData("_selected");
        this.selectedHoveredPaintData = new PaintData("_selected_hovered");
        this.selectedPressedPaintData = new PaintData("_selected_pressed");
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    /**
     * <p>Initializes and then returns a self-reference to this {@link ImageButton}.</p>
     * <p><b>Initialization Process</b></p>
     * <ol>
     *     <li>
     *         Initializes {@link #getImagePane() ImagePane}.
     *         <ol>
     *             <li>
     *                 Constructs an {@link Array} of all {@link Observable Observables} that will cause the {@link #getImage() Image} for this {@link ImageButton} to be {@link ObjectBinding#computeValue() Recalculated} upon invalidation.
     *             </li>
     *             <li>
     *                 Constructs an {@link ObjectBinding} that {@link ObjectBinding#bind(Observable...) binds} the {@link ImagePane#imageProperty() Image Property} of this {@link ImageButton ImageButton's} {@link #getImagePane() ImagePane} to the value of <code><i>{@link #getImage()}</i></code>.
     *                 <ul>
     *                     <li>Sets the {@link Observable Observales} that will trigger a {@link ObjectBinding#computeValue() recalculation} for the {@link ObjectBinding} to the aforementioned {@link Array} of {@link Observable Observales}. </li>
     *                 </ul>
     *             </li>
     *         </ol>
     *     </li>
     *     <li>
     *         Configures this {@link ImageButton ImageButton's} {@link #getImagePane() ImagePane}.
     *         <ol>
     *             <li>Sets {@link ImagePane#pickOnBoundsProperty() Pick on Bounds} to {@code true}.</li>
     *             <li>Sets {@link ImagePane#onMouseClickedProperty() On Mouse Clicked} to {@link Event#consume() Consume} its {@link Event}.</li>
     *             <li>Sets {@link ImagePane#onMousePressedProperty() On Mouse Pressed} to {@link Event#consume() Consume} its {@link Event}.</li>
     *             <li>Sets {@link ImagePane#onMouseReleasedProperty() On Mouse Released} to execute <code><i>{@link #onMouseReleased(MouseEvent)}</i></code>.</li>
     *         </ol>
     *     </li>
     *     <li>Returns a self-reference ({@code this}) to this {@link ImageButton}. Note that the return value is oftentimes superfluous.</li>
     * </ol>
     *
     * @return A self-reference ({@code this}) to this {@link ImageButton}.
     */
    public ImageButton initialize() {
        // Note: MUST be called manually after an ImageButton is constructed.
        
        final Observable[] observables = new Observable[]{
                nameBinding,
                hoveredBinding,
                pressedBinding,
                selectedProperty,
                disabledProperty
        };
        
        imagePane.imageProperty().bind(Bindings.createObjectBinding(
                () -> getImage(), observables));
        
        imagePane.setPickOnBounds(true);
        
        imagePane.setOnMouseClicked(Event::consume);
        imagePane.setOnMousePressed(Event::consume);
        imagePane.setOnMouseReleased(event -> onMouseReleased(event));
        
        return this;
    }
    
    /**
     * <p>A helper method that is executed whenever this {@link ImageButton ImageButton's} {@link #getImagePane() ImagePane} triggers a {@link MouseEvent#MOUSE_RELEASED Mouse Released Event}.</p>
     * <p><b>Execution Process</b></p>
     * <ol>
     *     <li>
     *         If <i>any</i> of the following conditions are {@code true}, the {@link MouseEvent} is {@link MouseEvent#consume() consumed}, no other actions are taken, and {@link #onMouseReleased(MouseEvent) this method} returns silently.
     *         <ol>
     *             <li>The {@link Event#getSource() Event Source} is not this {@link ImageButton ImageButton's} {@link #getImagePane() ImagePane}.</li>
     *             <li>The {@link MouseEvent} was not triggered while {@link FXTools#isMouseOnEventSource(MouseEvent) on the} {@link Event#getSource() Event Source}.</li>
     *             <li>This {@link ImageButton} is currently {@link #disabledProperty() disabled}.</li>
     *         </ol>
     *     </li>
     *     <li>The <code><i>{@link #toggle()}</i></code> method is called on this {@link ImageButton} instance.</li>
     *     <li>The <code><i>{@link #onAction()}</i></code> method is called on this {@link ImageButton} instance.</li>
     *     <li>The {@link MouseEvent} is {@link MouseEvent#consume() consumed}.</li>
     * </ol>
     *
     * @param event The triggering {@link MouseEvent} object.
     */
    private void onMouseReleased(MouseEvent event) {
        if (Objects.equals(event.getSource(), imagePane) && FXTools.get().isMouseOnEventSource(event) && !isDisabled()) {
            toggle();
            onAction();
        }
        event.consume();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="Action Responding Properties">
    
    /**
     * <p>Returns the {@link ObjectProperty} containing the {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     * <p><b>Execution Details</b></p>
     * <ol>
     *     <li>The {@link Runnable} is executed in a {@link Task Background Task} by the core {@link ThreadPoolExecutor Executor} for the entire application instance.</li>
     *     <li>The {@link Task} {@link ThreadPoolExecutor#execute(Runnable) execution} is non-blocking.</li>
     *     <li>To execute a {@link Runnable} on the {@link FXTools#isFXThread() JavaFX Thread}, refer to <code><i>{@link #actionResponderFXProperty()}</i></code>.</li>
     *     <li>If the {@link Runnable value} contained by the {@link ObjectProperty} is {@code null}, no action response will be executed.</li>
     * </ol>
     *
     * @return The {@link ObjectProperty} containing the {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderFXProperty()
     */
    public @NotNull ObjectProperty<Runnable> actionResponderProperty() {
        return actionResponderProperty;
    }
    
    /**
     * <p>Returns the {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @return The {@link Runnable} that is executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderProperty()
     */
    public @Nullable Runnable getActionResponder() {
        return actionResponderProperty.get();
    }
    
    /**
     * <p>Sets the {@link Runnable} to be executed when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @param actionResponder The {@link Runnable} to be executed in a {@link Task Background Task} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderProperty()
     */
    public void setActionResponder(@Nullable Runnable actionResponder) {
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
    public @NotNull ObjectProperty<Runnable> actionResponderFXProperty() {
        return actionResponderFXProperty;
    }
    
    /**
     * <p>Returns the {@link Runnable} that is executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @return The {@link Runnable} that is executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderFXProperty()
     */
    public @Nullable Runnable getActionResponderFX() {
        return actionResponderFXProperty.get();
    }
    
    /**
     * <p>Sets the {@link Runnable} to be executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.</p>
     *
     * @param actionResponder The {@link Runnable} to be executed on the {@link FXTools#isFXThread() JavaFX Thread} when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *
     * @see #actionResponderFXProperty()
     */
    public void setActionResponderFX(@Nullable Runnable actionResponder) {
        actionResponderFXProperty.set(actionResponder);
    }
    
    /**
     * <p>Executes the {@link #actionResponderProperty() Background Action Responder} and {@link #actionResponderFXProperty() FX Action Responder} {@link Runnable Runnables}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>
     *         Executed automatically when this {@link ImageButton} is {@link Button#onActionProperty() pressed}.
     *         <br><i>See <code>{@link #onMouseReleased(MouseEvent)}</code> for details.</i>
     *     </li>
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
    private void onAction() {
        final Runnable actionResponder = getActionResponder();
        if (actionResponder != null)
            ctx().getBean(LogiCore.class).execute(
                    new Task<>() {
                        @Override
                        protected Object call() {
                            actionResponder.run();
                            return null;
                        }
                    });
        
        final Runnable actionResponderFX = getActionResponderFX();
        if (actionResponderFX != null)
            FXTools.get().runFX(actionResponderFX, false);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Image Properties">
    
    public final String SUFFIX_STANDARD = "";
    public final String SUFFIX_HOVERED = "_hovered";
    public final String SUFFIX_PRESSED = "_pressed";
    public final String SUFFIX_DISABLED = "_disabled";
    public final String SUFFIX_SELECTED = "_selected";
    public final String SUFFIX_SELECTED_HOVERED = "_selected_hovered";
    public final String SUFFIX_SELECTED_PRESSED = "_selected_pressed";
    
    public final PaintData paintData(String suffix) {
        return switch (suffix) {
            case SUFFIX_STANDARD -> paintData;
            case SUFFIX_HOVERED -> hoveredPaintData;
            case SUFFIX_PRESSED -> pressedPaintData;
            case SUFFIX_DISABLED -> disabledPaintData;
            case SUFFIX_SELECTED -> selectedPaintData;
            case SUFFIX_SELECTED_HOVERED -> selectedHoveredPaintData;
            case SUFFIX_SELECTED_PRESSED -> selectedPressedPaintData;
            
            default -> throw ExceptionTools.unsupported("Suffix \"" + suffix + "\" is not supported.");
        };
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
    public BooleanBinding hoveredBinding() {
        return hoveredBinding;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is in a {@link ButtonState#HOVERED HOVERED} state.</p>
     *
     * @return True if this {@link ImageButton} is in a {@link ButtonState#HOVERED HOVERED} state, false if it is in any other {@link ButtonState ButtonState}.
     *
     * @see #hoveredBinding()
     */
    public boolean isHovered() {
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
    public BooleanBinding pressedBinding() {
        return pressedBinding;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is toggleable or not.</p>
     *
     * @return True if this {@link ImageButton} is toggleable, false if it is not.
     */
    public boolean isToggleable() {
        return toggleable;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is in a {@link ButtonState#PRESSED PRESSED} state.</p>
     *
     * @return True if this {@link ImageButton} is in a {@link ButtonState#PRESSED PRESSED} state, false if it is in any other {@link ButtonState ButtonState}.
     *
     * @see #pressedBinding()
     */
    public boolean isPressed() {
        return pressedBinding.get();
    }
    
    /**
     * <p>Returns the {@link BooleanProperty property} containing a boolean that reflects if this {@link ImageButton} is currently selected or not.</p>
     * <p><b>Changing the Value</b></p>
     * <ol>
     *     <li>The {@link #getButtonGroup() ImageButtonGroup} {@link ImageButtonGroup#selectedButtonProperty() selection} is automatically updated via listener when this value is changed.</li>
     *     <li></li>
     * </ol>
     *
     * @return The {@link BooleanProperty property} containing a boolean that reflects if this {@link ImageButton} is currently selected or not.
     */
    public final BooleanProperty selectedProperty() {
        return selectedProperty;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is currently {@link #selectedProperty() selected} or not.</p>
     *
     * @return If this {@link ImageButton} is currently {@link #selectedProperty() selected} or not.
     *
     * @see #selectedProperty()
     */
    public final boolean isSelected() {
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
    public final boolean setSelected(boolean selected) {
        if (isToggleable()) {
            selectedProperty.set(selected);
            return true;
        }
        return false;
    }
    
    /**
     * <p>Toggles the {@link #selectedProperty() selection} state of this {@link ImageButton} by setting it to the opposite of its {@link #isSelected() current} state.</p>
     * <blockquote><b>Passthrough Definition:</b> <code><i>{@link #setSelected(boolean) setSelected}<b>(!</b>{@link #isSelected()}<b>)</b></i></code></blockquote>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If this {@link ImageButton} is not {@link #isToggleable() toggleable}, {@link #toggle() this method} does nothing and silently returns {@code false}.</li>
     * </ol>
     *
     * @return True if the {@link #setSelected(boolean) toggle} operation was successful, false if it was not.
     *
     * @see #setSelected(boolean)
     */
    public boolean toggle() {
        return setSelected(!isSelected());
    }
    
    /**
     * <p>Returns the {@link BooleanProperty} reflecting if this {@link ImageButton} is {@link ButtonState#DISABLED disabled} or not.</p>
     *
     * @return The {@link BooleanProperty} reflecting if this {@link ImageButton} is {@link ButtonState#DISABLED disabled} or not.
     *
     * @see ButtonState#DISABLED
     */
    public final BooleanProperty disabledProperty() {
        return disabledProperty;
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is currently in a {@link #disabledProperty() disabled} {@link ButtonState#DISABLED state} or not.</p>
     *
     * @return True if this {@link ImageButton} is currently in a {@link #disabledProperty() disabled} {@link ButtonState#DISABLED state}, false if it is not (i.e., if it is enabled).
     *
     * @see #disabledProperty()
     */
    public final boolean isDisabled() {
        return disabledProperty.get();
    }
    
    /**
     * <p>Sets the {@link #disabledProperty() disabled} {@link ButtonState#DISABLED state} of this {@link ImageButton} to the specified value.</p>
     *
     * @param disabled True to disable this {@link ImageButton}, false to enable it.
     *
     * @see #disabledProperty()
     */
    public final void setDisabled(boolean disabled) {
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
    public @NotNull ImagePane getImagePane() {
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
    public @NotNull StringBinding nameBinding() {
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
    public @NotNull String getName() {
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
    private @NotNull String getID() {
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
    private @NotNull String getPathID() {
        return "buttons/" + getID() + "/";
    }
    
    /**
     * <P>Returns the {@link ObjectProperty} containing the {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.</P>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All logic related to adding or removing an {@link ImageButton} from a {@link ImageButtonGroup} is done via a {@link ChangeListener} that observes the {@link ObjectProperty} returned by {@link #buttonGroupProperty() this method}.</li>
     *     <li>The aforementioned {@link ChangeListener} is configured in the {@link ImageButton} {@link #ImageButton(Springable, ImagePane, ObservableStringValue, Runnable, Runnable, boolean, Point2D) constructor}.</li>
     * </ol>
     *
     * @return The {@link ObjectProperty} containing the {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.
     */
    public @NotNull ObjectProperty<ImageButtonGroup> buttonGroupProperty() {
        return buttonGroupProperty;
    }
    
    /**
     * <p>Returns the {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.</p>
     *
     * @return The {@link ImageButtonGroup} that this {@link ImageButton} is in, or {@code null} if this {@link ImageButton} is not in a {@link ImageButtonGroup}.
     */
    public @Nullable ImageButtonGroup getButtonGroup() {
        return buttonGroupProperty.get();
    }
    
    /**
     * <p>Sets the {@link ImageButtonGroup} that this {@link ImageButton} is in to the specified {@code value}.</p>
     *
     * @param buttonGroup The {@link ImageButtonGroup} that this {@link ImageButton} is being added to.
     */
    public void setButtonGroup(@Nullable ImageButtonGroup buttonGroup) {
        buttonGroupProperty.set(buttonGroup);
    }
    
    /**
     * <p>Checks if this {@link ImageButton} is in a {@link ImageButtonGroup} or not.</p>
     *
     * @return True  if this {@link ImageButton} is in a {@link ImageButtonGroup}, false if it is not.
     */
    public boolean isInButtonGroup() {
        return getButtonGroup() != null;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- BUTTON STATE ---">
    
    /**
     * An {@code enum} defining the current {@code state} of this {@link ImageButton}.
     */
    public enum ButtonState {
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
        DISABLED,
        
        // TO-DOC
        SELECTED, SELECTED_HOVERED, SELECTED_PRESSED;
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
    public ButtonState getState() {
        final Image standardImage = paintData.getImage();
        final Image pressedImage = pressedPaintData.getImage();
        final Image hoveredImage = hoveredPaintData.getImage();
        final Image disabledImage = disabledPaintData.getImage();
        final Image selectedImage = selectedPaintData.getImage();
        final Image selectedHoveredImage = selectedHoveredPaintData.getImage();
        final Image selectedPressedImage = selectedPressedPaintData.getImage();
        
        if (disabledImage != null && isDisabled())
            return ButtonState.DISABLED;
        else if (pressedImage != null && isHovered() && isPressed() && !isSelected())
            return ButtonState.PRESSED;
        else if (hoveredImage != null && isHovered() && !isSelected())
            return ButtonState.HOVERED;
        else if (isSelected())
            if (selectedImage != null && !isHovered() && !isPressed())
                return ButtonState.SELECTED;
            else if (selectedPressedImage != null && isPressed())
                return ButtonState.SELECTED_PRESSED;
            else if (selectedHoveredImage != null && isHovered())
                return ButtonState.SELECTED_HOVERED;
        return ButtonState.STANDARD;
    }
    
    /**
     * <p>Returns the {@link Image} matching the current {@link ButtonState state} of this {@link ImageButton}.</p>
     *
     * @return The {@link Image} matching the current {@link ButtonState state} of this {@link ImageButton}.
     */
    public Image getImage() {
        return switch (getState()) {
            case HOVERED -> hoveredPaintData.getImage();
            case PRESSED -> pressedPaintData.getImage();
            case DISABLED -> disabledPaintData.getImage();
            case SELECTED -> selectedPaintData.getImage();
            case SELECTED_HOVERED -> selectedHoveredPaintData.getImage();
            case SELECTED_PRESSED -> selectedPressedPaintData.getImage();
            default -> paintData.getImage();
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
    
    public Point2D getSize() {
        return new Point2D(getImagePane().getWidth(), getImagePane().getHeight());
    }
    
    /**
     * <p>Returns the {@link String Size ID} for the current {@link #getSize() size} of this {@link ImageButton}.</p>
     *
     * @return The {@link String Size ID} for the current {@link #getSize() size} of this {@link ImageButton}.
     *
     * @see #getSize()
     */
    public String getSizeID() {
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
    //
    //    /**
    //     * <p>Constructs a new {@link ObjectBinding} bound to the {@link Image} variation represented by the specified {@link String suffix}.</p>
    //     * <p><b>Details</b></p>
    //     * <ol>
    //     *     <li>The {@link Image} bound to the returned {@link ObjectBinding} is automatically updated when the {@link #nameBinding() name} of this {@link ImageButton} changes.</li>
    //     *     <li>If the {@link Image} variation with the specified {@link String suffix} does not exist at the file location defined by the {@link Image} {@link #getPathID() mapping} for this {@link ImageButton}, {@link #missingno(String) missingno} is used instead.</li>
    //     * </ol>
    //     * <p><b>Missingno</b></p>
    //     * <ol>
    //     *     <li>The {@link #missingno(String) missingno} {@link Image images} are universal files displaying an icon indicating the intended {@link Image} could not be found.</li>
    //     *     <li>The name {@link #missingno(String) "missingno"} comes from the bugged Pokemon that would appear in the 1st Generation games due to a bug where in specific circumstances, a player could encounter a null Pokemon.</li>
    //     *     <li>{@link #missingno(String) "missingno"} stands for {@code Missing Number}.</li>
    //     * </ol>
    //     *
    //     * @param suffix The {@link String} representing the {@link Image} variation to be bound to the {@link ObjectProperty} returned by {@link #createImageBinding(String) this method}.
    //     *
    //     * @return A new {@link ObjectBinding} bound to the {@link Image} variation represented by the specified {@link String suffix}.
    //     *
    //     * @throws NullPointerException If the specified {@link String suffix} is {@code null}.
    //     * @see #missingno(String)
    //     */
    //    private @NotNull ObjectBinding<Image> createImageBinding(@NotNull String suffix) {
    //        ExceptionTools.nullCheck(suffix, "Suffix");
    //
    //        return Bindings.createObjectBinding(() -> {
    //            Image image = ResourceTools.get().getImage(getPathID(), getID() + "_blank", "png");
    //            if (image != null) {
    //                final WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
    //                PixelReader reader = image.getPixelReader();
    //
    //                for (int i = 0; i < image.getWidth(); i++) {
    //                    for (int j = 0; j < image.getHeight(); j++) {
    //                        Color rawColor = reader.getColor(i, j);
    //                        debugger().print("[" + i + ", " + j + "] - Raw Color " + getName() + ":  " + rawColor);
    //                        Color color = rawColor.equals(Color.BLACK) ? switch (suffix) {
    //                            case "" -> FXTools.Colors.from255(95, 95, 95);
    //                            case "_pressed", "_hovered" -> FXTools.Colors.from255(153, 153, 153);
    //                            case "_disabled" -> FXTools.Colors.from255(50, 50, 50);
    //                            default -> Color.BLACK;
    //                        } : switch (suffix) {
    //                            case "", "_disabled" -> Color.TRANSPARENT;
    //                            case "_pressed" -> FXTools.Colors.from255(64, 64, 64, 70);
    //                            case "_hovered" -> FXTools.Colors.from255(64, 64, 64, 35);
    //                            default -> Color.WHITE;
    //                        };
    //                        debugger().print("[" + i + ", " + j + "] - Raw Color " + getName() + ":  " + color);
    //                        writableImage.getPixelWriter().setColor(i, j, color);
    //                    }
    //                }
    //
    //                return writableImage != null ? writableImage : missingno(suffix);
    //            } else {
    //                debugger().print("Image is null  [" + getName() + "_" + suffix + "]");
    //                image = ResourceTools.get().getImage(getPathID(), getID() + suffix, "png");
    //                return image != null ? image : missingno(suffix);
    //            }
    //
    //        }, nameBinding());
    
    
    //</editor-fold>
    
    public final class PaintData {
        
        // The suffix
        private final String suffix;
        
        private final ObjectProperty<Color> foregroundColorProperty;
        private final ObjectProperty<Color> backgroundColorProperty;
        
        private final ObjectBinding<Image> imageBinding;
        
        public PaintData(String suffix) {
            this.suffix = suffix;
            
            this.foregroundColorProperty = new SimpleObjectProperty<>(defaultColor(true));
            this.backgroundColorProperty = new SimpleObjectProperty<>(defaultColor(false));
            
            this.imageBinding = generateImageBinding();
        }
        
        //<editor-fold desc="--- PROPERTIES ---">
        
        public ObjectProperty<Color> foregroundColorProperty() {
            return foregroundColorProperty;
        }
        
        public Color getForegroundColor() {
            return foregroundColorProperty.get();
        }
        
        public Color setForegroundColor(Color newValue) {
            Color oldValue = getForegroundColor();
            foregroundColorProperty.set(newValue);
            return oldValue;
        }
        
        
        public ObjectProperty<Color> backgroundColorProperty() {
            return backgroundColorProperty;
        }
        
        public Color getBackgroundColor() {
            return backgroundColorProperty.get();
        }
        
        public Color setBackgroundColor(Color newValue) {
            Color oldValue = getBackgroundColor();
            backgroundColorProperty.set(newValue);
            return oldValue;
        }
        
        
        public ObjectProperty<Color> getColorProperty(boolean foreground) {
            return foreground ? foregroundColorProperty : backgroundColorProperty;
        }
        
        public Color getColor(boolean foreground) {
            return getColorProperty(foreground).get();
        }
        
        
        //
        
        public String getSuffix() {
            return suffix;
        }
        
        
        public ObjectBinding<Image> imageProperty() {
            return imageBinding;
        }
        
        public Image getImage() {
            return imageBinding.get();
        }
        
        //</editor-fold>
        
        //<editor-fold desc="--- HELPER METHODS ---">
        
        private @NotNull ObjectBinding<Image> generateImageBinding() {
            return Bindings.createObjectBinding(() -> {
                Image image = ResourceTools.get().getImage(ImageButton.this.getPathID(), ImageButton.this.getID() + "_blank", "png");
                if (image != null) {
                    final WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
                    PixelReader reader = image.getPixelReader();
                    
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            Color rawColor = reader.getColor(i, j);
                            ImageButton.this.debugger().print("[" + i + ", " + j + "] - Raw Color " + ImageButton.this.getName() + ":  " + rawColor);
                            Color color = getColor(rawColor.equals(Color.BLACK));
                            ImageButton.this.debugger().print("[" + i + ", " + j + "] - Color " + ImageButton.this.getName() + ":  " + color);
                            writableImage.getPixelWriter().setColor(i, j, color);
                        }
                    }
                    
                    return writableImage != null ? writableImage : missingno();
                } else {
                    ImageButton.this.debugger().print("Image is null  [" + ImageButton.this.getName() + "_" + suffix + "]");
                    image = ResourceTools.get().getImage(ImageButton.this.getPathID(), ImageButton.this.getID() + suffix, "png");
                    return image != null ? image : missingno();
                }
            }, ImageButton.this.nameBinding(), foregroundColorProperty, backgroundColorProperty);
        }
        
        private Color defaultColor(boolean foreground) {
            return foreground ? switch (suffix) {
                case SUFFIX_STANDARD -> FXTools.Colors.from255(95, 95, 95);
                case SUFFIX_PRESSED, SUFFIX_HOVERED -> FXTools.Colors.from255(153, 153, 153);
                case SUFFIX_DISABLED -> FXTools.Colors.from255(50, 50, 50);
//                case SUFFIX_SELECTED_HOVERED, SUFFIX_SELECTED_PRESSED -> FXTools.Colors.from255(23, 77, 154);
                case SUFFIX_SELECTED_HOVERED, SUFFIX_SELECTED_PRESSED -> FXTools.Colors.from255(40, 83, 156);
                case SUFFIX_SELECTED -> FXTools.Colors.from255(30, 62, 117);
                default -> Color.BLACK;
            } : switch (suffix) {
                case SUFFIX_STANDARD, SUFFIX_DISABLED -> Color.TRANSPARENT;
                case SUFFIX_PRESSED -> FXTools.Colors.from255(64, 64, 64, 70);
                case SUFFIX_HOVERED -> FXTools.Colors.from255(64, 64, 64, 35);
                case SUFFIX_SELECTED -> FXTools.Colors.from255(35, 35, 35);
                case SUFFIX_SELECTED_PRESSED -> FXTools.Colors.from255(45, 45, 45);
                case SUFFIX_SELECTED_HOVERED -> FXTools.Colors.from255(40, 40, 40);
                default -> Color.WHITE;
            };
        }
        
        /**
         * <p>Returns the {@code missingno} {@link Image variation} based on the specified {@link #getSuffix() suffix}.</p>
         * <p><b>Details</b></p>
         * <ol>
         *     <li>Refer to <code><i>{@link #generateImageBinding()}</i></code> for additional information.</li>
         * </ol>
         *
         * @return The {@code missingno} {@link Image} variation based on this {@link PaintData Paint Data's} {@link #getSuffix() suffix (name)}.
         *
         * @see #generateImageBinding()
         */
        private Image missingno() {
            return ResourceTools.get().getImage("buttons/missingno/", "missingno" + suffix, "png");
        }
        
        //</editor-fold>
    }
}
