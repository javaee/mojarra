/*
 * $Id: ResultDataModel.java,v 1.13 2004/01/26 06:49:40 craigmcc Exp $
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
import java.util.SortedMap;
import javax.faces.FacesException;
import javax.servlet.jsp.jstl.sql.Result;


/**
 * <p><strong>ResultDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps a JSTL <code>Result</code> object, typically
 * representing the results of executing an SQL query via JSTL tags.</p>
 */

public class ResultDataModel extends DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ResultDataModel} with no specified
     * wrapped data.</p>
     */
    public ResultDataModel() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ResultDataModel} wrapping the specified
     * <code>Result</code>.</p>
     *
     * @param result <code>Result</code> to be wrapped (if any)
     */
    public ResultDataModel(Result result) {

        super();
        setWrappedData(result);

    }


    // ------------------------------------------------------ Instance Variables


    // The current row index (zero relative)
    private int index = -1;


    // The Result we are wrapping
    private Result result = null;


    // The individual rows of this Result, each represented as a Map
    // with column names as keys and associated data values as values
    private SortedMap rows[] = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return <code>true</code> if there is <code>wrappedData</code>
     * available, and the current value of <code>rowIndex</code> is greater
     * than or equal to zero, and less than the length of the array returned
     * by calling <code>getRows()</code> on the underlying <code>Result</code>.
     * Otherwise, return <code>false</code>.</p>
     *
     * @exception FacesException if an error occurs getting the row availability
     */ 
    public boolean isRowAvailable() {

        if (result == null) {
	    return (false);
        } else if ((index >= 0) && (index < rows.length)) {
            return (true);
        } else {
            return (false);
        }

    }


    /**
     * <p>If there is <code>wrappedData</code> available, return the
     * length of the array returned by calling <code>getRows()</code>
     * on the underlying <code>Result</code>.  If no <code>wrappedData</code>
     * is available, return -1.</p>
     *
     * @exception FacesException if an error occurs getting the row count
     */
    public int getRowCount() {

        if (result == null) {
	    return (-1);
        }
        return (rows.length);

    }


    /**
     * <p>If row data is available, return the <code>SortedMap</code> array
     * element at the index specified by <code>rowIndex</code> of the
     * array returned by calling <code>getRows()</code> on the underlying
     * <code>Result</code>.  If no wrapped data is available,
     * return <code>null</code>.</p>
     *
     * <p>Note that, if a non-<code>null</code> <code>Map</code> is returned
     * by this method, it will contain the values of the columns for the
     * current row, keyed by column name.  Column name comparisons must be
     * performed in a case-insensitive manner.</p>
     *
     * @exception FacesException if an error occurs getting the row data
     * @exception IllegalArgumentException if now row data is available
     *  at the currently specified row index
     */
    public Object getRowData() {

        if (result == null) {
	    return (null);
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            return (rows[index]);
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
	if (result == null) {
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

        return (this.result);

    }


    /**
     * @exception ClassCastException if <code>data</code> is
     *  non-<code>null</code> and is not a <code>Result</code>
     */
    public void setWrappedData(Object data) {

        if (data == null) {
            result = null;
            rows = null;
            setRowIndex(-1);
        } else {
            result = (Result) data;
            rows = result.getRows();
            index = -1;
            setRowIndex(0);
        }

    }


}
