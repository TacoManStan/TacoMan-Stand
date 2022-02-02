package com.taco.suit_lady.ui.contents.test;

import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.pages.example_page.ExamplePage;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ResourcesSL;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;

public class TestContent extends Content<TestContent, TestContentData, TestContentController>
{
    public TestContent(@NotNull Springable springable)
    {
        super(springable);
    
        
        final UIBook[] books = new UIBook[]{
                new UIBook(this,
                           "Repository Red",
                           "invalid_button_id",
                           uiBook -> ResourcesSL.get(
                                   "pages",
                                   uiBook.getUID(uiBook.getButtonID()),
                                   () -> new ExamplePage(uiBook, "red")),
                           null),
                new UIBook(this,
                           "Repository Blue",
                           "invalid_button_id",
                           uiBook -> ResourcesSL.get(
                                   "pages",
                                   uiBook.getUID(uiBook.getButtonID()),
                                   () -> new ExamplePage(uiBook, "blue")),
                           null),
                new UIBook(this,
                           "Repository Green",
                           "invalid_button_id",
                           uiBook -> ResourcesSL.get(
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
    
    @Override protected boolean handleKeyEvent(@NotNull KeyEvent keyEvent) { return false; }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    //</editor-fold>
}
