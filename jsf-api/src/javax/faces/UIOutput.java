/*
 * $Id: UIOutput.java,v 1.3 2002/01/28 18:30:08 visvan Exp $
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
    private String modelReference;

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
     * If this component's modelReference property is non-null, it will
     * return the current value contained in the object
     * referenced by the modelReference property. If the modelReference 
     * property is null, it will return a locally stored value.
     *
     * @see #getModel
     * @param rc the render context used to render this component
     * @return String containing the current text value
     */
    public String getValue(RenderContext rc) {

        String label = null;
        if ( modelReference == null )  {
            return (String) value;
        }
        else {
            try {
                label = (String) rc.getObjectAccessor().
                        getObject(rc.getRequest(), (String) modelReference);
            } catch ( FacesException e ) {
                // PENDING (visvan) skip this exception ??
                return (String) value;
            }
            return label;
        }
    }

    /**
     * Sets the current text value for this component.
     * If this component's modelReference property is non-null, it will
     * store the new value in the object referenced by the
     * modelReference property.  If the modelReference property is null, it
     * will store the value locally.
     * @param rc the render context used to render this component
     * @param text String containing the new text value for this component
     */
    public void setValue(RenderContext rc, String label) {
        if ( modelReference == null ) {
            value = label;
        } else {
            try {
                rc.getObjectAccessor().setObject(rc.getRequest(), 
				 (String)modelReference, label);
            } catch ( FacesException e ) {
                // PENDING ( visvan ) skip this exception ??
                value = label;
            }
        }
    }

    /**
     * The model-reference property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  The model-reference
     * must resolve to an object which implements one of the following types:
     * <ul>
     * <li><code>java.lang.String</code>
     * <li><code>java.util.Collection</code> of <code>String</code> objects;
     *     each element is converted to a separate line of text.
     * </ul>
     * @see #setModelReference
     * @return String containing the model-reference for this component
     */
    public String getModelReference() {
        return modelReference;
    }

    /**
     * Sets the model-reference property on this data-bound component.
     * @see #getModelReference
     * @param modelReference the String which contains a reference to the
     *        object which acts as the data-source for this component
     */
    public void setModelReference(String modelReference) {
        this.modelReference = modelReference;
    }
}
