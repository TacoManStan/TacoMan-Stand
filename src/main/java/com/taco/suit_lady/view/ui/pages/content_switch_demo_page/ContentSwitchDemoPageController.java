package com.taco.suit_lady.view.ui.pages.content_switch_demo_page;

import com.taco.suit_lady.view.ui.UIPageController;
import com.taco.suit_lady.view.ui.AppUI;
import com.taco.suit_lady.view.ui.Content;
import com.taco.suit_lady.view.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.suit_lady.view.ui.contents.test.TestContent;
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
    
    private final Content<?, ?> content1;
    private final Content<?, ?> content2;
    private final Content<?, ?> content3;
    
    protected ContentSwitchDemoPageController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.content1 = new MandelbrotContent(this);
        this.content2 = new MandelbrotContent(this);
        this.content3 = new TestContent(this);
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
        ctx().getBean(AppUI.class).getContentManager().setContent(content1);
    }
    
    private void button2()
    {
        ctx().getBean(AppUI.class).getContentManager().setContent(content2);
    }
    
    private void button3()
    {
        ctx().getBean(AppUI.class).getContentManager().setContent(content3);
    }
}
