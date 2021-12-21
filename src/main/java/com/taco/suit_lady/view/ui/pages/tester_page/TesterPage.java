package com.taco.suit_lady.view.ui.pages.tester_page;

import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class TesterPage extends UIPage<TesterPageController> {
    
    public TesterPage(UIBook owner) {
        super(owner);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) {
    
    }
    
    @Override
    protected @NotNull Class<TesterPageController> controllerDefinition() {
        return TesterPageController.class;
    }
}
