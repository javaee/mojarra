/*
 * $Id: Output_TextTag.java,v 1.1 2001/11/08 00:18:21 visvan Exp $
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

// Output_TextTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WOutput;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>Output_TextTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Output_TextTag.java,v 1.1 2001/11/08 00:18:21 visvan Exp $
 * 
 *
 */

public class Output_TextTag extends TagSupport
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

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public Output_TextTag()
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
     * Renders Output_Text's start tag
     */
    public int doStartTag() throws JspException{
        // check if the tag is already created and exists in the 
        // JSP pool. If not, create form component.
        
        // JV figure out the scope. For now use session scope
        // JV use tagext class to validate attributes.
        if ( name != null ) {
            RenderContext rc = (RenderContext)pageContext.getSession().
                    getAttribute("renderContext");
            Renderer text_renderer = getRenderer(rc);
            HttpSession session = pageContext.getSession();
            WOutput c = (WOutput) session.getAttribute(name);
            if (c == null) {
                c = createComponent(rc);
                addToScope(c, session);
            }	
            try {
                text_renderer.renderStart(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering Output component: "+
                        e.getMessage());
            }
        }
        return(EVAL_BODY_INCLUDE);
    }

    public Renderer getRenderer(RenderContext rc ) throws JspException{

        Renderer renderer = null;
        RenderKit renderKit = rc.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }
        try {
            String class_name = "com.sun.faces.renderkit.html_basic.TextRenderer";
            renderer = renderKit.getRenderer(class_name);
        } catch (FacesException e) {
            e.printStackTrace();
            throw new JspException("FacesException " + e.getMessage());
        }

        if (renderer == null) {
            throw new JspException(
                "Could not determine 'renderer' for Output component");
        }
        return renderer;	
    }

    /**
     * Creates a Output component and sets renderer specific
     * properties.
     */
    protected WOutput createComponent(RenderContext rc) {
        WOutput c = new WOutput();
        // set renderer specific properties 
        c.setAttribute(rc, "name", name);
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
     * Adds the component to the specified scope.
     *
     * @param c component to add to scope.
     * @param scope scope to which the component is to be added
     * For now use session scope.
     *
     */
    void addToScope(WOutput c, HttpSession session) {
        // System.out.println("adding Text to session: " + name);
        session.setAttribute(name, c);
    }

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
     * Sets the "value" attribute
     * @param name value of "value" attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

    public static void main(String [] args)
    {
        Assert.setEnabled(true);
        FormTag me = new FormTag();
        Log.setApplicationName("FormTag");
        Log.setApplicationVersion("0.0");
        Log.setApplicationVersionDate("$Id: Output_TextTag.java,v 1.1 2001/11/08 00:18:21 visvan Exp $");
    
    }

// ----VERTIGO_TEST_END

} // end of class FormTag
