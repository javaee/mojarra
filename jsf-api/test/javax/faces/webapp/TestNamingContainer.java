/*
 * $Id: TestNamingContainer.java,v 1.3 2003/09/25 07:46:52 craigmcc Exp $
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


    public TestNamingContainer(String id) {
        super();
        setId(id);
    }


    public String getComponentType() {
        return ("NAMING");
    }


    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("/B");
        String id = getId();
        if (id != null) {
            writer.write(id);
        }
    }


    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("/E");
        String id = getId();
        if (id != null) {
            writer.write(id);
        }
    }


}
