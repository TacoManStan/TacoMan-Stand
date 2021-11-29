package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.UIPageHandler;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummySidebarController;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.function.Function;

/**
 * <p>Page used when a {@link UIPageHandler} fails to load the correct {@link UIPage} implementation, hence the name, {@link FallbackPage}.</p>
 * <p>Refer to the {@link UINode} {@link UINode#UINode(FxWeaver, ConfigurableApplicationContext, String, String, Function, Runnable, StackPane) constructor} for implementation.</p>
 */
public class FallbackPage extends UIPage<DummySidebarController>
{
    public FallbackPage(UINode owner)
    {
        super(owner);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<DummySidebarController> controllerDefinition()
    {
        return DummySidebarController.class;
    }
}
