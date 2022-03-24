package com.taco.tacository.ui.contents.mandelbrot.mandelbrot_content_selector_page;

import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContentData;
import com.taco.tacository.ui.pages.impl.content_selector.ContentSelectorPageController;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@FxmlView("/fxml/sidebar/pages/impl/content_selector_page.fxml")
public class MandelbrotContentSelectorPageController extends ContentSelectorPageController<
        MandelbrotContentData,
        MandelbrotContentSelectorPage,
        MandelbrotContentSelectorPageController,
        MandelbrotElementController,
        MandelbrotContentHandler,
        MandelbrotContent> {
    
    public MandelbrotContentSelectorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
}
