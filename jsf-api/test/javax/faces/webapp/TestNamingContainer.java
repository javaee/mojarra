/*
 * $Id: TestNamingContainer.java,v 1.1 2003/03/13 22:02:38 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


// Test UINamingContainer Class
public class TestNamingContainer extends UINamingContainer {


    public TestNamingContainer() {
        super();
    }


    public TestNamingContainer(String componentId) {
        super();
        setComponentId(componentId);
    }


    public String getComponentType() {
        return ("NAMING");
    }


    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("/B");
        String componentId = getComponentId();
        if (componentId != null) {
            writer.write(componentId);
        }
    }


    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("/E");
        String componentId = getComponentId();
        if (componentId != null) {
            writer.write(componentId);
        }
    }


}
