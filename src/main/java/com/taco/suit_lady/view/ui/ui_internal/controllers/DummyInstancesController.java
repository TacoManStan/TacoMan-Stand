package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.util.ArrayTools;
import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import com.taco.suit_lady.view.ui.jfx.lists.ListCellFX;
import com.taco.suit_lady.view.ui.jfx.lists.WrappingCell;
import com.taco.suit_lady.view.ui.ui_internal.contents.DummyInstance;
import com.taco.suit_lady.view.ui.ui_internal.pages.DummyInstancesPage;
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

import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@FxmlView("/fxml/sidebar/client_instance_list/client_instance_list.fxml")
public final class DummyInstancesController extends SidebarNodeGroupController<DummyInstancesPage>
{
    
    //<editor-fold desc="FXML">
    
    @FXML private Pane root;
    
    @FXML private ListView<DummyInstance> instanceListView;
    
    @FXML private ImagePane testImagePane;
    @FXML private ImagePane addInstanceImagePane;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    
    private final BooleanProperty playingProperty; // TEST
    private final ReadOnlyObjectWrapper<DummyInstance> selectedInstanceMMProperty;
    
    //
    
    private ImageButton addInstanceImageButton;
    
    public DummyInstancesController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        this.lock = new ReentrantLock();
        
        this.playingProperty = new SimpleBooleanProperty();
        this.selectedInstanceMMProperty = new ReadOnlyObjectWrapper<>();
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    @FXML public void initialize()
    {
        initButtonViews();
        
        instanceListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new WrappingCell<>(
                        listView, listCellFX,
                        element -> this.weaver().loadController(DummyInstanceElementController.class)
                )));
        
        ArrayTools.applyChangeHandler(TB.handler().instances(), this::onAdded, this::onRemoved);
        
        // TODO - Instead of using a MM property, create a contentChangeRequest(...) method in either AppEngine or ClientHandler.
        selectedInstanceMMProperty.addListener((observable, oldClient, newClient) -> instanceListView.getSelectionModel().select(newClient));
        instanceListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldClient, newClient) -> selectedInstanceMMProperty.set(newClient));
        TB.handler().selectedInstanceProperty().bindBidirectional(selectedInstanceMMProperty); // Bind "middle-man" property
    }
    
    private void initButtonViews()
    {
        this.addInstanceImageButton = new ImageButton(
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
    
    //<editor-fold desc="Properties">
    
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
    
    //
    
    public ReadOnlyObjectProperty<DummyInstance> selectedInstanceProperty()
    {
        return selectedInstanceMMProperty.getReadOnlyProperty();
    }
    
    public DummyInstance getSelectedInstance()
    {
        return selectedInstanceMMProperty.get();
    }
    
    //</editor-fold>
    
    private void onAdded(DummyInstance instance)
    {
        FXTools.get().runFX(() -> FXTools.get().addElement(instance, instanceListView, true), true);
    }
    
    private void onRemoved(DummyInstance instance)
    {
        FXTools.get().runFX(() -> instanceListView.getItems().remove(instance), true);
        // TODO - Select next most relevant element
    }
    
    //
    
    private void addInstance()
    {
        TB.handler().newInstance();
    }
}
