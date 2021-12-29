package com.taco.suit_lady.ui.pages.impl.content_selector.mandelbrot_test;

import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.suit_lady.ui.pages.impl.content_selector.ContentElementController;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/pages/impl/content_element.fxml")
@Scope("prototype")
public class MandelbrotElementController extends ContentElementController<MandelbrotContent> {
    
    public MandelbrotElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
}
