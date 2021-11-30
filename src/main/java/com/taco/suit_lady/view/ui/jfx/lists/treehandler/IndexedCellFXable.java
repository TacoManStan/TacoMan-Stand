package com.taco.suit_lady.view.ui.jfx.lists.treehandler;

import com.taco.suit_lady.view.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;

public interface IndexedCellFXable<T, C extends CellController<T>>
{
    CellControlManager<T, C> getCellControlManager();
}
