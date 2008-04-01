/*
 * $Id: TextEntry_SecretTag.java,v 1.5 2001/11/21 22:32:40 visvan Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// TextEntry_SecretTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WTextEntry;
import javax.faces.ObjectTable;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>TextEntry_SecretTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_SecretTag.java,v 1.5 2001/11/21 22:32:40 visvan Exp $
 * 
 *
 */

public class TextEntry_SecretTag extends TagSupport
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables
    private String name = null;
    private String value = null;
    private String size = null;
    private String maxlength = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TextEntry_SecretTag()
    {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init()
    {
        // super.init();
    }

    
    //
    // Class methods
    //

    //
    // General Methods
    //

    /**
     * Renders TextEntry_SecretTag's start tag and its attributes.
     */
    public int doStartTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // PENDING(visvan) use tagext class to validate attributes.
        // get ObjectTable from ServletContext.
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute("objectTable");
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(),
                "renderContext");
        Assert.assert_it( rc != null );

        if ( name != null ) {
            Renderer renderer = getRenderer(rc);
            WTextEntry c = (WTextEntry) ot.get(pageContext.getRequest(), name);
            if (c == null) {
                c = createComponent(rc);
                // PENDING (visvan ) scope should be an attribute of the tag
                // for now using the default scope, request
                ot.put(pageContext.getRequest(), name, c);
            }
            try {
               renderer.renderStart(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering Password component: "+
                        e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                    f.getMessage());
            }
        }
        return(EVAL_BODY_INCLUDE);
    }

    /**
     * Returns the appropriate renderer for the tag
     *
     * @param rc RenderContext to obtain renderkit
     */
    public Renderer getRenderer(RenderContext rc ) throws JspException{

        Renderer renderer = null;
        RenderKit renderKit = rc.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }
        try {
            String class_name = "com.sun.faces.renderkit.html_basic.SecretRenderer";
            renderer = renderKit.getRenderer(class_name);
        } catch (FacesException e) {
            e.printStackTrace();
            throw new JspException("FacesException " + e.getMessage());
        }

        if (renderer == null) {
            throw new JspException(
                "Could not determine 'renderer' for TextEntry component");
        }
        return renderer;	
    }

    /**
     * Creates a TextEntry component and sets renderer specific
     * properties.
     */
    protected WTextEntry createComponent(RenderContext rc) {
        WTextEntry c = new WTextEntry();
        // set renderer specific properties 
        c.setAttribute(rc, "name", name);
        c.setAttribute(rc, "size", size);
        c.setAttribute(rc, "maxlength", maxlength);
        // set render independent attributes 
        c.setValue(value);
        return c;
    }

    /**
     * Figures out the name of the package to which the
     * class belongs.
     *
     * @param class_name name of the class
     * @return String package name of the class
     *
    protected String getRendererPackage(String class_name) {
        Class renderclass = null;
        System.out.println("class_name " + class_name);
        try {
            renderclass = Class.forName(class_name);
        } catch ( ClassNotFoundException e ) {
            System.out.println("Couldn't find Text Renderer class");
        }
        String packageName = (renderclass.getPackage()).getName();
        if ( packageName == null ) {
            System.out.println("Package name is null");
        }
        String full_name = packageName + "." + class_name;
        return full_name;
    } */

    /**
     * Returns the value of the "name" attribute
     *
     * @return String value of "name" attribute
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the "name" attribute
     * @param name value of "name" attribute 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of the "value" attribute
     *
     * @return String value of "value" attribute
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets "value" attribute
     * @param value value of "value" attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the size attribute
     * @param size value of size attribute
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Returns the value of size attribute
     *
     * @return String value of size attribute
     */
    public String getSize() {
        return this.size;
    }

    /**
     * Sets  maxlength attribute
     * @param  maxlength value of maxlength attribute
     */
    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

   /**
     * Returns the value of maxlength attribute
     *
     * @return String value of maxlength attribute
     */
    public String getMaxlength() {
        return this.maxlength;
    }

} // end of class TextEntry_SecretTag
