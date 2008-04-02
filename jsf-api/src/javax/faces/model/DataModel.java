/*
 * $Id: DataModel.java,v 1.5 2003/10/16 00:42:23 craigmcc Exp $
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
import javax.faces.component.Repeater;


/**
 * <p><strong>DataModel</strong> is an abstraction around arbitrary data
 * binding technologies that can be used to adapt a variety of data sources
 * for use by JavaServer Faces components that implement {@link Repeater}.</p>
 *
 * <p>The data collection underlying a {@link DataModel} instance is
 * modeled as a collection of row objects that can be accessed by
 * a zero-relative cursor (row index).  The APIs provide mechanisms to
 * position to a specified zero-relative row index, and to retrieve an
 * object that represents the data that corresponds to the current
 * row index.</p>
 *
 * <p>Event listeners may be registered to receive notifications
 * of when a new row index is selected.</p>
 */

public abstract class DataModel {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the number of rows of data objects represented by this
     * {@link DataModel}.</p>
     *
     * @exception FacesException if an error occurs getting the row count
     */
    public abstract int getRowCount();


    /**
     * <p>Return an object representing the data for the currenty selected
     * row index.  If row index is -1, <code>null</code> is returned.</p>
     *
     * @exception FacesException if an error occurs getting the data
     */
    public abstract Object getRowData();


    /**
     * <p>Return the zero-relative index of the currently selected row.
     * If we are positioned before the first row, -1 will be returned.
     * When a {@link DataModel} instance is initialized, the
     * <code>rowIndex</code> will be zero if the underlying data has at least
     * one row, or -1 otherwise.</p>
     * </p>
     *
     * @exception FacesException if an error occurs getting the row index
     */
    public abstract int getRowIndex();


    /**
     * <p>Set the zero-relative index of the currently selected row.  Setting
     * the index to -1 indicates that no row is currently selected.  If
     * the current index is changed by this method, send a
     * {@link DataModelEvent} to the <code>rowSelected()</code> method of each
     * registered {@link DataModelListener}.</p>
     *
     * @param rowIndex The new zero-relative index, or -1 to select no row
     *
     * @exception FacesException if an error occurs setting the row index
     * @exception IllegalArgumentException if <code>rowIndex</code>
     *  is &lt; -1 or &gt;= the number of available rows
     */
    public abstract void setRowIndex(int rowIndex);


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The list of registered {@link DataModelListener}s for this
     * {@link DataModel}.  This variable will be <code>null</code> unless
     * there is at least one registered listener.</p>
     */
    protected List listeners = null;


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
