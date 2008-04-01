/*
 * $Id: FormTag.java,v 1.12 2001/12/08 00:33:53 rogerk Exp $
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

// FormTag.java

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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.faces.ObjectTable;
import java.util.Vector;

/**
 *
 *  <B>FormTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormTag.java,v 1.12 2001/12/08 00:33:53 rogerk Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class FormTag extends TagSupport
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
    private String model = null;
    private String scope = null;
    private String formListener = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public FormTag()
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
     * Renders Form's start tag
     */
    public int doStartTag() throws JspException{

        // PENDING(visvan) use tagext class to validate attributes.
        // get ObjectTable from ServletContext.
        Assert.assert_it( pageContext != null );
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(), 
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );

        if ( name != null ) {
            Renderer renderer = getRenderer(rc);
            WForm c = (WForm) ot.get(pageContext.getRequest(), name);
            if (c == null) {
                c = createComponent(rc);
                addToScope(c, ot);
            }
            try {
                rc.pushChild(c); 
                renderer.renderStart(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering Form component: "+
                        e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                f.getMessage());
            }
            // PENDING (visvan) return evaluate body tag again because listener
            // tags might be nested
        }
        return(EVAL_BODY_INCLUDE);
    }

    /**
     * Returns the appropriate renderer for WForm Component
     * @param RenderContext Contains client information.
     * @return Renderer Renderer for Form compoenent
     * @exception JspException if an renderer could not be found
     */
     public Renderer getRenderer(RenderContext rc ) throws JspException {
        Renderer renderer = null;
   
        RenderKit renderKit = rc.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }
        try {
            String class_name = "com.sun.faces.renderkit.html_basic.FormRenderer";
            renderer = renderKit.getRenderer(class_name);
        } catch (FacesException e) {
            e.printStackTrace();
            throw new JspException("FacesException " + e.getMessage());
        }

        if (renderer == null) {
            throw new JspException(
                "Could not determine 'renderer' for Form component");
        }
        return renderer;	
    }
    
    /** Adds the component and listener to the ObjectTable
     * in the appropriate scope
     *
     * @param c WComponent to be stored in namescope
     * @param ot Object pool
     */
    public void addToScope(WForm c, ObjectTable ot) {
   
        Vector listeners = null; 
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        ot.put(pageContext.getSession(), name, c);

        // PENDING ( visvan ) this should be done in Component's 
        // addListener method. 
        String lis_name = name.concat(Constants.REF_FORMLISTENERS);
        listeners = (Vector) ot.get(pageContext.getRequest(), lis_name);
        if ( listeners == null) {
            listeners = new Vector();
        }    
        // this vector contains only the name of the listeners. The
        // listener itself is stored in the objectTable.
        listeners.add(formListener);
        ot.put(pageContext.getSession(),lis_name, listeners);
    }

    /**
     * Creates a Form component and sets renderer specific
     * properties.
     *
     * @param rc renderContext
     */
    protected WForm createComponent(RenderContext rc) {
        
        WForm c = new WForm();

        // set renderer specific properties 
        c.setAttribute(rc, "name", name);

        // set render independent attributes
        // make sure that the model object is registered
        if ( model != null ) {
            c.setModel(model);
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
     * Returns the value of formListener attribute
     *
     * @return String value of formListener attribute
     */
    public String getFormListener() {
        return this.formListener;
    }

    /**
     * Sets formListener attribute
     * @param form_listener value of formListener attribute
     */
    public void setFormListener(String form_listener) {
        this.formListener = form_listener;
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

        WForm c = (WForm) ot.get(pageContext.getRequest(), name);
        if ( c != null ) {
            Renderer form_renderer = getRenderer(rc);
            try {
                form_renderer.renderComplete(rc, c);
                rc.popChild();
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering Form component: "+
                        e.getMessage());
            }catch (FacesException e) {
                e.printStackTrace();
                throw new JspException("FacesException " + e.getMessage());
            }
        }
        return(EVAL_PAGE);
    }

} // end of class FormTag
