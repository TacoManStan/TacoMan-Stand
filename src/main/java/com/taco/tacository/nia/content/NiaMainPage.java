package com.taco.tacository.nia.content;

import com.taco.tacository.logic.ContentComponent;
import com.taco.tacository.nia.content.ui.NiaMainPageController;
import com.taco.tacository.ui.UIBook;
import com.taco.tacository.ui.UIPage;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines the {@link NiaContent#getCoverPage() Cover} {@link UIPage Page} for {@link NiaContent}.</p>
 *
 * @see NiaMainPageController
 */
public class NiaMainPage extends UIPage<NiaMainPageController>
        implements ContentComponent<NiaContent> {
    
    private final NiaContent content;
    
    public NiaMainPage(@NotNull UIBook owner, @NotNull NiaContent content, Object... constructorParams) {
        super(owner, constructorParams);
        this.content = content;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull NiaContent getContent() { return content; }
    
    //
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) { }
    @Override protected @NotNull Class<NiaMainPageController> controllerDefinition() { return NiaMainPageController.class; }
    
    //</editor-fold>
}
