package com.taco.tacository._to_sort.json;

import com.github.cliftonlabs.json_simple.JsonObject;

public class TestSubData
        implements JObject, JLoadableObject {
    
    private String jID;
    
    private String var1;
    private String var2;
    private int var3;
    
    public TestSubData() {
        this (null, null, null, -1);
    }
    
    public TestSubData(String jID, String var1, String var2, int var3) {
        this.jID = jID;
        
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }
    
    public String getVar1() {
        return var1;
    }
    
    public void setVar1(String var1) {
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
        var1 = JUtil.loadString(parent, "sub-var-1");
        var2 = JUtil.loadString(parent, "sub-var-2");
        var3 = JUtil.loadInt(parent, "sub-var-3");
    }
    
    @Override
    public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("sub-var-1", var1),
                JUtil.create("sub-var-2", var2),
                JUtil.create("sub-var-3", var3),
                JUtil.create("jID", jID)
        };
    }
    
    @Override
    public String toString() {
        return "TestSubData{" +
               "jID='" + jID + '\'' +
               ", var1='" + var1 + '\'' +
               ", var2='" + var2 + '\'' +
               ", var3=" + var3 +
               '}';
    }
}
