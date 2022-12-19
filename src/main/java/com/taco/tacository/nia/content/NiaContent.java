package com.taco.tacository.nia.content;

import com.taco.tacository.game.GameMap;
import com.taco.tacository.game.attributes.AttributePage;
import com.taco.tacository.game.ui.pages.GameTileEditorPage;
import com.taco.tacository.game.ui.pages.GameViewPage;
import com.taco.tacository.nia.content.ui.NiaContentController;
import com.taco.tacository.ui.*;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.tools.Props;
import com.taco.tacository.util.tools.Stuff;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;


public class NiaContent
        extends Content<NiaContent, NiaContentData, NiaContentController, NiaFooter, NiaFooterController>
        implements UIDProcessable, Lockable {
    
    private final ReentrantLock lock;
    
    //
    
    private SidebarBookshelf bookshelf;
    
    private NiaMainPage coverPage;
    
    
    private final ObjectProperty<GameMap> gameMapProperty;
    
    private ObjectBinding<Point2D> mouseOnMapBinding;
    
    public NiaContent(@NotNull Springable springable) {
        super(springable);
        this.lock = new ReentrantLock();
        
        //
        
        this.gameMapProperty = new SimpleObjectProperty<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public NiaContent init() {
        initUIPage();
        return super.init();
    }
    
    private void initUIPage() {
        bookshelf = injectBookshelf("Nia Main",
                                    new UIBook(
                                            this,
                                            "Main Page",
                                            "game_engine",
                                            uiBook -> Stuff.get(
                                                    "pages",
                                                    uiBook.getUID(uiBook.getButtonID()),
                                                    () -> coverPage = new NiaMainPage(uiBook, this)),
                                            null)).select();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected NiaMainPage getCoverPage() { return coverPage; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
//    public @NotNull GameUIData getUIData() { return getData().getUIData(); }
    
    
    @Override protected @NotNull NiaContentData loadData() { return new NiaContentData(this); }
    @Override protected @NotNull Class<NiaContentController> controllerDefinition() { return NiaContentController.class; }
    @Override protected NiaFooter constructFooter() { return new NiaFooter(this); }
    
    @Override protected void onActivate() { }
    @Override protected void onDeactivate() { }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game");
        return uidProcessor;
    }
    
    //
    
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    @Override public boolean isNullableLock() { return true; }
    
    //</editor-fold>
}
