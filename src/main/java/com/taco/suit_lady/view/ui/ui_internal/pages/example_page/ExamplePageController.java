package com.taco.suit_lady.view.ui.ui_internal.pages.example_page;

import com.taco.suit_lady.view.ui.ui_internal.controllers.SidebarNodeGroupController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/pages/example_page/example_page.fxml")
@Scope("prototype")
public class ExamplePageController extends SidebarNodeGroupController<ExamplePage>
{
    @FXML private BorderPane root;
    
    public ExamplePageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    @FXML public void initialize() { }
    
    @Override
    protected void onPageBindingComplete()
    {
        root().setStyle("-fx-background-color: " + getPage().getColor());
    }
}