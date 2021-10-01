package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.view.ui.jfx.button.BoundButtonViewGroup;
import com.taco.suit_lady.view.ui.jfx.button.ButtonViewGroup;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class SidebarNodeGroup extends UINodeGroup {

	private final ReentrantLock lock;
	private final ReadOnlyStringWrapper nameProperty;

	private final Sidebar owner;
	private final VBox buttonBox;
	private final Button button;

	private final BoundButtonViewGroup<UINode> nodeButtonGroup;

	private final ReadOnlyBooleanWrapper selectedProperty;
	private final ReadOnlyObjectWrapper<Runnable> clickResponderProperty;

	public SidebarNodeGroup(Sidebar owner, Button menuButton) {
		this(owner, menuButton, new StackPane());
	}

	public SidebarNodeGroup(Sidebar owner, Button menuButton, StackPane contentPane) {
		super(contentPane);

		this.lock = owner.getLock();
		this.nameProperty = new ReadOnlyStringWrapper();

		this.owner = owner;
		this.buttonBox = new VBox();
		this.button = menuButton;

		this.nodeButtonGroup = new BoundButtonViewGroup<>(getNodes(), lock);

		this.selectedProperty = new ReadOnlyBooleanWrapper();
		this.clickResponderProperty = new ReadOnlyObjectWrapper<>();

		//

		this.nodeButtonGroup.selectedButtonProperty().addListener((observable, oldButton, newButton) -> getNodeDisplayer().setDisplay(this.nodeButtonGroup.getViewableByButton(newButton)));
	}

	protected void initialize() {
		lock.lock();
		try {
			List<UINode> nodes = getNodes();
			for (UINode node : nodes) {
				ImageButton imageButton = node.getButtonView();
				if (imageButton != null)
					buttonBox.getChildren().add(imageButton.getImagePane());
				node.setGroup(this);
				buttonBox.setSpacing(1.0);
				buttonBox.setAlignment(Pos.TOP_LEFT);
			}
			button.setOnAction(event -> select());
		} finally {
			lock.unlock();
		}
	}

	//<editor-fold desc="Properties">

	public ReadOnlyStringProperty nameProperty() {
		return nameProperty.getReadOnlyProperty();
	}

	public String getName() {
		return nameProperty.get();
	}

	//

	protected Sidebar getOwner() {
		return owner;
	}

	protected VBox getButtonBox() {
		return buttonBox;
	}

	protected Button getButton() {
		return button;
	}

	//

	public ButtonViewGroup getButtonViewGroup() {
		return nodeButtonGroup;
	}

	//

	public void clearSelection() {
		nodeButtonGroup.clearSelection();
	}

	//

	public ReadOnlyBooleanProperty selectedProperty() {
		return selectedProperty.getReadOnlyProperty();
	}

	public boolean isSelected() {
		return selectedProperty.get();
	}

	private void setSelected(boolean selected) {
		selectedProperty.set(selected);
	}

	//

	public ReadOnlyObjectProperty<Runnable> clickResponderProperty() {
		return clickResponderProperty.getReadOnlyProperty();
	}

	public Runnable getClickResponder() {
		return clickResponderProperty.get();
	}

	//</editor-fold>

	protected void select() {
		owner.setSelectedNodeGroup(this);
	}
}
