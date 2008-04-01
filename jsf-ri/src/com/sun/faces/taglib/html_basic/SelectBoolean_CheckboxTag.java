/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.8 2001/12/08 00:33:53 rogerk Exp $
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

// SelectBoolean_CheckboxTag.java

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
import javax.faces.WForm;
import javax.faces.WSelectBoolean;
import javax.faces.ObjectTable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>SelectBoolean_CheckboxTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectBoolean_CheckboxTag.java,v 1.8 2001/12/08 00:33:53 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectBoolean_CheckboxTag extends TagSupport {
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

    private String checked = null;
    private String name = null;
    private String value = null;
    private String label = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectBoolean_CheckboxTag() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        Assert.assert_it( pageContext != null );
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext renderContext = 
            (RenderContext)ot.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

        if (name != null) {

            // 1. Get or create the component instance.
            //
            WSelectBoolean wSelectBoolean = (WSelectBoolean) 
                ot.get(pageContext.getRequest(), name);
            if ( wSelectBoolean == null ) {
                wSelectBoolean = new WSelectBoolean();
            }

            wSelectBoolean.setAttribute(renderContext, "checked", getChecked());
            wSelectBoolean.setAttribute(renderContext, "name", getName());
            wSelectBoolean.setAttribute(renderContext, "value", getValue());
            wSelectBoolean.setAttribute(renderContext, "label", getLabel());
            ot.put(pageContext.getRequest(), name, wSelectBoolean);

            // 2. Get a RenderKit and associated Renderer for this
            //    component.
            //
            RenderKit renderKit = renderContext.getRenderKit();
            if (renderKit == null) {
                throw new JspException("Can't determine RenderKit!");
            }

            Renderer renderer = null;
            try {
                renderer = renderKit.getRenderer(
                    "com.sun.faces.renderkit.html_basic.CheckboxRenderer");
            } catch (FacesException e) {
                throw new JspException(
                    "FacesException!!! " + e.getMessage());
            }

            if (renderer == null) {
                throw new JspException(
                    "Could not determine 'renderer' for component");
            }

            // 3. Render the component. (Push the component on
            //    the render stack first).
            //
            try {
                renderContext.pushChild(wSelectBoolean);
                renderer.renderStart(renderContext, wSelectBoolean);
//PENDING(rogerk) complet/pop should be done in doEndTag
//
                renderer.renderComplete(renderContext, wSelectBoolean);
                renderContext.popChild();
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering component: "+
                    e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                    f.getMessage());
            }
        }
        return (EVAL_BODY_INCLUDE);
    }

    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException{

        return EVAL_PAGE;
    }


} // end of class SelectBoolean_CheckboxTag
