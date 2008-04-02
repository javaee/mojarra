/*
 * $Id: FacetComponent.java,v 1.2 2003/09/05 18:57:09 eburns Exp $
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
        UIOutput facet = (UIOutput) getFacets().get("header");
        if (facet != null) {
            writer.write("[" + facet.getValue() + "] ");
        } else {
            writer.write("[] ");
        }
        writer.write("[");
        writer.write((String) getValue());
        writer.write("] ");
        facet = (UIOutput) getFacets().get("footer");
        if (facet != null) {
            writer.write("[" + facet.getValue() + "] ");
        } else {
            writer.write("[] ");
        }
    }


}
