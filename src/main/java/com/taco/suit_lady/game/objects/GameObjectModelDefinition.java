package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import org.jetbrains.annotations.NotNull;

public class GameObjectModelDefinition
        implements WrappedGameComponent {
    
    private final GameObjectModel model;
    
    private final ReadOnlyIntegerWrapper xOffsetProperty;
    private final ReadOnlyIntegerWrapper yOffsetProperty;
    
    private final ReadOnlyIntegerWrapper widthProperty;
    private final ReadOnlyIntegerWrapper heightProperty;
    
    private IntegerBinding xLocationBinding;
    private IntegerBinding yLocationBinding;
    
    private final BoundsBinding rawBoundsBinding;
    private BoundsBinding boundsBinding;
    
    protected GameObjectModelDefinition(@NotNull GameObjectModel model) {
        this.model = model;
        
        this.xOffsetProperty = new ReadOnlyIntegerWrapper();
        this.yOffsetProperty = new ReadOnlyIntegerWrapper();
        
        this.widthProperty = new ReadOnlyIntegerWrapper();
        this.heightProperty = new ReadOnlyIntegerWrapper();
        
        this.rawBoundsBinding = new BoundsBinding(xOffsetProperty, yOffsetProperty, widthProperty, heightProperty);
    }
    
    protected final GameObjectModelDefinition init() {
        this.xLocationBinding = BindingsSL.intBinding(
                () -> -getModel().getOwner().getGameMap().getModel().getCamera().getAggregateX() + getOffsetX() + getModel().getOwner().getLocationX(false),
                xOffsetProperty, getModel().getOwner().xLocationProperty(), getModel().getOwner().getGameMap().getModel().getCamera().xAggregateBinding());
        this.yLocationBinding = BindingsSL.intBinding(
                () -> -getModel().getOwner().getGameMap().getModel().getCamera().getAggregateY() + getOffsetY() + getModel().getOwner().getLocationY(false),
                xOffsetProperty, getModel().getOwner().yLocationProperty(), getModel().getOwner().getGameMap().getModel().getCamera().yAggregateBinding());
//        this.yLocationBinding = BindingsSL.intBinding(
//                () -> getOffsetY() + getModel().getOwner().getLocationY(false),
//                yOffsetProperty, getModel().getOwner().yLocationProperty());
        
        this.boundsBinding = new BoundsBinding(xLocationBinding, yLocationBinding, widthProperty, heightProperty);
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObjectModel getModel() { return model; }
    
    //
    
    public final ReadOnlyIntegerProperty readOnlyOffsetPropertyX() { return xOffsetProperty.getReadOnlyProperty(); }
    public final int getOffsetX() { return xOffsetProperty.get(); }
    protected final int setOffsetX(@NotNull Number newValue) { return PropertiesSL.setProperty(xOffsetProperty, newValue.intValue()); }
    
    public final ReadOnlyIntegerProperty readOnlyOffsetPropertyY() { return yOffsetProperty.getReadOnlyProperty(); }
    public final int getOffsetY() { return yOffsetProperty.get(); }
    protected final int setOffsetY(@NotNull Number newValue) { return PropertiesSL.setProperty(yOffsetProperty, newValue.intValue()); }
    
    
    public final ReadOnlyIntegerProperty readOnlyWidthProperty() { return widthProperty.getReadOnlyProperty(); }
    public final int getWidth() { return widthProperty.get(); }
    protected final int setWidth(@NotNull Number newValue) { return PropertiesSL.setProperty(widthProperty, newValue.intValue()); }
    
    public final ReadOnlyIntegerProperty readOnlyHeightProperty() { return heightProperty.getReadOnlyProperty(); }
    public final int getHeight() { return heightProperty.get(); }
    protected final int setHeight(@NotNull Number newValue) { return PropertiesSL.setProperty(heightProperty, newValue.intValue()); }
    
    
    public final IntegerBinding xLocationBinding() { return xLocationBinding; }
    public final int getLocationX() { return xLocationBinding.get(); }
    
    public final IntegerBinding yLocationBinding() { return yLocationBinding; }
    public final int getLocationY() { return yLocationBinding.get(); }
    
    
    public final @NotNull BoundsBinding rawBoundsBinding() { return rawBoundsBinding; }
    public final @NotNull Bounds getRawBounds() { return rawBoundsBinding.getBounds(); }
    
    public final @NotNull BoundsBinding boundsBinding() { return boundsBinding; }
    public final @NotNull Bounds getBounds() { return boundsBinding.getBounds(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return getModel().getGame(); }
    
    //</editor-fold>
    
    protected final GameObjectModelDefinition bindToOwner() {
        setOffsetX(0);
        setOffsetY(0);
        
        widthProperty.bind(getModel().getOwner().widthProperty());
        heightProperty.bind(getModel().getOwner().heightProperty());
    
        if (getModel().getOwner().isTestObject1()) {
            printer().get(this).print("Setting Model Definition Values For: " + getModel().getOwner());
            heightProperty.bind(BindingsSL.doubleBinding(() -> getModel().getOwner().getHeight() * 2, getModel().getOwner().heightProperty()));
            yOffsetProperty.bind(BindingsSL.doubleBinding(() -> -getModel().getOwner().getHeight() / 2, getModel().getOwner().yLocationProperty()));
//            widthProperty.bind(BindingsSL.doubleBinding(() -> getModel().getOwner().getWidth() * 1.5, getModel().getOwner().widthProperty()));
//            heightProperty.bind(BindingsSL.doubleBinding(() -> getModel().getOwner().getHeight() * 1.5, getModel().getOwner().heightProperty()));
        }
        
        return this;
    }
}
