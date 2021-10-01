package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.util.collections.SelectionList;

public class UIPageGroup {

	private final SelectionList<UIPageHandler> pageList;

	public UIPageGroup() {
		this.pageList = TB.collections().presets().selectionArrayList();
	}

	public SelectionList<UIPageHandler> getPageList() {
		return pageList;
	}
}
