package com.taco.suit_lady.view.ui;

import com.taco.suit_lady._to_sort._new.interfaces.NameableProperty;
import com.taco.suit_lady._to_sort._new.interfaces.ReadOnlyNameableProperty;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.property.*;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Overlay
        implements Springable, Lockable, ReadOnlyNameableProperty
{
    private final Springable springable;
    private final ReentrantLock lock;
    private final ReadOnlyStringWrapper nameProperty;
    
    private final ReadOnlyIntegerWrapper paintPriorityProperty;
    
    
    public Overlay(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name, int paintPriority)
    {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input").asStrict();
        this.lock = lock;
        this.nameProperty = new ReadOnlyStringWrapper();
        
        this.paintPriorityProperty = new ReadOnlyIntegerWrapper(0);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyIntegerProperty paintPriorityProperty()
    {
        return paintPriorityProperty.getReadOnlyProperty();
    }
    
    public final int getPaintPriority()
    {
        return paintPriorityProperty.get();
    }
    
    public final void setPaintPriority(int paintPriority)
    {
        if (paintPriority < 0)
            throw ExceptionTools.ex(new IndexOutOfBoundsException("Paint Priority Must Be Non-Negative! [" + paintPriority + "]"));
        paintPriorityProperty.set(paintPriority);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull FxWeaver weaver()
    {
        return springable.weaver();
    }
    
    @Override
    public final @NotNull ConfigurableApplicationContext ctx()
    {
        return springable.ctx();
    }
    
    @Override
    public final @NotNull Lock getLock()
    {
        return lock != null ? lock : new ReentrantLock();
    }
    
    @Override
    public final ReadOnlyStringProperty nameProperty()
    {
        return nameProperty.getReadOnlyProperty();
    }
    
    //</editor-fold>
}
