package com.taco.suit_lady.view.ui.ui_internal.contents_sl.dummy_instances;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.pages.example_page.ExamplePage;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;

public class SLDummyInstancesContent extends SLContent<SLDummyInstancesContentData, SLDummyInstancesContentController>
{
    public SLDummyInstancesContent(@NotNull Springable springable)
    {
        super(springable);
        
        initBookshelves();
    }
    
    private void initBookshelves()
    {
        FXTools.get().runFX(() -> {
            final Sidebar sidebar = ctx().getBean(AppUI.class).getSidebar();
    
            final Button testSidebarButton = new Button("Test");
            final SidebarBookshelf testSidebarBookshelf = new SidebarBookshelf(sidebar, testSidebarButton);
    
            testSidebarBookshelf.getBooks().add(new UIBook(
                    weaver(), ctx(),
                    "Repository Red", "invalid_button_id",
                    uiBook -> TB.resources().get(
                            "pages", uiBook.getUID(uiBook.getButtonID()),
                            () -> new ExamplePage(uiBook, "red")
                    ), null
            ));
        }, true);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull SLDummyInstancesContentData loadData()
    {
        return new SLDummyInstancesContentData();
    }
    
    @Override
    protected @NotNull Class<SLDummyInstancesContentController> controllerDefinition()
    {
        return SLDummyInstancesContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}
