package com.taco.suit_lady.view.ui.pages.tester_page;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.taco.suit_lady.view.ui.ui_internal.controllers.SidebarNodeGroupController;
import com.taco.tacository.json.JUtil;
import com.taco.tacository.json.TestData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.bson.Document;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("/fxml/sidebar/pages/tester_page/tester_page.fxml")
@Scope("prototype")
public class TesterPageController extends SidebarNodeGroupController<TesterPage> {
    
    @FXML private BorderPane root;
    @FXML private Button test1Button;
    @FXML private Button test2Button;
    @FXML private Button test3Button;
    
    public TesterPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    private void runTest1() {
        System.out.println("Running Test 1");
        System.out.println();
        
        //        CountryService service = ctx().getBean(CountryService.class);
        //        service.save(new Country("HEHEXD", 333));
        //        service.save(new Country("Country 2", 1));
        //        service.print();
        TestData testData = JUtil.load("jid-test", new TestData());
        String jsonString = JUtil.getAsString(testData);
        JUtil.saveMongoDB(testData);
    }
    
    private void runTest2() {
        System.out.println("Running Test 2");
        System.out.println();
    
        List<TestData> list = JUtil.loadMongoDB("test", "test-data");
    }
    
    private void runTest3() {
        System.out.println("Running Test 3");
        System.out.println();
    }
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    @FXML public void initialize() {
        test1Button.setOnAction(event -> runTest1());
        test2Button.setOnAction(event -> runTest2());
        test3Button.setOnAction(event -> runTest3());
    }
    
    @Override
    protected void onPageBindingComplete() { }
}