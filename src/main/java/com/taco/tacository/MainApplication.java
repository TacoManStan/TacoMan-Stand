package com.taco.tacository;

import com.taco.tacository.ui.FXApplication;
import javafx.application.Application;
import javafx.scene.text.Font;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import net.rgielen.fxweaver.spring.boot.autoconfigure.FxWeaverAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {
    
    /**
     * <p><b>The entry point for the <i>{@link FXApplication Application}</i> and corresponding functionalities (e.g., Spring).</b></p>
     * <br>
     * <hr>
     */
    public static void main(String[] args) {
        System.out.println(Font.loadFont(MainApplication.class.getResourceAsStream("/fonts/Menlo-Regular.ttf"), 10));
        Application.launch(FXApplication.class, args);
    }
    
    /**
     * <p><b>Creates a <i>{@link Bean Singleton Bean}</i> of type <i>{@link FxWeaver}</i> using the specified <i>{@link ConfigurableApplicationContext ApplicationContext}</i></b></p>
     * <br>
     * <hr>
     * <ul>
     *     <li><i>The {@link FxWeaver} instance constructed by this method will always be of type {@link SpringFxWeaver}.</i></li>
     *     <li>
     *         <i>This {@link Bean} configuration method is technically optional —</i>
     *         <ul>
     *             <li><i>If omitted, {@link FxWeaverAutoConfiguration} will serve as the fallback for {@link FxWeaverAutoConfiguration#fxWeaver(ConfigurableApplicationContext) Bean Constructon}.</i></li>
     *         </ul>
     *     </li>
     * </ul>
     * <hr>
     * <br>
     *
     * @param ctx The {@link ConfigurableApplicationContext ApplicationContext} passed to the {@link SpringFxWeaver} upon its construction.
     *
     * @return A new {@link SpringFxWeaver} instance, loaded and managed as a {@code singleton} by {@code Spring}.
     */
    @Bean
    public FxWeaver fxweaver(ConfigurableApplicationContext ctx) {
        return new SpringFxWeaver(ctx);
    }
}
