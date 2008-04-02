/*
 * $Id: DataModel.java,v 1.2 2003/09/11 15:26:11 craigmcc Exp $
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


import javax.faces.FacesException;
import javax.faces.component.Repeater;


/**
 * <p><strong>DataModel</strong> is an abstraction around arbitrary data
 * binding technologies that can be used to adapt a variety of data sources
 * for use by JavaServer Faces components that implement {@link Repeater}.</p>
 *
 * <p>The data collection underlying a {@link DataModel} instance is
 * modeled as a collection of row objects that can be accessed by
 * a one-relative cursor (row index).  The APIs provide mechanisms to
 * position to a specified one-relative row index, and to retrieve an
 * object that represents the data that corresponds to the current
 * row index.</p>
 *
 * <p>Event listeners may be registered to receive notifications
 * of when access to the underlying data is initiated or terminated, as well
 * as when a new row index is selected.</p>
 */

public interface DataModel {


    // ------------------------------------------------------- Lifecycle Methods


    /**
     * <p>Terminate access to the underlying data represented by this
     * {@link DataModel}.  Send a {@link DataModelEvent} to the
     * <code>modelClosed()</code> method of all registered
     * {@link DataModelListener}s.  Any further attempt
     * to navigate to new rows, or to access the current row data,
     * will throw <code>IllegalStateException</code>.</p>
     *
     * @exception FacesException if any error occurs
     * @exception IllegalStateException if this {@link DataModel} is not open
     */
    public void close();


    /**
     * <p>Initiate access to the underlying data represented by this
     * {@link DataModel}.  Set the current <code>rowIndex</code>
     * property to zero, indicating that no row is currently selected.
     * Send a {@link DataModelEvent} to the <code>modelOpened</code>
     * method of all registered {@link DataModelListener}s.  This method must
     * be called before row navigation, or access to the current row
     * data, can be performed.</p>
     *
     * @exception FacesException if any error occurs
     * @exception IllegalStateException if this {@link DataModel} is
     *  currently open
     */
    public void open() throws FacesException;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return <code>true</code> if this data model is currently open.</p>
     */
    public boolean isOpen();


    /**
     * <p>Return the number of rows of data objects represented by this
     * {@link DataModel}.</p>
     *
     * @exception IllegalStateException if this {@link DataModel} is not open
     */
    public int getRowCount();


    /**
     * <p>Return an object representing the data for the currenty selected
     * row index.  If row index is zero, <code>null</code> is returned.</p>
     *
     * @exception IllegalArgumentException if the current row index is zero or
     *  exceeds the number of available rows
     * @exception IllegalStateException if this {@link DataModel} is not open
     */
    public Object getRowData();


    /**
     * <p>Return the one-relative index of the currently selected row.
     * If we are positioned before the first row, zero will be returned.
     * If we are positioned after the last row, the value
     * <code>getRowCount() + 1</code> will be returned.</p>.
     *
     * @exception IllegalStateException if this {@link DataModel} is not open
     */
    public int getRowIndex();


    /**
     * <p>Set the one-relative index of the currently selected row.  Setting
     * the index to zero indicates that no row is currently selected.  If
     * the current index is changed by this method, send a
     * {@link DataModelEvent} to the <code>modelSelected()</code> method of each
     * registered {@link DataModelListener}.</p>
     *
     * @param rowIndex The new one-relative index, or zero to select no row
     *
     * @exception IllegalArgumentException if <code>rowIndex</code>
     *  is negative
     * @exception IllegalStateException if this {@link DataModel} is not open
     * @exception FacesException if any error occurs
     */
    public void setRowIndex(int rowIndex);


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
    public void addDataModelListener(DataModelListener listener);


    /**
     * <p>Remove an existing {@link DataModelListener} from the set
     * interested in notifications from this {@link DataModel}.</p>
     *
     * @param listener The old {@link DataModelListener} to be deregistered
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void removeDataModelListener(DataModelListener listener);


}
