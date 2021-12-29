package com.taco.suit_lady.ui.pages.impl.content_selector;

import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public abstract class ContentSelectorPage<T extends Content<?, ?> & ListableContent, C extends ContentSelectorPageController<T>, EC extends ContentElementController<T>> extends UIPage<C> {
    
    private ContentHandler<T, EC> contentHandler;
    
    public ContentSelectorPage(@NotNull UIBook owner, Object... constructorParams) {
        super(owner, constructorParams);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ContentHandler<T, EC> getContentHandler() {
        return contentHandler;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected void initializePage(@NotNull Object @NotNull [] constructorParams) {
        this.contentHandler = constructContentHandler(this);
    }
    
    //</editor-fold>
    
    protected abstract ContentHandler<T, EC> constructContentHandler(ContentSelectorPage<T, C, EC> parentPage);
    
    protected abstract Class<EC> elementControllerDefinition();
    
    @Override
    protected abstract @NotNull Class<C> controllerDefinition();
}
