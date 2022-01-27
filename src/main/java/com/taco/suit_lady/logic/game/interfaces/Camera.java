package com.taco.suit_lady.logic.game.interfaces;

import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.CroppedImagePaintCommand;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;

public interface Camera {
    
    GameViewContent getContent();
    GameMap getMap();
    
    CroppedImagePaintCommand getPaintCommand();
    
    //
    
    IntegerProperty xLocationProperty();
    IntegerProperty yLocationProperty();
    
    IntegerProperty xOffsetProperty();
    IntegerProperty yOffsetProperty();
    
    //
    
    ObjectBinding<Image> mapImageBinding();
    
    IntegerBinding mapImageWidthBinding();
    IntegerBinding mapImageHeightBinding();
    
    
    IntegerBinding viewportWidthBinding();
    IntegerBinding viewportHeightBinding();
    
    DoubleBinding xMultiplierBinding();
    DoubleBinding yMultiplierBinding();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    //<editor-fold desc="> Property/Binding Accessors">
    
    default int getMapWidth() { return getMap().getFullWidth(); }
    default int getMapHeight() { return getMap().getFullHeight(); }
    
    //
    
    default int getLocationX() { return xLocationProperty().get(); }
    default int setLocationX(int newValue) { return PropertiesSL.setProperty(xLocationProperty(), newValue); }
    default int moveX(int amount) { return setLocationX(getLocationX() + amount); }
    
    default int getLocationY() { return yLocationProperty().get(); }
    default int setLocationY(int newValue) { return PropertiesSL.setProperty(yLocationProperty(), newValue); }
    default int moveY(int amount) { return setLocationY(getLocationY() + amount); }
    
    
    default int getOffsetX() { return xOffsetProperty().get(); }
    default int setOffsetX(int newValue) { return PropertiesSL.setProperty(xOffsetProperty(), newValue); }
    default int moveOffsetX(int amount) { return setOffsetX(getOffsetX() + amount); }
    
    default int getOffsetY() { return yOffsetProperty().get(); }
    default int setOffsetY(int newValue) { return PropertiesSL.setProperty(yOffsetProperty(), newValue); }
    default int moveOffsetY(int amount) { return setOffsetY(getOffsetY() + amount); }
    
    //
    
    default Image getMapImage() { return mapImageBinding().get(); }
    
    default int getMapImageWidth() { return mapImageWidthBinding().get(); }
    default int getMapImageHeight() { return mapImageHeightBinding().get(); }
    
    
    default int getViewportWidth() { return viewportWidthBinding().get(); }
    default int getViewportHeight() { return viewportHeightBinding().get(); }
    
    default double getMultiplierX() { return xMultiplierBinding().get(); }
    default double getMultiplierY() { return yMultiplierBinding().get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Utility">
    
    default void print() {
        System.out.println("X Location: " + getLocationX());
        System.out.println("Y Location: " + getLocationY());
        
        
        System.out.println("X Offset: " + getOffsetX());
        System.out.println("Y Offset: " + getOffsetY());
        
        
        System.out.println("Viewport Width: " + getViewportWidth());
        System.out.println("Viewport Height: " + getViewportHeight());
        
        
        System.out.println("Map Image Width: " + getMapImageWidth());
        System.out.println("Map Image Height: " + getMapImageHeight());
        
        System.out.println("Game Map Width: " + getMap().getFullWidth());
        System.out.println("Game Map Height: " + getMap().getFullHeight());
        
        
        System.out.println("X Multiplier: " + getMultiplierX());
        System.out.println("Y Multiplier: " + getMultiplierY());
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
