package com.taco.tacository.game.ui.nia.framework;

import com.taco.tacository.game.GameMap;
import com.taco.tacository.game.objects.tiles.GameTile;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.game.ui.GameViewContentData;
import com.taco.tacository.ui.AppUI;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.tools.Props;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

/**
 * <p>Contains all {@link Property Properties} pertaining to {@link AppUI UI} values and members.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@link NiaUIData} instance for a {@link GameViewContent} object can be accessed using the <i>{@link GameViewContentData#getUIData()}</i> method.</li>
 *     <li>
 *         Example {@link NiaUIData} Member
 *         <ul>
 *             <li>The <i>{@link #selectedTileProperty()}</i> defines the {@link GameTile} currently selected on the currently-visible {@link GameMap} instance.</li>
 *         </ul>
 *     </li>
 * </ol>
 */
//TO-EXPAND
public class NiaUIData
        implements SpringableWrapper, Lockable {
    
    private final NiaContentData parent;
    
    private final ReadOnlyObjectWrapper<GameTile> selectedTileProperty;
    
    protected NiaUIData(@NotNull NiaContentData parent) {
        this.selectedTileProperty = new ReadOnlyObjectWrapper<>();
        this.selectedTileProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.getModel().setBorderShowing(false);
            if (newValue != null)
                newValue.getModel().setBorderShowing(true);
        });
        
        this.parent = parent;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final ReadOnlyObjectWrapper<GameTile> selectedTileProperty() { return selectedTileProperty; }
    public final ReadOnlyObjectProperty<GameTile> readOnlySelectedTileProperty() { return selectedTileProperty.getReadOnlyProperty(); }
    public final GameTile getSelectedTile() { return selectedTileProperty.get(); }
    protected final GameTile setSelectedTile(GameTile newValue) { return Props.setProperty(selectedTileProperty, newValue); }
    
    //</editor-fold>
    
    @Override public @NotNull Springable springable() { return parent; }
    @Override public @NotNull Lock getLock() { return parent.getLock(); }
}
