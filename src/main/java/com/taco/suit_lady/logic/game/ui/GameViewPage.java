package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class GameViewPage extends UIPage<GameViewPageController>
{
    private final GameViewContent slContent;
    
    public GameViewPage(@NotNull UIBook owner, @NotNull GameViewContent slContent, Object... constructorParams)
    {
        super(owner, constructorParams);
        
        this.slContent = slContent;
    }
    
    public GameViewContent getSLContent()
    {
        return slContent;
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) {
    
    }
    
    @Override
    protected @NotNull Class<GameViewPageController> controllerDefinition()
    {
        return GameViewPageController.class;
    }
}
