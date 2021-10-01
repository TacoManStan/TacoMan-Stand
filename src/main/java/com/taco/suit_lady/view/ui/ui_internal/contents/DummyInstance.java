package com.taco.suit_lady.view.ui.ui_internal.contents;

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
        return this.ui().shutdown();
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
