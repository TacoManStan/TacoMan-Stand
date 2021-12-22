package com.taco.suit_lady.ui.pages.tester_page;

import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
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
