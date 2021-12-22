package com.taco.suit_lady.ui.pages.fallback_page;

import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import com.taco.suit_lady.ui.UIPageHandler;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.function.Function;

/**
 * <p>Page used when a {@link UIPageHandler} fails to load the correct {@link UIPage} implementation, hence the name, {@link FallbackPage}.</p>
 * <p>Refer to the {@link UIBook} {@link UIBook#UIBook(FxWeaver, ConfigurableApplicationContext, String, String, Function, Runnable, StackPane) constructor} for implementation.</p>
 */
public class FallbackPage extends UIPage<FallbackPageController>
{
    public FallbackPage(UIBook owner)
    {
        super(owner);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<FallbackPageController> controllerDefinition()
    {
        return FallbackPageController.class;
    }
}
