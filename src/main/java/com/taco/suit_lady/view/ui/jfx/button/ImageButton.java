package com.taco.suit_lady.view.ui.jfx.button;

import com.taco.suit_lady.uncategorized.UndefinedRuntimeException;
import com.taco.suit_lady.util.ResourceTools;
import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ImageButton
        implements Nameable
{
    private ApplicationContext ctx;
    
    private final ImagePane imagePane;
    
    private final StringProperty nameProperty;
    private final ObjectProperty<ButtonViewGroup> buttonGroupProperty;
    
    private final ReadOnlyObjectWrapper<Image> imageProperty;
    private final ReadOnlyObjectWrapper<Image> hoveredImageProperty;
    private final ReadOnlyObjectWrapper<Image> pressedImageProperty;
    private final ReadOnlyObjectWrapper<Image> disabledImageProperty;
    
    private final ObjectProperty<Runnable> actionResponderProperty;
    
    private final ReadOnlyBooleanWrapper hoveredProperty;
    private final ReadOnlyBooleanWrapper pressedProperty;
    private final ReadOnlyBooleanWrapper selectedProperty;
    private final BooleanProperty disabledProperty;
    
    private final boolean isTheme;
    private final boolean toggleable;
    
    private final boolean lockName;
    
    //
    
    public ImageButton(ImagePane imagePane, String name, Runnable actionResponder, boolean toggleable, boolean isTheme, Point2D size)
    {
        this(imagePane, new SimpleStringProperty(name), false, actionResponder, toggleable, isTheme, size);
    }
    
    public ImageButton(ImagePane imagePane, ObservableValue<String> nameProperty, Runnable actionResponder, boolean toggleable, boolean isTheme, Point2D size)
    {
        this(imagePane, nameProperty, true, actionResponder, toggleable, isTheme, size);
    }
    
    private ImageButton(ImagePane imagePane, ObservableValue<String> nameProperty, boolean lockName, Runnable actionResponder, boolean toggleable, boolean isTheme, Point2D size)
    {
        this.imagePane = imagePane == null ? new ImagePane() : imagePane;
        
        if (size != null)
        {
            this.imagePane.setPrefSize(size.getX(), size.getY());
            this.imagePane.setMaxSize(size.getX(), size.getY());
        }
        
        this.lockName = lockName;
        this.nameProperty = new SimpleStringProperty();
        if (this.lockName)
            this.nameProperty.bind(nameProperty);
        else
            this.setName(nameProperty.getValue());
        
        this.buttonGroupProperty = new ReadOnlyObjectWrapper<>();
        
        this.imageProperty = new ReadOnlyObjectWrapper<>();
        this.hoveredImageProperty = new ReadOnlyObjectWrapper<>();
        this.pressedImageProperty = new ReadOnlyObjectWrapper<>();
        this.disabledImageProperty = new ReadOnlyObjectWrapper<>();
        
        this.actionResponderProperty = new SimpleObjectProperty<>(actionResponder);
        
        this.hoveredProperty = new ReadOnlyBooleanWrapper();
        this.pressedProperty = new ReadOnlyBooleanWrapper();
        
        this.selectedProperty = new ReadOnlyBooleanWrapper();
        this.disabledProperty = new SimpleBooleanProperty();
        
        this.isTheme = isTheme;
        this.toggleable = toggleable;
        
        //
        
        this.buttonGroupProperty.addListener((observable, oldButtonGroup, newButtonGroup) -> {
            if (!Objects.equals(oldButtonGroup, newButtonGroup))
            {
                selectedProperty.unbind();
                if (oldButtonGroup != null)
                    oldButtonGroup.buttonViews().remove(this);
                if (newButtonGroup != null)
                {
                    newButtonGroup.buttonViews().add(this);
                    selectedProperty.addListener((observable1, oldValue, selected) -> {
                        if (selected)
                            newButtonGroup.setSelectedButton(this);
                        else
                            newButtonGroup.clearSelection(this); // Only clear the selection if the selection is this ImageButton
                    });
                }
            }
        });
        
        this.imageProperty.bind(createImageBinding(""));
        this.hoveredImageProperty.bind(createImageBinding("_hovered"));
        this.pressedImageProperty.bind(createImageBinding("_pressed"));
        this.disabledImageProperty.bind(createImageBinding("_disabled"));
        
        if (!this.lockName)
            setName(this.nameProperty.getValue());
        
        try
        {
            this.hoveredProperty.bind(this.imagePane.hoverProperty());
            this.pressedProperty.bind(this.imagePane.pressedProperty());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        
        //		this.selectedProperty.addListener((observable, oldValue, newValue) -> onAction());
    }
    
    //<editor-fold desc="Initialize">
    
    public void initialize()
    {
        Objects.requireNonNull(imagePane, "Image view cannot be null");
        Objects.requireNonNull(imageProperty, "Standard property cannot be null");
        
        ArrayList<Observable> observables = new ArrayList<>(Arrays.asList(
                nameProperty,
                hoveredProperty,
                pressedProperty,
                selectedProperty,
                disabledProperty,
                imageProperty
        ));
        
        imagePane.imageProperty().bind(Bindings.createObjectBinding(() -> {
            Image standardImage = getImage();
            Image pressedImage = getPressedImage();
            Image hoveredImage = getHoveredImage();
            Image disabledImage = getDisabledImage();
            if (disabledImage != null && isDisabled())
                return disabledImage;
            else if (pressedImage != null && isHovered() && isPressed())
                return pressedImage;
            else if (hoveredImage != null && isHovered())
                return hoveredImage;
            else if (pressedImage != null && isToggleable() && isSelected())
                return pressedImage;
            return standardImage;
        }, observables.toArray(new Observable[observables.size()])));
        
        imagePane.setPickOnBounds(true);
        imagePane.setOnMouseClicked(Event::consume);
        imagePane.setOnMousePressed(Event::consume);
        imagePane.setOnMouseReleased(event -> {
            if (Objects.equals(event.getSource(), imagePane) && FXTools.get().isMouseOnEventSource(event))
                if (!isDisabled())
                {
                    toggleSelected();
                    onAction();
                }
            event.consume();
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Properties">
    
    public ImagePane getImagePane()
    {
        return imagePane;
    }
    
    //
    
    public StringProperty nameProperty()
    {
        return nameProperty;
    }
    
    @Override public String getName()
    {
        return nameProperty.get();
    }
    
    public void setName(String id)
    {
        if (lockName)
            throw new UndefinedRuntimeException("A bound ButtonView cannot be set.");
        nameProperty.set(id);
    }
    
    private String getID()
    {
        String name = getName();
        if (name != null)
            return name.replace(" ", "_").toLowerCase();
        else
            return "missingno";
    }
    
    private String getPathID()
    {
        return "buttons/" + getID() + "/";
    }
    
    //
    
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
    
    //
    
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
    
    //
    
    public boolean isToggleable()
    {
        return toggleable;
    }
    
    public boolean isTheme()
    {
        return isTheme;
    }
    
    protected boolean getLockName()
    {
        return lockName;
    }
    
    //
    
    //<editor-fold desc="Image Properties">
    
    public ReadOnlyObjectProperty<Image> imageProperty()
    {
        return imageProperty.getReadOnlyProperty();
    }
    
    public Image getImage()
    {
        return imageProperty.get();
    }
    
    //
    
    public ReadOnlyObjectProperty<Image> hoveredImageProperty()
    {
        return hoveredImageProperty.getReadOnlyProperty();
    }
    
    public Image getHoveredImage()
    {
        return hoveredImageProperty.get();
    }
    
    //
    
    public ReadOnlyObjectProperty<Image> pressedImageProperty()
    {
        return pressedImageProperty.getReadOnlyProperty();
    }
    
    public Image getPressedImage()
    {
        return pressedImageProperty.get();
    }
    
    //
    
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
    
    public ReadOnlyBooleanProperty hoveredProperty()
    {
        return hoveredProperty.getReadOnlyProperty();
    }
    
    public boolean isHovered()
    {
        return hoveredProperty.get();
    }
    
    //
    
    public ReadOnlyBooleanProperty pressedProperty()
    {
        return pressedProperty.getReadOnlyProperty();
    }
    
    public boolean isPressed()
    {
        return pressedProperty.get();
    }
    
    //
    
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
    
    public boolean toggleSelected()
    {
        return setSelected(!isSelected());
    }
    
    //
    
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
        Runnable actionResponder = getActionResponder();
        if (actionResponder != null)
        {
            TB.executor().execute(new Task<>()
            {
                @Override
                protected Object call() throws Exception
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
        }, nameProperty);
    }
    
    private Image missingno(String suffix)
    {
        return ResourceTools.get().getImage("buttons/missingno/", "missingno" + suffix, "png");
    }
    
    //
    
    //<editor-fold desc="Sizes">
    
    public static final Point2D SMALL = new Point2D(30.0, 25.0);
    public static final Point2D SMALL_BOX = new Point2D(30.0, 30.0);
    
    public static final Point2D MEDIUM = new Point2D(60.0, 50.0);
    public static final Point2D MEDIUM_BOX = new Point2D(60.0, 60.0);
    
    public static final Point2D LARGE = new Point2D(120.0, 100.0);
    public static final Point2D LARGE_BOX = new Point2D(120.0, 120.0);
    
    private String getSizeString()
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
}
