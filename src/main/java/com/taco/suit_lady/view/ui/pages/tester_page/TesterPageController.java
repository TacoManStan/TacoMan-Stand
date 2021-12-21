package com.taco.suit_lady.view.ui.pages.tester_page;

import com.github.cliftonlabs.json_simple.Jsoner;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.taco.suit_lady.view.ui.ui_internal.controllers.SidebarNodeGroupController;
import com.taco.tacository.json.JUtil;
import com.taco.tacository.json.TestData;
import com.taco.tacository.json.TestData2;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.bson.Document;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        String jsonString = JUtil.getJson(testData);
        System.out.println("JSON String:");
        System.out.println(jsonString);
        
        JUtil.saveMongoDB("test", "test-data", testData);
    }
    
    private void runTest2() {
        System.out.println("Running Test 2");
        System.out.println();
    
//        List<TestData> list = JUtil.loadMongoDB("test", "test-data");
        List<TestData2> list = JUtil.loadMongoDB("test", "test-data-2", () -> new TestData2());
        ArrayList<String> prettyList = list.stream().map(data -> Jsoner.prettyPrint(
                JUtil.getJson(data))).collect(Collectors.toCollection(ArrayList::new));
        
        debugger().setPrintEnabled(true);
        debugger().printList(list, "TestData2 MongoDB");
        debugger().printList(prettyList, "TestData2 MongoDB Pretty");
    
        TestData2 testData = JUtil.load("test-data-2", new TestData2());
        System.out.println("Test Data 2 (From File):");
        testData.print();
    }
    
    private void runTest3() {
        System.out.println("Running Test 3");
        System.out.println();
    
        TestData2 testData = new TestData2("test-data-2", Color.CYAN, "Test String HeheXD", 2222);
        String jsonString = JUtil.getJson(testData);
        System.out.println("JSON String:");
        System.out.println(jsonString);
        JUtil.saveMongoDB("test", "test-data-2", testData);
        JUtil.save(testData);
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