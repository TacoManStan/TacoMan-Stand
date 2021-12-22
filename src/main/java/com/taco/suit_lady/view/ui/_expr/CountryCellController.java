package com.taco.suit_lady.view.ui._expr;

import com.taco.suit_lady.data._expr.mongodb.test_country.Country;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p><b><i>{@link ListCell}</i> implementation defining how to display a <i>{@link Country}</i> in a <i>{@link ListView JavaFX ListView}</i>.</b></p>
 * <br>
 * <hr>
 * <p>- When using {@link FxWeaver}, the {@link FxWeaver} is responsible for creating and managing JavaFX Views and Controllers.</p>
 * <p>- {@link FxWeaver} uses the {@code Spring Framework} to handle JavaFX elements, controllers, views, etc.</p>
 * <p>- To setup a JavaFX Controller for use with {@link FxWeaver}, the {@code JavaFX Controller} must be configured as a {@code Spring Bean} using either the {@link Bean @Bean}
 * or {@link Component @Component} annotation, depending on circumstances.</p>
 * <p>- The {@link FxmlView @FxmlView} annotation completes the configuration process by specifying the {@code FXML} document for this {@code JavaFX Controller}.</p>
 * <hr><br>
 * <p><i><b>TODO: </b>Move generic instructional documentation (shown above) to appropriate location.</i></p>
 * <br>
 */
@Component
@Scope("prototype")
@FxmlView("CountryCell.fxml")
public class CountryCellController extends ListCell<Country> {
    @FXML private Pane cellContainer;
    
    @FXML private Label countryNameLabel;
    @FXML private Label countryPopulationLabel;
    @FXML private ImageView countryIcon;
    
    //
    
    @FXML
    public void initialize() { }
    
    @Override
    protected void updateItem(Country item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty || item == null) {
            setText("EMPTY");
            setGraphic(null);
        } else {
            countryNameLabel.setText(item.getName());
            countryPopulationLabel.setText("" + item.getPopulation());
            if (countryIcon.getImage() == null)
                countryIcon.setImage(new Image("https://i.imgur.com/GYuCVtS.png"));
            
            setText(null);
            setGraphic(cellContainer);
        }
    }
}
