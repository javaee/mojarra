/*
 * $Id: UIForm.java,v 1.47 2005/04/21 18:55:29 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UIForm</strong> is a {@link UIComponent} that represents an
 * input form to be presented to the user, and whose child components represent
 * (among other things) the input fields to be included when the form is
 * submitted.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Form</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIForm extends UIComponentBase implements NamingContainer {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Form";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Form";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIForm} instance with default property
     * values.</p>
     */
    public UIForm() {

        super();
        setRendererType("javax.faces.Form");

    }


    // ------------------------------------------------------ Instance Variables


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>The form submitted flag for this {@link UIForm}.</p>
     */
    private boolean submitted = false;


    /**
     * <p>Returns the current value of the <code>submitted</code>
     * property.  The default value is <code>false</code>.  See {@link
     * #setSubmitted} for details.</p>
     *
     */
    public boolean isSubmitted() {

        return (this.submitted);

    }


    /**
     * <p>If <strong>this</strong> <code>UIForm</code> instance (as
     * opposed to other forms in the page) is experiencing a submit
     * during this request processing lifecycle, this method must be
     * called, with <code>true</code> as the argument, during the {@link
     * UIComponent#decode} for this <code>UIForm</code> instance.  If
     * <strong>this</strong> <code>UIForm</code> instance is
     * <strong>not</strong> experiencing a submit, this method must be
     * called, with <code>false</code> as the argument, during the
     * {@link UIComponent#decode} for this <code>UIForm</code>
     * instance.</p>
     *
     * <p>The value of a <code>UIForm</code>'s submitted property must
     * not be saved as part of its state.</p>
     */
    public void setSubmitted(boolean submitted) {

        this.submitted = submitted;

    }
    
    private boolean prependId = true;
    
    /**
     * <p>If true, this <code>UIForm</code> instance does allow its id
     * to be pre-pended to its descendent's id during the generation of
     * clientIds for the descendents.  The default value of this
     * property is <code>true</code>.</p>
     */
    public boolean isPrependId() {
        return this.prependId;
    }
    
    /**
     * <p>Set the value of the <code>prependId</code> property.  See
     * {@link #isPrependId}.</p>
     */
    public void setPrependId(boolean prependId) {
        this.prependId = prependId;
    }


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>Override {@link UIComponent#processDecodes} to ensure that the
     * form is decoded <strong>before</strong> its children.  This is
     * necessary to allow the <code>submitted</code> property to be
     * correctly set.</p>
     *
     * @exception NullPointerException {@inheritDoc}
     */
    public void processDecodes(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
	
        // Process this component itself
        decode(context);

	// if we're not the submitted form, don't process children.
	if (!isSubmitted()) {
	    return;
	}

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processDecodes(context);
        }
	
    }


    /**
     * <p>Override {@link UIComponent#processValidators} to ensure that
     * the children of this <code>UIForm</code> instance are only
     * processed if {@link #isSubmitted} returns <code>true</code>.</p>
     * 
     * @exception NullPointerException {@inheritDoc}
     */
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
	if (!isSubmitted()) {
	    return;
	}

        // Process all the facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processValidators(context);
        }

    }


    /**
     * <p>Override {@link UIComponent#processUpdates} to ensure that the
     * children of this <code>UIForm</code> instance are only processed
     * if {@link #isSubmitted} returns <code>true</code>.</p>
     * 
     * @exception NullPointerException {@inheritDoc}
     */
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
	if (!isSubmitted()) {
	    return;
	}

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processUpdates(context);
        }

    }

    
    /**
     * <p>Override the {@link UIComponent#getContainerClientId} to allow
     * users to disable this form from prepending its <code>clientId</code> to
     * its descendent's <code>clientIds</code> depending on the value of
     * this form's {@link #isPrependId} property.</p>
     */
    protected String getContainerClientId(FacesContext context) {
        if (this.isPrependId()) {
            return super.getContainerClientId(context);
        } else {
            UIComponent parent = this.getParent();
            while (parent != null) {
                if (parent instanceof NamingContainer) {
                    return parent.getContainerClientId(context);
                }
                parent = parent.getParent();
            }
        }
        return null;
    }

}
