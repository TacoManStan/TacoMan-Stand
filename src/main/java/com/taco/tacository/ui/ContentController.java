package com.taco.tacository.ui;

import com.taco.tacository.game.ui.GFXObject;
import com.taco.tacository.logic.TaskManager;
import com.taco.tacository.logic.Tickable;
import com.taco.tacository.logic.triggers.Galaxy;
import com.taco.tacository.ui.ui_internal.controllers.Controller;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.Exe;
import com.taco.tacository.util.tools.fx_tools.FX;
import com.taco.tacository.util.values.numbers.Num2D;
import javafx.beans.property.ObjectProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>The {@link Content} module defining the {@code JavaFX Components} of a specific {@link Content} implementation.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>
 *         As an implementation of {@link Controller}, {@link ContentController} instances are auto-constructed by the {@link SpringApplication Spring Framework}.
 *         <ul>
 *             <li><i>See {@link Controller} for in-depth instructions on {@link Controller} implementation.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 * <br><hr><br>
 * <p><b>Initialization</b></p>
 * <p>{@link ContentController} offers {@code 2} separate initialization scopes in addition to the {@link ContentController} {@link ContentController#ContentController(FxWeaver, ConfigurableApplicationContext) Constructor}.</p>
 * <ol>
 *     <li>
 *         <b>{@link #initialize()}</b>
 *         <ul>
 *             <li>Called automatically by the {@code JavaFX} framework.</li>
 *             <li>Guaranteed to execute on the {@link FX#isFXThread() JavaFX Application Thread}.</li>
 *             <li>Called immediately succeeding {@link ContentController} {@link ContentController#ContentController(FxWeaver, ConfigurableApplicationContext) Construction}.</li>
 *             <li>Called prior to <i>{@link #init(Content)}</i>.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>{@link #init(Content)}</b>
 *         <ul>
 *             <li>Called automatically by the {@link Content} {@link #getContent() Owner} of this {@link ContentController} instance from within the <i>{@link Content#init()}</i> method.</li>
 *             <li>Not guaranteed to execute on a specific {@link Thread} — however, <i>{@link #init(Content)}</i> is most commonly executed on the {@link FX#isFXThread() JavaFX Application Thread}.</li>
 *             <li>Called subsequent to both {@link ContentController} {@link ContentController#ContentController(FxWeaver, ConfigurableApplicationContext) Construction} and {@link #initialize() JavaFX Initialization}.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>Initialization Order</b>
 *         <ol>
 *             <li><i>new {@link Controller#Controller(FxWeaver, ConfigurableApplicationContext)}</i></li>
 *             <li><i>new {@link ContentController#ContentController(FxWeaver, ConfigurableApplicationContext)}</i></li>
 *             <li><i>{@link Controller#initialize()}</i></li>
 *             <li><i>{@link ContentController#initialize()}</i></li>
 *             <li><i>{@link ContentController#init(Content)}</i></li>
 *             <li><i>Implementing class initialization</i></li>
 *         </ol>
 *     </li>
 * </ol>
 * <br><hr><br>
 * <p><b>Properties</b></p>
 * <ol>
 *     <li><i><b>{@link #getContent()}:</b> Returns the {@link Content} object this {@link ContentController} is assigned to.</i></li>
 *     <li><i><b>{@link #getContentPane()}:</b> Defines the {@link Pane JavaFX Pane} containing the primary content of this {@link ContentController}.</i></li>
 *     <li><i><b>{@link #getMouseOnContent()}:</b> An {@link ObjectProperty} containing the current {@link MouseEvent Mouse} {@link Num2D Pixel Coordinates} relative to the {@link #getContentPane() ContentPane} of this {@link ContentController} instance.</i></li>
 *     <li><i><b>{@link #getMouseOnContentSafe()}:</b> Identical to {@link #getMouseOnContent()} except {@link #getMouseOnContentSafe()} guarantees the {@link Num2D Pixel Coordinates} are within the bounds of the {@link #getContentPane() Content Pane}.</i></li>
 * </ol>
 * <br><hr><br>
 * <p><b>Logic</b></p>
 * <ol>
 *     <li>
 *         <b>{@link TaskManager}</b>
 *         <ul>
 *             <li>The {@link #taskManager() Task Manager} assigned to a {@link ContentController} is used primarily to handle {@link GFXObject} operations.</li>
 *             <li>In most cases, the {@link ContentController} {@link #taskManager() Task Manager} should only be used to process {@link #updateGfx() Graphics Updates}.</li>
 *             <li>However, full {@link TaskManager} and {@link Tickable} functionality is available to all {@link ContentController} instances if needed.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>GFX Operations</b>
 *         <ul>
 *             <li>Use <i>{@link #addOperation(Runnable)}</i> to submit a new {@link GFXObject GFXObject Operation}.</li>
 *             <li>All {@link Runnable Runnables} passed to <i>{@link #addOperation(Runnable)}</i> are executed automatically by the {@link GFXObject} framework, defined by the {@link ContentController} <i>{@link #updateGfx()}</i> implementation.</li>
 *             <li>The {@link #addOperation(Runnable) Graphics Operations} are cleared subsequent to every call to <i>{@link #updateGfx()}.</i></li>
 *             <li>As a result, all {@link Runnable Graphics Operations} submitted to <i>{@link #addOperation(Runnable)}</i> are executed <i>once</i> and <i>only once</i>.</li>
 *             <li>
 *                 Place persistent/periodic {@link Runnable Graphics Operation Logic} in the <i>{@link #onGfxUpdate()}</i> and/or <i>{@link #onGfxUpdateAlways()}</i> methods of the implementing {@link ContentController} object.
 *                 <ul>
 *                     <li><i>See {@link GFXObject} for details.</i></li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T>  The {@link Content} implementation assigned to the {@link Content} owning this {@link ContentController} instance.
 * @param <TD> The {@link ContentData} implementation assigned to the {@link Content} owning this {@link ContentController} instance.
 * @param <TC> The {@link ContentController} implementation assigned to the {@link Content} owning this {@link ContentController} instance.
 * @param <F>  The {@link Footer} implementation assigned to the {@link Content} owning this {@link ContentController} instance.
 * @param <FC> The {@link FooterController} implementation assigned to the {@link Content} owning this {@link ContentController} instance.
 */
public abstract class ContentController<T extends Content<T, TD, TC, F, FC>, TD extends ContentData<T, TD, TC, F, FC>, TC extends ContentController<T, TD, TC, F, FC>,
        F extends Footer<F, FC, T, TD, TC>, FC extends FooterController<F, FC, T, TD, TC>>
        extends Controller
        implements GFXObject<TC> {
    
    private final ReentrantLock gfxLock;
    
    private TaskManager<TC> taskManager;
    private T content;
    
    //
    
    private final ArrayList<Runnable> gfxOperations;
    private boolean needsUpdate;
    
    public ContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.gfxLock = new ReentrantLock();
        
        this.content = null;
        
        this.gfxOperations = new ArrayList<>();
        this.needsUpdate = false;
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public void initialize() { }
    
    public TC init(@NotNull T content) {
        if (this.content != null)
            throw Exc.unsupported("Content has already been set (" + getContent() + ")");
        this.content = content;
        
        this.taskManager = new TaskManager<>((TC) this).init();
        initMouseEventHandling();
        
        
        return (TC) this;
    }
    
    private void initMouseEventHandling() {
        getContentPane().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (getContent().handleMousePressEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMousePressEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (getContent().handleMouseReleaseEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseReleaseEvent(event, false)));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (getContent().handleMouseMoveEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseMoveEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (getContent().handleMouseDragEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseDragEvent(event, false)));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            if (getContent().handleMouseEnterEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseEnterEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            if (getContent().handleMouseExitEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseExitEvent(event, false)));
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public abstract <P extends Pane> P getContentPane();
    
    public final T getContent() { return content; }
    public final TD getData() { return getContent().getData(); }
    
    public final @NotNull Num2D getMouseOnContent() { return ui().getMouseOnRegion(root()); }
    public final @NotNull Num2D getMouseOnContentSafe() { return ui().getMouseOnRegionSafe(root()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean needsGfxUpdate() { return needsUpdate; }
    @Override public void updateGfx() {
        Exe.sync(gfxLock, () -> {
            onGfxUpdateAlways();
            if (needsGfxUpdate()) {
                gfxOperations.forEach(Runnable::run);
                gfxOperations.clear();
                onGfxUpdate();
                needsUpdate = false;
            }
        });
    }
    
    
    @Override public @NotNull TaskManager<TC> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected boolean hasFooter() { return false; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final void addOperation(@NotNull Runnable operation) {
        Exe.sync(gfxLock, () -> {
            gfxOperations.add(operation);
            needsUpdate = true;
        });
    }
    
    //</editor-fold>
}
