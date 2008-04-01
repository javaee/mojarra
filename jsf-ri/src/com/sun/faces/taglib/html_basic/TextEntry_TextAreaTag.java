/*
 * $Id: TextEntry_TextAreaTag.java,v 1.8 2001/12/08 00:33:53 rogerk Exp $
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

// TextEntry_TextAreaTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WTextEntry;
import javax.faces.ObjectTable;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *
 *  <B>TextEntry_TextAreaTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_TextAreaTag.java,v 1.8 2001/12/08 00:33:53 rogerk Exp $
 * 
 *
 */

public class TextEntry_TextAreaTag extends BodyTagSupport
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
    private String rows = null;
    private String cols = null;
    private String wrap = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TextEntry_TextAreaTag()
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
     * Renders TextEntry_TextAreaTag's start tag and its attributes.
     */
    public int doStartTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // PENDING(visvan) use tagext class to validate attributes.
        // get ObjectTable from ServletContext.
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(),
                Constants.REF_RENDERCONTEXT);
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
               rc.pushChild(c);
               renderer.renderStart(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering TextArea component: "+
                        e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                    f.getMessage());
            }

        }
        return(EVAL_BODY_INCLUDE);
    }

    /**
     * Gets the TextEntry_TextAreaTag's body if specified.
     */
    public int doAfterBody() throws JspException {

        Assert.assert_it( pageContext != null );
    
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(),
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );

        WTextEntry c = (WTextEntry) ot.get(pageContext.getRequest(), name);
        if ( c != null ) {
           if ( getBodyContent() != null ) {
               c.setText(rc, getBodyContent().getString());
           }
        }
        return SKIP_BODY;
    }

    /**
     * Renders the Form's end Tag
     */
    public int doEndTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // get ObjectTable from ServletContext.
        ObjectTable ot = (ObjectTable)pageContext.getServletContext().
                 getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(), 
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );

        WTextEntry c = (WTextEntry) ot.get(pageContext.getRequest(), name);
        if ( c != null ) {
            Renderer renderer = getRenderer(rc);
            try {
                renderer.renderComplete(rc, c);
                rc.popChild();
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering TextArea component: "+
                        e.getMessage());
            }catch (FacesException e) {
                e.printStackTrace();
                throw new JspException("FacesException " + e.getMessage());
            }
        }
        return(EVAL_PAGE);
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
            String class_name = "com.sun.faces.renderkit.html_basic.TextAreaRenderer";
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
        c.setAttribute(rc, "rows", rows);
        c.setAttribute(rc, "cols", cols);
        c.setAttribute(rc, "wrap", wrap);
        // set render independent attributes 
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
     * Sets the cols attribute
     * @param cols value of cols attribute
     */
    public void setCols(String cols) {
        this.cols = cols;
    }

    /**
     * Returns the value of cols attribute
     *
     * @return String value of cols attribute
     */
    public String getCols() {
        return this.cols;
    }

    /**
     * Sets  rows attribute
     * @param  rows value of row attribute
     */
    public void setRows(String rows) {
        this.rows = rows;
    }

   /**
     * Returns the value of rows attribute
     *
     * @return String value of rows attribute
     */
    public String getRows() {
        return this.rows;
    }

    /**
     * Sets  wrap attribute
     * @param  wrap value of wrap attribute
     */
    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

   /**
     * Returns the value of wrap attribute
     *
     * @return String value of wrap attribute
     */
    public String getWrap() {
        return this.wrap;
    }


} // end of class TextEntry_TextAreaTag
