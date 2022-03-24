package com.taco.tacository.ui.contents.mandelbrot.mandelbrot_content_selector_page;

import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContentData;
import com.taco.tacository.ui.pages.impl.content_selector.ContentElementController;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/pages/impl/content_element.fxml")
@Scope("prototype")
public class MandelbrotElementController extends ContentElementController<
        MandelbrotContentData,
        MandelbrotContentSelectorPage,
        MandelbrotContentSelectorPageController,
        MandelbrotElementController,
        MandelbrotContentHandler,
        MandelbrotContent> {
    
    public MandelbrotElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
}
