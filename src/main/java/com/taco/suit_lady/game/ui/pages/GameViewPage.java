package com.taco.suit_lady.game.ui.pages;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class GameViewPage extends UIPage<GameViewPageController>
        implements GameComponent {
    
    private final GameViewContent content;
    
    public GameViewPage(@NotNull UIBook owner, @NotNull GameViewContent content, Object... constructorParams) {
        super(owner, constructorParams);
        this.content = content;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) { }
    @Override protected @NotNull Class<GameViewPageController> controllerDefinition() { return GameViewPageController.class; }
    
    //</editor-fold>
}
