/*
 * $Id: TextEntry_TextAreaTag.java,v 1.10 2001/12/13 00:16:00 rogerk Exp $
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
import java.util.Vector;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *
 *  <B>TextEntry_TextAreaTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_TextAreaTag.java,v 1.10 2001/12/13 00:16:00 rogerk Exp $
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

           // 1. Get or create the component instance.
           //
            WTextEntry c = (WTextEntry) ot.get(pageContext.getRequest(), name);
            if (c == null) {
                c = createComponent(rc);
                addToScope(c, ot);
            }

            // 2. Render the component.
            //
            try {
                c.setRendererName(rc, "TextAreaRenderer");
                c.render(rc);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering component: "+
                    e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                    f.getMessage());
            }
        }
        return(EVAL_BODY_INCLUDE);
    }

    /** Adds the component and listener to the ObjectTable
     * in the appropriate scope
     *
     * @param c WComponent to be stored in namescope
     * @param ot Object pool
     */
    public void addToScope(WTextEntry c, ObjectTable ot) {

        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        ot.put(pageContext.getSession(), name, c);

        if ( valueChangeListener != null ) {
            String lis_name = name.concat(Constants.REF_VALUECHANGELISTENERS);
            Vector listeners = (Vector) ot.get(pageContext.getRequest(), lis_name);
            if ( listeners == null) {
                listeners = new Vector();
            }
            // this vector contains only the name of the listeners. The
            // listener itself is stored in the objectTable.
            listeners.add(valueChangeListener);
            ot.put(pageContext.getSession(),lis_name, listeners);
        }
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

//PENDING(rogerk)can we eliminate this extra get if component is instance
//variable? If so, threading issue?
//
        WTextEntry c = (WTextEntry) ot.get(pageContext.getRequest(), name);
        Assert.assert_it( c != null );

        // Complete the rendering process
        //
        try {
            c.renderComplete(rc);
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

        name = null;
        value = null;
        rows = null;
        cols = null;
        wrap = null;
        model = null;
        scope = null;
        valueChangeListener = null;
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

        // If model attribute is not found get it
        // from parent form if it exists. If not
        // set text as an attribute so that it can be
        // used during rendering.

        // PENDING ( visvan )
        // make sure that the model object is registered
        if ( model != null ) {
            c.setModel(model);
        } else {
            // PENDING ( visvan ) all tags should implement a common
            // interface ??
            FormTag ancestor = null;
            try {
                ancestor = (FormTag) findAncestorWithClass(this,
                    FormTag.class);
               String model_str = ancestor.getModel();
               if ( model_str != null ) {
                   model = "$" + model_str + "." + name;
                   c.setModel(model);
               }
            } catch ( Exception e ) {
                // If form tag cannot be found then model is null
            }
        }
        return c;
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
