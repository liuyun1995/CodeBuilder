package com.liuyun.builder.config;

public abstract class TypedPropertyHolder extends PropertyHolder {

    private String configurationType;
    
    public TypedPropertyHolder() {
        super();
    }

    public String getConfigurationType() {
        return configurationType;
    }
    
    public void setConfigurationType(String configurationType) {
        if (!"DEFAULT".equalsIgnoreCase(configurationType)) { //$NON-NLS-1$
            this.configurationType = configurationType;
        }
    }
    
}
