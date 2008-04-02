/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
 * <p>{@link Rectangle} is a JavaServer Faces component that renders
 * a <code>SVG</code> markup for a rectangle.</p>
 */

public class Rectangle extends Shape {

    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "Rectangle";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "Rectangle";
    
    public Rectangle() {
        super();
    }

    
    /**
     * <p>Return the component family for this component.
     */
    public String getFamily() {

        return (COMPONENT_FAMILY);

    }
}
