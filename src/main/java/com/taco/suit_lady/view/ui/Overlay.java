package com.taco.suit_lady.view.ui;

import com.taco.suit_lady._to_sort._new.ReadOnlyObservableList;
import com.taco.suit_lady._to_sort._new.ReadOnlyObservableListWrapper;
import com.taco.suit_lady._to_sort._new.interfaces.ReadOnlyNameableProperty;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.list_tools.ListTools;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Overlay
        implements Springable, Lockable, ReadOnlyNameableProperty, Comparable<Overlay> {
    
    private final Springable springable;
    private final ReentrantLock lock;
    private final ReadOnlyStringWrapper nameProperty;
    
    private final StackPane root;
    
    private final ReadOnlyIntegerWrapper paintPriorityProperty;
    
    private final ReadOnlyObservableListWrapper<SLPaintCommand<Node>> paintCommands;
    
    public Overlay(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name, int paintPriority) {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input").asStrict();
        this.lock = lock; // Null-checking is done in get method via lazy instantiation
        this.nameProperty = new ReadOnlyStringWrapper(name);
        
        this.root = new StackPane();
        
        this.paintPriorityProperty = new ReadOnlyIntegerWrapper(paintPriority);
        
        this.paintCommands = new ReadOnlyObservableListWrapper<>();
        
        ListTools.applyListener(lock, paintCommands, (op1, op2, opType, triggerType) -> {
        
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final StackPane root() {
        return root;
    }
    
    //
    
    public final ReadOnlyIntegerProperty paintPriorityProperty() {
        return paintPriorityProperty.getReadOnlyProperty();
    }
    
    public final int getPaintPriority() {
        return paintPriorityProperty.get();
    }
    
    public final void setPaintPriority(int paintPriority) {
        if (paintPriority < 0)
            throw ExceptionTools.ex(new IndexOutOfBoundsException("Paint Priority Must Be Non-Negative! [" + paintPriority + "]"));
        paintPriorityProperty.set(paintPriority);
    }
    
    //
    
    public final ReadOnlyObservableList<SLPaintCommand<Node>> paintCommands() {
        return paintCommands.readOnlyList();
    }
    
    public final void addPaintCommand(@NotNull SLPaintCommand<Node> paintCommand) {
        paintCommands.add(ExceptionTools.nullCheck(paintCommand, "Paint Command Input"));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public final @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    @Override
    public final @NotNull Lock getLock() {
        return lock != null ? lock : new ReentrantLock();
    }
    
    @Override
    public final ReadOnlyStringProperty nameProperty() {
        return nameProperty.getReadOnlyProperty();
    }
    
    //
    
    @Override
    public int compareTo(@NotNull Overlay o) {
        return Integer.compare((Math.abs(getPaintPriority())), Math.abs(o.getPaintPriority()));
    }
    
    //</editor-fold>
}
