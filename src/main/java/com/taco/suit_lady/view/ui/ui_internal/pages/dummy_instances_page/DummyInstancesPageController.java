package com.taco.suit_lady.view.ui.ui_internal.pages.dummy_instances_page;

import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.components.ImagePane;
import com.taco.suit_lady.view.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.view.ui.jfx.lists.ListCellFX;
import com.taco.suit_lady.view.ui.ui_internal.contents_old.DummyContentsHandler;
import com.taco.suit_lady.view.ui.ui_internal.contents_old.DummyInstance;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummyInstanceElementController;
import com.taco.suit_lady.view.ui.ui_internal.controllers.SidebarNodeGroupController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@FxmlView("/fxml/sidebar/pages/dummy_instances_page/dummy_instances_page.fxml")
public final class DummyInstancesPageController extends SidebarNodeGroupController<DummyInstancesPage>
{
    
    //<editor-fold desc="--- FXML ---">
    
    @FXML private Pane root;
    
    @FXML private ListView<DummyInstance> instanceListView;
    
    @FXML private ImagePane testImagePane;
    @FXML private ImagePane addInstanceImagePane;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    
    private final DummyContentsHandler contentsHandler;
    
    private final BooleanProperty playingProperty; // TEST
    private final ReadOnlyObjectWrapper<DummyInstance> selectedInstanceMMProperty;
    
    //
    
    private ImageButton addInstanceImageButton;
    
    public DummyInstancesPageController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
        
        this.contentsHandler = new DummyContentsHandler(ctx(), weaver());
        
        this.playingProperty = new SimpleBooleanProperty();
        this.selectedInstanceMMProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override
    @FXML public void initialize()
    {
        initButtonViews();
        
        instanceListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX, element -> this.weaver().loadController(DummyInstanceElementController.class))));
        
        ArrayTools.applyChangeHandler(contentsHandler.instances(), this::onAdded, this::onRemoved);
        
        // TODO - Instead of using a MM property, create a contentChangeRequest(...) method in either AppEngine or ClientHandler.
        selectedInstanceMMProperty.addListener((observable, oldClient, newClient) -> instanceListView.getSelectionModel().select(newClient));
        instanceListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldClient, newClient) -> selectedInstanceMMProperty.set(newClient));
        
        contentsHandler.selectedInstanceProperty().bindBidirectional(selectedInstanceMMProperty); // Bind "middle-man" property
    }
    
    private void initButtonViews()
    {
        this.addInstanceImageButton = new ImageButton(
                weaver(), ctx(),
                addInstanceImagePane,
                "plus",
                this::addInstance,
                null,
                false,
                ImageButton.SMALL
        );
        
        //
        
        addInstanceImageButton.initialize();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public Lock getLock()
    {
        return lock;
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    public DummyContentsHandler getContentsHandler()
    {
        return contentsHandler;
    }
    
    public BooleanProperty playingProperty()
    {
        lock.lock();
        try {
            return playingProperty;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isPlaying()
    {
        return playingProperty.get();
    }
    
    public void setPlaying(boolean playing)
    {
        playingProperty.set(playing);
    }
    
    public void togglePlaying()
    {
        lock.lock();
        try {
            setPlaying(!isPlaying());
        } finally {
            lock.unlock();
        }
    }
    
    public ReadOnlyObjectProperty<DummyInstance> selectedInstanceProperty()
    {
        return selectedInstanceMMProperty.getReadOnlyProperty();
    }
    
    public DummyInstance getSelectedInstance()
    {
        return selectedInstanceMMProperty.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    private void onAdded(DummyInstance instance)
    {
        FXTools.get().runFX(() -> FXTools.get().addElement(instance, instanceListView, true), true);
    }
    
    private void onRemoved(DummyInstance instance)
    {
        FXTools.get().runFX(() -> instanceListView.getItems().remove(instance), true);
    }
    
    private void addInstance()
    {
        ctx().getBean(DummyContentsHandler.class).newInstance();
    }
    
    //</editor-fold>
}
