package com.taco.suit_lady.view.ui.ui_internal.contents_new.dummy_instances_content;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.contents_new.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.pages.example_page.ExamplePage;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;

public class DummyInstancesSLContent extends SLContent<DummyInstancesContentNewController>
{
    public DummyInstancesSLContent(@NotNull Springable springable)
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
    
    @Override
    protected void onSet() { }
    
    @Override
    protected void onRemoved() { }
    
    @Override
    protected @NotNull Class<DummyInstancesContentNewController> controllerDefinition()
    {
        return DummyInstancesContentNewController.class;
    }
}
