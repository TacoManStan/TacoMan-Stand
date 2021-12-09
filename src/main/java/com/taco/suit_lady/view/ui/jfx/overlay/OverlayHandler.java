package com.taco.suit_lady.view.ui.jfx.overlay;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OverlayHandler
        implements Springable, Lockable
{
    private final Springable springable;
    private final ReentrantLock lock;
    
    public OverlayHandler(@NotNull Springable springable, @Nullable ReentrantLock lock)
    {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input").asStrict();
        this.lock = lock;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return springable.ctx();
    }
    
    @Override
    public @NotNull Lock getLock()
    {
        return lock != null ? lock : new ReentrantLock();
    }
    
    //</editor-fold>
}
