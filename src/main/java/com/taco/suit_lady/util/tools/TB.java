package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.tools.fxtools.FXTools;

public class TB // Stands for ToolBox
{
    //<editor-fold desc="--- STATIC INITIALIZATIONS ---">
    
    // --- EXTERNAL --- //
    
    private static final ResourceTools RESOURCE_TOOLS;
    private static final GeneralTools GENERAL_TOOLS;
    private static final FXTools FX_TOOLS;
    private static final StringTools STRING_TOOLS;
    private static final EnumTools ENUM_TOOLS;
    private static final CollectionTools COLLECTION_TOOLS;
    private static final WebTools WEB_TOOLS;
    private static final RandomTools RANDOM_TOOLS;
    
    static
    {
        // --- TOOL MODULES --- //
        
        RESOURCE_TOOLS = new ResourceTools();
        GENERAL_TOOLS = new GeneralTools();
        FX_TOOLS = new FXTools();
        STRING_TOOLS = new StringTools();
        ENUM_TOOLS = new EnumTools();
        COLLECTION_TOOLS = new CollectionTools();
        WEB_TOOLS = new WebTools();
        RANDOM_TOOLS = new RandomTools();
    }
    
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
    
    //</editor-fold>
}
