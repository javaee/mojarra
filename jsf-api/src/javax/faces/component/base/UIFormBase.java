/*
 * $Id: UIFormBase.java,v 1.5 2003/09/22 16:08:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UIFormBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIForm}.</p>
 */

public class UIFormBase extends UINamingContainerBase implements UIForm {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIFormBase} instance with default property
     * values.</p>
     */
    public UIFormBase() {

        super();
        setRendererType("Form");

    }

    // ------------------------------------------------ Properties

    /**
     * <p>See {@link UIForm#isSubmitted}</p>
     */
    
    private boolean submitted;

    public boolean isSubmitted() {
	return submitted;
    }

    public void setSubmitted(boolean submitted) {
	this.submitted = submitted;
    }

    // ------------------------------------------------ UIComponent overrides

    public void processDecodes(FacesContext context) throws IOException {

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

	// Validate this component itself
	if (this instanceof UIInput) {
	    // PENDING(craigmcc): shouldn't this be in UIInputBase
	    ((UIInput) this).validate(context);
	}

	// Advance to Render Response if this component is not valid
        if ((this instanceof UIInput) &&
            !((UIInput) this).isValid()) {
	    // PENDING(craigmcc): shouldn't this be in UIInputBase
            context.renderResponse();
        }
    }

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

        // Process this component itself
        if (this instanceof UIInput) {
	    // PENDING(craigmcc): shouldn't this be in UIInputBase
            ((UIInput) this).updateModel(context);
            if (!((UIInput) this).isValid()) {
                context.renderResponse();
            }
        }
    }

    // ------------------------------------------------  StateHolder overrides

    public Object saveState(FacesContext context) {
	setSubmitted(false);
	return super.saveState(context);
    }






}
