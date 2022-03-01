package com.taco.suit_lady.game.attributes;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.ui.jfx.lists.ListCellFX;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
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
                        cellData -> ResourcesSL.get(
                                cellData,
                                () -> weaver().loadController(AttributeElementController.class),
                                listView.hashCode()))));
        
        testButton.setOnAction(event -> ToolsFX.requireFX(() -> {
            addTestAttributes(getGame().getTestObject().attributes());
        }));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return getPage().getGame(); }
    
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
