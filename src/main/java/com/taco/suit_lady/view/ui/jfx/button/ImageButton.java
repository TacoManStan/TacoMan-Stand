package com.taco.suit_lady.view.ui.jfx.button;

import com.taco.suit_lady.util.BindingTools;
import com.taco.suit_lady.util.ResourceTools;
import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableStringValue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class that wraps an {@link ImageView} and {@link Button} into a single class.
 * Note that {@link ImageButton} does <i>not</i> implement or extend any UI framework classes, but rather has methods that allow it to be used in JFX UI environments.
 * At some point, it might be a good idea to change that, allowing {@link ImageButton ImageButtons} to be added directly to the JFX UI and have their functionality handled internally.
 * Currently, the {@link ImageButton} is handled by passing a {@link ImagePane} object as a parameter that will be responsible for containing the {@link ImageButton}.
 * In the future, combining those two classes would likely be preferred, with the image itself being controlled/switched via cache references.
 */
public class ImageButton
        implements Nameable
{
    private ApplicationContext ctx;
    
    private final ImagePane imagePane;
    
    private final StringBinding nameBinding;
    private final ObjectProperty<ButtonViewGroup> buttonGroupProperty;
    
    private final ReadOnlyObjectWrapper<Image> standardImageProperty;
    private final ReadOnlyObjectWrapper<Image> hoveredImageProperty;
    private final ReadOnlyObjectWrapper<Image> pressedImageProperty;
    private final ReadOnlyObjectWrapper<Image> disabledImageProperty;
    
    private final ObjectProperty<Runnable> actionResponderProperty;
    
    private final BooleanBinding hoveredBinding;
    private final BooleanBinding pressedBinding;
    
    private final ReadOnlyBooleanWrapper selectedProperty;
    private final BooleanProperty disabledProperty;
    
    private final boolean isTheme;
    private final boolean toggleable;
    
    public ImageButton(ImagePane imagePane, String name, Runnable actionResponder, boolean toggleable, boolean isTheme, Point2D size)
    {
        this(imagePane, BindingTools.createStringBinding(name), actionResponder, toggleable, isTheme, size);
    }
    
    public ImageButton(ImagePane imagePane, ObservableStringValue nameBinding, Runnable actionResponder, boolean toggleable, boolean isTheme, Point2D size)
    {
        this.imagePane = imagePane == null ? new ImagePane() : imagePane;
        
        if (size != null) {
            this.imagePane.setPrefSize(size.getX(), size.getY());
            this.imagePane.setMaxSize(size.getX(), size.getY());
        }
        
        this.nameBinding = Bindings.createStringBinding(() -> {
            final String name = nameBinding.get();
            return name != null ? name : "missingno";
        }, nameBinding);
        
        this.buttonGroupProperty = new ReadOnlyObjectWrapper<>();
        
        this.standardImageProperty = new ReadOnlyObjectWrapper<>();
        this.hoveredImageProperty = new ReadOnlyObjectWrapper<>();
        this.pressedImageProperty = new ReadOnlyObjectWrapper<>();
        this.disabledImageProperty = new ReadOnlyObjectWrapper<>();
        
        this.actionResponderProperty = new SimpleObjectProperty<>(actionResponder);
        
        this.selectedProperty = new ReadOnlyBooleanWrapper();
        this.disabledProperty = new SimpleBooleanProperty();
        
        this.isTheme = isTheme;
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
    
    /**
     * <p>Returns the {@link ImagePane} object containing this {@link ImageButton}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Required because {@link ImageButton ImageButtons} are not actually {@link Node UI elements}.</li>
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
     *     <li>The {@link Image} variations are essentially each a different styling of the same original image.</li> // TODO - Eventually have styling be done via code automatically.
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
     * <blockquote>Refer to the <code><i>{@link #nameBinding()}</i></code> docs for additional information.</blockquote>
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
    
    public ObjectProperty<ButtonViewGroup> buttonGroupProperty()
    {
        return buttonGroupProperty;
    }
    
    public ButtonViewGroup getButtonGroup()
    {
        return buttonGroupProperty.get();
    }
    
    public void setButtonGroup(ButtonViewGroup buttonGroup)
    {
        buttonGroupProperty.set(buttonGroup);
    }
    
    public boolean isInButtonGroup()
    {
        return getButtonGroup() != null;
    }
    
    public ObjectProperty<Runnable> actionResponderProperty()
    {
        return actionResponderProperty;
    }
    
    public Runnable getActionResponder()
    {
        return actionResponderProperty.get();
    }
    
    public void setActionResponder(Runnable actionResponder)
    {
        actionResponderProperty.set(actionResponder);
    }
    
    public boolean isToggleable()
    {
        return toggleable;
    }
    
    public boolean isTheme()
    {
        return isTheme;
    }
    
    //
    
    //<editor-fold desc="Image Properties">
    
    public ReadOnlyObjectProperty<Image> standardImageProperty()
    {
        return standardImageProperty.getReadOnlyProperty();
    }
    
    public Image getStandardImage()
    {
        return standardImageProperty.get();
    }
    
    public ReadOnlyObjectProperty<Image> hoveredImageProperty()
    {
        return hoveredImageProperty.getReadOnlyProperty();
    }
    
    public Image getHoveredImage()
    {
        return hoveredImageProperty.get();
    }
    
    public ReadOnlyObjectProperty<Image> pressedImageProperty()
    {
        return pressedImageProperty.getReadOnlyProperty();
    }
    
    public Image getPressedImage()
    {
        return pressedImageProperty.get();
    }
    
    public ReadOnlyObjectProperty<Image> disabledImageProperty()
    {
        return disabledImageProperty.getReadOnlyProperty();
    }
    
    public Image getDisabledImage()
    {
        return disabledImageProperty.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Button Status Properties">
    
    public BooleanBinding hoveredBinding()
    {
        return hoveredBinding;
    }
    
    public boolean isHovered()
    {
        return hoveredBinding.get();
    }
    
    public BooleanBinding pressedBinding()
    {
        return pressedBinding;
    }
    
    public boolean isPressed()
    {
        return pressedBinding.get();
    }
    
    public final ReadOnlyBooleanProperty selectedProperty()
    {
        return selectedProperty.getReadOnlyProperty();
    }
    
    public final boolean isSelected()
    {
        return selectedProperty.get();
    }
    
    public final boolean setSelected(boolean selected)
    {
        if (isToggleable())
            selectedProperty.set(selected);
        return false;
    }
    
    public boolean toggle()
    {
        return setSelected(!isSelected());
    }
    
    public final BooleanProperty disabledProperty()
    {
        return disabledProperty;
    }
    
    public final boolean isDisabled()
    {
        return disabledProperty.get();
    }
    
    public final void setDisabled(boolean disabled)
    {
        disabledProperty.set(disabled);
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
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
    }
    
    private ObjectBinding<Image> createImageBinding(String suffix)
    {
        return Bindings.createObjectBinding(() -> {
            Image image = ResourceTools.get().getImage(getPathID(), getID() + suffix, "png");
            return image == null ? missingno(suffix) : image;
        }, nameBinding());
    }
    
    private Image missingno(String suffix)
    {
        return ResourceTools.get().getImage("buttons/missingno/", "missingno" + suffix, "png");
    }
    
    //<editor-fold desc="--- SIZE ---">
    
    public static final Point2D SMALL = new Point2D(30.0, 25.0);
    public static final Point2D SMALL_BOX = new Point2D(30.0, 30.0);
    
    public static final Point2D MEDIUM = new Point2D(60.0, 50.0);
    public static final Point2D MEDIUM_BOX = new Point2D(60.0, 60.0);
    
    public static final Point2D LARGE = new Point2D(120.0, 100.0);
    public static final Point2D LARGE_BOX = new Point2D(120.0, 120.0);
    
    public String getSizeID()
    {
        Point2D size = new Point2D(imagePane.getWidth(), imagePane.getHeight());
        if (size.equals(SMALL))
            return "small";
        else if (size.equals(SMALL_BOX))
            return "small_box";
        else if (size.equals(MEDIUM))
            return "medium";
        else if (size.equals(MEDIUM_BOX))
            return "medium_box";
        else if (size.equals(LARGE))
            return "large";
        else if (size.equals(LARGE_BOX))
            return "large_box";
        return "default";
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
         *             <lI>The {@link ImageButton} is not {@link #disabledProperty() disabled}.</lI>
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
}
