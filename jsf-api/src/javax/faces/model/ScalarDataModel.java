/*
 * $Id: ScalarDataModel.java,v 1.16 2004/05/12 18:29:14 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import java.util.List;
import javax.faces.FacesException;


/**
 * <p><strong>ScalarDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps an individual Java object.</p>
 */

public class ScalarDataModel extends DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ScalarDataModel} with no specified
     * wrapped data.</p>
     */
    public ScalarDataModel() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ScalarDataModel} wrapping the specified
     * scalar object.</p>
     *
     * @param scalar Scalar to be wrapped (if any)
     */
    public ScalarDataModel(Object scalar) {

        super();
        setWrappedData(scalar);

    }


    // ------------------------------------------------------ Instance Variables


    // The currently selected row index (zero-relative)
    private int index;


    // The scalar we are wrapping
    private Object scalar;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return <code>true</code> if there is <code>wrappedData</code>
     * available, and the current value of <code>rowIndex</code> is zero.
     * Otherwise, return <code>false</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row availability
     */
    public boolean isRowAvailable() {

        if (scalar == null) {
	    return (false);
        } else if (index == 0) {
            return (true);
        } else {
            return (false);
        }

    }


    /**
     * <p>If there is <code>wrappedData</code> available, return 1.
     * If no <code>wrappedData</code> is available, return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row count
     */
    public int getRowCount() {

        if (scalar == null) {
	    return (-1);
        }
        return (1);

    }


    /**
     * <p>If wrapped data is available, return the wrapped data instance.
     * Otherwise, return <code>null</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row data
     * @exception IllegalArgumentException if now row data is available
     *  at the currently specified row index
     */
    public Object getRowData() {

        if (scalar == null) {
	    return (null);
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            return (scalar);
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
	if (scalar == null) {
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

        return (this.scalar);

    }


    /**
     * @exception ClassCastException {@inheritDoc}
     */
    public void setWrappedData(Object data) {

        if (data == null) {
            scalar = null;
            setRowIndex(-1);
        } else {
            scalar = data;
            index = -1;
            setRowIndex(0);
        }

    }


}
