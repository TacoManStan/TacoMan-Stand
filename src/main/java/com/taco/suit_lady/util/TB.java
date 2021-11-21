package com.taco.suit_lady.util;

import com.taco.suit_lady.view.ui.console.Console;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.contents.DummyContentsHandler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TB // Stands for ToolBox
{
    //<editor-fold desc="--- STATIC INITIALIZATIONS ---">
    
    // --- EXTERNAL --- //
    
    private static final ResourceTools RESOURCE_TOOLS;
    private static final GeneralTools GENERAL_TOOLS;
    private static final FXTools FX_TOOLS;
    private static final BindingTools BINDING_TOOLS;
    private static final StringTools STRING_TOOLS;
    private static final EnumTools ENUM_TOOLS;
    private static final CollectionTools COLLECTION_TOOLS;
    private static final WebTools WEB_TOOLS;
    private static final RandomTools RANDOM_TOOLS;
    
    // --- OTHER SINGLETON OBJECTS --- //
    
    private static final ThreadPoolExecutor EXECUTOR;
    private static final AppUI APP_UI;
    private static final Console CONSOLE;
    private static final DummyContentsHandler DUMMY_CONTENTS_HANDLER; // TODO - Not sure where this should go
    
    //
    
    static
    {
        // --- TOOL MODULES --- //
        
        RESOURCE_TOOLS = new ResourceTools();
        GENERAL_TOOLS = new GeneralTools();
        FX_TOOLS = new FXTools();
        BINDING_TOOLS = new BindingTools();
        STRING_TOOLS = new StringTools();
        ENUM_TOOLS = new EnumTools();
        COLLECTION_TOOLS = new CollectionTools();
        WEB_TOOLS = new WebTools();
        RANDOM_TOOLS = new RandomTools();
    
        // --- OTHER SINGLETON OBJECTS --- //
        
        EXECUTOR = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        APP_UI = new AppUI();
        CONSOLE = new Console();
        DUMMY_CONTENTS_HANDLER = new DummyContentsHandler();
    }
    
    //
    
    // --- TOOL MODULES --- //
    
    public static ResourceTools resources()
    {
        return RESOURCE_TOOLS;
    }
    
    public static GeneralTools general()
    {
        return GENERAL_TOOLS;
    }
    
    public static FXTools fx()
    {
        return FX_TOOLS;
    }
    
    public static BindingTools bindings()
    {
        return BINDING_TOOLS;
    }
    
    public static StringTools strings()
    {
        return STRING_TOOLS;
    }
    
    public static EnumTools enums()
    {
        return ENUM_TOOLS;
    }
    
    public static CollectionTools collections()
    {
        return COLLECTION_TOOLS;
    }
    
    public static WebTools web()
    {
        return WEB_TOOLS;
    }
    
    public static RandomTools random()
    {
        return RANDOM_TOOLS;
    }
    
    // --- OTHER SINGLETON OBJECTS --- //
    
    public static ThreadPoolExecutor executor()
    {
        return EXECUTOR;
    }
    
    public static AppUI ui()
    {
        return APP_UI;
    }
    
    public static Console console()
    {
        return CONSOLE;
    }
    
    public static DummyContentsHandler handler()
    {
        return DUMMY_CONTENTS_HANDLER;
    }
    
    //</editor-fold>
    
    //
    

}
