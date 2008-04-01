/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.3 2001/11/10 01:34:37 edburns Exp $
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

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WForm;
import javax.faces.WSelectBoolean;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>SelectBoolean_CheckboxTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectBoolean_CheckboxTag.java,v 1.3 2001/11/10 01:34:37 edburns Exp $
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
        // Get the RenderContext from the session. It was set there
        // in the BeginTag.
        //
        RenderContext renderContext;
        renderContext = (RenderContext)pageContext.getSession().
            getAttribute("renderContext");

        // 1. get an instance of "WSelectBoolean"
        // Normally, this would be retrieved from some instance pool,
        // but for now, we will just instantiate one..
        //
        WSelectBoolean wSelectBoolean = new WSelectBoolean();

        // 2. set tag attributes into the instance..
        //
        wSelectBoolean.setAttribute(renderContext, "checked", getChecked());
        wSelectBoolean.setAttribute(renderContext, "name", getName());
        wSelectBoolean.setAttribute(renderContext, "value", getValue());
        wSelectBoolean.setAttribute(renderContext, "label", getLabel());

        // 3. find the parent (WForm), and add WSelectBoolean instance as
        // a child.
        // wForm.add(...
        //

        // 4. place back in namespace..
        //

        // 5. Obtain "Renderer" instance from the "RenderKit
        //
        Renderer renderer = null;

        RenderKit renderKit = renderContext.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }

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

        // 6. Render the good stuff...
        //
        try {
            renderer.renderStart(renderContext, wSelectBoolean);
        } catch (java.io.IOException e) {
            throw new JspException("Problem rendering component: "+
                e.getMessage());
        }

        return (EVAL_BODY_INCLUDE);
    }

    // ----VERTIGO_TEST_START

    //
    // Test methods
    //

    public static void main(String [] args) {
        Assert.setEnabled(true);
        SelectBoolean_CheckboxTag me = new SelectBoolean_CheckboxTag();
        Log.setApplicationName("SelectBoolean_CheckboxTag");
        Log.setApplicationVersion("0.0");
        Log.setApplicationVersionDate("$Id: SelectBoolean_CheckboxTag.java,v 1.3 2001/11/10 01:34:37 edburns Exp $");
    
    }

    // ----VERTIGO_TEST_END

} // end of class SelectBoolean_CheckboxTag
