package com.taco.tacository.ui.pages.fallback_page;

import com.taco.tacository.ui.UIPageController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/pages/fallback_page/fallback_page.fxml")
@Scope("prototype")
public class FallbackPageController extends UIPageController<FallbackPage>
{
    @FXML private BorderPane root;
    
    public FallbackPageController(FxWeaver weaver, ConfigurableApplicationContext ctx)
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