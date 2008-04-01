/*
 * $Id: TestComponent.java,v 1.2 2002/06/07 23:31:13 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p>Test <code>UIComponent</code> for unit tests.</p>
 */

public class TestComponent extends UIComponentBase {


    public TestComponent() {
        this("test");
    }

    public TestComponent(String componentId) {
        super();
        setComponentId(componentId);
    }

    public String getComponentType() {
        return ("TestComponent");
    }


}
