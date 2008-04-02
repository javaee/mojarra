/*
 * $Id: DataModel.java,v 1.11 2004/01/22 20:08:46 craigmcc Exp $
 */

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

package javax.faces.model;


import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.UIData;


/**
 * <p><strong>DataModel</strong> is an abstraction around arbitrary data
 * binding technologies that can be used to adapt a variety of data sources
 * for use by JavaServer Faces components that support per-row processing
 * for their child components (such as {@link UIData}.</p>
 *
 * <p>The data collection underlying a {@link DataModel} instance is
 * modeled as a collection of row objects that can be accessed by
 * a zero-relative cursor (row index).  The APIs provide mechanisms to
 * position to a specified zero-relative row index, and to retrieve an
 * object that represents the data that corresponds to the current
 * row index.</p>
 *
 * <p>A concrete {@link DataModel} instance is attached to a particular
 * collection of underlying data by calling the <code>setWrappedData()</code>
 * method.  It can be detached from that underlying data collection by
 * passing a <code>null</code> parameter to this method.</p>
 *
 * <p>Concrete {@link DataModel} implementations must provide a public
 * zero-arguments constructor that calls <code>setWrappedData(null)</code>.
 * A convenience constructor that takes a wrapped object of the appropriate
 * type (and passes it on via a call to <code>setWrappedData()</code>,
 * should also be provided.</p>
 *
 * <p>Event listeners may be registered to receive notifications
 * of when a new row index is selected.</p>
 */

public abstract class DataModel {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return a flag indicating whether there is <code>rowData</code>
     * available at the current <code>rowIndex</code>.  If no
     * <code>wrappedData</code> is available, return <code>false</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row availability
     */
    public abstract boolean isRowAvailable();


    /**
     * <p>Return the number of rows of data objects represented by this
     * {@link DataModel}.  If the number of rows is unknown, or no
     * <code>wrappedData</code> is available, return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row count
     */
    public abstract int getRowCount();


    /**
     * <p>Return an object representing the data for the currenty selected
     * row index.  If no <code>wrappedData</code> is available, return
     * <code>null</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row data
     * @exception IllegalArgumentException if now row data is available
     *  at the currently specified row index
     */
    public abstract Object getRowData();


    /**
     * <p>Return the zero-relative index of the currently selected row.  If
     * we are not currently positioned on a row, or no <code>wrappedData</code>
     * is available, return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row index
     */
    public abstract int getRowIndex();


    /**
     * <p>Set the zero-relative index of the currently selected row, or -1
     * to indicate that we are not positioned on a row.  It is
     * possible to set the row index at a value for which the underlying data
     * collection does not contain any row data.  Therefore, callers may
     * use the <code>isRowAvailable()</code> method to detect whether row data
     * will be available for use by the <code>getRowData()</code> method.</p>
     *
     * <p>If there is no <code>wrappedData</code> available when this method
     * is called, the specified <code>rowIndex</code> is stored (and may be
     * retrieved by a subsequent call to <code>getRowData()</code>), but no
     * event is sent.  Otherwise, if the currently selected row index is
     * changed by this call, a {@link DataModelEvent} will be sent to the
     * <code>rowSelected()</code> method of all registered
     * {@link DataModelListener}s.</p>
     *
     * @param rowIndex The new zero-relative index (must be non-negative)
     *
     * @exception FacesException if an error occurs setting the row index
     * @exception IllegalArgumentException if <code>rowIndex</code>
     *  is less than -1
     */
    public abstract void setRowIndex(int rowIndex);


    /**
     * <p>Return the object representing the data wrapped by this
     * {@link DataModel}, if any.</p>
     */
    public abstract Object getWrappedData();


    /**
     * <p>Set the object representing the data collection wrapped by this
     * {@link DataModel}.  If the specified <code>data</code> is
     * <code>null</code>, detach this {@link DataModel} from any previously
     * wrapped data collection instead.</p>
     *
     * <p>If <code>data</code> is non-<code>null</code>, the currently selected
     * row index must be set to zero, and a {@link DataModelEvent} must be sent
     * to the <code>rowSelected()</code> method of all registered
     * {@link DataModelListener}s indicating that this row is now selected.</p>
     *
     * @param data Data collection to be wrapped, or <code>null</code> to
     *  detach from any previous data collection
     *
     * @exception ClassCastException if <code>data</code> is not of the
     *  appropriate type for this {@link DataModel} implementation
     */
    public abstract void setWrappedData(Object data);



    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The list of registered {@link DataModelListener}s for this
     * {@link DataModel}.  This variable will be <code>null</code> unless
     * there is at least one registered listener.</p>
     */
    private List listeners = null;


    // --------------------------------------------- Event Listener Registration


    /**
     * <p>Add a new {@link DataModelListener} to the set interested in
     * notifications from this {@link DataModel}.</p>
     *
     * @param listener The new {@link DataModelListener} to be registered
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void addDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);

    }


    /**
     * <p>Return the set of {@link DataModelListener}s interested in
     * notifications from this {@link DataModel}.  If there are no such
     * listeners, an empty array is returned.</p>
     */
    public DataModelListener[] getDataModelListeners() {

	if (listeners == null) {
	    return (new DataModelListener[0]);
	} else {
	    return ((DataModelListener[]) listeners.toArray
		    (new DataModelListener[listeners.size()]));
	}

    }


    /**
     * <p>Remove an existing {@link DataModelListener} from the set
     * interested in notifications from this {@link DataModel}.</p>
     *
     * @param listener The old {@link DataModelListener} to be deregistered
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void removeDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.size() == 0) {
                listeners = null;
            }
        }

    }


}
