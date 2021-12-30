package com.taco.suit_lady.ui.pages.impl.content_selector;

import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ListableContent<
        D extends ContentData,
        C extends ContentController,
        P extends ContentSelectorPage<D, P, SC, EC, H, T>,
        SC extends ContentSelectorPageController<D, P, SC, EC, H, T>,
        EC extends ContentElementController<D, P, SC, EC, H, T>,
        H extends ContentHandler<D, P, SC, EC, H, T>,
        T extends ListableContent<D, C, P, SC, EC, H, T>>
        extends Content<D, C> implements UIDProcessable, Lockable {
    
    private final H contentHandler;
    
    public ListableContent(@NotNull H contentHandler) {
        super(contentHandler);
        this.contentHandler = contentHandler;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final H getContentHandler() {
        return contentHandler;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void onShutdown();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull Lock getLock() {
        return getContentHandler().getLock();
    }
    
    
    private UIDProcessor uidProcessor;
    
    @Override
    public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("selectable-content");
        return uidProcessor;
    }
    
    //</editor-fold>
    
    public final void shutdown() {
        sync(() -> getContentHandler().shutdown((T) this));
    }
}
