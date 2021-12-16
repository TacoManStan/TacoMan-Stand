package com.taco.suit_lady.view.ui.overlay.paint_commands;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainer;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.util.Bounds2D;
import com.taco.suit_lady.view.ui.overlay.Overlay;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class SLPaintCommand<N extends Node>
        implements Lockable, Springable, Nameable, Comparable<SLPaintCommand<?>>, ObservablePropertyContainer {
    
    private final ReentrantLock lock;
    private final StrictSpringable springable;
    private final String name;
    
    private final ReadOnlyObjectWrapper<Overlay> ownerProperty; // Try to decouple this if you can.
    private final ReadOnlyObjectWrapper<N> nodeProperty;
    
    private final Predicate<? super SLPaintCommand<N>> autoRemoveCondition;
    private final BooleanProperty activeProperty;
    private final IntegerProperty paintPriorityProperty;
    private final boolean scaleToParent;
    
    //
    
    private final IntegerProperty xProperty;
    private final IntegerProperty yProperty;
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    private final ObjectBinding<Bounds2D> boundsBinding;
    
    public SLPaintCommand(@Nullable ReentrantLock lock,
                          @NotNull Springable springable,
                          @NotNull String name,
                          @Nullable Predicate<? super SLPaintCommand<N>> autoRemoveCondition,
                          boolean scaleToParent,
                          int priority) {
        this.lock = lock;
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input").asStrict();
        this.name = name;
        
        this.ownerProperty = new ReadOnlyObjectWrapper<>();
        this.nodeProperty = new ReadOnlyObjectWrapper<>();
        
        this.autoRemoveCondition = autoRemoveCondition != null ? autoRemoveCondition : paintCommand -> false;
        this.activeProperty = new SimpleBooleanProperty(true);
        
        this.paintPriorityProperty = new SimpleIntegerProperty();
        
        this.scaleToParent = scaleToParent;
        
        this.xProperty = new SimpleIntegerProperty();
        this.yProperty = new SimpleIntegerProperty();
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        this.boundsBinding = createObjectBinding(() -> new Bounds2D(getX(), getY(), getWidth(), getHeight()));
        this.boundsBinding.addListener((observable, oldValue, newValue) -> nodeProperty.set(refreshNodeImpl()));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Overlay> ownerProperty() {
        return ownerProperty.getReadOnlyProperty();
    }
    
    public final Overlay getOwner() {
        return ownerProperty.get();
    }
    
    protected final ReadOnlyObjectWrapper<Overlay> ownerPropertyImpl() {
        return ownerProperty;
    }
    
    public final Overlay setOwner(Overlay overlay) {
        return sync(() -> {
            Overlay oldOverlay = getOwner();
            ownerProperty.set(overlay);
            return oldOverlay;
        });
    }
    
    protected final ReadOnlyObjectProperty<N> nodeProperty() {
        return nodeProperty.getReadOnlyProperty();
    }
    
    public final N getNode() {
        //        System.out.println("get node...");
        return sync(() -> {
            if (nodeProperty.get() != null)
                return nodeProperty.get();
            nodeProperty.set(refreshNodeImpl());
            return getNode();
        });
    }
    
    protected final Predicate<? super SLPaintCommand<N>> getAutoRemoveCondition() {
        return autoRemoveCondition;
    }
    
    public final BooleanProperty activeProperty() {
        return activeProperty;
    }
    
    public final SLPaintCommand<N> activate() {
        activeProperty.set(true);
        return this;
    }
    
    public final SLPaintCommand<N> deactivate() {
        activeProperty.set(false);
        return this;
    }
    
    public final boolean isScaleToParent() {
        return scaleToParent;
    }
    
    public final IntegerProperty paintPriorityProperty() {
        return paintPriorityProperty;
    }
    
    public final int getPaintPriority() {
        return paintPriorityProperty.get();
    }
    
    //
    
    public final IntegerProperty xProperty() {
        return xProperty;
    }
    
    public final int getX() {
        return xProperty.get();
    }
    
    public final void setX(int x) {
        xProperty.set(x);
    }
    
    //
    
    public final IntegerProperty yProperty() {
        return yProperty;
    }
    
    public final int getY() {
        return yProperty.get();
    }
    
    public final void setY(int y) {
        yProperty.set(y);
    }
    
    //
    
    public final IntegerProperty widthProperty() {
        return widthProperty;
    }
    
    public final int getWidth() {
        return widthProperty.get();
    }
    
    public final int getWidthSafe() {
        return getWidth() > 0 ? getWidth() : 1;
    }
    
    public final void setWidth(int width) {
        widthProperty.set(width);
    }
    
    //
    
    public final IntegerProperty heightProperty() {
        return heightProperty;
    }
    
    public final int getHeight() {
        return heightProperty.get();
    }
    
    public final int getHeightSafe() {
        return getHeight() > 0 ? getHeight() : 1;
    }
    
    public final void setHeight(int height) {
        heightProperty.set(height);
    }
    
    //
    
    public final void setBounds(@NotNull Bounds2D bounds) {
        ExceptionTools.nullCheck(bounds, "Bounds");
        
        setX(bounds.x());
        setY(bounds.y());
        setWidth(bounds.width());
        setHeight(bounds.height());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- BINDINGS ---">
    
    public final ObjectBinding<Bounds2D> boundsBinding() {
        return boundsBinding;
    }
    
    public final Bounds2D getBounds() {
        return boundsBinding.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract N refreshNode();
    
    protected void applyRefresh(@NotNull N n) {
        //        System.out.println("Applying refresh settings...");
        n.setManaged(false);
        n.visibleProperty().bind(activeProperty);
        
        syncBounds(n, getBounds());
    }
    
    protected void syncBounds(@NotNull N n, @NotNull Bounds2D newBounds) {
        getNode().resizeRelocate(getX(), getY(), getWidthSafe(), getHeightSafe());
    }
    
    protected abstract void onAdded(@NotNull Overlay owner);
    
    protected abstract void onRemoved(@NotNull Overlay owner);
    
    //
    
    protected ObservableValue<?>[] regenerateTriggers() {
        return new ObservableValue<?>[]{boundsBinding};
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull Lock getLock() {
        return lock != null ? lock : new ReentrantLock();
    }
    
    @Override
    public final String getName() {
        return name;
    }
    
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public Observable[] properties() {
        return new Observable[]{ownerProperty, nodeProperty, paintPriorityProperty, xProperty, yProperty, widthProperty, heightProperty};
    }
    
    @Override
    public int compareTo(@NotNull SLPaintCommand<?> o) {
        return Integer.compare((Math.abs(getPaintPriority())), Math.abs(o.getPaintPriority()));
    }
    
    //</editor-fold>
    
    private N refreshNodeImpl() {
        debugger().print("Refreshing Node...");
        debugger().warn("Refreshing Node, but as a warning...");
        
        final N n = refreshNode();
        applyRefresh(n);
        return n;
    }
}
