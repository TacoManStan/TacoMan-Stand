package com.taco.tacository.ui.ui_internal.controllers;

import com.taco.tacository.util.springable.Springable;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * <p>Defines the {@link Controller} responsible for the {@code JavaFX UI Content} of an {@link Object} {@link Class Type}.</p>
 * <p><b>Setup Instructions</b></p>
 * <ol>
 *     <li>First, define a {@code non-abstract} implementation of {@link Controller}.</li>
 *     <li>
 *         To configure the {@link Controller} implementation to work with {@link FxWeaver} and the {@link SpringApplication Spring Framework}, define the following {@link Annotation Annotations}:
 *         <ul>
 *             <li><i><b>{@link Component}:</b> Tells the {@link SpringApplication Spring Framework} that the {@link Controller} implementation is a {@link SpringApplication Spring} {@link Component}.</i></li>
 *             <li><i><b>{@link FxmlView}:</b> Defines the File Path to the {@link FXML} File defining the contents of this {@link Controller} implementation ( e.g., {@link FxmlView @FxmlView}<b>(</b>"/fxml/console/console.fxml"<b>)</b> ).</i></li>
 *             <li><i><b>{@link Scope}:</b> Define the {@link Scope} used by the {@link SpringApplication Spring Framework} to {@code prototype} ( {@link Scope @Scope}<b>(</b>"prototype"<b>)</b> )</i></li>
 *         </ul>
 *     </li>
 * </ol>
 */
public abstract class Controller
        implements Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    /**
     * <p>Constructs a new {@link Controller} instance given the specified {@link FxWeaver} and {@link ConfigurableApplicationContext} {@link SpringApplication Spring} values.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All {@link Controller} instances are auto-constructed by the {@link SpringApplication Spring Framework} and should not be constructed directly.</li>
     *     <li>The only instance in which the {@link Controller} {@link Controller#Controller(FxWeaver, ConfigurableApplicationContext) Constructor} is called from outside the {@link SpringApplication Spring Framework} is by the {@code Constructor} of an implementing class.</li>
     * </ol>
     *
     * @param weaver The {@link FxWeaver} of this {@link Controller} instance.
     * @param ctx    The {@link ConfigurableApplicationContext} of this {@link Controller} instance.
     */
    public Controller(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
    }
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    /**
     * <p>Returns the {@link Pane JavaFX Root Pane} containing the UI content for this {@link Controller} implementation.</p>
     *
     * @return The {@link Pane JavaFX Root Pane} containing the UI content for this {@link Controller} implementation.
     */
    //TO-EXPAND
    public abstract Pane root();
    
    /**
     * <p>Contains the {@link #initialize() Initialization Logic} for this {@link Controller} implementation.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li><i>{@link #initialize()}</i> is called internally upon {@link Controller} construction.</li>
     * </ol>
     */
    //TO-EXPAND
    public abstract void initialize();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    /** {@inheritDoc} */
    @Override public @NotNull FxWeaver weaver() { return this.weaver; }
    
    /** {@inheritDoc} */
    @Override public @NotNull ConfigurableApplicationContext ctx() { return this.ctx; }
    
    //</editor-fold>
}