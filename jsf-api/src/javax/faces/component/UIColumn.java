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

package javax.faces.component;


/**
 * <p><strong>UIColumn</strong> is a {@link UIComponent} that represents
 * a single column of data within a parent {@link UIData} component.</p>
 */

public class UIColumn extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Column";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Column";


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UIColumn} instance with default property
     * values.</p>
     */
    public UIColumn() {

        super();
        setRendererType(null);

    }

  
    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the footer facet of the column (if any).  A convenience
     * method for <code>getFacet("footer")</code>.</p>
     */
    public UIComponent getFooter() {

        return getFacet("footer");

    }


    /**
     * <p>Set the footer facet of the column.  A convenience
     * method for <code>getFacets().put("footer", footer)</code>.</p>
     * 
     * @param footer the new footer facet
     * 
     * @exception NullPointerException if <code>footer</code> is
     *   <code>null</code>
     */
    public void setFooter(UIComponent footer) {

        getFacets().put("footer", footer);

    }


    /**
     * <p>Return the header facet of the column (if any).  A convenience
     * method for <code>getFacet("header")</code>.</p>
     */
    public UIComponent getHeader() {

        return getFacet("header");

    }


    /**
     * <p>Set the header facet of the column.  A convenience
     * method for <code>getFacets().put("header", header)</code>.</p>
     * 
     * @param header the new header facet
     * 
     * @exception NullPointerException if <code>header</code> is
     *   <code>null</code>
     */
    public void setHeader(UIComponent header) {

        getFacets().put("header", header);

    }


}
