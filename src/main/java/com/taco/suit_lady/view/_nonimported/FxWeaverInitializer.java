package com.taco.suit_lady.view._nonimported;

import com.taco.suit_lady.MainApplication;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.ui_internal.AppController;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p><b>The <i>{@link ApplicationListener}</i> implementation triggered when the application <i>{@link Stage}</i> is fully initialized.</b></p>
 * <br>
 * <hr>
 * <p><i><b>Note:</b> There should only ever be a single instance of {@link FxWeaverInitializer} per JVM instance.</i></p>
 * <p><i>The {@link FxWeaverInitializer} instance is loaded and managed by the {@code SpringFramework} and should not be created manually using the {@code new} keyword.</i></p>
 * <p><i><b>Note:</b> {@link Bean Spring Bean} definition instructions are located in {@link MainApplication#fxweaver(ConfigurableApplicationContext) MainApplication}.</i></p>
 * <hr>
 * <br>
 *
 * @see MainApplication#fxweaver(ConfigurableApplicationContext)
 */
@Component
public class FxWeaverInitializer
        implements ApplicationListener<FxWeaverInitializer.StageReadyEvent>, Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    /**
     * <p><b>Constructs a new <i>{@link FxWeaverInitializer}</i> object for the specified <i>{@link FxWeaver}</i> instance.</b></p>
     * <br>
     * <hr>
     *
     * @param weaver The {@link FxWeaver} to be initialized by this {@link FxWeaverInitializer}.
     * @see FXApplication#start(Stage)
     */
    public FxWeaverInitializer(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
    }
    
    //<editor-fold desc="--- WEAVING ---">
    
    /**
     * <p><b>Handles <i>{@link FxWeaver}</i> initialization <i>{@link FXApplication#start(Stage) starting}</i> the
     * <i>{@link FXApplication}</i>.</b></p>
     * <br>
     * <hr>
     *
     * @param event The {@link StageReadyEvent implementation} of {@link ApplicationEvent} containing the {@code JavaFX} data required for {@link FxWeaver} initialization.
     */
    @Override
    public void onApplicationEvent(StageReadyEvent event)
    {
        System.out.println("ApplicationEvent Triggered...");
        final Stage stage = event.getStage();
        System.out.println("ApplicationEvent Post-Stage: " + stage);
        if (!weave(stage))
            throw new RuntimeException("Weaving Failed");
        System.out.println("Post-Weaving... Showing Stage...: " + stage);
        stage.show();
        System.out.println("Post-Show...");
    }
    
    //
    
    final int ROOT_WIDTH = 1440; // The *width* of the JFX Primary Stage root node
    final int ROOT_HEIGHT = 810; // The *height* of the JFX Primary Stage root node
    //    final Class<?> MAIN_CONTROLLER = FXMainController.class; // The *Class* to be constructed and used by FxWeaver & Spring to weave the JFX Primary Stage.
    final Class<?> MAIN_CONTROLLER = AppController.class;
    
    /**
     * <p><b><i>{@link FxWeaver#loadView(Class) Weaves}</i> a new <i>{@link Scene Scene Instance}</i> into the specified <i>{@link Stage}</i>.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Execution Process:</b></i></p>
     * <ol>
     * <li><i>A new {@link FXMainController JFX Controller} instance is constructed.</i></li>
     * <li><i>A new {@link Scene} is constructed using the {@link FXMainController JFX Controller} initialization instructions and referenced {@link FXML FXML Template} files
     * .</i></li>
     * <li><i>The {@link Scene} is assigned to the specified {@link Stage}.</i></li>
     * </ol>
     * <p><i>- The majority of the execution process is handled by either {@link FxWeaver} and/or the {@code Spring Framework}.</i></p>
     * <hr>
     * <br>
     *
     * @return True if the {@link Scene} instance generated by {@link FxWeaver} was properly loaded, linked to its {@code JFX Controller}, and weaved into the {@link Stage}.
     * <br>
     * If, for any reason, any above the above operations fail, this method returns false.
     */
    boolean weave(Stage stage)
    {
        System.out.println("Weaving Stage...: " + stage);
        
        if (stage == null)
            return false;
        
        try
        {
            stage.initStyle(StageStyle.UNDECORATED);
            
            Parent parent = weaver.loadView(MAIN_CONTROLLER);
            
            System.out.println("Weaving Parent...: " + parent);
            
            final Scene scene = new Scene(parent);
            
            System.out.println("Weaving Scene...: " + scene);
            System.out.println("Setting Stage Scene...");
            
            stage.setScene(scene);
            stage.setOnHidden(event -> {
                System.exit(0);
            });
    
            ctx().getBean(AppUI.class).getController().initialize(stage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
    
    /**
     * <p><b>The {@link ApplicationEvent} triggered when the application {@link Stage} is ready.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Note:</b> A {@link Stage} is considered {@code ready} when it has completed its {@code JavaFX} initialization sequence and is otherwise ready for {@link FxWeaver}
     * integration.</i></p>
     * <hr>
     * <br>
     */
    public static class StageReadyEvent extends ApplicationEvent
    {
        /**
         * <p><b>Constructs a new <i>{@link StageReadyEvent StageReadyEvent}</i> for the specified <i>{@link Stage}</i>.</b></p>
         * <br>
         * <hr>
         * <p><i><b>Note:</b> The specified {@link Stage} is expected to be both {@code non-null} and {@code ready} <u>before</u> constructing a {@link StageReadyEvent
         * StageReadyEvent}.</i></p>
         * <p><i>- A {@link Stage} is considered {@code ready} when it has completed its {@code JavaFX} initialization sequence and is ready for {@link FxWeaver}
         * integration.</i></p>
         * <hr>
         * <br>
         *
         * @param stage the {@link Stage} instance responsible for triggering this event.
         */
        public StageReadyEvent(Stage stage)
        {
            super(stage);
            System.out.println("Constructing Stage Ready Event....");
        }
        
        /**
         * <p><b>Gets the {@link Stage} instance that triggered this {@code StageReadyEvent}.</b></p>
         *
         * @return the {@link Stage} instance.
         */
        public Stage getStage()
        {
            System.out.println("Getting Stage...");
            try
            {
                return (Stage) getSource();
            }
            catch (ClassCastException e)
            {
                System.out.println("Stage is of wrong type: " + getSource().getClass().getName());
                return null;
            }
        }
    }
}
