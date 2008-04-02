/*
 * $Id: ChartItemComponent.java,v 1.1 2004/05/20 17:08:47 jvisvanathan Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.components;


import java.io.IOException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.component.UIComponentBase;


/**
 * <p><strong>ChartItemComponent</strong> is a component that may be nested
 * inside a {@link ChartComponent}, and causes the addition of a 
 * {@link DataItem} instance to the list of available options for the 
 * parent component.  The contents of the
 * {@link ChartItem} can be specified in one of the following ways:</p>
 * <ul>
 * <li>The <code>value</code> attribute's value is an instance of
 *     {@link ChartItem}.</li>
 * <li>The associated {@link ValueBinding} points at a model data
 *     item of type {@link ChartItem}.</li>
 * <li>A new {@link ChartItem} instance is synthesized from the values
 *     of the <code>itemLabel</code>, <code>color</code>, <code>value</code></li>
 * </ul>
 */

public class ChartItemComponent extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "ChartItem";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "ChartItem";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItem} instance with default property
     * values.</p>
     */
    public ChartItemComponent() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    private String itemLabel = null;
    private String itemColor = null;
    private Object itemValue = null;
    private Object value = null;


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the label for this chart item.</p>
     */
    public String getItemLabel() {

	if (this.itemLabel != null) {
	    return (this.itemLabel);
	}
	ValueBinding vb = getValueBinding("itemLabel");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }


    /**
     * <p>Set the label for this chart item.</p>
     *
     * @param label The new label
     */
    public void setItemLabel(String label) {

        this.itemLabel = label;

    }

    /**
     * <p>Return the color for this chart item.</p>
     */
    public String getItemColor() {

	if (this.itemColor != null) {
	    return (this.itemColor);
	}
	ValueBinding vb = getValueBinding("itemColor");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }


    /**
     * <p>Set the color for this chart item.</p>
     *
     * @param color The new color
     */
    public void setItemColor(String color) {

        this.itemColor = color;

    }
    
    /**
     * <p>Return the server value for this selection item.</p>
     */
    public Object getItemValue() {

	if (this.itemValue != null) {
	    return (this.itemValue);
	}
	ValueBinding vb = getValueBinding("itemValue");
	if (vb != null) {
	    return ((Integer) (vb.getValue(getFacesContext())));
	} else {
	    return null;
	}
    }
    
    /**
     * <p>Set the server value for this selection item.</p>
     *
     * @param itemValue The new server value
     */
    public void setItemValue(Object itemValue) {

        this.itemValue = itemValue;

    }

    /**
     * <p>Returns the <code>value</code> of this chart item</p>
     */
    public Object getValue() {

	if (this.value != null) {
	    return (this.value);
	}
	ValueBinding vb = getValueBinding("value");
	if (vb != null) {
	    return (vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}
    }


    /**
     * <p>Sets the <code>value</code> property of this chart item</p>
     * 
     * @param value the new value
     */
    public void setValue(Object value) {

        this.value = value;

    }


    // ----------------------------------------------------- StateHolder Methods
    public Object saveState(FacesContext context) {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = itemLabel;
        values[2] = itemColor;
        values[3] = itemValue;
        values[4] = value;
        return (values);
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        itemLabel = (String) values[1];
        itemColor = (String) values[2];
        itemValue = values[3];
        value = values[4];
    }
}
