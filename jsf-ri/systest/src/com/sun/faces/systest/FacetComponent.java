/*
 * $Id: FacetComponent.java,v 1.3 2003/09/26 20:00:40 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.component.base.UIOutputBase;


/**
 * <p>Test <code>UIComponent</code> for sys tests.</p>
 */

public class FacetComponent extends UIOutputBase {


    public FacetComponent() {
        super();
    }

    public FacetComponent(String id) {
        super();
        setId(id);
    }


    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        UIOutput facet = (UIOutput) getFacet("header");
        if (facet != null) {
            writer.write("[" + facet.getValue() + "] ");
        } else {
            writer.write("[] ");
        }
        writer.write("[");
        writer.write((String) getValue());
        writer.write("] ");
        facet = (UIOutput) getFacet("footer");
        if (facet != null) {
            writer.write("[" + facet.getValue() + "] ");
        } else {
            writer.write("[] ");
        }
    }


}
