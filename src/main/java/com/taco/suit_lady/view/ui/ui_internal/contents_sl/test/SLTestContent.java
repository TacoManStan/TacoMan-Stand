package com.taco.suit_lady.view.ui.ui_internal.contents_sl.test;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.pages.example_page.ExamplePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SLTestContent extends SLContent<SLTestContentData, SLTestContentController>
{
    public SLTestContent(@NotNull Springable springable)
    {
        super(springable);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @Nullable List<SidebarBookshelf> loadBookshelves()
    {
        final UIBook[] books = new UIBook[]{
                new UIBook(weaver(), ctx(),
                           "Repository Red",
                           "invalid_button_id",
                           uiBook -> TB.resources().get(
                                   "pages",
                                   uiBook.getUID(uiBook.getButtonID()),
                                   () -> new ExamplePage(uiBook, "red")),
                           null),
                new UIBook(weaver(), ctx(),
                           "Repository Blue",
                           "invalid_button_id",
                           uiBook -> TB.resources().get(
                                   "pages",
                                   uiBook.getUID(uiBook.getButtonID()),
                                   () -> new ExamplePage(uiBook, "blue")),
                           null),
                new UIBook(weaver(), ctx(),
                           "Repository Green",
                           "invalid_button_id",
                           uiBook -> TB.resources().get(
                                   "pages",
                                   uiBook.getUID(uiBook.getButtonID()),
                                   () -> new ExamplePage(uiBook, "green")),
                           null)};
        
        final ArrayList<SidebarBookshelf> bookshelves = new ArrayList<>();
        bookshelves.add(injectBookshelf("Test", books));
        
        return bookshelves;
    }
    
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
