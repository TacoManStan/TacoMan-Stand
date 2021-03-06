package com.taco.tacository.ui.pages.impl.content_selector;

import com.taco.tacository.ui.Footer;
import com.taco.tacository.ui.FooterController;
import com.taco.tacository.ui.Content;
import com.taco.tacository.ui.ContentController;
import com.taco.tacository.ui.ContentData;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.tools.Props;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;

public abstract class ListableContent<
        TD extends ContentData<T, TD, TC, F, FC>,
        TC extends ContentController<T, TD, TC, F, FC>,
        P extends ContentSelectorPage<TD, P, SC, EC, H, T>,
        SC extends ContentSelectorPageController<TD, P, SC, EC, H, T>,
        EC extends ContentElementController<TD, P, SC, EC, H, T>,
        H extends ContentHandler<TD, P, SC, EC, H, T>,
        T extends ListableContent<TD, TC, P, SC, EC, H, T, F, FC>,
        F extends Footer<F, FC, T, TD, TC>,
        FC extends FooterController<F, FC, T, TD, TC>>
        extends Content<T, TD, TC, F, FC>
        implements UIDProcessable, Lockable, Serializable {
    
    private final H contentHandler;
    
    private final ObjectProperty<EC> elementControllerProperty;
    private final ObjectProperty<Image> iconImageProperty;
    
    public ListableContent(@NotNull H contentHandler) {
        super(contentHandler);
        
        this.contentHandler = contentHandler;
        
        this.elementControllerProperty = new SimpleObjectProperty<>();
        this.iconImageProperty = new SimpleObjectProperty<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final H getContentHandler() { return contentHandler; }
    
    public final ObjectProperty<EC> elementControllerProperty() { return elementControllerProperty; }
    public final EC getElementController() { return elementControllerProperty.get(); }
    public final EC setElementController(EC newValue) { return Props.setProperty(elementControllerProperty, newValue); }
    
    public final ObjectProperty<Image> iconImageProperty() { return iconImageProperty; }
    public final Image getIconImage() { return iconImageProperty.get(); }
    public final Image setIconImage(Image newValue) { return Props.setProperty(iconImageProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void onShutdown();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull Lock getLock() { return getContentHandler().getLock(); }
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("selectable-content");
        return uidProcessor;
    }
    
    //</editor-fold>
    
    public final void shutdown() {
        sync(() -> getContentHandler().shutdown((T) this));
    }
}
