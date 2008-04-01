/*
 * $Id: TextEntry_InputTag.java,v 1.9 2001/12/08 00:33:53 rogerk Exp $
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

// TextEntry_InputTag.java

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
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>TextEntry_InputTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_InputTag.java,v 1.9 2001/12/08 00:33:53 rogerk Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class TextEntry_InputTag extends TagSupport
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
    private String model = null;
    private String scope = null;
    private String valueChangeListener = null;
    private String maxlength = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TextEntry_InputTag()
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
     * Renders TextEntry_InputTag's start tag and its attributes.
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
                addToScope(c, ot); 
            }
            try {
               rc.pushChild(c);
               renderer.renderStart(rc, c);
//PENDING(rogerk) complet/pop should be done in doEndTag
//
               renderer.renderComplete(rc, c);
               rc.popChild();
            } catch (java.io.IOException e) {
                //e.printStackTrace();
                throw new JspException("Problem rendering Input component: "+
                        e.getMessage());
            } catch (FacesException f) {

                throw new JspException("Problem rendering component: "+
                f.getMessage());

            }
        }

        // JV return evaluate body tag again because listener
        // tags might be nested
        return(EVAL_BODY_INCLUDE);
    }
    
    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException{

        return EVAL_PAGE;
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
            String class_name = "com.sun.faces.renderkit.html_basic.InputRenderer";
            renderer = renderKit.getRenderer(class_name);
        } catch (FacesException e) {
            e.printStackTrace();
            throw new JspException("FacesException " + e.getMessage());
        }

        if (renderer == null) {
            throw new JspException(
                "Could not determine renderer for TextEntry component");
        }
        return renderer;	
    }

    /**
     * Creates a TextEntry component and sets renderer specific
     * properties.
     *
     * @param rc renderContext client information
     */
    protected WTextEntry createComponent(RenderContext rc) throws
            JspException {
        WTextEntry c = new WTextEntry();
        // set renderer specific properties 
        c.setAttribute(rc, "name", name);
        c.setAttribute(rc, "size", size);
        c.setAttribute(rc, "maxlength", maxlength);
        
        // If model attribute is not found get it 
        // from parent form if it exists. If not
        // set text as an attribute so that it can be
        // used during rendering.

        // PENDING ( visvan )
        // make sure that the model object is registered
        if ( model != null ) {
            c.setModel(model);
        } else {
            // JV CHANGE
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
        if ( value != null ) {
            c.setText(rc,value);
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


} // end of class TextEntry_InputTag
