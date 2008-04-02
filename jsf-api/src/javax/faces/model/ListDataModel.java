/*
 * $Id: ListDataModel.java,v 1.13 2004/01/26 06:49:40 craigmcc Exp $
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


import java.util.List;
import javax.faces.FacesException;


/**
 * <p><strong>ListDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps an <code>List</code> of Java objects.</p>
 */

public class ListDataModel extends DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ListDataModel} with no specified
     * wrapped data.</p>
     */
    public ListDataModel() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ListDataModel} wrapping the specified
     * list.</p>
     *
     * @param list List to be wrapped (if any)
     */
    public ListDataModel(List list) {

        super();
        setWrappedData(list);

    }


    // ------------------------------------------------------ Instance Variables


    // The current row index (zero relative)
    private int index = -1;


    // The list we are wrapping
    private List list;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return <code>true</code> if there is <code>wrappedData</code>
     * available, and the current value of <code>rowIndex</code> is greater
     * than or equal to zero, and less than the size of the list.  Otherwise,
     * return <code>false</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row availability
     */
    public boolean isRowAvailable() {

        if (list == null) {
	    return (false);
        } else if ((index >= 0) && (index < list.size())) {
            return (true);
        } else {
            return (false);
        }

    }


    /**
     * <p>If there is <code>wrappedData</code> available, return the
     * length of the list.  If no <code>wrappedData</code> is available,
     * return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row count
     */
    public int getRowCount() {

        if (list == null) {
	    return (-1);
        }
        return (list.size());

    }


    /**
     * <p>If row data is available, return the array element at the index
     * specified by <code>rowIndex</code>.  If no wrapped data is available,
     * return <code>null</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row data
     * @exception IllegalArgumentException if now row data is available
     *  at the currently specified row index
     */
    public Object getRowData() {

        if (list == null) {
	    return (null);
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            return (list.get(index));
        }

    }


    /**
     * @exception FacesException {@inheritDoc}     
     */ 
    public int getRowIndex() {

        return (index);

    }


    /**
     * @exception FacesException {@inheritDoc}
     * @exception IllegalArgumentException {@inheritDoc}
     */ 
    public void setRowIndex(int rowIndex) {

        if (rowIndex < -1) {
            throw new IllegalArgumentException();
        }
        int old = index;
        index = rowIndex;
	if (list == null) {
	    return;
	}
	DataModelListener [] listeners = getDataModelListeners();
        if ((old != index) && (listeners != null)) {
            Object rowData = null;
            if (isRowAvailable()) {
                rowData = getRowData();
            }
            DataModelEvent event =
                new DataModelEvent(this, index, rowData);
            int n = listeners.length;
            for (int i = 0; i < n; i++) {
		if (null != listeners[i]) {
		    listeners[i].rowSelected(event);
		}
            }
        }

    }


    public Object getWrappedData() {

        return (this.list);

    }


    /**
     * @exception ClassCastException if <code>data</code> is
     *  non-<code>null</code> and is not a <code>List</code>
     */
    public void setWrappedData(Object data) {

        if (data == null) {
            list = null;
            setRowIndex(-1);
        } else {
            list = (List) data;
            index = -1;
            setRowIndex(0);
        }

    }


}
