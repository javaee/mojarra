/*
 * $Id: ArrayDataModel.java,v 1.15 2004/02/04 23:38:22 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import javax.faces.FacesException;


/**
 * <p><strong>ArrayDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps an array of Java objects.</p>
 */

public class ArrayDataModel extends DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ArrayDataModel} with no specified
     * wrapped data.</p>
     */
    public ArrayDataModel() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ArrayDataModel} wrapping the specified
     * array.</p>
     *
     * @param array Array to be wrapped (if any)
     */
    public ArrayDataModel(Object array[]) {

        super();
        setWrappedData(array);

    }


    // ------------------------------------------------------ Instance Variables


    // The array we are wrapping
    private Object array[];


    // The current row index (zero relative)
    private int index = -1;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return <code>true</code> if there is <code>wrappedData</code>
     * available, and the current value of <code>rowIndex</code> is greater
     * than or equal to zero, and less than the length of the array.  Otherwise,
     * return <code>false</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row availability
     */
    public boolean isRowAvailable() {

        if (array == null) {
	    return (false);
        } else if ((index >= 0) && (index < array.length)) {
            return (true);
        } else {
            return (false);
        }

    }


    /**
     * <p>If there is <code>wrappedData</code> available, return the
     * length of the array.  If no <code>wrappedData</code> is available,
     * return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row count
     */
    public int getRowCount() {

        if (array == null) {
	    return (-1);
        }
        return (array.length);

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

        if (array == null) {
	    return (null);
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            return (array[index]);
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
	if (array == null) {
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

        return (this.array);

    }


    /**
     * @exception ClassCastException if <code>data</code> is
     *  non-<code>null</code> and is not an array of Java objects.
     */
    public void setWrappedData(Object data) {

        if (data == null) {
            array = null;
            setRowIndex(-1);
        } else {
            array = (Object[]) data;
            index = -1;
            setRowIndex(0);
        }

    }


}
