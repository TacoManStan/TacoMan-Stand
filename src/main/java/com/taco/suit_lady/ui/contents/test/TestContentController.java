package com.taco.suit_lady.ui.contents.test;

import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.ui.ContentController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/content/test/test_content.fxml")
@Scope("prototype")
public class TestContentController extends ContentController<TestContent, TestContentData, TestContentController>
{
    @FXML private AnchorPane root;
    
    public TestContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    @Override public AnchorPane getContentPane() {
        return root;
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    public void initialize() { super.initialize(); }
    
    
    private TaskManager<TestContentController> taskManager;
    @Override public @NotNull TaskManager<TestContentController> taskManager() {
        if (taskManager == null)
            taskManager = new TaskManager<>(this).init();
        return taskManager;
    }
}
