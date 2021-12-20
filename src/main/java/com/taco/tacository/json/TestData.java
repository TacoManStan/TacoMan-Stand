package com.taco.tacository.json;

public class TestData implements JObject {
    
    private final String var1;
    private final int var2;
    private final TestSubData var3;
    private final TestSubData[] var4;
    private final String[] var5;
    
    public TestData(String var1, int var2, TestSubData var3, TestSubData[] var4, String... var5) {
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
    
    @Override
    public String jID() {
        return "Test Data";
    }
    
    @Override
    public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("var1", var1),
                JUtil.create("var2", var2),
                var3,
                JUtil.createArray("var3", var4),
                JUtil.createArray("var4", var5)
        };
    }
}
