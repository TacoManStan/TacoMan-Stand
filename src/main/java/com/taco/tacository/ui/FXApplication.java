package com.taco.tacository.ui;

import com.mongodb.client.MongoDatabase;
import com.taco.tacository.MainApplication;
import com.taco.tacository._to_sort._expr.spring.beans.demo.BeansDemo;
import com.taco.tacository._to_sort.spring.config.StartupUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * <p><b>The <i>{@link Application JFX Application}</i> instance implemented and executed by the <i>{@link MainApplication Parent Application}</i>.</b></p>
 * <br>
 * <hr>
 * <p><i><b>Details</b></i></p>
 * <ul>
 *     <li><i>Handles the {@code UI Logic} using the {@code JavaFX Library}.</i></li>
 *     <li>
 *         <i>The {@link Application JFX Application} instance is typically the primary entry point for the application, but not when integrated with {@code Spring}:</i>
 *          <ul>
 *              <li><i>Instead, the {@link FxWeaver} library integrates {@link Application JavaFX} into the {@code Spring Framework}.</i></li>
 *              <li><i>Once integrated and initialized by {@link FxWeaver}, {@code Spring} manages the {@link Application JFX Application}.</i></li>
 *              <li>
 *                  <i>The {@link MainApplication} instead serves as an initializer and container for {@code JavaFX}, {@code Spring}, and any additional application-level functions.</i>
 *                  <ul>
 *                      <li><i>It is ideal to use {@code Spring} to handle as much of the application-level variables and functionality as possible.</i></li>
 *                      <li><i>TODO: A {@link MongoDatabase Database Module} to handle {@code Persistent Data} retrieval, storage, and general management must also be integrated.</i></li>
 *                  </ul>
 *              </li>
 *          </ul>
 *     </li>
 * </ul>
 * <hr>
 */
public class FXApplication extends Application {
    
    private ConfigurableApplicationContext ctx;
    
    /**
     * <p><b>Constructs, initializes, and runs the <i>{@link ApplicationContext}</i> for this <i>{@link FXApplication JavaFX Application}</i>.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Initialization Sequence</b></i></p>
     * <ol>
     *     <li>
     *         <i>Constructs a {@link ClassPathXmlApplicationContext}:</i>
     *         <ol>
     *             <li><i>Defines {@code array} defining the {@code XML Configuration Files}.</i></li>
     *             <li><i>Defines the {@link ConfigurableApplicationContext#getParent() Parent Context} of the {@link ClassPathXmlApplicationContext XML Context} as the {@link ConfigurableApplicationContext Root Context}, defined above.</i></li>
     *             <li><i>The {@link ClassPathXmlApplicationContext XML Context} then {@link ConfigurableApplicationContext#refresh() Refreshes} the {@link ApplicationContext Application Contexts}.</i></li>
     *         </ol>
     *     </li>
     *     <li>
     *         <i>Constructs the {@link ApplicationContext Root Context}:</i>
     *         <ol>
     *             <li><i>Constructs a new {@link SpringApplicationBuilder} instance.</i></li>
     *             <li><i>Adds {@link MainApplication MainApplication.class} to the {@link SpringApplicationBuilder} as a {@link SpringApplicationBuilder#sources(Class[]) source}.</i></li>
     *             <li><i>{@link SpringApplicationBuilder#run(String...) Constructs and Runs} the {@link ApplicationContext Root Context} using the {@link SpringApplicationBuilder}.</i></li>
     *         </ol>
     *     </li>
     *
     *     <li>
     *         <i>Registers {@link ConfigurableApplicationContext#registerShutdownHook() Shutdown Hooks} for all {@link ApplicationContext ApplicationContexts}.</i>
     *         <ul>
     *             <li><i>{@link ConfigurableApplicationContext#registerShutdownHook() Shutdown Hooks} instruct {@code Spring} to {@link ConfigurableApplicationContext#close() close} each {@link ApplicationContext Context} {@code automatically} when the {@link MainApplication} is {@link System#exit(int) terminated}.</i></li>
     *             <li><i>i.e., {@link ConfigurableApplicationContext#registerShutdownHook() Shutdown Hooks} remove the need to {@link ConfigurableApplicationContext#close() close} each {@link ApplicationContext Context} {@code manually}.</i></li>
     *         </ul>
     *     </li>
     * </ol>
     * <hr>
     */
    @Override
    public void init() {
        // TODO - Update JavaDoc for this method to accurately reflect ApplicationContext initialization process.
        
        // Constructs a new SpringApplicationBuilder instance to handle the ApplicationContext initialization
        ctx = new SpringApplicationBuilder().sources(MainApplication.class) // Defines all global-scope class-based configurations
                                            .parent(new ClassPathXmlApplicationContext(StartupUtil.ROOT.XML.ctx_config())) // Gives global scope access to applicable XML Configurations
                                            .run(getParameters().getRaw().toArray(new String[0])); // Executes the application builder using no arguments
        ctx.registerShutdownHook(); // Enables automatic/internal shutdown support
        
        ctx.getBean(BeansDemo.class).demo();
    }
    
    /**
     * <p><b>Initialization sequence bridging the fully initialized <i>{@link Stage JFX Stage}</i> and any <i>{@code Non-UI Functions}</i>.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Details</b></i></p>
     * <ul>
     *     <li><i>This method is called directly after the {@code JavaFX} application {@link Stage} and all corresponding JavaFX UI functionality has finished its initialization
     *     .</i></li>
     *     <li><i>Some {@code Non-UI Functions} are handled internally via {@code Spring}.</i></li>
     *     <li><i>Executed on the {@code JavaFX UI Application Thread}.</i></li>
     * </ul>
     * <hr>
     * <br>
     *
     * @param primaryStage The fully initialized {@link Stage JavaFX Stage} instance serving as the {@code View Module} of the main {@link MainApplication Application}.
     */
    @Override
    public void start(Stage primaryStage) {
        ctx.publishEvent(new FxWeaverInitializer.StageReadyEvent(primaryStage));
    }
    
    /**
     * <p><b>Executes any operations required for the proper <i>{@code Termination}</i> of the <i>{@link MainApplication}</i>.</b></p>
     * <br>
     * <hr>
     * <p><i><b>Details</b></i></p>
     * <ul>
     *     <li><i>Executed internally by {@link Application JavaFX} when the {@link MainApplication} is terminated.</i></li>
     *     <li>
     *         <i>In the future, this method can also serve to help prevent any {@code UI Memory Leaks}.</i>
     *         <ul>
     *             <li><i>This will be especially relevant with any future {@code multithreading} implementations.</i></li>
     *         </ul>
     *     </li>
     *     <li>
     *         <p><i>{@link ConfigurableApplicationContext Application Context} Termination:</i></p>
     *         <ul>
     *             <li><i>{@link ConfigurableApplicationContext#registerShutdownHook() Shutdown Hooks} automatically {@link ConfigurableApplicationContext#close() close} the {@link ConfigurableApplicationContext Application Contexts} internally.</i></li>
     *             <li><i>{@link ApplicationContext Application Contexts} do not need to be {@link ConfigurableApplicationContext#close() closed} manually.</i></li>
     *             <li><i>The {@link ConfigurableApplicationContext#registerShutdownHook() Shutdown Hooks} are registered when the {@link ConfigurableApplicationContext Contexts} are {@link #init() initialized}.</i></li>
     *         </ul>
     *     </li>
     * </ul>
     * <hr>
     */
    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}