/*
 * $Id: UIOutput.java,v 1.1 2002/01/10 22:32:22 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Hashtable;

/**
 * Class for representing a user-interface component which displays
 * output to the user.  This component type is not interactive -
 * a user cannot directly manipulate this component.
 */
public class UIOutput extends UIComponent {

    private static String TYPE = "Output";
    private Object value = null;
    private Object model = null;

    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;Output&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * Returns the current text value for this component.
     * If this component's model property is non-null, it will
     * return the current value contained in the object
     * referenced by the model property. If the model property
     * is null, it will return a locally stored value.
     *
     * @see #getModel
     * @param rc the render context used to render this component
     * @return String containing the current text value
     */
    public String getValue(RenderContext rc) {

        String label = null;
        if ( model == null )  {
            return (String) value;
        }
        else {
            try {
                label = (String) ModelAccessor.
                        getModelObject(rc, (String) model);
            } catch ( FacesException e ) {
                // PENDING (visvan) skip this exception ??
                return (String) value;
            }
            return label;
        }
    }

    /**
     * Sets the current text value for this component.
     * If this component's model property is non-null, it will
     * store the new value in the object referenced by the
     * model property.  If the model property is null, it
     * will store the value locally.
     * @param rc the render context used to render this component
     * @param text String containing the new text value for this component
     */
    public void setValue(RenderContext rc, String label) {
        if ( model == null ) {
            value = label;
        } else {
            try {
                ModelAccessor.setModelObject(rc, (String)model, label);
            } catch ( FacesException e ) {
                // PENDING ( visvan ) skip this exception ??
                value = label;
            }
        }
    }

    /**
     * The model property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  The supported types
     * for this reference:
     * <ul>
     * <li>String containing a model-reference in the scoped namespace
     *     e.g. &quot;user.lastName&quot; refers to an object named
     *          &quot;user&quot;
     *          with a property named &quot;lastName&quot;.
     * </ul>
     * @return Object describing the data-source for this component
     */
    public Object getModel() {
        return model;
    }

    /**
     * Sets the model property on this data-bound component.
     * @param model the Object which contains a reference to the
     *        object which acts as the data-source for this component
     */
    public void setModel(Object model) {
        this.model = model;
    }

}
