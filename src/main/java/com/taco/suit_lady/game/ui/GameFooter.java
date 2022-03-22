package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.Footer;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines the {@link Footer} implementation assigned to the {@link GameViewContent Game} {@link Content}.</p>
 *
 * @see GameFooterController
 */
//TO-EXPAND
public class GameFooter
        extends Footer<GameFooter, GameFooterController, GameViewContent, GameViewContentData, GameViewContentController> {
    
    public GameFooter(@NotNull GameViewContent content, Object... constructorParams) {
        super(content, constructorParams);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void initializeFooter(@NotNull Object[] constructorParams) {
    }
    
    @Override protected void onContentChange(boolean shown) {
        super.onContentChange(shown); //TODO
    }
    
    @Override protected @NotNull Class<GameFooterController> controllerDefinition() { return GameFooterController.class; }
    
    //</editor-fold>
}
