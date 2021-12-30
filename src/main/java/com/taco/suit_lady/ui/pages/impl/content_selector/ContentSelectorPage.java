package com.taco.suit_lady.ui.pages.impl.content_selector;

import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public abstract class ContentSelectorPage<
        D extends ContentData,
        P extends ContentSelectorPage<D, P, SC, EC, H, T>,
        SC extends ContentSelectorPageController<D, P, SC, EC, H, T>,
        EC extends ContentElementController<D, P, SC, EC, H, T>,
        H extends ContentHandler<D, P, SC, EC, H, T>,
        T extends ListableContent<D, ?, P, SC, EC, H, T>>
        extends UIPage<SC> {
    
    private H contentHandler;
    
    public ContentSelectorPage(@NotNull UIBook owner, Object... constructorParams) {
        super(owner, constructorParams);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final H getContentHandler() {
        return contentHandler;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected void initializePage(@NotNull Object @NotNull [] constructorParams) {
        this.contentHandler = constructContentHandler((P) this);
    }
    
    //</editor-fold>
    
    protected abstract H constructContentHandler(P parentPage);
    
    protected abstract Class<EC> elementControllerDefinition();
    
    @Override
    protected abstract @NotNull Class<SC> controllerDefinition();
}
