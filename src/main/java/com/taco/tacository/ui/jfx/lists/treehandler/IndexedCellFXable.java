package com.taco.tacository.ui.jfx.lists.treehandler;

import com.taco.tacository.ui.jfx.lists.CellControlManager;
import com.taco.tacository.ui.ui_internal.controllers.CellController;

import java.io.Serializable;

public interface IndexedCellFXable<T extends Serializable, C extends CellController<T>>
{
    CellControlManager<T, C> getCellControlManager();
}
