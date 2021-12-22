package com.taco.suit_lady.view.ui;

import com.taco.suit_lady._to_sort._expr.spring.beans.BaWT;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@FxmlView("DialogView.fxml")
public class FXDialogController
        implements ApplicationContextAware
{
    private Stage stage;
    
    @FXML private VBox dialog;
    @FXML private Label dialogLabel;
    
    //
    
    @FXML
    public void initialize()
    {
        stage = new Stage();
        stage.setScene(new Scene(dialog));
    }
    
    public void show()
    {
        stage.show();
    }
    
    //
    
    @FXML
    public void dialogButtonClicked()
    {
        dialogLabel.setText(ctx.getBean("bawt", BaWT.class).runBaWT_DialogContents());
    }
    
    //
    
    private ApplicationContext ctx;
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
    {
        this.ctx = ctx;
    }
}
