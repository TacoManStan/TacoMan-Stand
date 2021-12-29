package com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_list_page;

import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
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
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@FxmlView("/fxml/sidebar/pages/mandelbrot/mandelbrot_list/mandelbrot_data_list.fxml")
public class MandelbrotContentListPageController extends UIPageController<MandelbrotContentListPage> {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private BorderPane root;
    
    @FXML private ListView<MandelbrotContent> contentList;
    @FXML private ImagePane addInstanceImagePane;
    @FXML private TextField searchField;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    private final BooleanProperty playingProperty;
    private final ReadOnlyObjectWrapper<MandelbrotContent> selectedContentMMProperty;
    
    private ImageButton addInstanceImageButton;
    
    protected MandelbrotContentListPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.lock = new ReentrantLock();
        
        this.playingProperty = new SimpleBooleanProperty();
        this.selectedContentMMProperty = new ReadOnlyObjectWrapper<>();
    }
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        initButtonViews();
        
        contentList.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> TB.resources().get(
                                cellData,
                                () -> weaver().loadController(MandelbrotContentElementController.class),
                                listView.hashCode()))));
        
        ListTools.applyListener(ui().getMandelbrotContentHandler().contentList(), (op, opType, triggerType) -> {
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
        
        ui().getMandelbrotContentHandler().selectedContentProperty().bindBidirectional(selectedContentMMProperty);
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
    
    private void onAdded(MandelbrotContent content) {
        FXTools.runFX(() -> FXTools.addElement(content, contentList, true), true);
    }
    
    private void onRemoved(MandelbrotContent content) {
        FXTools.runFX(() -> contentList.getItems().remove(content), true);
    }
    
    private void addInstance() {
        debugger().print("Add Instance Pressed");
        logiCore().execute(() -> ui().getMandelbrotContentHandler().newInstance());
    }
}
