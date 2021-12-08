package com.taco.suit_lady.view.ui.ui_internal.pages.content_switch_demo_page;

import com.taco.suit_lady.view.ui.UIPageController;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.SLMandelbrotContent;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.test.SLTestContent;
import com.taco.util.quick.ConsoleBB;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@FxmlView("/fxml/sidebar/pages/content_switch_demo_page/content_switch_demo_page.fxml")
public class ContentSwitchDemoPageController extends UIPageController<ContentSwitchDemoPage>
{
    @FXML private AnchorPane root;
    
    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    
    private final SLContent<?, ?> content1;
    private final SLContent<?, ?> content2;
    private final SLContent<?, ?> content3;
    
    protected ContentSwitchDemoPageController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.content1 = new SLMandelbrotContent(this);
        this.content2 = new SLMandelbrotContent(this);
        this.content3 = new SLTestContent(this);
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    public void initialize() {
        button1.setOnAction(event -> button1());
        button2.setOnAction(event -> button2());
        button3.setOnAction(event -> button3());
    }
    
    private void button1()
    {
        ConsoleBB.CONSOLE.print("B1 Pressed");
        ctx().getBean(AppUI.class).getContentManager().setContent(content1);
    }
    
    private void button2()
    {
        ConsoleBB.CONSOLE.print("B2 Pressed");
        ctx().getBean(AppUI.class).getContentManager().setContent(content2);
    }
    
    private void button3()
    {
        ConsoleBB.CONSOLE.print("B3 Pressed");
        ctx().getBean(AppUI.class).getContentManager().setContent(content3);
    }
}
