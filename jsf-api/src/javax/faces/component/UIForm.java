/*
 * $Id: UIForm.java,v 1.33 2003/09/30 14:35:00 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
 * "<code>Form</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIForm extends UIComponentBase implements NamingContainer {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIForm} instance with default property
     * values.</p>
     */
    public UIForm() {

        super();
        setRendererType("Form");

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link NamingContainer} implementation that we delegate to
     */
    private NamingContainerSupport namespace = new NamingContainerSupport();


    // -------------------------------------------------------------- Properties


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
     * <p>If this <code>UIForm</code> instance is experiencing a submit
     * during this request processing lifecycle, this method must be
     * called, passing <code>true</code>, during the {@link
     * UIComponent#decode} for this <code>UIForm</code> instance.</p>
     *
     * <p>The standard implementation delegates the responsibility for
     * calling this method to the {@link javax.faces.render.Renderer}
     * for this <code>UIForm</code> instance.  The standard form
     * renderer includes a hidden field in the form, named by the
     * <code>clientId</code> of this <code>UIForm</code> instance.  The
     * renderer checks for the presence of a request parameter named the
     * by the <code>clientId</code> of this <code>UIForm</code>
     * instance.  If present, it calls <code>setSubmitted(true)</code>.
     * If not present, it may call <code>setSubmitted(false)</code>.</p>
     *
     * <p>The value of a <code>UIForm</code>'s submitted property must
     * not be saved as part of its state.</p>
     */
    public void setSubmitted(boolean submitted) {

        this.submitted = submitted;

    }


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>Override {@link UIComponent#processDecodes} to ensure that the
     * form is decoded <strong>before</strong> its children.  This is
     * necessary to allow the <code>submitted</code> property to be
     * correctly set.</p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    public void processDecodes(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
	
        // Process this component itself
        decode(context);

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
     * @throws NullPointerException {@inheritDoc}
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
     * @throws NullPointerException {@inheritDoc}
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


    // ------------------------------------------------- NamingContainer Methods

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IllegalStateException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void addComponentToNamespace(UIComponent namedComponent) {

	namespace.addComponentToNamespace(namedComponent);

    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}    
     */ 
    public UIComponent findComponentInNamespace(String name) {

	return namespace.findComponentInNamespace(name);

    }


    public synchronized String generateClientId() {

	return namespace.generateClientId();

    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public void removeComponentFromNamespace(UIComponent namedComponent) {

	namespace.removeComponentFromNamespace(namedComponent);

    }


    // ----------------------------------------------------- StateHolder Methods


    /**
     * <p>Override <code>saveState()</code> to call
     * <code>setSubmitted(false)</code>.</p>
     */
    public Object saveState(FacesContext context) {

	setSubmitted(false);
	return super.saveState(context);

    }


}
