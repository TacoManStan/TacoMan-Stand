package com.taco.suit_lady.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.printing.PrintData;
import com.taco.suit_lady.util.values.numbers.Bounds;
import com.taco.suit_lady.util.values.numbers.bindings.BoundsBinding;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Props;
import com.taco.tacository.json.*;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public class GameObjectModelDefinition
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadable {
    
    private final GameObjectModel model;
    
    private final ReadOnlyIntegerWrapper xOffsetProperty;
    private final ReadOnlyIntegerWrapper yOffsetProperty;
    
    private final ReadOnlyIntegerWrapper widthProperty;
    private final ReadOnlyIntegerWrapper heightProperty;
    
    private IntegerBinding xLocationBinding;
    private IntegerBinding yLocationBinding;
    
    private final BoundsBinding rawBoundsBinding;
    private BoundsBinding boundsBinding;
    
    //
    
    private final ReadOnlyStringWrapper imageTypeProperty;
    private final ReadOnlyStringWrapper imageIdProperty;
    
    private StringBinding jIdBinding;
    private StringBinding jIdBindingUnsafe;
    
    protected GameObjectModelDefinition(@NotNull GameObjectModel model, @Nullable String imageType, @Nullable String imageId) {
        this.model = model;
        
        this.xOffsetProperty = new ReadOnlyIntegerWrapper(1);
        this.yOffsetProperty = new ReadOnlyIntegerWrapper(1);
        
        this.widthProperty = new ReadOnlyIntegerWrapper(1);
        this.heightProperty = new ReadOnlyIntegerWrapper(1);
        
        this.rawBoundsBinding = new BoundsBinding(xOffsetProperty, yOffsetProperty, widthProperty, heightProperty);
        
        //
        
        this.imageTypeProperty = new ReadOnlyStringWrapper(imageType != null ? imageType : "units");
        this.imageIdProperty = new ReadOnlyStringWrapper(imageId != null ? imageId : "taco");
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected final GameObjectModelDefinition init() {
//        printer().get(getClass()).setEnabled(false);
        
        initJson();
        initBounds();
        
        return this;
    }
    
    //
    
    private void initBounds() {
        final PrintData p = printer().get(this);
        p.setPrintPrefix(false);
        
        this.xLocationBinding = Bind.intBinding(
                () -> -getCamera().getAggregateX() + getOffsetX() + getModel().getOwner().getLocationX(false),
                xOffsetProperty, getModel().getOwner().xLocationProperty(), getCamera().xAggregateBinding());
        this.yLocationBinding = Bind.intBinding(
                () -> -getCamera().getAggregateY() + getOffsetY() + getModel().getOwner().getLocationY(false),
                yOffsetProperty, getModel().getOwner().yLocationProperty(), getCamera().yAggregateBinding());
        //        this.yLocationBinding = BindingsSL.intBinding(
        //                () -> getOffsetY() + getModel().getOwner().getLocationY(false),
        //                yOffsetProperty, getModel().getOwner().yLocationProperty());
        
        this.boundsBinding = new BoundsBinding(xLocationBinding, yLocationBinding, widthProperty, heightProperty);
        
//        if (getModel().getOwner().isTestObject1()) {
//            this.xLocationBinding.addListener((observable, oldValue, newValue) -> p.print("Location X Changed: [" + oldValue + " --> " + newValue + "]"));
//            this.yLocationBinding.addListener((observable, oldValue, newValue) -> p.print("Location Y Changed: [" + oldValue + " --> " + newValue + "]"));
//
//            this.boundsBinding.addListener((observable, oldValue, newValue) -> p.print("Bounds Changed: [" + oldValue + " --> " + newValue + "]"));
//        }
        
//        printer().get(this).print("Bounds: " + getBounds());
    }
    
    private void initJson() {
        printer().get(getClass()).print("Initializing JSON");
        
        this.jIdBinding = Bind.stringBinding(() -> calcJId(true), imageTypeProperty, imageIdProperty);
        this.jIdBindingUnsafe = Bind.stringBinding(() -> calcJId(false), imageTypeProperty, imageIdProperty);
        
        this.jIdBinding.addListener((observable, oldValue, newValue) -> reload());
        
        reload();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObjectModel getModel() { return model; }
    
    
    public final @NotNull ReadOnlyStringProperty readOnlyImageTypeProperty() { return imageTypeProperty.getReadOnlyProperty(); }
    public final @NotNull String getImageType() { return imageTypeProperty.get(); }
    protected final String setImageType(@NotNull String newValue) { return Props.setProperty(imageTypeProperty, newValue); }
    
    public final @NotNull ReadOnlyStringProperty readOnlyImageIdProperty() { return imageIdProperty.getReadOnlyProperty(); }
    public final @NotNull String getImageId() { return imageIdProperty.get(); }
    protected final String setImageId(@NotNull String newValue) { return Props.setProperty(imageIdProperty, newValue); }
    
    
    public final @NotNull StringBinding jIdBinding(boolean safe) { return safe ? jIdBinding : jIdBindingUnsafe; }
    public final @Nullable String getJId(boolean safe) { return jIdBinding(safe).get(); }
    
    
    //<editor-fold desc="> Bounds">
    
    public final ReadOnlyIntegerProperty readOnlyOffsetPropertyX() { return xOffsetProperty.getReadOnlyProperty(); }
    public final int getOffsetX() { return xOffsetProperty.get(); }
    protected final int setOffsetX(@NotNull Number newValue) { return Props.setProperty(xOffsetProperty, newValue.intValue()); }
    
    public final ReadOnlyIntegerProperty readOnlyOffsetPropertyY() { return yOffsetProperty.getReadOnlyProperty(); }
    public final int getOffsetY() { return yOffsetProperty.get(); }
    protected final int setOffsetY(@NotNull Number newValue) { return Props.setProperty(yOffsetProperty, newValue.intValue()); }
    
    
    public final ReadOnlyIntegerProperty readOnlyWidthProperty() { return widthProperty.getReadOnlyProperty(); }
    public final int getWidth() { return widthProperty.get(); }
    protected final int setWidth(@NotNull Number newValue) { return Props.setProperty(widthProperty, newValue.intValue()); }
    
    public final ReadOnlyIntegerProperty readOnlyHeightProperty() { return heightProperty.getReadOnlyProperty(); }
    public final int getHeight() { return heightProperty.get(); }
    protected final int setHeight(@NotNull Number newValue) { return Props.setProperty(heightProperty, newValue.intValue()); }
    
    
    public final IntegerBinding xLocationBinding() { return xLocationBinding; }
    public final int getLocationX() { return xLocationBinding.get(); }
    
    public final IntegerBinding yLocationBinding() { return yLocationBinding; }
    public final int getLocationY() { return yLocationBinding.get(); }
    
    
    public final @NotNull BoundsBinding rawBoundsBinding() { return rawBoundsBinding; }
    public final @NotNull Bounds getRawBounds() { return rawBoundsBinding.getBounds(); }
    
    public final @NotNull BoundsBinding boundsBinding() { return boundsBinding; }
    public final @NotNull Bounds getBounds() { return boundsBinding.getBounds(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return getModel().getGame(); }
    
    @Override public final @NotNull Springable springable() { return getModel(); }
    @Override public @Nullable Lock getLock() { return getModel().getLock(); }
    
    //<editor-fold desc="> JSON">
    
    //    @Override public String getJID() { return getImageType() + "-" + getImageId(); }
    @Override public String getJID() { return getJId(true); }
    
    @Override public void load(JsonObject parent) {
        printer().get(getClass()).print("Loading GameObjectModelDefinition");
        
        setImageType(JUtil.loadString(parent, "image-type"));
        setImageId(JUtil.loadString(parent, "image-id"));
        
        setOffsetX(JUtil.loadInt(parent, "offset-x"));
        setOffsetY(JUtil.loadInt(parent, "offset-y"));
        setWidth(JUtil.loadInt(parent, "width"));
        setHeight(JUtil.loadInt(parent, "height"));
    }
    
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("image-type", getImageType()),
                JUtil.create("image-id", getImageId()),
                JUtil.create("offset-x", getOffsetX()),
                JUtil.create("offset-y", getOffsetY()),
                JUtil.create("width", getWidth()),
                JUtil.create("height", getHeight())
        };
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected final GameObjectModelDefinition bindToOwner() {
        setOffsetX(0);
        setOffsetY(0);
        
        widthProperty.bind(getModel().getOwner().widthProperty());
        heightProperty.bind(getModel().getOwner().heightProperty());
        
        if (getModel().getOwner().isTestObject1()) {
//            printer().get(this).print("Setting Model Definition Values For: " + getModel().getOwner());
//            heightProperty.bind(BindingsSL.doubleBinding(() -> getModel().getOwner().getHeight() * 2, getModel().getOwner().heightProperty()));
//            yOffsetProperty.bind(BindingsSL.doubleBinding(() -> -getModel().getOwner().getHeight() / 2, getModel().getOwner().yLocationProperty()));
            //            widthProperty.bind(BindingsSL.doubleBinding(() -> getModel().getOwner().getWidth() * 1.5, getModel().getOwner().widthProperty()));
            //            heightProperty.bind(BindingsSL.doubleBinding(() -> getModel().getOwner().getHeight() * 1.5, getModel().getOwner().heightProperty()));
        }
        
        return this;
    }
    
    //<editor-fold desc="> JSON Internal">
    
    private final String jIdPrefix = "model-def_";
    
    private @Nullable String calcJId(boolean safe) {
        String imageType = getImageType();
        String imageId = getImageId();
        if (!safe || (imageType != null && imageId != null))
            return jIdPrefix + imageType + "-" + imageId;
        return null;
    }
    
    
    private void reload() {
        final String jID = getJId(false);
        if (jID != null) {
            printer().get(getClass()).print("Attempting GameObjectModel Load...");
            JFiles.load(this);
        } else {
            printer().get(getClass()).err("Cannot Load JSON Data: Calculated JID is null [" + getJId(false) + "]");
        }
    }
    
    private void autoSave() {
        JFiles.save(this);
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
