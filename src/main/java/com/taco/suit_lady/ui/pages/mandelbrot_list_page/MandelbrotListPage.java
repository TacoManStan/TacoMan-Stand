package com.taco.suit_lady.ui.pages.mandelbrot_list_page;

import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;


public class MandelbrotListPage extends UIPage<MandelbrotListPageController> {
    
    public MandelbrotListPage(@NotNull UIBook owner, Object... constructorParams) {
        super(owner, constructorParams);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) {
    
    }
    
    @Override
    protected @NotNull Class<MandelbrotListPageController> controllerDefinition() {
        return MandelbrotListPageController.class;
    }
}
