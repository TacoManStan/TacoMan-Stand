package com.taco.suit_lady.ui.jfx.lists.treehandler;

import com.taco.suit_lady.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;

public interface IndexedCellFXable<T, C extends CellController<T>>
{
    CellControlManager<T, C> getCellControlManager();
}
