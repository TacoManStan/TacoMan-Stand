package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
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

public class OverlayCommand<N extends Node>
        implements Lockable, Springable, Nameable, ObservablePropertyContainable, Paintable {
    
    private final ReentrantLock lock;
    private final StrictSpringable springable;
    private final String name;
    
    private final ReadOnlyObjectWrapper<Overlay> ownerProperty; // Try to decouple this if you can.
    private final ReadOnlyObjectWrapper<N> nodeProperty;
    
    private final Predicate<? super OverlayCommand<N>> autoRemoveCondition;
    private final BooleanProperty activeProperty;
    private final IntegerProperty paintPriorityProperty;
    
    
    private final IntegerProperty xProperty;
    private final IntegerProperty yProperty;
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    private final ObjectBinding<Bounds> boundsBinding;
    
    public OverlayCommand(@NotNull Springable springable, @NotNull String name, int priority) {
        this(null, springable, name, null, priority);
    }
    
    public OverlayCommand(@NotNull Springable springable, @NotNull String name, @Nullable Predicate<? super OverlayCommand<N>> autoRemoveCondition, int priority) {
        this(null, springable, name, autoRemoveCondition, priority);
    }
    
    public OverlayCommand(@Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name, int priority) {
        this(lock, springable, name, null, priority);
    }
    
    public OverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<N>> autoRemoveCondition, int priority) {
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
        
        
        this.boundsBinding = createObjectBinding(() -> new Bounds(getX(), getY(), getWidth(), getHeight()));
        this.boundsBinding.addListener((observable, oldValue, newValue) -> nodeProperty.set(refreshNodeImpl()));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
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
    
    //</editor-fold>
    
    //<editor-fold desc="--- BINDINGS ---">
    
    @Override public ObjectProperty<PaintableCanvas> ownerProperty() {
        return null;
    }
    @Override public ObjectProperty<Predicate<PaintableCanvas>> autoRemoveConditionProperty() {
        return null;
    }
    @Override public BooleanProperty disabledProperty() {
        return null;
    }
    @Override public IntegerProperty paintPriorityProperty() {
        return null;
    }
    @Override public BoundsBinding boundsBinding() {
        return null;
    }
    @Override public void onAdd(PaintableCanvas owner) {
    
    }
    @Override public void onRemove(PaintableCanvas owner) {
    
    }
    @Override public void paint() {
    
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected void applyRefresh(@NotNull N n) {
        n.setManaged(false);
        n.visibleProperty().bind(activeProperty);
        
        sync(() -> syncBounds(n));
    }
    
    protected void syncBounds(@NotNull N n) {
        getNode().resizeRelocate(getX(), getY(), getWidth(), getHeight());
    }
    
    //
    
    protected ObservableValue<?>[] regenerateTriggers() {
        return new ObservableValue<?>[]{boundsBinding};
    } //wtf is this supposed to do
    
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
    

//    public int compareTo(@NotNull OverlayCommand<?> o) {
//        return Integer.compare((Math.abs(getPaintPriority())), Math.abs(o.getPaintPriority()));
//    }
    
    //
    
    
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private N refreshNodeImpl() {
        return null;
//        final N n = refreshNode();
//        applyRefresh(n);
//        return n;
    }
    
    // CURRENTLY UNUSED
    private boolean autoRemove() {
        return false;
//        return sync(() -> {
//            final Overlay overlay = getOwner();
//            if (overlay != null && autoRemoveCondition.test(this))
//                return overlay.removePaintable(this);
//            return false;
//        });
    }
    
    //</editor-fold>
}
