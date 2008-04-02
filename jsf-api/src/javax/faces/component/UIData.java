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


import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.RepeaterEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;


/**
 * <p><strong>UIData</strong> is a {@link UIComponent} that supports
 * data binding to a collection of data objects represented by a
 * {@link DataModel} instance, which is the current value of this component
 * itself (typically established via the <code>valueRef</code> property).
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
    implements NamingContainer, Repeater, ValueHolder {


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
    private int rowIndex = 0;


    /**
     * <p>The number of rows to display, or zero for all remaining
     * rows in the table.</p>
     */
    private int rows = 0;


    /**
     * <p>This map contains <code>SavedState</code> instances for each
     * descendant component, keyed by the client identifier of the
     * descendant.  Because descendant client identifiers will contain
     * the <code>rowIndex</code> value of the parent, per-row state
     * information is actually preserved.</p>
     */
    private Map saved = null;


    /**
     * <p>The local value of this {@link UIComponent}.</p>
     */
    private Object value = null;


    /**
     * <p>The value reference expression pointing at the associated
     * model data for this {@link UIComponent}.</p>
     */
    private String valueRef = null;


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

        return (this.first);

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

    }


    /**
     * <p>Return <code>true</code> to indicate that this component takes
     * responsibility for rendering its children.</p>
     */
    public boolean getRendersChildren() {

        return (true);

    }


    /**
     * <p>Return the number of rows in the underlying data model.</p>
     */
    public int getRowCount() {

        return (getDataModel(FacesContext.getCurrentInstance()).getRowCount());

    }


    /**
     * <p>Return the data object representing the data for the currently
     * selected row index.</p>
     */
    public Object getRowData() {

        return (getDataModel(FacesContext.getCurrentInstance()).getRowData());

    }


    public int getRowIndex() {

        return (this.getRowIndex());

    }


    /**
     * <p>Set the zero relative index of the current row, or -1 to indicate that
     * no row is currently selected, by implementing the following algorithm.
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
     *     <code>ConvertableValueHolder</code>, save the state of the
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
     *     <code>ConvertableValueHolder</code>, restore the state of the
     *     <code>valid</code> property.</li>
     * <li>If the descendant is also an instance of <code>UIInput</code>,
     *     restore the state of the <code>previous</code> property.</li>
     * </ul>
     *
     * @param rowIndex The new row index value, or -1 for no associated row
     *
     * @exception IllegalArgumentException if <code>rowIndex</code>
     *  is less than -1
     */
    public void setRowIndex(int rowIndex) {

        // Save current state for the previous row index
        saveDescendantState();

        // Update to the new row index
        this.rowIndex = rowIndex;
        getDataModel(FacesContext.getCurrentInstance()).setRowIndex(rowIndex);

        // Clear or expose the current row data as a request scope attribute
        if (var != null) {
            Map requestMap =
                FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap();
            if (rowIndex == -1) {
                requestMap.remove(var);
            } else {
                requestMap.put(var, getRowData());
            }
        }

        // Reset current state information for the new row index
        restoreDescendantState();

    }


    /**
     * <p>Return the number of rows to be displayed, or zero for all
     * remaining rows in the table.</p>
     */
    public int getRows() {

        return (this.rows);

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

    }


    /**
     * <p>Return the request-scope attribute under which the data object
     * for the current row will be exposed when iterating.</p>
     */
    public String getVar() {

        return (this.var);

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

        Object values[] = new Object[8];
        values[0] = super.saveState(context);
        values[1] = new Integer(first);
        values[2] = new Integer(rowIndex);
        values[3] = new Integer(rows);
        values[4] = saved;
        values[5] = value;
        values[6] = valueRef;
        values[7] = var;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        first = ((Integer) values[1]).intValue();
        rowIndex = ((Integer) values[2]).intValue();
        rows = ((Integer) values[3]).intValue();
        saved = (Map) values[4];
        value = values[5];
        valueRef = (String) values[6];
        var = (String) values[7];

    }


    // -------------------------------------------------- ValueHolder Properties


    public Object getValue() {

        return (this.value);

    }


    public void setValue(Object value) {

        this.value = value;

    }


    public String getValueRef() {

        return (this.valueRef);

    }


    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

    }


    // ----------------------------------------------------- ValueHolder Methods


    /**
     * @exception EvaluationException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}  
     */
    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String valueRef = getValueRef();
        if (valueRef != null) {
            Application application = context.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            return (binding.getValue(context));
        }
        return (null);

    }


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>Return a client identifier for this component that includes the
     * current value of the <code>rowIndex</code> property.  This implies
     * that multiple calls to <code>getClientId()</code> may return different
     * results, but ensures that child components can themselves will generate
     * row-specific client identifiers (since {@link UIData} is a
     * {@link NamingContainer}).</p>
     *
     * @exception NullPointerExcepton if <code>context</code>
     *  is <code>null</code>
     */
    public String getCientId(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return (super.getClientId(context) +
                NamingContainer.SEPARATOR_CHAR +
                rowIndex);

    }


    /**
     * <p>Override the default {@link UIComponentBase#queueEvent} processing
     * to wrap any queued events in a {@link RepeaterEvent} so that the current
     * row index can be restored.
     *
     * @param event {@link FacesEvent} to be queued
     *
     * @exception IllegalStateException if this component is not a
     *  descendant of a {@link UIViewRoot}
     * @exception NullPointerException if <code>event</code>
     *  is <code>null</code>
     */
    public void queueEvent(FacesEvent event) {

        super.queueEvent(new RepeaterEvent(this, event, getRowIndex()));

    }


    /**
     * <p>Override the default {@link UIComponentBase#broadcast} processing
     * to unwrap any {@link RepeaterEvent} and reset the current row index,
     * and optionally expose the data object for the current row as a
     * request attribute under the key specified by the <code>var</code>
     * property (if any), before the event is actually broadcast.  The default
     * processing will be performed on non-{@link RepeaterEvent} events sent
     * from this component.</p>
     *
     * @param event The {@link FacesEvent} to be broadcast
     * @param phaseId The {@link PhaseId} of the current phase of the
     *  request processing lifecycle
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @exception IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @exception IllegalStateException if PhaseId.ANY_PHASE is passed
     *  for the phase identifier
     * @exception NullPointerException if <code>event</code> or
     *  <code>phaseId</code> is <code>null</code>
     */
    public boolean broadcast(FacesEvent event, PhaseId phaseId)
	throws AbortProcessingException {

	if (!(event instanceof RepeaterEvent)) {
	    return (super.broadcast(event, phaseId));
	}

	// Set up the correct context and fire our wrapped event
	RepeaterEvent revent = (RepeaterEvent) event;
	Map requestMap =
	    FacesContext.getCurrentInstance().getExternalContext().
            getRequestMap();
	String var = getVar();
	Object old = null;
	if (var != null) {
	    old = requestMap.get(var);
	}
	setRowIndex(revent.getRowIndex());
	if (var != null) {
	    requestMap.put(var, getRowData());
	}
	FacesEvent rowEvent = revent.getFacesEvent();
	boolean returnValue =
            rowEvent.getComponent().broadcast(rowEvent, phaseId);
	if (var != null) {
	    if (old != null) {
		requestMap.put(var, old);
	    } else {
		requestMap.remove(var);
	    }
	}
	return (returnValue);

    }

    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void processDecodes(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}
	iterate(context, PhaseId.APPLY_REQUEST_VALUES);
	decode(context);

    }

    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void processValidators(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}

	// Process all facets and children of this component
	iterate(context, PhaseId.PROCESS_VALIDATIONS);

        // This is not a UIInput, so no further processing is required

    }

    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void processUpdates(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}

	// Process all facets and children of this component
	iterate(context, PhaseId.UPDATE_MODEL_VALUES);

        // This is not a UIInput, so no further processing is required

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the {@link DataModel} object representing the data objects
     * that we will iterate over in this component's rendering.</p>
     *
     * @param context {@link FacesContext} for the current request
     */
    private DataModel getDataModel(FacesContext context) {

        // Return any previously cached DataModel instance
        if (this.model != null) {
            return (model);
        }

        // Synthesize a DataModel around our current value if possible
        Object current = currentValue(context);
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
	int first = getFirst();                  // Zero relative
	int rowCount = getRowCount();
	int processed = 0;
	int rows = getRows();
        for (int rowIndex = first; rowIndex < rowCount; rowIndex++) {

	    // Have we processed the requested number of rows?
	    if ((rows > 0) && (++processed > rows)) {
		break;
	    }

	    // Expose the current row in the specified request attribute
	    setRowIndex(rowIndex);

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

        System.err.println("restoreDescendantState(" + rowIndex + ")");
        FacesContext context = FacesContext.getCurrentInstance();
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UIColumn) {
                restoreDescendantState(kid, context);
            }
        }
        System.err.println("retoreDescendantState(COMPLETED)");

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

        // Restore state for this component
        if (component instanceof ValueHolder) {
            String clientId = component.getClientId(context);
            System.err.println("  clientId=" + clientId);
            SavedState state = (SavedState) saved.get(clientId);
            if (state == null) {
                return;
            }
            System.err.println("     value=" + state.getValue());
            ((ValueHolder) component).setValue(state.getValue());
            if (component instanceof ConvertableValueHolder) {
                System.err.println("     valid=" + state.isValid());
                ((ConvertableValueHolder) component).setValid(state.isValid());
            }
            if (component instanceof UIInput) {
                System.err.println("      prev=" + state.getPrevious());
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

        System.err.println("saveDescendantState(" + rowIndex + ")");
        FacesContext context = FacesContext.getCurrentInstance();
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UIColumn) {
                saveDescendantState(kid, context);
            }
        }
        System.err.println("saveDescendantState(COMPLETED)");

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
            System.err.println("  clientId=" + clientId);
            System.err.println("     value=" + (((ValueHolder) component).getValue()));
            state.setValue(((ValueHolder) component).getValue());
            if (component instanceof ConvertableValueHolder) {
                System.err.println("     valid=" + (((ConvertableValueHolder) component).isValid()));
                state.setValid(((ConvertableValueHolder) component).isValid());
            }
            if (component instanceof UIInput) {
                System.err.println("      prev=" + (((UIInput) component).getPrevious()));
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


// Private Class To Represent Saved Information
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
