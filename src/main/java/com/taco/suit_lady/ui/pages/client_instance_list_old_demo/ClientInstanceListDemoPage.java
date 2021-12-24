package com.taco.suit_lady.ui.pages.client_instance_list_old_demo;

import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;


public class ClientInstanceListDemoPage extends UIPage<ClientInstanceListDemoPageController> {
    
    public ClientInstanceListDemoPage(@NotNull UIBook owner, Object... constructorParams) {
        super(owner, constructorParams);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<ClientInstanceListDemoPageController> controllerDefinition() {
        return ClientInstanceListDemoPageController.class;
    }
}
