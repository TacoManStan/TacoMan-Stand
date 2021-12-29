package com.taco.suit_lady.ui.pages.impl.content_selector.mandelbrot_test;

import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.suit_lady.ui.pages.impl.content_selector.ContentSelectorPageController;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@FxmlView("/fxml/sidebar/pages/impl/content_selector_page.fxml")
public class MandelbrotContentSelectorPageController extends ContentSelectorPageController<MandelbrotContent> {
    
    public MandelbrotContentSelectorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
}
