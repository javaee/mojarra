/*
 * $Id: TestTag.java,v 1.3 2003/09/23 04:38:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;


// Test UIComponent Tag
public class TestTag extends UIComponentTag {

    public TestTag() {
        super();
    }


    public TestTag(String componentId) {
        this(componentId, componentId);
    }

    public TestTag(String componentId, String label) {
        super();
        setId(componentId);
        setLabel(label);
    }

    private String label = null;

    public void setLabel(String label) {
        this.label = label;
    }

    private boolean rendersChildren = false;
    private boolean rendersChildrenSet = false;

    public void setRendersChildren(boolean rendersChildren) {
        this.rendersChildren = rendersChildren;
        this.rendersChildrenSet = true;
    }

    public void release() {
        super.release();
        this.label = null;
        this.rendersChildrenSet = false;
    }

    public String getComponentType() {
        return ("TestComponent");
    }

    public String getRendererType() {
        return (null);
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (rendersChildrenSet) {
            ((TestComponent) component).setRendersChildren(rendersChildren);
        }
        if (label != null) {
            ((TestComponent) component).setLabel(label);
        }
    }


}
