/*
 * $Id: TextEntry_TextAreaTag.java,v 1.22 2002/04/05 19:41:19 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_TextAreaTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.util.Util;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.FacesContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.UIComponent;
import javax.faces.UITextEntry;
import javax.faces.ObjectManager;
import javax.faces.TreeNavigator;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 *
 *  <B>TextEntry_TextAreaTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_TextAreaTag.java,v 1.22 2002/04/05 19:41:19 jvisvanathan Exp $
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
    private String id = null;
    private String value = null;
    private String rows = null;
    private String cols = null;
    private String wrap = null;
    private String model = null;
    private String scope = null;
    private String valueChangeListener = null;

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
	String rendererType = null;
	TreeNavigator treeNav = null;
        ObjectManager objectManager = null;
	FacesContext facesContext = null;

	objectManager = (ObjectManager) pageContext.getServletContext().
	    getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
	
        facesContext = 
	    (FacesContext)objectManager.get(pageContext.getRequest(),
			     Constants.REF_FACESCONTEXT);
        Assert.assert_it(null != facesContext);
        
	treeNav = (TreeNavigator)objectManager.get(facesContext.getRequest(), 
			   Constants.REF_TREENAVIGATOR);
	Assert.assert_it(null!= treeNav);
	
        UIComponent uiComponent = treeNav.getNextStart();
	Assert.assert_it(null != uiComponent);

        // Render the component, if it has a renderer
        //
	try {
	    uiComponent.setRendererType("TextAreaRenderer");
	    uiComponent.render(facesContext);
	} catch (java.io.IOException e) {
	    throw new JspException("Problem rendering component: "+
				   e.getMessage());
	} catch (FacesException f) {
	    throw new JspException("Problem rendering component: "+
				   f.getMessage());
	}
        return(EVAL_BODY_INCLUDE);
    }

    /**
     * Gets the TextEntry_TextAreaTag's body if specified.
     */
    public int doAfterBody() throws JspException {

        Assert.assert_it( pageContext != null );
    
        ObjectManager ot = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        FacesContext rc = (FacesContext)ot.get(pageContext.getRequest(),
                Constants.REF_FACESCONTEXT);
        Assert.assert_it( rc != null );

        UITextEntry c = (UITextEntry) ot.get(pageContext.getRequest(), id);
        if ( c != null ) {
           if ( getBodyContent() != null ) {
               c.setText(getBodyContent().getString());
           }
        }
        return SKIP_BODY;
    }

    /**
     * Renders the Form's end Tag
     */
    public int doEndTag() throws JspException{
	String rendererType = null;
	TreeNavigator treeNav = null;
        ObjectManager objectManager = null;
	FacesContext facesContext = null;
	
	objectManager = (ObjectManager) pageContext.getServletContext().
	    getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
	
        facesContext = 
	    (FacesContext)objectManager.get(pageContext.getRequest(),
			   Constants.REF_FACESCONTEXT);
        Assert.assert_it(null != facesContext);
        
	treeNav = (TreeNavigator)objectManager.get(facesContext.getRequest(), 
					   Constants.REF_TREENAVIGATOR);
	Assert.assert_it(null!= treeNav);
	
        UIComponent uiComponent = treeNav.getNextEnd();
	Assert.assert_it(null != uiComponent);

        // Complete the rendering process
        //
	try {
	    uiComponent.renderComplete(facesContext);
	} catch (java.io.IOException e) {
	    throw new JspException("Problem completing rendering: "+
				   e.getMessage());
	} catch (FacesException f) {
	    throw new JspException("Problem completing rendering: "+
				   f.getMessage());
	}

        return(EVAL_PAGE);
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        id = null;
        value = null;
        rows = null;
        cols = null;
        wrap = null;
        model = null;
        scope = null;
        valueChangeListener = null;
    }

    /**
     * Returns the value of the "id" attribute
     *
     * @return String value of "id" attribute
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the "id" attribute
     * @param id value of "id" attribute 
     */
    public void setId(String id) {
        this.id = id;
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

    /**
     * Returns the value of valueChangeListener attribute
     *
     * @return String value of valueChangeListener attribute
     */
    public String getValueChangeListener() {
        return this.valueChangeListener;
    }

    /**
     * Sets valueChanheListener attribute
     * @param change_listener value of formListener attribute
     */
    public void setValueChangeListener(String change_listener) {
        this.valueChangeListener = change_listener;
    }

    /**
     * Returns the value of the scope attribute
     *
     * @return String value of scope attribute
     */
    public String getScope() {
        return this.scope;
    }

    /**
     * Sets scope attribute
     * @param scope value of scope attribute
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Returns the value of the model attribute
     *
     * @return String value of model attribute
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Sets the model attribute
     * @param model value of model attribute
     */
    public void setModel(String model) {
        this.model = model;
    }


} // end of class TextEntry_TextAreaTag
