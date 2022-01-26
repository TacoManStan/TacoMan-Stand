package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class GameViewPage extends UIPage<GameViewPageController> {
    
    private final GameViewContent content;
    
    public GameViewPage(@NotNull UIBook owner, @NotNull GameViewContent content, Object... constructorParams) {
        super(owner, constructorParams);
        this.content = content;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public GameViewContent getContentView() { return content; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) { }
    @Override protected @NotNull Class<GameViewPageController> controllerDefinition() { return GameViewPageController.class; }
    
    //</editor-fold>
}
