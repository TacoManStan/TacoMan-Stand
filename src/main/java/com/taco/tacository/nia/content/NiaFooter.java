package com.taco.tacository.nia.content;

import com.taco.tacository.game.ui.GameFooterController;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.ui.Content;
import com.taco.tacository.ui.Footer;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines the {@link Footer} implementation assigned to the {@link GameViewContent Game} {@link Content}.</p>
 *
 * @see GameFooterController
 */
//TO-EXPAND
public class NiaFooter
        extends Footer<NiaFooter, NiaFooterController, NiaContent, NiaContentData, NiaContentController> {
    
    public NiaFooter(@NotNull NiaContent content, Object... constructorParams) {
        super(content, constructorParams);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void initializeFooter(@NotNull Object[] constructorParams) {
    }
    
    @Override protected void onContentChange(boolean shown) {
        super.onContentChange(shown); //TODO
    }
    
    @Override protected @NotNull Class<NiaFooterController> controllerDefinition() { return NiaFooterController.class; }
    
    //</editor-fold>
}
