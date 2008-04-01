/*
 * $Id: FormTag.java,v 1.17 2002/01/10 22:32:50 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import javax.faces.UIForm;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.faces.ObjectManager;
import java.util.Vector;

/**
 *
 *  <B>FormTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormTag.java,v 1.17 2002/01/10 22:32:50 edburns Exp $
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
        // get ObjectManager from ServletContext.
        Assert.assert_it( pageContext != null );
        ObjectManager ot = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(), 
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );

        if ( name != null ) {

            // 1. Get or create the component instance.
            //
            UIForm c = (UIForm) ot.get(pageContext.getRequest(), name);
            if ( c == null ) {
                c = createComponent(rc);
                addToScope(c, ot);
            }

            // 2. Render the component.
            //
            try {
                c.setRendererName(rc, "FormRenderer");
                c.render(rc);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering component: "+
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

    /** Adds the component and listener to the ObjectManager
     * in the appropriate scope
     *
     * @param c UIComponent to be stored in namescope
     * @param ot Object pool
     */
    public void addToScope(UIForm c, ObjectManager ot) {
   
        Vector listeners = null; 
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        ot.put(pageContext.getSession(), name, c);

        // PENDING ( visvan ) this should be done in Component's 
        // addListener method. 
        if ( formListener != null ) {
            String lis_name = name.concat(Constants.REF_FORMLISTENERS);
            listeners = (Vector) ot.get(pageContext.getRequest(), lis_name);
            if ( listeners == null) {
                listeners = new Vector();
            }    
            // this vector contains only the name of the listeners. The
            // listener itself is stored in the objectManager.
            listeners.add(formListener);
            ot.put(pageContext.getSession(),lis_name, listeners);
        }
    }

    /**
     * Creates a Form component and sets renderer specific
     * properties.
     *
     * @param rc renderContext
     */
    protected UIForm createComponent(RenderContext rc) {
        
        UIForm c = new UIForm();

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
        // get ObjectManager from ServletContext.
        ObjectManager ot = (ObjectManager)pageContext.getServletContext().
                 getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(), 
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );

//PENDING(rogerk)can we eliminate this extra get if component is instance
//variable? If so, threading issue?
//
        UIForm c = (UIForm) ot.get(pageContext.getRequest(), name);
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
        model = null;
        scope = null;
        formListener = null;
    }


} // end of class FormTag
