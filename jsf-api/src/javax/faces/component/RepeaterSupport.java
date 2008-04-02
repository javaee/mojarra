/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
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

package javax.faces.component;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;


/**
 * <p><strong>RepeaterSupport</strong> is a utility class that may be
 * utilized by {@link UIComponent}s that implement {@link Repeater} to
 * delegate repeating component management methods.
 *
 * PENDING(craigmcc) - This will probably need to end up implementing
 * {@link StateHolder} as well
 */

public class RepeaterSupport implements Serializable {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The default number of elements for internal collections.</p>
     */
    private static final int DEFAULT_SIZE = 10;


    /**
     * <p>The default character used to separate components of generated
     * client identifiers.</p>
     */
    private static final char SEPARATOR = '_';


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link RepeaterSupport} instance configured for the
     * default number of rows.</p>
     */
    public RepeaterSupport() {

	this(DEFAULT_SIZE);

    }


    /**
     * <p>Construct a new {@link RepeaterSupport} instance configured for the
     * specified number of rows.</p>
     *
     * @param size Number of rows for which to configure
     */
    public RepeaterSupport(int size) {

	list = new ArrayList(size);

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Each element of this <code>List</code> represents the saved state
     * information for a particular row number of a repeating set.  The
     * element is a <code>Map</code> of <code>RepeaterSupportData</code>
     * instances, keyed by the <code>UIComponent</code> for which that data
     * is maintained.
     */
    private List list = null;


    /**
     * <p>The current zero-relative row subscript.  A -1 value indicates
     * that no row has currently been selected.</p>
     */
    private int row = -1;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the zero-relative subscript of the current row.  A -1 value
     * indicates that no row has currently been selected.</p>
     */
    public int getRowIndex() {

	return (this.row);

    }


    /**
     * <p>Set the zero-relative subscript of the current row.  A -1 value
     * indicates that no row has currently been selected (which will
     * be the case when a new instance of this class is instantiated).</p>
     *
     * @param row The new subscript
     *
     * @exception IllegalArgumentException if <code>row</code>
     *  is less than -1
     */
    public void setRowIndex(int row) {

	if (row < -1) {
	    throw new IllegalArgumentException();
	}
	this.row = row;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Generate and return a unique client identifier for the currently
     * selected row of the child component with the specified identifier.</p>
     *
     * @param context {@link FacesContext} for this request
     * @param parent Parent {@link UIComponent}
     * @param childClientId Base client identifier for the
     *  descendant component
     *
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public String getChildClientId(FacesContext context,
				   UIComponent parent,
				   String childClientId) {

	if ((context == null) || (parent == null) || (childClientId == null)) {
	    throw new NullPointerException();
	}
	int row = getRowIndex();
	return (getFlattenedId(parent.getClientId(context),
			       childClientId, row));

    }


    /**
     * <p>Return the previous value for the current row, for the specified
     * child component.</p>
     *
     * @param component Child {@link UIComponent} for which to retrieve
     *  a previous value
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public Object getChildPrevious(UIComponent component) {

	// Validate our input parameters and state
	if (component == null) {
	    throw new NullPointerException();
	}

	// Look up and return the requested previous value
	if ((row < 0) || (row >= list.size())) {
	    return (null);
	}
	Map map = (Map) list.get(row);
	if (map == null) {
	    return (null);
	}
	RepeaterSupportData data = (RepeaterSupportData) map.get(component);
	if (data == null) {
	    return (null);
	}
	return (data.getPrevious());

    }


    /**
     * <p>Set the previous value for the current row, for the specified
     * child component.</p>
     *
     * @param component Child {@link UIComponent} for which to set a
     *  previous value
     * @param previous The new value, which may be <code>null</code>
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void setChildPrevious(UIComponent component, Object previous) {

	// Validate our input parameters and state
	if (component == null) {
	    throw new NullPointerException();
	}

	// Save the new previous value
	while (list.size() <= row) {
	    list.add(null);
	}
	Map map = (Map) list.get(row);
	if (map == null) {
	    map = new HashMap();
	    list.set(row, map);
	}
	RepeaterSupportData data = (RepeaterSupportData) map.get(component);
	if (data == null) {
	    data = new RepeaterSupportData();
	    map.put(component, data);
	}
	data.setPrevious(previous);

    }


    /**
     * <p>Return <code>true</code> if the local value of the specified
     * child component, for the current row, is valid.</p>
     *
     * @param component Child {@link UIComponent} for which to retrieve the
     *  validity
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public boolean isChildValid(UIComponent component) {

	// Validate our input parameters and state
	if (component == null) {
	    throw new NullPointerException();
	}

	// Look up and return the requested valid flag
	if ((row < 0) || (row >= list.size())) {
	    return (true);
	}
	Map map = (Map) list.get(row);
	if (map == null) {
	    return (true);
	}
	RepeaterSupportData data = (RepeaterSupportData) map.get(component);
	if (data == null) {
	    return (true);
	}
	return (data.isValid());

    }


    /**
     * <p>Set the valid property for the specified child component, for the
     * current row, to the specified state.</p>
     *
     * @param component Child {@link UIComponent} for which to set validity
     * @param valid New valid state
     *
     * @exception IllegalStateException if called when we are
     *  not iterating over a row
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void setChildValid(UIComponent component, boolean valid) {

	// Validate our input parameters and state
	if (component == null) {
	    throw new NullPointerException();
	}

	// Save the new valid flag
	while (list.size() <= row) {
	    list.add(null);
	}
	Map map = (Map) list.get(row);
	if (map == null) {
	    map = new HashMap();
	    list.set(row, map);
	}
	RepeaterSupportData data = (RepeaterSupportData) map.get(component);
	if (data == null) {
	    data = new RepeaterSupportData();
	    map.put(component, data);
	}
	data.setValid(valid);

    }


    /**
     * <p>Return the local value for the current row, for the specified
     * child component.</p>
     *
     * @param component Child {@link UIComponent} for which to retrieve
     *  a local value
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public Object getChildValue(UIComponent component) {

	// Validate our input parameters and state
	if (component == null) {
	    throw new NullPointerException();
	}

	// Look up and return the requested current value
	if ((row < 0) || (row >= list.size())) {
	    return (null);
	}
	Map map = (Map) list.get(row);
	if (map == null) {
	    return (null);
	}
	RepeaterSupportData data = (RepeaterSupportData) map.get(component);
	if (data == null) {
	    return (null);
	}
	return (data.getValue());

    }


    /**
     * <p>Set the local value for the current row, for the specified
     * child component.</p>
     *
     * @param component Child {@link UIComponent} for which to set a
     *  local value
     * @param value The new value, which may be <code>null</code>
     *
     * @exception IllegalStateException if called when we are
     *  not iterating over a row
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void setChildValue(UIComponent component, Object value) {

	// Validate our input parameters and state
	if (component == null) {
	    throw new NullPointerException();
	}

	// Save the new current value
	while (list.size() <= row) {
	    list.add(null);
	}
	Map map = (Map) list.get(row);
	if (map == null) {
	    map = new HashMap();
	    list.set(row, map);
	}
	RepeaterSupportData data = (RepeaterSupportData) map.get(component);
	if (data == null) {
	    data = new RepeaterSupportData();
	    map.put(component, data);
	}
	data.setValue(value);

    }


    // ---------------------------------------------------------- Static Methods


    /**
     * <p>Find and return the closest parent {@link UIComponent} that
     * implements {@link Repeater}, if any.  If there is no such component,
     * return <code>null</code> instead.</p>
     *
     * @param component {@link UIComponent} for which to locate a parent
     *  implementing {@link Repeater}
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public static Repeater findParentRepeater(UIComponent component) {

	if (component == null) {
	    throw new NullPointerException();
	}
	while (component != null) {
	    component = component.getParent();
	    if (component instanceof Repeater) {
		return ((Repeater) component);
	    }
	}
	return (null);

    }


    /**
     * <p>Generate and return a unique client identifier for the specified
     * component, delegating to any parent component that implements
     * {@link Repeater} (if one exists), or generating one normally otherwise.
     *
     * @param context {@link FacesContext} for the current request
     * @param component {@link UIComponent} for which to generate and
     *  return a client identifier
     *
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public static String getClientId(FacesContext context,
				     UIComponent component) {

	if ((context == null) || (component == null)) {
	    throw new NullPointerException();
	}
	String clientId = component.getClientId(context);
	Repeater repeater = findParentRepeater(component);
	if (repeater != null) {
	    clientId = repeater.getChildClientId(context, clientId);
	}
	return (clientId);

    }


    /**
     * <p>Generate and return a flattened client identifier based on the
     * input parameters.</p>
     *
     * @param parentId Client identifier of the parent component, or
     *  <code>null</code> if the parent does not have a client identifier
     * @param childId Client identifier of the child component
     * @param row Current row number of the child component
     */
    private static String getFlattenedId(String parentId,
					 String childId, int row) {

	int size = 0;
	if (parentId != null) {
	    size += parentId.length() + 1; // 1 == separator
	}
	size += 3 + 1; // subscript + separator
	if (childId != null) {
	    size += childId.length();
	}
	StringBuffer sb = new StringBuffer(size);
	if (parentId != null) {
	    sb.append(parentId);
	    sb.append(SEPARATOR);
	}
	if (childId != null) {
	    sb.append(childId);
	    sb.append(SEPARATOR);
	}
	sb.append("" + row);
	return (sb.toString());

    }

    public int getRowCount() {
        return list.size();
    }
}


// Private Class To Represent Saved Information
class RepeaterSupportData implements Serializable {

    private Object previous;
    Object getPrevious() {
	return (this.previous);
    }
    void setPrevious(Object previous) {
	this.previous = previous;
    }

    private boolean valid = true;
    boolean isValid() {
	return (this.valid);
    }
    void setValid(boolean valid) {
	this.valid = valid;
    }

    private Object value;
    Object getValue() {
	return (this.value);
    }
    public void setValue(Object value) {
	this.value = value;
    }

}
