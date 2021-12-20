package com.taco.tacository.json;

public class TestSubData
        implements JObject {
    
    private final String jID;
    
    private final String var1;
    private final String var2;
    private final int var3;
    
    public TestSubData(String jID, String var1, String var2, int var3) {
        this.jID = jID;
        
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }
    
    @Override
    public String jID() {
        return jID;
    }
    
    @Override
    public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("sub-var-1", var1),
                JUtil.create("sub-var-2", var2),
                JUtil.create("sub-var-3", var3)
        };
    }
}
