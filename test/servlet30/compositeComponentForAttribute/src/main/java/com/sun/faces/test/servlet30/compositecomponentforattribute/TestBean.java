package com.sun.faces.test.servlet30.compositecomponentforattribute;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "test")
public class TestBean implements Serializable {

    private String forValue = "name";

    public String getForValue() {
        return forValue;
    }

    public void setForValue(String forValue) {
        this.forValue = forValue;
    }

    private String property;

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
