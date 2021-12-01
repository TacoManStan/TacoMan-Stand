package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.pages.ExamplePage;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;

public class TestControllableContentNew extends ControllableContentNew<TestContentNewController>
{
    public TestControllableContentNew(@NotNull Springable springable)
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
                    "Repository Red", "repository",
                    uiBook -> TB.resources().get(
                            "pages", uiBook.getButtonID() + "_red",
                            () -> new ExamplePage(uiBook, "red")
                    ), null
            ));
            testSidebarBookshelf.getBooks().add(new UIBook(
                    weaver(), ctx(),
                    "Repository Blue", "repository",
                    uiBook -> TB.resources().get(
                            "pages", uiBook.getButtonID() + "_blue",
                            () -> new ExamplePage(uiBook, "blue")
                    ), null
            ));
            testSidebarBookshelf.getBooks().add(new UIBook(
                    weaver(), ctx(),
                    "Repository Green", "repository",
                    uiBook -> TB.resources().get(
                            "pages", uiBook.getButtonID() + "_green",
                            () -> new ExamplePage(uiBook, "green")
                    ), null
            ));
    
            testSidebarBookshelf.getButtonGroup().selectFirst();
            ArrayTools.addToFront(sidebar.bookshelvesProperty(), testSidebarBookshelf);
        }, true);
    }
    
    @Override
    protected void onSet() { }
    
    @Override
    protected void onRemoved() { }
    
    @Override
    protected @NotNull Class<TestContentNewController> controllerDefinition()
    {
        return TestContentNewController.class;
    }
}
