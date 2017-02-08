/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import javax.faces.model.ListDataModel;

/**
 * <p class="changed_added_2_3"><strong>IterableDataModel</strong> is an
 * implementation of {@link DataModel} that wraps an <code>Iterable</code>.</p>
 * 
 * <p>
 * This can be used to encapsulate nearly every collection type, including 
 * {@link Collection} derived types such as {@link List} and {@link Set}. 
 * As such this specific DataModel can be used instead of more specific 
 * DataModels like {@link ListDataModel} and {@link CollectionDataModel}.
 *
 */
public class IterableDataModel<E> extends DataModel<E> {

    /**
     * The current row index.
     */
    private int index = -1;
    
    /**
     * The iterable that this data model is primarily wrapping.
     */
    private Iterable<E> iterable;
    
    /**
     * A list that's optionally used to hold and cache data collected
     * from the iterable that this data model is wrapping.
     */
    private List<E> list;
    
    /**
     * <p>Construct a new {@link IterableDataModel} with no specified
     * wrapped data.</p>
     */
    public IterableDataModel() {
        this(null);
    }

    /**
     * <p>Construct a new {@link IterableDataModel} wrapping the specified
     * iterable.</p>
     *
     * @param iterable Iterable to be wrapped.
     */
    public IterableDataModel(Iterable<E> iterable) {
        setWrappedData(iterable);
    }

    /**
     * <p>Return a flag indicating whether there is <code>rowData</code>
     * available at the current <code>rowIndex</code>.  If no
     * <code>wrappedData</code> is available, return <code>false</code>.</p>
     *
     * @throws javax.faces.FacesException if an error occurs getting the row availability
     */
    @Override
    public boolean isRowAvailable() {
        return list != null && index >= 0 && index < list.size();
    }

    /**
     * <p>Return the number of rows of data objects represented by this
     * {@link DataModel}.  If the number of rows is unknown, or no
     * <code>wrappedData</code> is available, return -1.</p>
     *
     * @throws javax.faces.FacesException if an error occurs getting the row count
     */
    @Override
    public int getRowCount() {
        if (list == null) {
            return -1;
        }

        return list.size();
    }

    /**
     * <p>Return an object representing the data for the currenty selected
     * row index.  If no <code>wrappedData</code> is available, return
     * <code>null</code>.</p>
     *
     * @throws javax.faces.FacesException if an error occurs getting the row data
     * @throws IllegalArgumentException if now row data is available
     *  at the currently specified row index
     */
    @Override
    public E getRowData() {
        if (list == null) {
            return null;
        }
        if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        }

        return list.get(index);
    }

    /**
     * <p>Return the zero-relative index of the currently selected row.  If
     * we are not currently positioned on a row, or no <code>wrappedData</code>
     * is available, return -1.</p>
     *
     * @throws javax.faces.FacesException if an error occurs getting the row index
     */
    @Override
    public int getRowIndex() {
        return index;
    }

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
     * @throws javax.faces.FacesException if an error occurs setting the row index
     * @throws IllegalArgumentException if <code>rowIndex</code>
     *  is less than -1
     */
    @Override
    public void setRowIndex(int rowIndex) {

        if (rowIndex < -1) {
            throw new IllegalArgumentException();
        }

        int oldRowIndex = index;
        index = rowIndex;

        if (list == null) {
            return;
        }

        notifyListeners(oldRowIndex, rowIndex);
    }

    /**
     * <p>Return the object representing the data wrapped by this
     * {@link DataModel}, if any.</p>
     */
    @Override
    public Object getWrappedData() {
        return iterable;
    }

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
     * @throws ClassCastException if <code>data</code> is not of the
     *  appropriate type for this {@link DataModel} implementation
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setWrappedData(Object data) {
        if (data == null) {
            iterable = null;
            list = null;
            setRowIndex(-1);
        } else {
            iterable = (Iterable<E>) data;
            list = iterableToList(iterable);
            setRowIndex(0);
        }
    }

    /**
     * <p>Return an object representing the data for the currently selected
     * row index. If either no <code>wrappedData</code> is available or if 
     * there is no <code>rowData</code>available at the current <code>rowIndex</code>, 
     * return <code>null</code>.</p>
     * 
     * @return data for the currently selected row index, or null if there's no data
     */
    private E getRowDataOrNull() {
        if (isRowAvailable()) {
            return getRowData();
        }

        return null;
    }

    /**
     * Notifies all DataModelListeners
     * 
     * @param oldRowIndex the previous index
     * @param rowIndex The current zero-relative index (must be non-negative)
     */
    private void notifyListeners(int oldRowIndex, int rowIndex) {
        DataModelListener[] dataModelListeners = getDataModelListeners();
        if (oldRowIndex != rowIndex && dataModelListeners != null) {

            DataModelEvent dataModelEvent = new DataModelEvent(this, rowIndex, getRowDataOrNull());

            for (DataModelListener dataModelListener : dataModelListeners) {
                if (dataModelListener != null) {
                    dataModelListener.rowSelected(dataModelEvent);
                }
            }
        }
    }
    
    /**
     * Converts an iterable into a list.
     * <p>
     * This method makes NO guarantee to whether changes to the source iterable are
     * reflected in the returned list or not. For instance if the given iterable
     * already is a list, it's returned directly.
     *
     * @param <E> The generic iterable element type.
     * @param iterable The iterable to be converted.
     * @return The list representation of the given iterable, possibly the same instance as that iterable.
     */
    private static <E> List<E> iterableToList(Iterable<E> iterable) {

        List<E> list = null;

        if (iterable instanceof List) {
            list = (List<E>) iterable;
        } else if (iterable instanceof Collection) {
            list = new ArrayList<>((Collection<E>) iterable);
        } else {
            list = new ArrayList<>();
            Iterator<E> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }

        return list;
    }

}
