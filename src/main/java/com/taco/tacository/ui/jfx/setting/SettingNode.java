package com.taco.tacository.ui.jfx.setting;

import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.settings.SavableSetting;
import com.taco.tacository.util.tools.fx_tools.FX;
import com.taco.tacository._to_sort.obj_traits.common.Nameable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public abstract class SettingNode<V, T extends Region, U extends SavableSetting> extends HBox
        implements Nameable {
    
    private final String name;
    private final U setting;
    
    private final Label nameLabel;
    private final T inputNode;
    
    private final NodeOrder nodeOrder;
    
    public SettingNode(String name, NodeOrder nodeOrder) {
        this.name = Exc.nullCheck(name, "SettingNode Name");
        this.setting = createSetting();
        
        this.nameLabel = new Label(getName());
        this.inputNode = Exc.nullCheck(createInputNode(), "Input Node");
        
        this.nodeOrder = Exc.nullCheck(nodeOrder, "Node Order");
        
        //
        
        this.initialize();
    }
    
    //<editor-fold desc="Initialize">
    
    private void initialize() {
        getSetting().createBinding(getInputNode());
        
        FX.runFX(() -> {
            setFillHeight(true);
            setSpacing(5);
            
            setMinWidth(0);
            setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            
            //
            
            nameLabel.setMinWidth(0);
            inputNode.setMinWidth(0);
            
            nameLabel.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            inputNode.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            
            //
            
            if (nodeOrder == NodeOrder.NAME_CONTENT) {
                getChildren().add(nameLabel);
                getChildren().add(inputNode);
                HBox.setHgrow(nameLabel, Priority.NEVER);
                HBox.setHgrow(inputNode, Priority.ALWAYS);
            } else if (nodeOrder == NodeOrder.CONTENT_NAME) {
                getChildren().add(inputNode);
                getChildren().add(nameLabel);
                HBox.setHgrow(nameLabel, Priority.ALWAYS);
                HBox.setHgrow(inputNode, Priority.NEVER);
            } else
                throw Exc.unsupported("Unknown NodeOrder: " + nodeOrder);
        }, true);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Properties">
    
    @Override public final String getName() {
        return name;
    }
    
    public final T getInputNode() {
        return inputNode;
    }
    
    public final U getSetting() {
        return setting;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Abstract">
    
    protected abstract T createInputNode(Object... params);
    protected abstract U createSetting();
    public abstract V getValue();
    
    //</editor-fold>
    
    //<editor-fold desc="Classes">
    
    public enum NodeOrder {
        NAME_CONTENT,
        CONTENT_NAME
    }
    
    //</editor-fold>
}

/*
 * TODO LIST:
 * [S] When multiple SettingNodes are added as a list, the
 */