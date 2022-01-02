package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.ui.jfx.util.Bounds2D;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.tacository.obj_traits.common.Nameable;
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
        implements Lockable, Springable, Nameable, Comparable<SLPaintCommand<?>>, ObservablePropertyContainable {
    
    private final ReentrantLock lock;
    private final StrictSpringable springable;
    private final String name;
    
    private final ReadOnlyObjectWrapper<Overlay> ownerProperty; // Try to decouple this if you can.
    private final ReadOnlyObjectWrapper<N> nodeProperty;
    
    private final Predicate<? super SLPaintCommand<N>> autoRemoveCondition;
    private final BooleanProperty activeProperty;
    private final IntegerProperty paintPriorityProperty;
    
    
    private final IntegerProperty xProperty;
    private final IntegerProperty yProperty;
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    private final ObjectBinding<Bounds2D> boundsBinding;
    
    public SLPaintCommand(@NotNull Springable springable, @NotNull String name, int priority) {
        this(null, springable, name, null, priority);
    }
    
    public SLPaintCommand(@NotNull Springable springable, @NotNull String name, @Nullable Predicate<? super SLPaintCommand<N>> autoRemoveCondition, int priority) {
        this(null, springable, name, autoRemoveCondition, priority);
    }
    
    public SLPaintCommand(@Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name, int priority) {
        this(lock, springable, name, null, priority);
    }
    
    public SLPaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<N>> autoRemoveCondition, int priority) {
        this.lock = lock;
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input").asStrict();
        this.name = name;
        
        this.ownerProperty = new ReadOnlyObjectWrapper<>();
        this.nodeProperty = new ReadOnlyObjectWrapper<>();
        
        this.autoRemoveCondition = autoRemoveCondition != null ? autoRemoveCondition : paintCommand -> false;
        this.activeProperty = new SimpleBooleanProperty(true);
        
        this.paintPriorityProperty = new SimpleIntegerProperty(priority);
        
        this.xProperty = new SimpleIntegerProperty();
        this.yProperty = new SimpleIntegerProperty();
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        
        this.boundsBinding = createObjectBinding(() -> new Bounds2D(getX(), getY(), getWidth(), getHeight()));
        this.boundsBinding.addListener((observable, oldValue, newValue) -> nodeProperty.set(refreshNodeImpl()));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReadOnlyObjectProperty<Overlay> ownerProperty() {
        return ownerProperty.getReadOnlyProperty();
    }
    
    public final @Nullable Overlay getOwner() {
        return ownerProperty.get();
    }
    
    protected final @NotNull ReadOnlyObjectWrapper<Overlay> ownerPropertyImpl() {
        return ownerProperty;
    }
    
    public final @Nullable Overlay setOwner(@Nullable Overlay overlay) {
        return sync(() -> {
            Overlay oldOverlay = getOwner();
            ownerProperty.set(overlay);
            return oldOverlay;
        });
    }
    
    
    protected final @NotNull ReadOnlyObjectProperty<N> nodeProperty() {
        return nodeProperty.getReadOnlyProperty();
    }
    
    public final @NotNull N getNode() {
        //        System.out.println("get node...");
        return sync(() -> {
            if (nodeProperty.get() != null)
                return nodeProperty.get();
            nodeProperty.set(refreshNodeImpl());
            return getNode();
        });
    }
    
    
    protected final @NotNull Predicate<? super SLPaintCommand<N>> getAutoRemoveCondition() {
        return autoRemoveCondition;
    }
    
    
    public final @NotNull BooleanProperty activeProperty() {
        return activeProperty;
    }
    
    public final @NotNull SLPaintCommand<N> activate() {
        activeProperty.set(true);
        return this;
    }
    
    public final @NotNull SLPaintCommand<N> deactivate() {
        activeProperty.set(false);
        return this;
    }
    
    
    public final @NotNull IntegerProperty paintPriorityProperty() {
        return paintPriorityProperty;
    }
    
    public final int getPaintPriority() {
        return paintPriorityProperty.get();
    }
    
    //
    
    public final @NotNull IntegerProperty xProperty() {
        return xProperty;
    }
    
    public final int getX() {
        return xProperty.get();
    }
    
    public final void setX(double x) {
        xProperty.set((int) x);
    }
    
    
    public final @NotNull IntegerProperty yProperty() {
        return yProperty;
    }
    
    public final int getY() {
        return yProperty.get();
    }
    
    public final void setY(double y) {
        yProperty.set((int) y);
    }
    
    
    public final @NotNull IntegerProperty widthProperty() {
        return widthProperty;
    }
    
    public final int getWidth() {
        return widthProperty.get();
    }
    
    public final int getWidthSafe() {
        return getWidth() > 0 ? getWidth() : 1;
    }
    
    public final void setWidth(double width) {
        widthProperty.set((int) width);
    }
    
    
    public final @NotNull IntegerProperty heightProperty() {
        return heightProperty;
    }
    
    public final int getHeight() {
        return heightProperty.get();
    }
    
    public final int getHeightSafe() {
        return getHeight() > 0 ? getHeight() : 1;
    }
    
    public final void setHeight(double height) {
        heightProperty.set((int) height);
    }
    
    
    public final void setBounds(@NotNull Bounds2D bounds) {
        ExceptionTools.nullCheck(bounds, "Bounds");
        setBounds(bounds.x(), bounds.y(), bounds.width(), bounds.height());
    }
    
    public final void setBounds(double x, double y, double width, double height) {
        sync(() -> {
            setX(x);
            setY(y);
            setWidth(width);
            setHeight(height);
        });
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
        n.setManaged(false);
        n.visibleProperty().bind(activeProperty);
        
        sync(() -> syncBounds(n));
    }
    
    protected void syncBounds(@NotNull N n) {
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
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public final String getName() {
        return name;
    }
    
    
    @Override
    public Observable[] properties() {
        return new Observable[]{ownerProperty, nodeProperty, paintPriorityProperty, xProperty, yProperty, widthProperty, heightProperty};
    }
    
    //
    
    @Override
    public int compareTo(@NotNull SLPaintCommand<?> o) {
        return Integer.compare((Math.abs(getPaintPriority())), Math.abs(o.getPaintPriority()));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private N refreshNodeImpl() {
        final N n = refreshNode();
        applyRefresh(n);
        return n;
    }
    
    // CURRENTLY UNUSED
    private boolean autoRemove() {
        return sync(() -> {
            final Overlay overlay = getOwner();
            if (overlay != null && autoRemoveCondition.test(this))
                return overlay.removePaintCommand(this);
            return false;
        });
    }
    
    //</editor-fold>
}
