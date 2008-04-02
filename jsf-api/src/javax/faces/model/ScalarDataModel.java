/*
 * $Id: ScalarDataModel.java,v 1.10 2004/01/23 04:07:01 craigmcc Exp $
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
     * @exception FacesException {@inheritDoc}
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
     * @exception FacesException {@inheritDoc}     
     */ 
    public int getRowCount() {

        if (scalar == null) {
	    return (-1);
        }
        return (1);

    }


    /**
     * @exception FacesException {@inheritDoc}     
     * @exception IllegalArgumentException (@inheritDoc}
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
