package com.taco.suit_lady.view.ui.ui_internal.contents_sl.test_content;

import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentController;
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
public class SLTestContentController extends SLContentController<SLTestContent>
{
    @FXML private AnchorPane root;
    
    public SLTestContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
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
