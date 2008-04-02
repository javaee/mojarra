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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.component.base.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.RepeaterEvent;
import javax.faces.event.ValueChangedEvent;
import javax.faces.model.DataModel;
import javax.faces.validator.Validator;


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
     * <p>The first row number (one-relative) to be displayed, or zero to
     * start from the very beginning.</p>
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
     * <p>The request scope attribute under which the data object for the
     * current row will be exposed when iterating.</p>
     */
    private String var = null;



    // -------------------------------------------------------------- Properties


    /**
     * <p>The converter {@link Converter} (if any)
     * that is registered for this {@link UIComponent}.</p>
     */
    private Converter converter = null;


    public Converter getConverter() {

        return (this.converter);

    }


    public void setConverter(Converter converter) {

        this.converter = converter;

    }


    /**
     * <p>Return the one-relative row number of the first row to be
     * displayed, or zero for starting at the beginning of the data.</p>
     */
    public int getFirst() {

        return (this.first);

    }


    /**
     * <p>Set the one-relative row number of the first row to be
     * displayed, or zero for starting at the beginning of the data.</p>
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


    /**
     * <p>The local value of this {@link UIComponent} (if any).</p>
     */
    private Object value = null;


    public Object getValue() {

        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            return (repeater.getChildValue(this));
        } else {
            return (this.value);
        }

    }


    public void setValue(Object value) {

        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            repeater.setChildValue(this, value);
        } else {
            this.value = value;
        }

    }


    /**
     * <p>The value reference expression for this {@link UIOutput} component
     * (if any).</p>
     */
    private String valueRef = null;


    public String getValueRef() {

        return (this.valueRef);

    }


    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

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


    // ---------------------------------------------------------- Public Methods


    // PENDING(craigmcc) - erase() needs to be exposed publicly because there
    // is no other way to bypass an unwanted execution of Update Model Values
    // when (for example) a row-specific ActionEvent is processed.  Once we have
    // a way to bypass Update Model Values in a controlled way, the need for
    // this method to be public can be re-evaluated
    /**
     * <p>Erase the previous and local values of all child input components
     * that are of type {@link UIInput} for this {@link UIData} component.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void erase(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}

	repeater.erase();

    }


    // -------------------------------------------------------- Repeater Methods


    public String getChildClientId(FacesContext context,
				   String childClientId) {

	return (repeater.getChildClientId(context, this, childClientId));

    }


    public Object getChildPrevious(UIComponent component) {

	return (repeater.getChildPrevious(component));

    }


    public void setChildPrevious(UIComponent component, Object value) {

	repeater.setChildPrevious(component, value);

    }


    public boolean isChildValid(UIComponent component) {

	return (repeater.isChildValid(component));

    }


    public void setChildValid(UIComponent component, boolean valid) {

	repeater.setChildValid(component, valid);

    }


    public Object getChildValue(UIComponent component) {

	return (repeater.getChildValue(component));

    }


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
     *  is <code>negative</code>
     */
    public void setRowIndex(int index) {

	repeater.setRowIndex(index);
        getDataModel(FacesContext.getCurrentInstance()).setRowIndex(index);
        if (var != null) {
            Map requestMap =
                FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap();
            if (index == 0) {
                requestMap.remove(var);
            } else {
                requestMap.put(var, getRowData());
            }
        }

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[8];
        values[0] = super.saveState(context);
        List[] converterList = new List[1];
        List theConverter = new ArrayList(1);
        theConverter.add(converter);
        converterList[0] = theConverter;
        values[1] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, "converter", converterList);
        values[2] = new Integer(first);
        List[] repeaterList = new List[1];
        List theRepeater = new ArrayList(1);
        theRepeater.add(repeater);
        repeaterList[0] = theRepeater;
        values[3] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, "repeater", repeaterList);
        values[4] = new Integer(rows);
        values[5] = value;
        values[6] = valueRef;
        values[7] = var;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        List[] converterList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[1]);
	if (converterList != null) {
            List theConverter = converterList[0];
            if ((theConverter != null) && (theConverter.size() > 0)) {
                converter = (Converter) theConverter.get(0);
            }
	}
        first = ((Integer) values[2]).intValue();
        List[] repeaterList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[3]);
	if (repeaterList != null) {
            List theRepeater = repeaterList[0];
            if ((theRepeater != null) && (theRepeater.size() > 0)) {
                repeater = (RepeaterSupport) theRepeater.get(0);
            }
	}
        rows = ((Integer) values[4]).intValue();
        value = values[5];
        valueRef = (String) values[6];
        var = (String) values[7];

    }


    // ----------------------------------------------------- ValueHolder Methods


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


    public boolean broadcast(FacesEvent event, PhaseId phaseId)
	throws AbortProcessingException {

	if (!(event instanceof RepeaterEvent)) {
	    return (super.broadcast(event, phaseId));
	}

	// Set up the correct context and fire our wrapped event
	RepeaterEvent revent = (RepeaterEvent) event;
	Map requestMap =
	    FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
	String var = getVar();
	Object old = null;
	if (var != null) {
	    old = requestMap.get(var);
	}
	setRowIndex(revent.getRowIndex());
	Object row = getRowData();
	if (var != null) {
	    requestMap.put(var, row);
	}
	FacesEvent rowEvent = revent.getFacesEvent();
	boolean returnValue = rowEvent.getComponent().broadcast
	    (rowEvent, phaseId);
	if (var != null) {
	    if (old != null) {
		requestMap.put(var, old);
	    } else {
		requestMap.remove(var);
	    }
	}
	return (returnValue);

    }


    public void processDecodes(FacesContext context) throws IOException {

	if (context == null) {
	    throw new NullPointerException();
	}
	iterate(context, PhaseId.APPLY_REQUEST_VALUES);
	decode(context);

    }


    public void processValidators(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}

	// Process all facets and children of this component
	try {
	    iterate(context, PhaseId.PROCESS_VALIDATIONS);
	} catch (IOException e) {
	    ; // Cannot happen
	}

        // This is not a UIInput, so no further processing is required

    }


    public void processUpdates(FacesContext context) {

	if (context == null) {
	    throw new NullPointerException();
	}

	// Process all facets and children of this component
	try {
	    iterate(context, PhaseId.UPDATE_MODEL_VALUES);
	} catch (IOException e) {
	    ; // Cannot happen
	}

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
        } else {
            // PENDING(craigmcc) - Support ResultSet/RowSet?
        }

        // Open the model if it is not already open
	if ((model != null) && !model.isOpen()) {
	    // PENDING(craigmcc) - So who closes it when all is said and done?
	    model.open();
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
     *
     * @exception IOException if an input/output error occurs
     */
    private void iterate(FacesContext context, PhaseId phaseId)
	throws IOException {

	// Process each facet exactly once
	setRowIndex(0);
	Iterator facets = getFacets().keySet().iterator();
	while (facets.hasNext()) {
	    UIComponent facet = (UIComponent)
                getFacets().get((String) facets.next());
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
	int first = getFirst();                  // One relative
	if (first < 1) {
	    first = 1;
	}
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
	setRowIndex(0);

    }


}
