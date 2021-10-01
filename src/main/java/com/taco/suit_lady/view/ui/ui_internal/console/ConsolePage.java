package com.taco.suit_lady.view.ui.ui_internal.console;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.ConsoleController;
import com.taco.util.quick.ConsoleBB;

public class ConsolePage extends UIPage<ConsoleController>
{
    public ConsolePage(UINode owner)
    {
        super(owner);
        
//        final ConsoleController pageController = this.ctx().getBean(ConsoleController.class);
        final ConsoleController pageController = this.weaver().loadController(ConsoleController.class);
        ConsoleBB.CONSOLE.print("Console Page Controller: " + pageController);
        pageController.setPage(this);
        setController(pageController);
//        pageController.initialize();
    }
    
    //<editor-fold desc="Classes">
    

    
    //</editor-fold>
}
