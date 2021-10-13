package com.taco.suit_lady.view.ui.ui_internal.contents;

import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;

public class DummyInstance
        implements UIDProcessable
{
    private final DummyContentsInstanceUI ui;
    
    public DummyInstance()
    {
        this.ui = new DummyContentsInstanceUI(this);
    }
    
    public DummyContentsInstanceUI ui()
    {
        return this.ui;
    }
    
    public boolean shutdown()
    {
        return TB.handler().shutdown(this);
    }
    
    protected final void shutdownInstanceEngine()
    {
        // CHANGE-HERE
        // This used to be where the thread executor system would be wrapped up and then shutdown when used in TRiBotFX.
    }
    
    private UIDProcessor uidProcessor;
    
    @Override
    public UIDProcessor getUIDProcessor()
    {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("dummy-instance");
        return uidProcessor;
    }
}
