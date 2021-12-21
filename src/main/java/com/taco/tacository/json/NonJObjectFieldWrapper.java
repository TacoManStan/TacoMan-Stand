package com.taco.tacository.json;

public abstract class NonJObjectFieldWrapper<T>
        implements JObject, JLoadableObject {
    
    private String jID;
    private T value;
    
    public NonJObjectFieldWrapper(String jID) {
        this.jID = jID;
    }
    
    @Override
    public String getJID() {
        return jID;
    }
    
    @Override
    public void setJID(String jID) {
        this.jID = jID;
    }
    
    protected abstract T value();
    
    protected abstract void loadFromValue(T value);
}
