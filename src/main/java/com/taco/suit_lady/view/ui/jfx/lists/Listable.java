package com.taco.suit_lady.view.ui.jfx.lists;

public interface Listable {
    
    String getShortText();
    String getLongText();
    
    // Default
    
    default String[] getSearchKeys() {
        return new String[]{getShortText(), getLongText()};
    }
}