package com.taco.tacository.game.attributes;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.ui.UIPageController;
import com.taco.tacository.ui.jfx.lists.CellControlManager;
import com.taco.tacository.ui.jfx.lists.ListCellFX;
import com.taco.tacository.util.tools.Stuff;
import com.taco.tacository.util.tools.fx_tools.FX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/game/pages/attribute_page.fxml")
@Scope("prototype")
public class AttributePageController extends UIPageController<AttributePage>
        implements GameComponent {
    
    @FXML private AnchorPane root;
    
    @FXML private ListView<Attribute<?>> attributeListView;
    @FXML private Button testButton;
    
    //    @FXML private ListView
    
    protected AttributePageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public void initialize() {
        attributeListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> Stuff.get(
                                cellData,
                                () -> weaver().loadController(AttributeElementController.class),
                                listView.hashCode()))));
        
        testButton.setOnAction(event -> FX.requireFX(() -> {
            addTestAttributes(getGame().getTestObject1().attributes());
        }));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getContent() { return getPage().getGame(); }
    
    //
    
    @Override public Pane root() { return root; }
    
    //</editor-fold>
    
    public void addTestAttributes(@NotNull AttributeManager attributeManager) {
        attributeListView.getItems().clear();
        attributeManager.attributeList().forEach(attribute -> {
            attributeListView.getItems().add(attribute);
        });
    }
}
