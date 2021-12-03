package com.taco.suit_lady.view.ui.ui_internal;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.console.Console;
import com.taco.suit_lady.view.ui.console.ConsoleMessageable;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXDialogTools;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import com.taco.suit_lady.view.ui.jfx.lists.treehandler.WrappingTreeCellData;
import com.taco.suit_lady.view.ui.ui_internal.console.ConsoleUIDataContainer;
import com.taco.suit_lady.view.ui.ui_internal.contents.DummyContentsInstancePane;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentManager;
import com.taco.suit_lady.view.ui.ui_internal.controllers.SettingsController;
import com.taco.suit_lady.view.ui.ui_internal.pages.content_switch_demo_page.ContentSwitchDemoPage;
import com.taco.suit_lady.view.ui.ui_internal.pages.dummy_instances_page.DummyInstancesPage;
import com.taco.suit_lady.view.ui.ui_internal.pages.entity_debug_page.EntityDebugPage;
import com.taco.suit_lady.view.ui.ui_internal.pages.example_page.ExamplePage;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The {@code AppController} is designed to...
 * <ol>
 * <li>Handle the initialization of the UI components.</li>
 * <li>Hold relevant FXML references.</li>
 * <li>Manage any internal data that would only clutter the {@link AppUI} class.</li>
 * </ol>
 * <p>
 * Simply put, any high-level and/or custom UI attribute should be placed in the {@link AppUI} class instead of here.
 */
@Component
@FxmlView("/fxml/main.fxml")
public class AppController
        implements Springable
{
    // <editor-fold desc="--- STATIC CONSTANTS ---">
    
    private static final long TRANSITION_TIME = 350;
    private static final long MICRO_TRANSITION_TIME = 150;
    private static final long FRAME_WIDTH_STEP = 25;
    
    //
    
    private static final String MINIMIZE_URL = "http://i.imgur.com/GIQcBBe.png";
    private static final String MINIMIZE_HOVERED_URL = "http://i.imgur.com/3A9fkry.png";
    private static final String MINIMIZE_SELECTED_URL = "http://i.imgur.com/b9bOE6h.png";
    
    private static final String MAXIMIZE_URL = "http://i.imgur.com/PbFJ11x.png";
    private static final String MAXIMIZE_HOVERED_URL = "http://i.imgur.com/kN4j1AW.png";
    private static final String MAXIMIZE_SELECTED_URL = "http://i.imgur.com/YufULjO.png";
    
    private static final String MAXIMIZE_BOTH_URL = "http://i.imgur.com/uZ5l6WA.png";
    private static final String MAXIMIZE_BOTH_HOVERED_URL = "http://i.imgur.com/TKMzXUh.png";
    private static final String MAXIMIZE_BOTH_SELECTED_URL = "http://i.imgur.com/Ovs2BNK.png";
    
    private static final String CLOSE_URL = "http://i.imgur.com/btLFdtv.png";
    private static final String CLOSE_HOVERED_URL = "http://i.imgur.com/uKfJ22V.png";
    private static final String CLOSE_SELECTED_URL = "http://i.imgur.com/Dv3Qpim.png";
    
    //
    
    private static final String HIDE_URL = "http://i.imgur.com/SHfWiUI.png";
    private static final String HIDE_HOVERED_URL = "http://i.imgur.com/ac5qvng.png";
    private static final String HIDE_SELECTED_URL = "http://i.imgur.com/6gVcZlf.png";
    
    private static final String SHOW_URL = "http://i.imgur.com/SHfWiUI.png";
    private static final String SHOW_HOVERED_URL = "http://i.imgur.com/ac5qvng.png";
    private static final String SHOW_SELECTED_URL = "http://i.imgur.com/6gVcZlf.png";
    
    //
    
    private static final String ORIGINAL_LOGO_URL = "http://i.imgur.com/QpAWsNX.png";
    private static final String LOGO_URL = "http://i.imgur.com/Ub6tEdm.png";
    private static final String LOGO_HOVERED_URL = "http://i.imgur.com/sXg5fyh.png";
    private static final String LOGO_PRESSED_URL = "http://i.imgur.com/qpQYGV8.png";
    private static final String LOGO_IMAGE_OLD_URL = "http://i.imgur.com/IksGsQf.png";
    
    //
    
    private static final String[] IMAGE_URLS = {"http://i.imgur.com/nH14AMm.jpg", "http://i.imgur.com/nqejQH7.jpg"};
    
    //
    
    private static final int GAME_WIDTH = 765;
    private static final int GAME_HEIGHT = 503;
    
    private static final int PUI_WIDTH = 275;
    
    // </editor-fold>
    
    // <editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private StackPane root;
    
    @FXML private HBox windowBar;
    @FXML private StackPane dragBar;
    
    @FXML private GridPane gridPane;
    @FXML private StackPane contentStackPane;
    
    @FXML private TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> consoleTree;
    
    //
    
    @FXML private Pane cornerResizePane;
    @FXML private Pane topResizePane;
    @FXML private Pane bottomResizePane;
    @FXML private Pane leftResizePane;
    @FXML private Pane rightResizePane;
    
    //
    
    @FXML private TextField globalSearchField;
    
    //
    
    @FXML private BorderPane sidebarPane;
    
    @FXML private AnchorPane sidebarPaneAnchor;
    
    @FXML private ToolBar sidebarButtonBar;
    
    @FXML private StackPane sidebarChildButtonsPane;
    @FXML private StackPane sidebarContentPane;
    
    @FXML private ImagePane backImagePane;
    
    //
    
    @FXML private ImagePane settingsImagePane;
    @FXML private ImagePane sidebarImagePane;
    
    @FXML private ImagePane minimizeImagePane;
    @FXML private ImagePane maximizeImagePane;
    @FXML private ImagePane closeImagePane;
    
    @FXML private ImagePane logoImagePane;
    
    @FXML private CheckBox consoleTRiBotCheckBox;
    @FXML private CheckBox consoleClientCheckBox;
    @FXML private CheckBox consoleScriptCheckBox;
    @FXML private CheckBox consoleSelectedInstanceOnlyCheckBox;
    
    // </editor-fold>
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private double STAGE_MIN_WIDTH;
    private double STAGE_MIN_HEIGHT;
    
    //
    
    private Stage stage;
    private DummyContentsInstancePane contentPane;
    
    public AppController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
    }
    
    private SLContentManager contentManager;
    
    // <editor-fold desc="--- INITIALIZATION ---">
    
    // Called automatically by FXML loader.
    // As some properties may not yet be finished loading, all actual UI initialization should be done in initialize(Stage).
    @FXML
    public final void initialize()
    {
        final AppUI ui = ctx().getBean(AppUI.class);
        
        ui.setController(this);
        ui.setContentStackPane(contentStackPane);
        ui.setSidebar(new Sidebar(weaver(), ctx(), sidebarChildButtonsPane, sidebarContentPane, backImagePane, sidebarButtonBar));
    }
    
    public final void initialize(Stage stage)
    {
        this.stage = stage;
        //        this.contentPane = new DummyContentsInstancePane();
        
        // TODO: Synchronize with the actual title of the application.
        this.stage.titleProperty().set("Suit Lady");
        
        this.stage.getIcons().add(TB.resources().getImage("buttons/logo/", "logo", "png"));
        
        sidebarImagePane.setRotationAxis(Rotate.Y_AXIS);
        sidebarPaneAnchor.managedProperty().bind(sidebarPaneAnchor.visibleProperty());
        
        final AppUI ui = ctx().getBean(AppUI.class);
        ui.init();
        
        //        contentPane.contentProperty().bind(ctx.getBean(DummyContentsHandler.class).selectedInstanceProperty());
        //        contentStackPane.getChildren().add(contentPane);
        
        
        initImageButtons();
        initSidebar();
        
        ctx().getBean(Console.class).consolify(
                new ConsoleUIDataContainer(
                        consoleTree,
                        consoleTRiBotCheckBox.selectedProperty(),
                        consoleClientCheckBox.selectedProperty(),
                        consoleScriptCheckBox.selectedProperty(),
                        consoleSelectedInstanceOnlyCheckBox.selectedProperty()
                ));
        
        FXTools.get().constructDraggableNode(dragBar);
        FXTools.get().constructResizableNode(getStage(), cornerResizePane, topResizePane, bottomResizePane, leftResizePane, rightResizePane,
                                             minimizeImagePane, maximizeImagePane, closeImagePane, settingsImagePane, sidebarImagePane
        );
        
        stage.show();
        onShownInit();
    }
    
    //
    
    private void initSidebar()
    {
        final Sidebar sidebar = ctx().getBean(AppUI.class).getSidebar();
        
        final Button generalSidebarButton = new Button("General");
        final SidebarBookshelf generalSidebarBookshelf = new SidebarBookshelf(sidebar, generalSidebarButton);
        generalSidebarBookshelf.getBooks().add(new UIBook(
                AppController.this.weaver, AppController.this.ctx,
                "Clients", "clients",
                uiBook -> TB.resources().get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new DummyInstancesPage(uiBook)
                ), null
        ));
        generalSidebarBookshelf.getBooks().add(new UIBook(
                AppController.this.weaver, AppController.this.ctx,
                "Development", "popout_sidebar",
                uiBook -> TB.resources().get(
                        "pages", uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "green")
                ), null
        ));
        generalSidebarBookshelf.getButtonGroup().selectFirst();
        
        final Button inDevelopmentSidebarButton = new Button("In Dev");
        final SidebarBookshelf inDevelopmentSidebarBookshelf = new SidebarBookshelf(sidebar, inDevelopmentSidebarButton);
        inDevelopmentSidebarBookshelf.getBooks().add(new UIBook(
                AppController.this.weaver, AppController.this.ctx,
                "Entity Debug", "entity_debug",
                uiBook -> TB.resources().get(
                        "pages", uiBook.getButtonID(),
                        () -> new EntityDebugPage(uiBook)
                ), null
        ));
        inDevelopmentSidebarBookshelf.getBooks().add(new UIBook(
                weaver(), ctx(),
                "Content Switch Demo", "rerun",
                uiBook -> TB.resources().get(
                        "pages", uiBook.getUID(uiBook.getButtonID()),
                        () -> new ContentSwitchDemoPage(uiBook)
                ), null
        ));
        inDevelopmentSidebarBookshelf.getButtonGroup().selectFirst();
        
        final Button nyiSidebarButton = new Button("NYI");
        final SidebarBookshelf nyiSidebarBookshelf = new SidebarBookshelf(sidebar, nyiSidebarButton);
        nyiSidebarBookshelf.getBooks().add(new UIBook(
                AppController.this.weaver, AppController.this.ctx,
                "Social", "social",
                uiBook -> TB.resources().get(
                        "pages", uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "blue")
                ), null
        ));
        nyiSidebarBookshelf.getButtonGroup().selectFirst();
        
        sidebar.bookshelvesProperty().addAll(generalSidebarBookshelf, inDevelopmentSidebarBookshelf, nyiSidebarBookshelf);
        sidebar.initialize();
    }
    
    private void initImageButtons()
    {
        new ImageButton(
                weaver(), ctx(),
                settingsImagePane,
                "settings",
                null,
                this::openSettings,
                false,
                ImageButton.SMALL
        ).initialize();
        
        new ImageButton(
                weaver(), ctx(),
                sidebarImagePane,
                "hide_sidebar",
                null,
                this::toggleSidebar,
                false,
                ImageButton.SMALL
        ).initialize();
        
        new ImageButton(
                weaver(), ctx(),
                minimizeImagePane,
                "minimize",
                null,
                () -> stage.setIconified(!stage.isIconified()),
                false,
                ImageButton.SMALL
        ).initialize();
        
        new ImageButton(
                weaver(), ctx(),
                maximizeImagePane,
                Bindings.createStringBinding(() -> stage.isMaximized() ? "maximize_both" : "maximize", stage.maximizedProperty()),
                null,
                () -> stage.setMaximized(!stage.isMaximized()),
                false,
                ImageButton.SMALL
        ).initialize();
        
        new ImageButton(
                weaver(), ctx(),
                closeImagePane,
                "close",
                null,
                stage::close,
                false,
                ImageButton.SMALL
        ).initialize();
        
        new ImageButton(
                weaver(), ctx(),
                logoImagePane,
                "logo",
                () -> TB.web().browse("google", true),
                null,
                false,
                new Point2D(20.0, 20.0)
        ).initialize();
        
        sidebarImagePane.visibleProperty().bind(Bindings.not(stage.maximizedProperty()));
    }
    
    //
    
    private void onShownInit()
    {
        Stage stage = getStage();
        sidebarPane.setPrefWidth(PUI_WIDTH);
        STAGE_MIN_WIDTH = stage.getWidth() - PUI_WIDTH;
        STAGE_MIN_HEIGHT = stage.getHeight();
        FXTools.get().lockSize(stage, STAGE_MIN_WIDTH + PUI_WIDTH, STAGE_MIN_HEIGHT);
    }
    
    // </editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Stage getStage()
    {
        return stage;
    }
    
    public final TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> getConsoleTree()
    {
        return consoleTree;
    }
    
    //</editor-fold>
    
    private void toggleSidebar()
    {
        Stage stage = getStage();
        
        boolean hiding = sidebarPaneAnchor.isVisible();
        double stage_start_width = stage.getWidth();
        double end_rotation = !hiding ? 0.0 : 180.0;
        double end_pui_width = !hiding ? PUI_WIDTH : 0.0;
        double end_stage_width = stage_start_width + (PUI_WIDTH * (hiding ? -1 : 1));
        double end_stage_pref_width = STAGE_MIN_WIDTH + (PUI_WIDTH * (hiding ? 0 : 1));
        
        if (hiding)
            stage.setMinWidth(end_stage_pref_width);
        else {
            sidebarPaneAnchor.setPrefWidth(0);
            sidebarPaneAnchor.setMinWidth(0);
            sidebarPaneAnchor.setVisible(true);
        }
        
        BooleanProperty readyProperty = new SimpleBooleanProperty(false);
        DoubleProperty puiPanePrefWidthProperty = new SimpleDoubleProperty(sidebarPaneAnchor.getPrefWidth());
        DoubleProperty stageWidthProperty = new SimpleDoubleProperty(stage.getWidth());
        ChangeListener<Number> stageWidthChangeListener = (observable, oldValue, newValue) -> {
            if (Math.abs(stage.getWidth() - newValue.intValue()) > FRAME_WIDTH_STEP)
                readyProperty.set(true);
        };
        stageWidthProperty.addListener(stageWidthChangeListener);
        
        AnimationTimer animationTimer = new AnimationTimer()
        {
            @Override
            public void handle(long currentTime)
            {
                if (readyProperty.get()) {
                    readyProperty.set(false);
                    stage.setWidth(stageWidthProperty.intValue());
                    sidebarPaneAnchor.setPrefWidth(puiPanePrefWidthProperty.intValue());
                }
            }
        };
        
        Timeline animation = new Timeline(new KeyFrame(
                Duration.millis(MICRO_TRANSITION_TIME),
                new KeyValue(sidebarImagePane.rotateProperty(), end_rotation),
                new KeyValue(puiPanePrefWidthProperty, end_pui_width),
                new KeyValue(stageWidthProperty, end_stage_width)
        ));
        
        animation.setOnFinished(animationEvent -> {
            animationTimer.stop();
            
            stageWidthProperty.removeListener(stageWidthChangeListener);
            stage.setWidth(end_stage_width);
            FXTools.get().lockSize(stage, end_stage_pref_width, FXTools.get().NO_CHANGE_SIZE_LOCK);
            
            sidebarPaneAnchor.setVisible(!hiding);
            sidebarPaneAnchor.setPrefWidth(PUI_WIDTH);
        });
        
        animationTimer.start();
        animation.play();
    }
    
    private void openSettings()
    {
        FXDialogTools.showControllableDialog(
                "Settings",
                null,
                0.0,
                FXDialogTools.OK,
                true,
                weaver.loadController(SettingsController.class)
        );
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TESTING ---">
    
    @FXML void onLoadScriptAction(ActionEvent event) { }
    
    @FXML void onRunScriptAction(ActionEvent event) { }
    
    @FXML void onPrintAction(ActionEvent event) { }
    
    @FXML void onDebugAction(ActionEvent event) { }
    
    //</editor-fold>
}
