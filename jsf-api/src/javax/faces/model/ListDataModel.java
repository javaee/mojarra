/*
 * $Id: ListDataModel.java,v 1.17 2004/05/12 18:29:14 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
