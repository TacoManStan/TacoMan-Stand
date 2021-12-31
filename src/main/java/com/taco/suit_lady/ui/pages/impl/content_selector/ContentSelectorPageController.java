package com.taco.suit_lady.ui.pages.impl.content_selector;

import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.jfx.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.ui.jfx.lists.ListCellFX;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.util.tools.TaskTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.util.tools.list_tools.ListTools;
import com.taco.suit_lady.util.tools.list_tools.Operation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ContentSelectorPageController<
        D extends ContentData,
        P extends ContentSelectorPage<D, P, SC, EC, H, T>,
        SC extends ContentSelectorPageController<D, P, SC, EC, H, T>,
        EC extends ContentElementController<D, P, SC, EC, H, T>,
        H extends ContentHandler<D, P, SC, EC, H, T>,
        T extends ListableContent<D, ?, P, SC, EC, H, T>>
        extends UIPageController<ContentSelectorPage<D, P, SC, EC, H, T>> {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private BorderPane root;
    
    @FXML private ListView<T> contentList;
    @FXML private ImagePane addInstanceImagePane;
    @FXML private TextField searchField;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    private final BooleanProperty playingProperty;
    private final ReadOnlyObjectWrapper<T> selectedContentMMProperty;
    
    private ImageButton addInstanceImageButton;
    
    public ContentSelectorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.lock = new ReentrantLock();
        
        this.playingProperty = new SimpleBooleanProperty();
        this.selectedContentMMProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
    
    }
    
    public void doInit() {
        initButtonViews();
    
        contentList.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> TB.resources().get(
                                cellData,
                                () -> weaver().loadController(getPage().elementControllerDefinition()),
                                listView.hashCode()))));
    
        ListTools.applyListener(getPage().getContentHandler().contentList(), (op, opType, triggerType) -> {
            if (triggerType == Operation.TriggerType.CHANGE)
                switch (opType) {
                    case ADDITION -> onAdded(op.contents());
                    case REMOVAL -> onRemoved(op.contents());
                }
        });
    
        selectedContentMMProperty.addListener(
                (observable, oldValue, newValue) -> TaskTools.sync(
                        lock, () -> contentList.getSelectionModel().select(newValue)));
    
        contentList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> TaskTools.sync(
                        lock, () -> selectedContentMMProperty.set(newValue)));
    
        getPage().getContentHandler().selectedContentProperty().bindBidirectional(selectedContentMMProperty);
    }
    
    private void initButtonViews() {
        this.addInstanceImageButton = new ImageButton(
                this,
                "Add New Instance",
                "plus",
                addInstanceImagePane,
                this::addInstance,
                null,
                false,
                ImageButton.SMALL).init();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void onAdded(T content) {
        FXTools.runFX(() -> FXTools.addElement(content, contentList, true), true);
    }
    
    private void onRemoved(T content) {
        FXTools.runFX(() -> contentList.getItems().remove(content), true);
    }
    
    private void addInstance() {
        logiCore().executor().execute(() -> getPage().getContentHandler().addInstance());
    }
    
    //</editor-fold>
}
