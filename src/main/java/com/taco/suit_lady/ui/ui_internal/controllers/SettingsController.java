package com.taco.suit_lady.ui.ui_internal.controllers;

import com.taco.suit_lady.ui.content.TransitionContentView;
import com.taco.suit_lady.ui.jfx.dialog.DialogController;
import com.taco.suit_lady.ui.ui_internal.settings.SettingContainer;
import com.taco.suit_lady.ui.ui_internal.settings.SettingGroup;
import com.taco.suit_lady.ui.ui_internal.settings.SettingsTitledPane;
import com.taco.suit_lady.util.tools.BindingTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
@FxmlView("/fxml/settings/settings.fxml")
@Scope("prototype")
public class SettingsController extends DialogController<Void>
{
    
    //<editor-fold desc="FXML">
    
    @FXML private AnchorPane root;
    
    @FXML private Label titleLabel;
    @FXML private StackPane contentPane;
    @FXML private Accordion categoryAccordion;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    
    private Binding<SettingsTitledPane> selectedPaneBinding;
    private Binding<SettingGroup<? extends SettingGroupController>> selectedGroupBinding;
    
    private TransitionContentView<Pane> contentView;
    
    public SettingsController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
    }
    
    @Override public Pane root()
    {
        return root;
    }
    
    //<editor-fold desc="Initialize">
    
    @Override public void initialize()
    {
        this.contentView = new TransitionContentView<>(contentPane);
        
        this.selectedPaneBinding = Bindings.createObjectBinding(this::getExpandedPane, categoryAccordion.expandedPaneProperty());
        this.selectedGroupBinding = BindingTools.createRecursiveBinding(
                (TitledPane pane) -> pane != null
                        ? ((SettingsTitledPane) pane).selectedGroupProperty()
                        : BindingTools.createObjectBinding(null),
                expandedPaneProperty()
        );
        
        //
        
        selectedGroupBinding.addListener((observable, oldGroup, newGroup) -> {
            if (newGroup != null)
                contentView.setContent(newGroup.getController().root());
            else
                contentView.setContent(null);
        });
        
        initSettingGroups();
    }
    
    //<editor-fold desc="Initialize Helpers">
    
    private void initSettingGroups()
    {
        //		SettingContainer _generalContainer = new SettingContainer("General Settings");
        //		SettingContainer _graphicsContainer = new SettingContainer("Graphics Settings");
        //
        //		addSettingContainer(_generalContainer);
        //		addSettingContainer(_graphicsContainer);
        
        initTestSettingGroups();
    }
    
    private void initTestSettingGroups()
    {
        SettingContainer _testContainer1 = new SettingContainer("Test Container 1");
        SettingContainer _testContainer2 = new SettingContainer("Test Container 2");
        
        addSettingContainer(_testContainer1);
        addSettingContainer(_testContainer2);
        
        // CHANGE-HERE - how the fuck do I get this to work with FXWeaver/Spring...
        for (int i = 0; i < 3; i++)
            _testContainer1.settingGroups().add(new SettingGroup(() -> weaver().loadController(TestSettingGroupController.class)));
        for (int i = 0; i < 3; i++)
            _testContainer2.settingGroups().add(new SettingGroup(() -> ctx().getBean(TestSettingGroupController.class))); // CHANGE-HERE
        //            _testContainer2.settingGroups().add(new SettingGroup(TestSettingGroupController::new));
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="Properties">
    
    public final Binding<SettingsTitledPane> selectedContainerBinding()
    {
        return selectedPaneBinding;
    }
    
    public final SettingsTitledPane getSelectedContainer()
    {
        return selectedPaneBinding.getValue();
    }
    
    //
    
    private ReadOnlyObjectProperty<TitledPane> expandedPaneProperty()
    {
        return categoryAccordion.expandedPaneProperty();
    }
    
    private SettingsTitledPane getExpandedPane()
    {
        return (SettingsTitledPane) categoryAccordion.getExpandedPane();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Implementation">
    
    @Override
    protected Void loadValue()
    {
        return null;
    }
    
    @Override
    public void onClose() { }
    
    //</editor-fold>
    
    //
    
    private void addSettingContainer(SettingContainer settingContainer)
    {
        ExceptionTools.nullCheck(settingContainer, "Setting Container");
        SettingsTitledPane _pane = new SettingsTitledPane(settingContainer);
        //		_pane.setAnimated(false);
        categoryAccordion.getPanes().add(_pane);
    }
}

/*
 * NOTE LIST:
 * [S] SettingGroups must be added to a SettingContainer AFTER the SettingContainer has been added via the addSettingContainer(...) method.
 */