package com.taco.suit_lady.util.tools.list_tools;

public enum OpReturnType {
    
    /**
     * Indicates that a reference to the specified list object should be returned.
     */
    SELF,
    
    /**
     * Indicates that a reference to the previous list object occupying the element changed by the operation should be returned.
     */
    PRIOR,
    
    /**
     * Indicates that a reference to the default value for the list should be returned.
     */
    DEFAULT,
    
    /**
     * Indicates that null should always be returned.
     */
    NONE
}
