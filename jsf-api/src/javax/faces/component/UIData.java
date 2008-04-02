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
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
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
    implements Repeater, ValueHolder {


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
     * <p>The helper instance to assist us in implementing {@link Repeater}.</p>
     */
    private RepeaterSupport repeater = new RepeaterSupport();


    /**
     * <p>The number of rows to display, or zero for all remaining
     * rows in the table.</p>
     */
    private int rows = 0;


    /**
     * <p>The {@link ValueHolderSupport} instance to which we delegate
     * our {@link ValueHolder} implementation processing.</p>
     */
    private ValueHolderSupport support = new ValueHolderSupport(this);



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


    public Object getValue() {

        return (support.getValue());

    }


    public void setValue(Object value) {

        support.setValue(value);

    }


    public String getValueRef() {

        return (support.getValueRef());

    }


    public void setValueRef(String valueRef) {

        support.setValueRef(valueRef);

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


    // -------------------------------------------------------- Repeater Methods

    /**
     * @exception NullPointerException {@inheritDoc}  
     */ 
    public String getChildClientId(FacesContext context,
				   String childClientId) {

	return (repeater.getChildClientId(context, this, childClientId));

    }

    /**
     * @exception NullPointerException {@inheritDoc}
     */ 
    public Object getChildPrevious(UIComponent component) {

	return (repeater.getChildPrevious(component));

    }

    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void setChildPrevious(UIComponent component, Object value) {

	repeater.setChildPrevious(component, value);

    }


    public boolean isChildValid(UIComponent component) {

	return (repeater.isChildValid(component));

    }


    public void setChildValid(UIComponent component, boolean valid) {

	repeater.setChildValid(component, valid);

    }

    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public Object getChildValue(UIComponent component) {

	return (repeater.getChildValue(component));

    }

    /**
     * @exception NullPointerException {@inheritDoc}    
     */ 
    public void setChildValue(UIComponent component, Object value) {

	repeater.setChildValue(component, value);

    }


    public int getRowIndex() {

	return (repeater.getRowIndex());

    }


    /**
     * <p>In addition to the responsibilities described for this method for a
     * {@link Repeater}, expose the data object for the specified row
     * (if it is nonzero), if there is a value for the <code>var</code>
     * property.</p>
     *
     * @param index The new index value
     *
     * @exception IllegalArgumentException if <code>index</code>
     *  is less than -1
     */
    public void setRowIndex(int index) {

	repeater.setRowIndex(index);
        getDataModel(FacesContext.getCurrentInstance()).setRowIndex(index);
        if (var != null) {
            Map requestMap =
                FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap();
            if (index == -1) {
                requestMap.remove(var);
            } else {
                requestMap.put(var, getRowData());
            }
        }

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, support);
        values[2] = new Integer(first);
        values[3] = saveAttachedState(context, repeater);
        values[4] = new Integer(rows);
        values[5] = var;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        support = (ValueHolderSupport) restoreAttachedState(context, values[1]);
	support.setComponent(this);
        first = ((Integer) values[2]).intValue();
        repeater = (RepeaterSupport) restoreAttachedState(context, values[3]);
        rows = ((Integer) values[4]).intValue();
        var = (String) values[5];

    }


    // ----------------------------------------------------- ValueHolder Methods

    /**
     * @exception EvaluationException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public Object currentValue(FacesContext context) {

        return (support.currentValue(context));

    }


    // ----------------------------------------------------- UIComponent Methods


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


}
