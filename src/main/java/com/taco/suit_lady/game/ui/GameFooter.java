package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.ui.Footer;
import org.jetbrains.annotations.NotNull;

public class GameFooter
        extends Footer<GameFooter, GameFooterController, GameViewContent, GameViewContentData, GameViewContentController> {
    
    public GameFooter(@NotNull GameViewContent content, Object... constructorParams) {
        super(content, constructorParams);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) {
    
    }
    @Override protected @NotNull Class<GameFooterController> controllerDefinition() {
        return null;
    }
    
    //</editor-fold>
}
