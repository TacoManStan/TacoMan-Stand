package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.view.ui.ui_internal.pages.FallbackPage;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/dummy_sidebar.fxml")
@Scope("prototype")
public class DummySidebarController extends UIPageController<FallbackPage>
{
    @FXML private BorderPane root;
    
    public DummySidebarController(FxWeaver weaver, ConfigurableApplicationContext ctx)
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