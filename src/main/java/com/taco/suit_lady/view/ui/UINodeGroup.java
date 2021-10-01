package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.BindingTools;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * A UI element that can be displayed
 */
public abstract class UINodeGroup
		implements Displayable
{

	private final Displayer<UINode> nodeDisplayer;
	private final UIPageGroup pageGroup;

	private final ObservableList<UINode> nodes;

	private final BooleanBinding hasPagedContentBinding;

	public UINodeGroup(StackPane contentPane) {
		this(contentPane, null);
	}

	public UINodeGroup(StackPane contentPane, UIPageGroup pageGroup) {
		this.nodeDisplayer = new Displayer<>(contentPane);
		this.pageGroup = pageGroup;

		this.nodes = FXCollections.observableArrayList();


		Binding<Boolean> temp_binding = BindingTools.get().recursiveBinding((UINode uiNode) -> {
			if (uiNode != null) {
				UIPageHandler pageHandler = uiNode.getPageHandler();
				if (pageHandler != null)
					return pageHandler.hasPagedContentBinding();
			}
			return null;
		}, nodeDisplayer.displayProperty());
		this.hasPagedContentBinding = javafx.beans.binding.Bindings.createBooleanBinding(() -> {
			Boolean hasPagedContent = temp_binding.getValue();
			return hasPagedContent != null && hasPagedContent;
		}, temp_binding);
	}

	//<editor-fold desc="Properties">

	public Displayer<UINode> getNodeDisplayer() {
		return nodeDisplayer;
	}

	public UIPageGroup getPageGroup() {
		return pageGroup;
	}

	//

	public ObservableList<UINode> getNodes() {
		return nodes;
	}

	//

	public BooleanBinding hasPagedContentBinding() {
		return hasPagedContentBinding;
	}

	public boolean hasPagedContent() {
		return hasPagedContentBinding.getValue();
	}

	//</editor-fold>

	//

	@Override public Pane getContent() {
		return nodeDisplayer.getDisplayContainer();
	}
}
