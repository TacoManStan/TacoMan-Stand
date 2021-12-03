package com.taco.suit_lady.view.ui.ui_internal.contents_sl.dummy_instances_content;

import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/content/dummy_instances_content/dummy_instances_content.fxml")
@Scope("prototype")
public class SLDummyInstancesContentController extends SLContentController<SLDummyInstancesContent>
{
    @FXML private AnchorPane root;
    
    @FXML private Label nameLabel;
    
    public SLDummyInstancesContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
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
