/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.model;


import javax.faces.FacesException;


/**
 * <p><strong>ArrayDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps an array of Java objects.</p>
 */

public class ArrayDataModel<E> extends DataModel<E> {


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
    public ArrayDataModel(E[] array) {

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
     * @throws FacesException if an error occurs getting the row availability
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
     * @throws FacesException if an error occurs getting the row count
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
     * @throws FacesException if an error occurs getting the row data
     * @throws IllegalArgumentException if now row data is available
     *  at the currently specified row index
     */
    @SuppressWarnings({"unchecked"})
    public E getRowData() {

        if (array == null) {
	    return (null);
        } else if (!isRowAvailable()) {
            throw new NoRowAvailableException();
        } else {
            return ((E) array[index]);
        }

    }


    /**
     * @throws FacesException {@inheritDoc}     
     */ 
    public int getRowIndex() {

        return (index);

    }


    /**
     * @throws FacesException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
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
     * @throws ClassCastException if <code>data</code> is
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
