package com.taco.suit_lady.view.ui;

import com.mongodb.client.MongoClients;
import com.taco.suit_lady._to_sort._expr.spring.beans.BaWT;
import com.taco.suit_lady.data._expr.mongodb.test_country.Country;
import com.taco.suit_lady.data._expr.mongodb.test_country.CountryService;
import com.taco.suit_lady.view.ui._expr.CountryCellController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <p><b>The {@link FXMainController main controller} for this {@link FXApplication JavaFX Application}.</b></p>
 * <br>
 * <hr>
 * <p>- The {@link FXMainController main controller} is responsible for containing, monitoring, and ensuring proper functionality of all JFX-related functionalities of the
 * application.</p>
 * <p>- In addition to standard {@code JavaFX Controller} functionality, the {@link FXMainController main controller} is responsible for {@link FxWeaver} and
 * {@link ApplicationContext Spring} implementation and integration.</p>
 * <hr>
 * <br>
 */
@Component
@FxmlView("MainView.fxml")
public class FXMainController
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    //<editor-fold desc="— FXML Fields —">
    
    @FXML private BorderPane basePane;
    @FXML private Button openTestDialogButton;
    @FXML private Label springTextLabel;
    @FXML private ImageView avatarView;
    @FXML private Label usernameLabel;
    
    @FXML private ListView<Country> leftGutterListView;
    private ObservableList<Country> countryList;
    
    //</editor-fold>
    
    // — Class Body — //
    
    /**
     * <p><b>Constructs the {@link FXMainController} singleton instance for this application.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Note:</b> The {@link FXMainController} is constructed and handled by the {@code Spring Framework}.</i></p>
     * <p><i>- See {@link FxWeaverInitializer#weave(Stage)} for details.</i></p>
     * <hr>
     * <br>
     *
     * @param weaver The {@link FxWeaver} instance for this application.
     * @param ctx    The {@link ConfigurableApplicationContext ApplicationContext} instance for this application.
     * @see FxWeaverInitializer#weave(Stage)
     */
    public FXMainController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
        initializeConstructor();
    }
    
    private void initializeConstructor()
    {
        countryList = FXCollections.observableArrayList();
        countryList.addAll(
                new Country("USA", 2342),
                new Country("Pakistan", 23)
        );
    }
    
    /**
     * <p><b>Executes initializations required for this controller to function properly.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Note:</b> This method is executed internally via the JFX engine, and is therefore in an immutable index of the applications internal initialization sequence
     * .</i></p>
     * <p><i>This method is executed on the {@code JavaFX Main Application Thread}.</i></p>
     * <hr>
     * <br>
     */
    @FXML public void initialize()
    {
        initialize_BaW();
        initialize_BaWT();
    }
    
    // — Bells & Whistles (BaW) — //
    
    @FXML public void onSpringTextPress() { }
    
    @FXML public void onAvatarClick()
    {
        ctx.getBean(BaWT.class).runBaWT_AvatarClick();
    }
    
    //<editor-fold desc="— BaW Initialization —">
    
    /**
     * <p><b>Contains BaW (Bell and Whistle) initialization code.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Note:</b> This method should NOT contain...</i></p>
     * <ul>
     *     <li><i>test initialization implementation. Instead, see {@link #initialize_BaWT()}.</i></li>
     *     <li><i>content that is unlikely to be used upon actual usage of this application.</i></li>
     * </ul>
     * <hr>
     * <br>
     */
    private void initialize_BaW()
    {
        leftGutterListView.setItems(countryList);
        leftGutterListView.setCellFactory(param -> weaver.loadController(CountryCellController.class));
        
    }
    
    /**
     * <p><b>Contains BaWT (Bell and Whistle Test) initialization code.</b></p>
     * <br><hr>
     * <p><i><b>Note:</b> Production-level runtime functionality should *never* depend on BaWT code.
     * <br>In other words, production-level functionality should be unaffected should all BaWT implementation be removed.</i></p>
     * <hr>
     * <br>
     */
    private void initialize_BaWT()
    {
        openTestDialogButton.setOnAction(actionEvent -> weaver.loadController(FXDialogController.class).show());
        initializeAvatar();
        usernameLabel.setText("TacoManStan");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="— Avatar Code —">
    
    private InputStream getFileFromResourceAsStream(String fileName)
    {
        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        
        // the stream holding the file content
        if (inputStream == null)
            throw new IllegalArgumentException("file not found! " + fileName);
        
        return inputStream;
    }
    
    private final int AVATAR_WIDTH = 50;
    private final int AVATAR_HEIGHT = 50;
    private final String AVATAR_PATH = "images/Flork_of_Taco.png";
    
    private void initializeAvatar()
    {
        InputStream in = getFileFromResourceAsStream(AVATAR_PATH);
        Image image = new Image(in, AVATAR_WIDTH, AVATAR_HEIGHT, true, true);
        avatarView.setImage(image);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="— BaW Testing —">
    
    private static void test2(ConfigurableApplicationContext ctx)
    {
        final MongoTemplate mOps = new MongoTemplate(MongoClients.create(), "TestDB");
        
        IntStream.range(0, 5).mapToObj(i -> CountryService.generateCountry()).forEach(mOps::insert);
        
        List<Country> countries = mOps.findAll(Country.class);
        System.out.println(countries);
        
        // mOps.dropCollection("country");
    }
    
    //</editor-fold>
}
