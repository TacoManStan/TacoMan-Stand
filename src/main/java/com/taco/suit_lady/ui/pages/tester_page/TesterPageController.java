package com.taco.suit_lady.ui.pages.tester_page;

import com.github.cliftonlabs.json_simple.Jsoner;
import com.taco.suit_lady.ui.ui_internal.controllers.SidebarNodeGroupController;
import com.taco.tacository.json.JFiles;
import com.taco.tacository.json.TestData;
import com.taco.tacository.json.TestData2;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
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
        TestData testData = JFiles.load("jid-test", new TestData());
        String jsonString = JFiles.getJson(testData);
        System.out.println("JSON String:");
        System.out.println(jsonString);
    
        JFiles.saveMongoDB("test", "test-data", testData);
    }
    
    private void runTest2() {
        System.out.println("Running Test 2");
        System.out.println();
    
//        List<TestData> list = JUtil.loadMongoDB("test", "test-data");
        List<TestData2> list = JFiles.loadMongoDB("test", "test-data-2", () -> new TestData2());
        ArrayList<String> prettyList = list.stream().map(data -> Jsoner.prettyPrint(
                JFiles.getJson(data))).collect(Collectors.toCollection(ArrayList::new));
        
        debugger().setStatusEnabled(true);
        debugger().printList(list, "TestData2 MongoDB");
        debugger().printList(prettyList, "TestData2 MongoDB Pretty");
    
        TestData2 testData = JFiles.load("test-data-2", new TestData2());
        System.out.println("Test Data 2 (From File):");
        testData.print();
    }
    
    private void runTest3() {
        System.out.println("Running Test 3");
        System.out.println();
    
        TestData2 testData = new TestData2("test-data-2", Color.CYAN, "Test String HeheXD", 2222);
        String jsonString = JFiles.getJson(testData);
        System.out.println("JSON String:");
        System.out.println(jsonString);
        JFiles.saveMongoDB("test", "test-data-2", testData);
        JFiles.save(testData);
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