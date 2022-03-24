package com.taco.tacository._to_sort.json;

import com.github.cliftonlabs.json_simple.JsonObject;
import javafx.scene.paint.Color;

public class TestData2
implements JObject, JLoadableObject{
    
    private String jID;
    
    private Color var1;
    private String var2;
    private int var3;
    
    public TestData2() {
        this(null, null, null, -1);
    }
    
    public TestData2(String jID, Color var1, String var2, int var3) {
        this.jID = jID;
        
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }
    
    public Color getVar1() {
        return var1;
    }
    
    public void setVar1(Color var1) {
        this.var1 = var1;
    }
    
    public String getVar2() {
        return var2;
    }
    
    public void setVar2(String var2) {
        this.var2 = var2;
    }
    
    public int getVar3() {
        return var3;
    }
    
    public void setVar3(int var3) {
        this.var3 = var3;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public String getJID() {
        return jID;
    }
    
    @Override
    public void setJID(String jID) {
        this.jID = jID;
    }
    
    @Override
    public void doLoad(JsonObject parent) {
        var1 = JUtil.loadObject(parent, new ColorJObjectWrapper("var1")).value();
        var2 = JUtil.loadString(parent, "var2");
        var3 = JUtil.loadInt(parent, "var3");
    }
    
    @Override
    public JElement[] jFields() {
        return new JElement[]{
                new ColorJObjectWrapper("var1", var1),
                JUtil.create("var2", var2),
                JUtil.create("var3", var3)
        };
    }
    
    @Override
    public String toString() {
        return "TestData2{" +
               "jID='" + jID + '\'' +
               ", var1=" + var1 +
               ", var2='" + var2 + '\'' +
               ", var3=" + var3 +
               '}';
    }
    
    public void print() {
        System.out.println("JID: " + jID);
        
        System.out.println("Var 1: ");
        System.out.println("\tRed: " + var1.getRed());
        System.out.println("\tGreen: " + var1.getGreen());
        System.out.println("\tBlue: " + var1.getBlue());
        System.out.println("\tAlpha: " + var1.getOpacity());
        
        System.out.println("Var 2: " + var2);
        System.out.println("Var 3: " + var3);
    }
    
    //</editor-fold>
}
