package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady._to_sort._new.Debugger;

import java.util.Arrays;
import java.util.List;

public class TestData implements JObject, JLoadableObject {
    
    private String jID;
    
    private String var1;
    private int var2;
    private TestSubData var3;
    private TestSubData[] var4;
    private String[] var5;
    
    public TestData() {
        this(null, null, -1, null, null);
    }
    
    public TestData(String jID) {
        this(jID, null, -1, null, null);
    }
    
    public TestData(String jID, String var1, int var2, TestSubData var3, TestSubData[] var4, String... var5) {
        this.jID = jID;
        
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = var4;
        this.var5 = var5;
    }
    
    public String var1() {
        return var1;
    }
    
    public int var2() {
        return var2;
    }
    
    public TestSubData var3() {
        return var3;
    }
    
    public TestSubData[] var4() {
        return var4;
    }
    
    public String[] var5() {
        return var5;
    }
    
    //
    
    public void print() {
        Debugger debugger = new Debugger();
        debugger.setPrintEnabled(true);
        debugger.print("--- " + getJID() + "---");
        debugger.print("Var 1: " + var1);
        debugger.print("Var 2: " + var2);
        debugger.print("Var 3: " + var3);
        debugger.printList(Arrays.asList(var4), "var4");
        debugger.printList(Arrays.asList(var5), "var5");
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
        var1 = JUtil.loadString(parent, "var1");
        var2 = JUtil.loadInt(parent, "var2");
        var3 = JUtil.loadObject(parent, "var3", new TestSubData());
        List<TestSubData> subDataList = JUtil.loadArray(parent, "var4", o -> {
            JsonObject jsonObject = (JsonObject) o;
            TestSubData subData = new TestSubData();
//            subData.setJID((String) jsonObject.get("jID"));
            subData.load(jsonObject);
            return subData;
        });
        var4 = subDataList.toArray(new TestSubData[0]);
        List<String> stringList = JUtil.loadArray(parent, "var5", o -> (String) o);
        var5 = stringList.toArray(new String[0]);
    }
    
    @Override
    public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("var1", var1),
                JUtil.create("var2", var2),
                JUtil.createObject("var3", var3),
                JUtil.createArray("var4", var4),
                JUtil.createArray("var5", var5)
        };
    }
    
    @Override
    public String toString() {
        return "TestData{" +
               "jID='" + jID + '\'' +
               ", var1='" + var1 + '\'' +
               ", var2=" + var2 +
               ", var3=" + var3 +
               ", var4=" + Arrays.toString(var4) +
               ", var5=" + Arrays.toString(var5) +
               '}';
    }
}
