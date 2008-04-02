/*
 * $Id: ChildrenComponent.java,v 1.4 2004/02/04 23:42:31 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;


/**
 * <p>Test <code>UIComponent</code> for sys tests.</p>
 */

public class ChildrenComponent extends UIOutput {


    public ChildrenComponent() {
        super();
    }

    public ChildrenComponent(String id) {
        super();
        setId(id);
    }


    public boolean getRendersChildren() {
        return (true);
    }


    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("{ ");
    }


    public void encodeChildren(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            encodeRecursive(context, (UIComponent) kids.next());
            writer.write(" ");
        }
    }


    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("}\n");
    }


    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {
        
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);
    }


}
