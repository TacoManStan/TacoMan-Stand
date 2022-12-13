package com.taco.tacository.game.ui.pages;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.objects.GameObjectModel;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.ui.UIPageController;
import com.taco.tacository.ui.jfx.components.ImagePane;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.tools.Bind;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Component
@FxmlView("/fxml/game/pages/game_object_editor_page.fxml")
@Scope("prototype")
public class GameObjectEditorPageController extends UIPageController<GameObjectEditorPage>
        implements Lockable, GameComponent, UIDProcessable {
    
    @FXML private AnchorPane root;
    
    @FXML private Label titleLabel;
    @FXML private ImagePane gameObjectImagePane;
    
    
    private ObjectBinding<Image> selectedGameObjectImageBinding;
    
    protected GameObjectEditorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public GameObjectEditorPageController init() {
        selectedGameObjectImageBinding = Bind.recursiveObjBinding(gameObject -> {
            if (gameObject != null) {
                final GameObjectModel gameObjectModel = gameObject.getModel();
                if (gameObjectModel != null)
                    return gameObjectModel.imageBinding();
            }
            return Bind.constObjBinding(null);
        }, selectedGameObjectBinding());
        
        titleLabel.textProperty().bind(Bind.stringBinding(() -> {
            final GameObject gameObject = getSelectedGameObject();
            if (gameObject != null)
                return "Game Object [" + gameObject.getLocationX(false) + ", " + gameObject.getLocationY(false) + "]";
            else
                return "No Game Object Selected";
        }, selectedGameObjectBinding()));
        
        gameObjectImagePane.imageProperty().bind(selectedGameObjectImageBinding);
        selectedGameObjectImageBinding.addListener((observable, oldValue, newValue) -> getPage().back());
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectBinding<GameObject> selectedGameObjectBinding() { return getPage().getParent().getController().selectedGameObjectBinding(); }
    public final GameObject getSelectedGameObject() { return selectedGameObjectBinding().get(); }
    
    public final ObjectBinding<Image> selectedGameObjectImageBinding() { return selectedGameObjectImageBinding; }
    public final Image getSelectedGameObjectImage() { return selectedGameObjectImageBinding.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Lock getLock() { return getPage().getLock(); }
    
    //
    
    @Override public Pane root() { return root; }
    
    @Override public void initialize() { }
    
    
    @Override public @NotNull GameViewContent getContent() { return getPage().getGame(); }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game-tile-editor-data");
        return uidProcessor;
    }
    
    //</editor-fold>
}
