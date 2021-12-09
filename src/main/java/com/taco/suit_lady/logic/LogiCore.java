package com.taco.suit_lady.logic;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.concurrent.Task;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class LogiCore
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ThreadPoolExecutor executor; // TODO - Implement both asynchronous and synchronous executor options
    
    public LogiCore(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.executor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
    
    //<editor-fold desc="--- EXECUTION ---">
    
    public void execute(@NotNull Runnable runnable)
    {
        executor.execute(ExceptionTools.nullCheck(runnable, "Runnable Task"));
    }
    
    public <T> void execute(@NotNull Task<T> task)
    {
        executor.execute(ExceptionTools.nullCheck(task, "Task"));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
}
