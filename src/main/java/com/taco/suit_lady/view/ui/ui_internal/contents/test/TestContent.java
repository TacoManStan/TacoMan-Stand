package com.taco.suit_lady.view.ui.ui_internal.contents.test;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.ui_internal.Content;
import com.taco.suit_lady.view.ui.ui_internal.pages.example_page.ExamplePage;
import org.jetbrains.annotations.NotNull;

public class TestContent extends Content<TestContentData, TestContentController>
{
    public TestContent(@NotNull Springable springable)
    {
        super(springable);
    
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
        injectBookshelf("Demo", books);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull TestContentData loadData()
    {
        return new TestContentData();
    }
    
    @Override
    protected @NotNull Class<TestContentController> controllerDefinition()
    {
        return TestContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}