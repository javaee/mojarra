/*
 * $Id: UIForm.java,v 1.54 2008/02/22 01:49:30 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
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
    
    /**
     * <p>The prependId flag.</p>
     */
    private Boolean prependId;


    public boolean isPrependId() {

	if (this.prependId != null) {
	    return (this.prependId);
	}
	ValueExpression ve = getValueExpression("prependId");
	if (ve != null) {
	    try {
		return (Boolean.TRUE.equals(ve.getValue(getFacesContext().getELContext())));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (true);
	}

    }


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
     * @throws NullPointerException {@inheritDoc}
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

    
    /**
     * <p>Override the {@link UIComponent#getContainerClientId} to allow
     * users to disable this form from prepending its <code>clientId</code> to
     * its descendent's <code>clientIds</code> depending on the value of
     * this form's {@link #isPrependId} property.</p>
     */
    public String getContainerClientId(FacesContext context) {
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

    private Object[] values;

    @Override
    public Object saveState(FacesContext context) {

        if (values == null) {
             values = new Object[2];
        }
        values[0] = super.saveState(context);
        values[1] = prependId;

        return values;

    }

    @Override
    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        prependId = (Boolean) values[1];
        
    }
}
