package com.taco.suit_lady.view.ui.ui_internal.contents;

import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public class DummyInstance
        implements UIDProcessable, Springable
{
    private final Springable springable;
    
    private final DummyContentsInstanceUI ui;
    
    public DummyInstance(Springable springable)
    {
        this.springable = springable.asStrict();
        
        this.ui = new DummyContentsInstanceUI(this);
    }
    
    public DummyContentsInstanceUI ui()
    {
        return this.ui;
    }
    
    public boolean shutdown()
    {
        return ctx().getBean(DummyContentsHandler.class).shutdown(this);
    }
    
    protected final boolean shutdownInstanceEngine()
    {
        // CHANGE-HERE
        // This used to be where the thread executor system would be wrapped up and then shutdown when used in TRiBotFX.
        return true;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    private UIDProcessor uidProcessor;
    
    @Override
    public UIDProcessor getUIDProcessor()
    {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("dummy-instance");
        return uidProcessor;
    }
    
    @Override
    public FxWeaver weaver()
    {
        return springable.weaver();
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return springable.ctx();
    }
    
    //</editor-fold>
}
