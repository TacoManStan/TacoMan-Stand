package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/content/test_content/test_content.fxml")
@Scope("prototype")
public class TestContentNewController extends Controller
{
    @FXML private AnchorPane root;
    
    public TestContentNewController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    public void initialize() { }
}
