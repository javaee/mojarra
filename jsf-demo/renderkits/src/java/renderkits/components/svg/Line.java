/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package renderkits.components.svg;

import javax.faces.context.FacesContext;
import javax.faces.component.UIOutput;
import javax.faces.el.ValueBinding;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIViewRoot;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


/**
 * <p>{@link Line} is a JavaServer Faces component that renders
 * a <code>SVG</code> markup for a rectangle.</p>
 */

public class Line extends Shape {

    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "Line";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "Line";
    
    public Line() {
        super();
    }

    
    /**
     * <p>Return the component family for this component.
     */
    public String getFamily() {

        return (COMPONENT_FAMILY);

    }
}
