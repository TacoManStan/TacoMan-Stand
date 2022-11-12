package com.taco.tacository.game.ui.nia;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.GameMap;
import com.taco.tacository.game.attributes.Attribute;
import com.taco.tacository.game.attributes.AttributeManager;
import com.taco.tacository.game.attributes.AttributePage;
import com.taco.tacository.game.galaxy.abilities.specific.Ability_Blink;
import com.taco.tacository.game.galaxy.abilities.specific.Ability_Cleave;
import com.taco.tacository.game.galaxy.abilities.specific.Ability_LaunchMissile;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.objects.tiles.GameTile;
import com.taco.tacository.game.ui.*;
import com.taco.tacository.game.ui.pages.GameTileEditorPage;
import com.taco.tacository.game.ui.pages.GameViewPage;
import com.taco.tacository.logic.triggers.Galaxy;
import com.taco.tacository.ui.*;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.Obj;
import com.taco.tacository.util.tools.Props;
import com.taco.tacository.util.tools.Stuff;
import com.taco.tacository.util.tools.list_tools.L;
import com.taco.tacository.util.tools.printing.Printer;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;


public class NiaContent
        extends Content<NiaContent, NiaContentData, NiaContentController, NiaFooter, NiaFooterController>
        implements UIDProcessable, Lockable {
    
    private final ReentrantLock lock;
    
    //
    
    private SidebarBookshelf bookshelf;
    
    private GameViewPage coverPage;
    private GameTileEditorPage tileEditorPage;
    private AttributePage attributePage;
    
    
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
        ui().getContentManager().setContent(this);
        
        return super.init();
    }
    
    private void initUIPage() {
//        bookshelf = injectBookshelf("Game View",
//                                    new UIBook(
//                                            this,
//                                            "Tile Selector",
//                                            "details",
//                                            uiBook -> Stuff.get(
//                                                    "pages",
//                                                    uiBook.getUID(uiBook.getButtonID()),
//                                                    () -> tileEditorPage = new GameTileEditorPage(uiBook, this).init()),
//                                            null),
//                                    new UIBook(
//                                            this,
//                                            "Attribute List Test",
//                                            "clients",
//                                            uiBook -> Stuff.get(
//                                                    "pages",
//                                                    uiBook.getUID(uiBook.getButtonID()),
//                                                    () -> attributePage = new AttributePage(uiBook, this)),
//                                            null),
//                                    new UIBook(
//                                            this,
//                                            "Game View",
//                                            "game_engine",
//                                            uiBook -> Stuff.get(
//                                                    "pages",
//                                                    uiBook.getUID(uiBook.getButtonID()),
//                                                    () -> coverPage = new GameViewPage(uiBook, this)),
//                                            null)).select();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected GameViewPage getCoverPage() { return coverPage; }
    
    public final @NotNull ObjectProperty<GameMap> gameMapProperty() { return gameMapProperty; }
    public final GameMap getGameMap() { return gameMapProperty.get(); }
    public final GameMap setGameMap(@NotNull GameMap newValue) { return Props.setProperty(gameMapProperty, newValue); }
    
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
