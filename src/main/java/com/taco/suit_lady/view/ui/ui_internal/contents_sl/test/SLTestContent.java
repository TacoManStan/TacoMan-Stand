package com.taco.suit_lady.view.ui.ui_internal.contents_sl.test;

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

public class SLTestContent extends SLContent<SLTestContentData, SLTestContentController>
{
    public SLTestContent(@NotNull Springable springable)
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
            testSidebarBookshelf.getBooks().add(new UIBook(
                    weaver(), ctx(),
                    "Repository Blue", "invalid_button_id",
                    uiBook -> TB.resources().get(
                            "pages", uiBook.getUID(uiBook.getButtonID()),
                            () -> new ExamplePage(uiBook, "blue")
                    ), null
            ));
            testSidebarBookshelf.getBooks().add(new UIBook(
                    weaver(), ctx(),
                    "Repository Green", "invalid_button_id",
                    uiBook -> TB.resources().get(
                            "pages", uiBook.getUID(uiBook.getButtonID()),
                            () -> new ExamplePage(uiBook, "green")
                    ), null
            ));
            
            testSidebarBookshelf.getButtonGroup().selectFirst();
            sidebar.bookshelvesProperty().add(testSidebarBookshelf);
        }, true);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull SLTestContentData loadData()
    {
        return new SLTestContentData();
    }
    
    @Override
    protected @NotNull Class<SLTestContentController> controllerDefinition()
    {
        return SLTestContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}