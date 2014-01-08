package com.sun.faces.test.agnostic.facelets.c;

import java.io.Serializable;

public class NestedForEachItem implements Serializable {

    private String value;

    public NestedForEachItem(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String v) {
        value = v;
    }

    @Override
    public String toString() {
        return "item[" + value + "]";
    }
}
