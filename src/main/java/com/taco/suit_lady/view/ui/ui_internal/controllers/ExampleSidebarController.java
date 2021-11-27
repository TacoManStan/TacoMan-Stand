package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.view.ui.ui_internal.pages.ExamplePage;
import com.taco.util.quick.ConsoleBB;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/example_sidebar.fxml")
@Scope("prototype")
public class ExampleSidebarController extends SidebarNodeGroupController<ExamplePage>
{
    @FXML private BorderPane root;
    
    public ExampleSidebarController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
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
        ConsoleBB.CONSOLE.print("Setting Background Color To..." + getPage().getColor());
        root().setStyle("-fx-background-color: " + getPage().getColor());
    }
}