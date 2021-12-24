package com.taco.suit_lady.ui.pages.client_instance_list_old_demo;

import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.jfx.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.ui.jfx.lists.ListCellFX;
import com.taco.suit_lady.util.tools.ArrayTools;
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
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@FxmlView("/fxml/sidebar/pages/client_instance_list_page_old/client_instance_list.fxml")
public class ClientInstanceListDemoPageController extends UIPageController<ClientInstanceListDemoPage> {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private BorderPane root;
    
    @FXML private ListView<DummyClient> clientInstanceList;
    @FXML private ImagePane addInstanceImagePane;
    @FXML private TextField searchField;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    private final BooleanProperty playingProperty;
    private final ReadOnlyObjectWrapper<DummyClient> selectedClientMMProperty;
    
    private ImageButton addInstanceImageButton;
    
    protected ClientInstanceListDemoPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.lock = new ReentrantLock();
        
        this.playingProperty = new SimpleBooleanProperty();
        this.selectedClientMMProperty = new ReadOnlyObjectWrapper<>();
    }
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        initButtonViews();
        
        clientInstanceList.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> TB.resources().get(
                                cellData,
                                () -> weaver().loadController(ClientInstanceElementController.class),
                                listView.hashCode()))));
        
        ListTools.applyListener( clientHandler().clientList(), (op, opType, triggerType) -> {
            if (triggerType == Operation.TriggerType.CHANGE)
                switch (opType) {
                    case ADDITION -> onAdded(op.contents());
                    case REMOVAL -> onRemoved(op.contents());
                }
        });
        
        selectedClientMMProperty.addListener(
                (observable, oldValue, newValue) -> TaskTools.sync(
                        lock, () -> clientInstanceList.getSelectionModel().select(newValue)));
        
        clientInstanceList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> TaskTools.sync(
                        lock, () -> selectedClientMMProperty.set(newValue)));
        
        clientHandler().selectedClientProperty().bindBidirectional(selectedClientMMProperty);
    }
    
    private void initButtonViews() {
        this.addInstanceImageButton = new ImageButton(
                this,
                addInstanceImagePane,
                "plus",
                this::addInstance,
                null,
                false,
                ImageButton.SMALL).initialize();
    }
    
    private void onAdded(DummyClient content) {
        FXTools.runFX(() -> FXTools.addElement(content, clientInstanceList, true), true);
    }
    
    private void onRemoved(DummyClient content) {
        FXTools.runFX(() -> clientInstanceList.getItems().remove(content), true);
    }
    
    private void addInstance() {
        debugger().print("Add Instance Pressed");
        logiCore().execute(() -> clientHandler().newClient());
    }
}
