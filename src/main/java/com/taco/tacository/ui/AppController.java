package com.taco.tacository.ui;

import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.logic.triggers.Galaxy;
import com.taco.tacository.ui.jfx.components.button.ImageButton;
import com.taco.tacository.ui.jfx.components.ImagePane;
import com.taco.tacository.ui.pages.entity_debug_page.EntityDebugPage;
import com.taco.tacository.ui.pages.example_page.ExamplePage;
import com.taco.tacository.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentSelectorPage;
import com.taco.tacository.ui.pages.tester_page.TesterPage;
import com.taco.tacository.ui.ui_internal.controllers.SettingsController;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.*;
import com.taco.tacository.util.tools.fx_tools.DialogsFX;
import com.taco.tacository.util.tools.fx_tools.FX;
import com.taco.tacository.util.tools.printing.Printer;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
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
        implements Springable {
    
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
    @FXML private AnchorPane contentAnchorPane;
    
    @FXML private BorderPane footerRootPane;
    @FXML private AnchorPane footerPane;
    
    //    @FXML private TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> consoleTree;
    
    @FXML private Label bookshelfTitleLabel;
    
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
    
    @FXML private Label upsLabel;
    
    // </editor-fold>
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private double STAGE_MIN_WIDTH;
    private double STAGE_MIN_HEIGHT;
    
    //
    
    private Stage stage;
    
    private StackPane globalOverlayStackPane;
    private ObjectBinding<StackPane> selectionOverlayStackPaneBinding;
    private ReadOnlyListWrapper<Parent> globalOverlays;
    
    public AppController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
    }
    
    // <editor-fold desc="--- INITIALIZATION ---">
    
    // Called automatically by FXML loader.
    // As some properties may not yet be finished loading, all actual UI initialization should be done in initialize(Stage).
    @FXML public final void initialize() {
        ui().setController(this);
        ui().setSidebar(new Sidebar(weaver(), ctx(), sidebarChildButtonsPane, sidebarContentPane, backImagePane, sidebarButtonBar));
    }
    
    public final void initialize(@NotNull Stage stage) {
        debugger().enableAll();
        
        this.stage = stage;
        
        // TODO: Synchronize with the actual title of the application.
        this.stage.titleProperty().set("Suit Lady");
        
        this.stage.getIcons().add(Stuff.getImage("buttons/logo/", "logo", "png"));
        
        sidebarImagePane.setRotationAxis(Rotate.Y_AXIS);
        sidebarPaneAnchor.managedProperty().bind(sidebarPaneAnchor.visibleProperty());
        
        ui().init();
        
        contentAnchorPane.getChildren().add(ui().getContentManager().getContentBasePane());
        FX.setAnchors(ui().getContentManager().getContentBasePane());
        
        initImageButtons();
        initSidebar();
        initContent();
        
        //        console().consolify(
        //                new ConsoleUIDataContainer(
        //                        consoleTree,
        //                        consoleTRiBotCheckBox.selectedProperty(),
        //                        consoleClientCheckBox.selectedProperty(),
        //                        consoleScriptCheckBox.selectedProperty(),
        //                        consoleSelectedInstanceOnlyCheckBox.selectedProperty()
        //                ));
        
        FX.constructDraggableNode(dragBar);
        FX.constructResizableNode(getStage(), cornerResizePane, topResizePane, bottomResizePane, leftResizePane, rightResizePane,
                                  minimizeImagePane, maximizeImagePane, closeImagePane, settingsImagePane, sidebarImagePane);
        
        bookshelfTitleLabel.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    final SidebarBookshelf bookshelf = ui().getSidebar().getSelectedBookshelf();
                    return bookshelf != null ? bookshelf.getName() : "No Bookshelf Selected";
                }, ui().getSidebar().selectedBookshelfProperty()));
        
        //        initOverlays();
        
        stage.show();
        onShownInit();
        
        //
        
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            Printer.print("Filtering Key Event: " + event.getCode());
            if (ui().getContentManager().submitKeyEvent(event, true))
                event.consume();
            getGameContent().getController().taskManager().addTask(
                    Galaxy.newOneTimeTask(
                            getGameContent().getController(),
                            () -> ui().getContentManager().submitKeyEvent(event, false)));
        });
        
        logiCore().init(getGameContent());
        
        //
        
        upsLabel.textProperty().bind(Bind.stringBinding(() -> "" + logiCore().getUps(), logiCore().readOnlyUpsProperty()));
    }
    
    private void initSidebar() {
        final Sidebar sidebar = ui().getSidebar();
        
        final SidebarBookshelf generalSidebarBookshelf = new SidebarBookshelf(sidebar, "General", true);
        final SidebarBookshelf nyiSidebarBookshelf = new SidebarBookshelf(sidebar, "Not Yet Implemented", true);
        final SidebarBookshelf demoSidebarBookshelf = new SidebarBookshelf(sidebar, "Demo", true);
        final SidebarBookshelf numberSidebarBookshelf = new SidebarBookshelf(sidebar, "Num", true);
        
        //<editor-fold desc="Bookshelves">
        
        //<editor-fold desc="General SidebarBookshelf">
        
        generalSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Mandelbrot Content Selector Demo",
                "mandelbrot2",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getUID(uiBook.getButtonID()),
                        () -> new MandelbrotContentSelectorPage(uiBook)),
                null));
        generalSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Development",
                "game_engine",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "green")),
                null));
        generalSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Avatar ImageButton Demo",
                "avatar",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "red")),
                null));
        generalSidebarBookshelf.getButtonGroup().selectFirst();
        
        final SidebarBookshelf inDevelopmentSidebarBookshelf = new SidebarBookshelf(sidebar, "In Development", true);
        inDevelopmentSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Entity Debug",
                "entity-debug",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new EntityDebugPage(uiBook)),
                null));
        inDevelopmentSidebarBookshelf.getButtonGroup().selectFirst();
        
        //</editor-fold>
        
        //<editor-fold desc="NYI SidebarBookshelf">
        
        nyiSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Social",
                "play",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "blue")),
                null));
        nyiSidebarBookshelf.getButtonGroup().selectFirst();
        
        //</editor-fold>
        
        //<editor-fold desc="Demo SidebarBookshelf">
        
        demoSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Demo 1",
                "calendar",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "gray")),
                null));
        demoSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Demo 2",
                "social",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "gray")),
                null));
        demoSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Demo 3",
                "popout_var2",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "gray")),
                null));
        demoSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Demo 4",
                "account_manager",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "gray")),
                null));
        demoSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Demo 5",
                "entity_debug",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "gray")),
                null));
        demoSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Demo 6",
                "graph",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new ExamplePage(uiBook, "gray")),
                null));
        demoSidebarBookshelf.getBooks().add(new UIBook(
                this,
                "Tester",
                "demo",
                uiBook -> Stuff.get(
                        "pages",
                        uiBook.getButtonID(),
                        () -> new TesterPage(uiBook)),
                null));
        demoSidebarBookshelf.getButtonGroup().selectFirst();
        
        //</editor-fold>
        
        //<editor-fold desc="Number SidebarBookshelf (Test)">
        
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number One",
        //                "num_1",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "red")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Two",
        //                "num_2",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "green")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Three",
        //                "num_3",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "orange")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Four",
        //                "num_4",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "blue")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Five",
        //                "num_5",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "purple")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Six",
        //                "num_6",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "yellow")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Seven",
        //                "num_7",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "red")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Eight",
        //                "num_8",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "green")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Nine",
        //                "num_9",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "orange")),
        //                null));
        //        numberSidebarBookshelf.getBooks().add(new UIBook(
        //                this,
        //                "Number Zero",
        //                "num_0",
        //                uiBook -> TB.resources().get(
        //                        "pages",
        //                        uiBook.getButtonID(),
        //                        () -> new ExamplePage(uiBook, "blue")),
        //                null));
        //        numberSidebarBookshelf.getButtonGroup().selectFirst();
        
        //</editor-fold>
        
        //</editor-fold>
        
        sidebar.bookshelvesProperty().addAll(generalSidebarBookshelf, inDevelopmentSidebarBookshelf, nyiSidebarBookshelf, demoSidebarBookshelf);
        sidebar.initialize();
    }
    
    private void initImageButtons() {
        ImageButton settingsButton = new ImageButton(
                this,
                "Settings",
                "settings",
                settingsImagePane,
                null,
                this::openSettings,
                false,
                ImageButton.SMALL
        ).init();
        
        ImageButton toggleSidebarButton = new ImageButton(
                this,
                "Toggle Sidebar",
                "hide_sidebar",
                sidebarImagePane,
                null,
                this::toggleSidebar,
                false,
                ImageButton.SMALL
        ).init();
        
        ImageButton minimizeButton = new ImageButton(
                this,
                "Minimize",
                "minimize",
                minimizeImagePane,
                null,
                () -> stage.setIconified(!stage.isIconified()),
                false,
                ImageButton.SMALL
        ).init();
        
        ImageButton maximizeButton = new ImageButton(
                this,
                "Maximize",
                Bindings.createStringBinding(() -> stage.isMaximized() ? "maximize_both" : "maximize", stage.maximizedProperty()),
                maximizeImagePane,
                null,
                () -> stage.setMaximized(!stage.isMaximized()),
                false,
                ImageButton.SMALL
        ).init();
        
        ImageButton closeButton = new ImageButton(
                this,
                "Close",
                "close",
                closeImagePane,
                null,
                stage::close,
                false,
                ImageButton.SMALL
        ).init();
        
        ImageButton logoButton = new ImageButton(
                this,
                "Logo",
                "logo",
                logoImagePane,
                () -> Web.browse("google", true),
                null,
                false,
                new Point2D(20.0, 20.0)
        ).init();
        
        
        minimizeButton.setShowTooltip(false);
        maximizeButton.setShowTooltip(false);
        closeButton.setShowTooltip(false);
        
        logoButton.setShowTooltip(false);
        
        
        sidebarImagePane.visibleProperty().bind(Bindings.not(stage.maximizedProperty()));
    }
    
    private GameViewContent gameContent;
    
    private void initContent() {
        gameContent = (new GameViewContent(this)).init();
    }
    
    public final GameViewContent getGameContent() { return gameContent; }
    
    //
    
    private void onShownInit() {
        Stage stage = getStage();
        sidebarPane.setPrefWidth(PUI_WIDTH);
        STAGE_MIN_WIDTH = stage.getWidth() - PUI_WIDTH;
        STAGE_MIN_HEIGHT = stage.getHeight();
        FX.lockSize(stage, STAGE_MIN_WIDTH + PUI_WIDTH, STAGE_MIN_HEIGHT);
        Button b;
    }
    
    // </editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Stage getStage() { return stage; }
    
    protected final BorderPane footerRootPane() { return footerRootPane; }
    protected final AnchorPane footerPane() { return footerPane; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return ctx;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void toggleSidebar() {
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
        
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
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
            FX.lockSize(stage, end_stage_pref_width, FX.NO_CHANGE_SIZE_LOCK);
            
            sidebarPaneAnchor.setVisible(!hiding);
            sidebarPaneAnchor.setPrefWidth(PUI_WIDTH);
        });
        
        animationTimer.start();
        animation.play();
    }
    
    private void openSettings() {
        DialogsFX.showControllableDialog(
                "Settings",
                null,
                0.0,
                DialogsFX.OK,
                true,
                weaver.loadController(SettingsController.class));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TESTING ---">
    
    @FXML void onLoadScriptAction(ActionEvent event) { }
    
    @FXML void onRunScriptAction(ActionEvent event) { }
    
    @FXML void onPrintAction(ActionEvent event) { }
    
    @FXML void onDebugAction(ActionEvent event) { }
    
    //</editor-fold>
}
