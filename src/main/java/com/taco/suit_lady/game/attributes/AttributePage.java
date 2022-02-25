package com.taco.suit_lady.game.attributes;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class AttributePage extends UIPage<AttributePageController>
        implements GameComponent {
    
    private final GameViewContent content;
    
    public AttributePage(@NotNull UIBook owner, @NotNull GameViewContent content, Object... constructorParams) {
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
    @Override protected @NotNull Class<AttributePageController> controllerDefinition() { return AttributePageController.class; }
    
    //</editor-fold>
}
