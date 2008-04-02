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


import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p><strong>UIData</strong> is a {@link UIComponent} that supports
 * data binding to a collection of data objects represented by a
 * {@link DataModel} instance, which is the current value of this component
 * itself (typically established via a {@link ValueBinding}.
 * During iterative processing over the rows of data in the data model,
 * the object for the current row is exposed as a request attribute
 * under the key specified by the <code>var</code> property.</p>
 *
 * <p>Only children of type {@link UIColumn} should be processed by
 * renderers associated with this component.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * <code>Table</code>.  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIData extends UIComponentBase
    implements NamingContainer, ValueHolder {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIData} instance with default property
     * values.</p>
     */
    public UIData() {

        super();
        setRendererType("Table");

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The first row number (zero-relative) to be displayed.</p>
     */
    private int first = 0;
    private boolean firstSet = false;


    /**
     * <p>The {@link DataModel} associated with this component, lazily
     * instantiated if requested.  This object is not part of the saved
     * and restored state of the component.</p>
     */
    private transient DataModel model = null;


    /**
     * <p>The zero-relative index of the current row number, or -1 for
     * no current row association.</p>
     */
    private int rowIndex = -1;
    private boolean rowIndexSet = false;


    /**
     * <p>The number of rows to display, or zero for all remaining
     * rows in the table.</p>
     */
    private int rows = 0;
    private boolean rowsSet = false;


    /**
     * <p>This map contains <code>SavedState</code> instances for each
     * descendant component, keyed by the client identifier of the
     * descendant.  Because descendant client identifiers will contain
     * the <code>rowIndex</code> value of the parent, per-row state
     * information is actually preserved.</p>
     */
    private Map saved = new HashMap();


    /**
     * <p>The local value of this {@link UIComponent}.</p>
     */
    private Object value = null;


    /**
     * <p>The request scope attribute under which the data object for the
     * current row will be exposed when iterating.</p>
     */
    private String var = null;



    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the zero-relative row number of the first row to be
     * displayed.</p>
     */
    public int getFirst() {

	if (this.firstSet) {
	    return (this.first);
	}
	ValueBinding vb = getValueBinding("first");
	if (vb != null) {
	    Integer value = (Integer) vb.getValue(getFacesContext());
	    return (value.intValue());
	} else {
	    return (this.first);
	}

    }


    /**
     * <p>Set the zero-relative row number of the first row to be
     * displayed.</p>
     *
     * @param first New first row number
     *
     * @exception IllegalArgumentException if <code>first</code>
     *  is negative
     */
    public void setFirst(int first) {

	if (first < 0) {
	    throw new IllegalArgumentException("" + first);
	}
        this.first = first;
	this.firstSet = true;

    }


    /**
     * <p>Return <code>true</code> to indicate that this component takes
     * responsibility for rendering its children.</p>
     */
    public boolean getRendersChildren() {

        return (true);

    }


    /**
     * <p>Return a flag indicating whether there is <code>rowData</code>
     * available at the current <code>rowIndex</code>.  If no
     * <code>wrappedData</code> is available, return <code>false</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row availability
     */
    public boolean isRowAvailable() {

	return (getDataModel().isRowAvailable());

    }


    /**
     * <p>Return the number of rows in the underlying data model.  If the
     * number of available rows is unknown, return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row count
     */
    public int getRowCount() {

        return (getDataModel().getRowCount());

    }


    /**
     * <p>Return the data object representing the data for the currently
     * selected row index, if any.</p>
     *
     * @exception FacesException if an error occurs getting the row data
     * @exception IllegalArgumentException if now row data is available
     *  at the currently specified row index
     */
    public Object getRowData() {

        return (getDataModel().getRowData());

    }


    /**
     * <p>Return the zero-relative index of the currently selected row.  If
     * we are not currently positioned on a row, return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row index
     */
    public int getRowIndex() {

	if (this.rowIndexSet) {
	    return (this.rowIndex);
	}
	ValueBinding vb = getValueBinding("rowIndex");
	if (vb != null) {
	    Integer value = (Integer) vb.getValue(getFacesContext());
	    return (value.intValue());
	} else {
	    return (this.rowIndex);
	}

    }


    /**
     * <p>Set the zero relative index of the current row, or -1 to indicate that
     * no row is currently selected, by implementing the following algorithm.
     * It is possible to set the row index at a value for which the underlying
     * data collection does not contain any row data.  Therefore, callers may
     * use the <code>isRowAvailable()</code> method to detect whether row data
     * will be available for use by the <code>getRowData()</code> methodl</p>
     *</p>
     * <ul>
     * <li>Save current state information for all descendant components (as
     *     described below).
     * <li>Store the new row index, and pass it on to the {@link DataModel}
     *     associated with this {@link UIData} instance.</li>
     * <li>If the new <code>rowIndex</code> value is -1:
     *     <ul>
     *     <li>If the <code>var</code> property is not null,
     *         remove the corresponding request scope attribute (if any).</li>
     *     <li>Reset the state information for all descendant components
     *         (as described below).</li>
     *     </ul></li>
     * <li>If the new <code>rowIndex</code> value is not -1:
     *     <ul>
     *     <li>If the <code>var</code> property is not null, call
     *         <code>getRowData()</code> and expose the resulting data object
     *         as a request scope attribute whose key is the <code>var</code>
     *         property value.</li>
     *     <li>Reset the state information for all descendant components
     *         (as described below).
     *     </ul></li>
     * </ul>
     *
     * <p>To save current state information for all descendant components,
     * {@link UIData} must maintain per-row information for each descendant
     * as follows:<p>
     * <ul>
     * <li>If the descendant is an instance of <code>ValueHolder</code>, save
     *     the state of the <code>value</code> property.</li>
     * <li>If the descendant is also an instance of
     *     <code>ConvertibleValueHolder</code>, save the state of the
     *     <code>valid</code> property.</li>
     * <li>If the descendant is also an instance of <code>UIInput</code>,
     *     save the state of the <code>previous</code> property.</li>
     * </ul>
     *
     * <p>To restore current state information for all descendant components,
     * {@link UIData} must reference its previously stored information for the
     * current <code>rowIndex</code> and call setters for each descendant
     * as follows:</p>
     * <ul>
     * <li>If the descendant is an instance of <code>ValueHolder</code>,
     *     restore the <code>value</code> property.</li>
     * <li>If the descendant is also an instance of
     *     <code>ConvertibleValueHolder</code>, restore the state of the
     *     <code>valid</code> property.</li>
     * <li>If the descendant is also an instance of <code>UIInput</code>,
     *     restore the state of the <code>previous</code> property.</li>
     * </ul>
     *
     * @param rowIndex The new row index value, or -1 for no associated row
     *
     * @exception FacesException if an error occurs setting the row index
     * @exception IllegalArgumentException if <code>rowIndex</code>
     *  is less than -1
     */
    public void setRowIndex(int rowIndex) {

        // Save current state for the previous row index
        saveDescendantState();

        // Update to the new row index
        int previous = this.rowIndex;
        this.rowIndex = rowIndex;
        DataModel model = getDataModel();
        model.setRowIndex(rowIndex);

        // Clear or expose the current row data as a request scope attribute
        if (var != null) {
            Map requestMap =
                getFacesContext().getExternalContext().getRequestMap();
            if (rowIndex == -1) {
                requestMap.remove(var);
            } else if (isRowAvailable()) {
		requestMap.put(var, getRowData());
	    } else {
		requestMap.remove(var);
            }
        }

        // Reset current state information for the new row index
        restoreDescendantState();

	this.rowIndexSet = true;

    }


    /**
     * <p>Return the number of rows to be displayed, or zero for all
     * remaining rows in the table.</p>
     */
    public int getRows() {

	if (this.rowsSet) {
	    return (this.rows);
	}
	ValueBinding vb = getValueBinding("rows");
	if (vb != null) {
	    Integer value = (Integer) vb.getValue(getFacesContext());
	    return (value.intValue());
	} else {
	    return (this.rows);
	}

    }


    /**
     * <p>Set the number of rows to be displayed, or zero for all
     * remaining rows in the table.</p>
     *
     * @param rows New number of rows
     *
     * @exception IllegalArgumentException if <code>rows</code>
     *  is negative
     */
    public void setRows(int rows) {

	if (rows < 0) {
	    throw new IllegalArgumentException("" + rows);
	}
        this.rows = rows;
	this.rowsSet = true;

    }


    /**
     * <p>Return the request-scope attribute under which the data object
     * for the current row will be exposed when iterating.</p>
     */
    public String getVar() {

	if (this.var != null) {
	    return (this.var);
	}
	ValueBinding vb = getValueBinding("var");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }


    /**
     * <p>Set the request-scope attribute under which the data object
     * for the current row wil be exposed when iterating.</p>
     *
     * @param var The new request-scope attribute name
     */
    public void setVar(String var) {

        this.var = var;

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[10];
        values[0] = super.saveState(context);
        values[1] = new Integer(first);
	values[2] = firstSet ? Boolean.TRUE : Boolean.FALSE;
        values[3] = new Integer(rowIndex);
	values[4] = rowIndexSet ? Boolean.TRUE : Boolean.FALSE;
        values[5] = new Integer(rows);
	values[6] = rowsSet ? Boolean.TRUE : Boolean.FALSE;
        values[7] = saved;
        values[8] = value;
        values[9] = var;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        first = ((Integer) values[1]).intValue();
	firstSet = ((Boolean) values[2]).booleanValue();
        rowIndex = ((Integer) values[3]).intValue();
	rowIndexSet = ((Boolean) values[4]).booleanValue();
        rows = ((Integer) values[5]).intValue();
	rowsSet = ((Boolean) values[6]).booleanValue();
        saved = (Map) values[7];
        value = values[8];
        var = (String) values[9];

    }


    // -------------------------------------------------- ValueHolder Properties


    public Object getLocalValue() {

	return (this.value);

    }


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


    public void setValue(Object value) {
        this.model = null;
        this.value = value;

    }


    // ----------------------------------------------------- UIComponent Methods


    private transient String baseClientId = null;

    public void setValueBinding(String name, ValueBinding binding) {
        
        if ("value".equals(name)) {
            this.model = null;
        }
        super.setValueBinding(name, binding);
        
    }


    /**
     * <p>Return a client identifier for this component that includes the
     * current value of the <code>rowIndex</code> property, if it is not
     * set to -1.  This implies that multiple calls to
     * <code>getClientId()</code> may return different results,
     * but ensures that child components can themselves will generate
     * row-specific client identifiers (since {@link UIData} is a
     * {@link NamingContainer}).</p>
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public String getClientId(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (baseClientId == null) {
            baseClientId = super.getClientId(context);
        }
        if (rowIndex >= 0) {
            return (baseClientId + NamingContainer.SEPARATOR_CHAR + rowIndex);
        } else {
            return (baseClientId);
        }

    }


    /**
     * <p>Override the default {@link UIComponentBase#queueEvent} processing
     * to wrap any queued events in a wrapper so that we can reset the current
     * row index in <code>broadcast()</code>.</p>
     *
     * @param event {@link FacesEvent} to be queued
     *
     * @exception IllegalStateException if this component is not a
     *  descendant of a {@link UIViewRoot}
     * @exception NullPointerException if <code>event</code>
     *  is <code>null</code>
     */
    public void queueEvent(FacesEvent event) {

        super.queueEvent(new WrapperEvent(this, event, getRowIndex()));

    }


    /**
     * <p>Override the default {@link UIComponentBase#broadcast} processing
     * to unwrap any wrapped {@link FacesEvent} and reset the current row index,
     * before the event is actually broadcast.  For events that we did not wrap
     * (in <code>queueEvent()</code>), default processing will occur.</p>
     *
     * @param event The {@link FacesEvent} to be broadcast
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @exception IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @exception NullPointerException if <code>event</code> is
     * <code>null</code>
     */
    public void broadcast(FacesEvent event)
	throws AbortProcessingException {

	if (!(event instanceof WrapperEvent)) {
	    super.broadcast(event);
	    return;
	}

	// Set up the correct context and fire our wrapped event
	WrapperEvent revent = (WrapperEvent) event;
        int oldRowIndex = getRowIndex();
	setRowIndex(revent.getRowIndex());
	FacesEvent rowEvent = revent.getFacesEvent();
	rowEvent.getComponent().broadcast(rowEvent);
        setRowIndex(oldRowIndex);
	return;

    }


    /**
     * <p>Override the default {@link UIComponentBase#processDecodes}
     * processing to perform the following steps.</p>
     * <ul>
     * <li>If the <code>rendered</code> property of this {@link UIComponent}
     *     is <code>false</code>, skip further processing.</li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * <li>Call the <code>processDecodes()</code> method of all facets
     *     of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacets().keySet().iterator()</code>.</li>
     * <li>Iterate over the set of rows that were included when this
     *     component was rendered (i.e. those defined by the <code>first</code>
     *     and <code>rows</code> properties), performing the following
     *     processing for each row:
     *     <ul>
     *     <li>Set the current <code>rowIndex</code> to the appropriate
     *         value for this row.</li>
     *     <li>If <code>isRowAvailable()</code> returns <code>true</code>,
     *         iterate over the child components of this component,
     *         calling the <code>processDecodes()</code> method for each
     *         child that is a {@link UIColumn}.</li>
     *     </ul></li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * <li>Call the <code>decode()</code> method of this component.</li>
     * <li>If a <code>RuntimeException</code> is thrown during
     *     decode processing, call {@link FacesContext#renderResponse}
     *     and re-throw the exception.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */ 
    public void processDecodes(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}
        if (!isRendered()) {
            return;
        }
	iterate(context, PhaseId.APPLY_REQUEST_VALUES);
	decode(context);

    }


    /**
     * <p>Override the default {@link UIComponentBase#processValidators}
     * processing to perform the following steps.</p>
     * <ul>
     * <li>If the <code>rendered</code> property of this {@link UIComponent}
     *     is <code>false</code>, skip further processing.</li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * <li>Call the <code>processValidators()</code> method of all facets
     *     of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacets().keySet().iterator()</code>.</li>
     * <li>Iterate over the set of rows that were included when this
     *     component was rendered (i.e. those defined by the <code>first</code>
     *     and <code>rows</code> properties), performing the following
     *     processing for each row:
     *     <ul>
     *     <li>Set the current <code>rowIndex</code> to the appropriate
     *         value for this row.</li>
     *     <li>If <code>isRowAvailable()</code> returns <code>true</code>,
     *         iterate over the child components of this component,
     *         calling the <code>processValidators()</code> method for each
     *         child that is a {@link UIColumn}.</li>
     *     </ul></li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */ 
    public void processValidators(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}
        if (!isRendered()) {
            return;
        }
	iterate(context, PhaseId.PROCESS_VALIDATIONS);
        // This is not a UIInput, so no further processing is required

    }


    /**
     * <p>Override the default {@link UIComponentBase#processUpdates}
     * processing to perform the following steps.</p>
     * <ul>
     * <li>If the <code>rendered</code> property of this {@link UIComponent}
     *     is <code>false</code>, skip further processing.</li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * <li>Call the <code>processUpdates()</code> method of all facets
     *     of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacets().keySet().iterator()</code>.</li>
     * <li>Iterate over the set of rows that were included when this
     *     component was rendered (i.e. those defined by the <code>first</code>
     *     and <code>rows</code> properties), performing the following
     *     processing for each row:
     *     <ul>
     *     <li>Set the current <code>rowIndex</code> to the appropriate
     *         value for this row.</li>
     *     <li>If <code>isRowAvailable()</code> returns <code>true</code>,
     *         iterate over the child components of this component,
     *         calling the <code>processUpdates()</code> method for each
     *         child that is a {@link UIColumn}.</li>
     *     </ul></li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */ 
    public void processUpdates(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}
        if (!isRendered()) {
            return;
        }
	iterate(context, PhaseId.UPDATE_MODEL_VALUES);
        // This is not a UIInput, so no further processing is required

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the {@link DataModel} object representing the data objects
     * that we will iterate over in this component's rendering.</p>
     */
    private DataModel getDataModel() {

        // Return any previously cached DataModel instance
        if (this.model != null) {
            return (model);
        }

        // Synthesize a DataModel around our current value if possible
        Object current = getValue();
        if (current == null) {
            this.model = new ListDataModel(Collections.EMPTY_LIST);
        } else if (current instanceof DataModel) {
            this.model = (DataModel) current;
        } else if (current instanceof List) {
            this.model = new ListDataModel((List) current);
        } else if (isObjectArray(current)) {
            this.model = new ArrayDataModel((Object[]) current);
        } else if (current instanceof ResultSet) {
            this.model = new ResultSetDataModel((ResultSet) current);
        } else {
            this.model = new ScalarDataModel(current);
        }
	return (model);

    }


    /**
     * <p>Return <code>true</code> if the specified object is an array
     * of Objects.</p>
     *
     * @param current Object to be inspected
     */
    private boolean isObjectArray(Object current) {

        Object array[] = new Object[0];
        if (array.getClass().isAssignableFrom(current.getClass())) {
            return (true);
        } else {
            return (false);
        }

    }


    /**
     * <p>Iterate over our facets and children as appropriate.  In the current
     * design, facets are only used for per-column headers and footers, so they
     * do not need to be iterated over.  Therefore, we will deal with the
     * facets once, and iterate only over the children.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId {@link PhaseId} of the phase we are currently running     
     */
    private void iterate(FacesContext context, PhaseId phaseId) {

	// Process each facet exactly once
	setRowIndex(-1);
	Iterator facets = getFacets().keySet().iterator();
	while (facets.hasNext()) {
	    UIComponent facet = (UIComponent)
                getFacets().get(facets.next());
	    if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
		facet.processDecodes(context);
	    } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
		facet.processValidators(context);
	    } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
		facet.processUpdates(context);
	    } else {
		throw new IllegalArgumentException();
	    }
	}

	// Iterate over our children, once per row
	int processed = 0;
        int rowIndex = getFirst() - 1;
	int rows = getRows();

        while (true) {

	    // Have we processed the requested number of rows?
	    if ((rows > 0) && (++processed > rows)) {
		break;
	    }

	    // Expose the current row in the specified request attribute
	    setRowIndex(++rowIndex);
	    if (!isRowAvailable()) {
                break; // Scrolled past the last row
            }

	    // Perform phase-specific processing as required
	    Iterator kids = getChildren().iterator();
	    while (kids.hasNext()) {
		UIComponent kid = (UIComponent) kids.next();
		if (!(kid instanceof UIColumn)) {
		    continue;
		}
		if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
		    kid.processDecodes(context);
		} else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
		    kid.processValidators(context);
		} else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
		    kid.processUpdates(context);
		} else {
		    throw new IllegalArgumentException();
		}
	    }

	}

	// Clean up after ourselves
	setRowIndex(-1);

    }


    /**
     * <p>Restore state information for all descendant components, as described
     * for <code>setRowIndex()</code>.</p>
     */
    private void restoreDescendantState() {

        FacesContext context = getFacesContext();
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UIColumn) {
                restoreDescendantState(kid, context);
            }
        }

    }


    /**
     * <p>Restore state information for the specified component and its
     * descendants.</p>
     *
     * @param component Component for which to restore state information
     * @param context {@link FacesContext} for the current request
     */
    private void restoreDescendantState(UIComponent component,
                                        FacesContext context) {

        // Reset the client identifier for this component
        String id = component.getId();
        component.setId(id); // Forces client id to be reset

        // Restore state for this component
        if (component instanceof ValueHolder) {
            String clientId = component.getClientId(context);
            SavedState state = (SavedState) saved.get(clientId);
            if (state == null) {
                state = new SavedState();
            }
            ((ValueHolder) component).setValue(state.getValue());
            // System.err.println("restoreDescendantState(" + clientId + "," + state.getValue());
            if (component instanceof ConvertibleValueHolder) {
                ((ConvertibleValueHolder) component).setValid(state.isValid());
            }
            if (component instanceof UIInput) {
                ((UIInput) component).setPrevious(state.getPrevious());
            }
        }

        // Restore state for children of this component
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            restoreDescendantState((UIComponent) kids.next(), context);
        }


    }


    /**
     * <p>Save state information for all descendant components, as described
     * for <code>setRowIndex()</code>.</p>
     */
    private void saveDescendantState() {

        FacesContext context = getFacesContext();
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UIColumn) {
                saveDescendantState(kid, context);
            }
        }

    }


    /**
     * <p>Save state information for the specified component and its
     * descendants.</p>
     *
     * @param component Component for which to save state information
     * @param context {@link FacesContext} for the current request
     */
    private void saveDescendantState(UIComponent component,
                                     FacesContext context) {

        // Save state for this component
        if (component instanceof ValueHolder) {
            String clientId = component.getClientId(context);
            SavedState state = (SavedState) saved.get(clientId);
            if (state == null) {
                state = new SavedState();
                saved.put(clientId, state);
            }
            // System.err.println("saveDescendantState(" + clientId + "," + ((ValueHolder) component).getLocalValue());
            state.setValue(((ValueHolder) component).getLocalValue());
            if (component instanceof ConvertibleValueHolder) {
                state.setValid(((ConvertibleValueHolder) component).isValid());
            }
            if (component instanceof UIInput) {
                state.setPrevious(((UIInput) component).getPrevious());
            }
        }

        // Save state for children of this component
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            saveDescendantState((UIComponent) kids.next(), context);
        }

    }


}


// ------------------------------------------------------------- Private Classes


// Private class to represent saved state information
class SavedState implements Serializable {

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


// Private class to wrap an event with a row index
class WrapperEvent extends FacesEvent {


    public WrapperEvent(UIComponent component, FacesEvent event, int rowIndex) {
        super(component);
        this.event = event;
        this.rowIndex = rowIndex;
    }

    private FacesEvent event = null;
    private int rowIndex = -1;

    public FacesEvent getFacesEvent() {
        return (this.event);
    }

    public int getRowIndex() {
        return (this.rowIndex);
    }

    public PhaseId getPhaseId() {
	return (this.event.getPhaseId());
    }

    public void setPhaseId(PhaseId phaseId) {
	this.event.setPhaseId(phaseId);
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return (false);
    }

    public void processListener(FacesListener listener) {
        throw new IllegalStateException();
    }


}
