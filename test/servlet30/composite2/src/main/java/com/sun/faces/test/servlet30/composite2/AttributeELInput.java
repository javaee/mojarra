package com.sun.faces.test.servlet30.composite2;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

@FacesComponent("com.sun.faces.test.agnostic.facelets.composite.AttributeELInput")
public class AttributeELInput extends UINamingContainer {

    protected enum PropertyKeys {

        mandatory
    }

    public Boolean getMandatory() {
        Boolean val = (Boolean) getStateHelper().eval(PropertyKeys.mandatory);
        return val;
    }

    public void setMandatory(Boolean val) {
        getStateHelper().put(PropertyKeys.mandatory, val);
    }
}
