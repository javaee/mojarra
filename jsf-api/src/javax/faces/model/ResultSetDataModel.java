/*
 * $Id: ResultSetDataModel.java,v 1.1 2003/10/11 22:59:43 craigmcc Exp $
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


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.el.PropertyNotFoundException;


/**
 * <p><strong>ResultSetDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps a <code>ResultSet</code> of Java objects.
 * Note that the specified <code>ResultSet</code> <strong>MUST</strong>
 * be scrollable.  In addition, if input components (that will be updating
 * model values) reference this object in value reference expressions, the
 * specified <code>ResultSet</code> <strong>MUST</strong> be updatable.</p>
 */

public class ResultSetDataModel implements DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ResultSetDataModel} wrapping the specified
     * <code>ResultSet</code>.</p>
     *
     * @param resultSet <code>ResultSet</code> to be wrapped
     *
     * @exception NullPointerException if <code>resultSet</code>
     *  is <code>null</code>
     */
    public ResultSetDataModel(ResultSet resultSet) {

        if (resultSet == null) {
            throw new NullPointerException();
        }
        this.resultSet = resultSet;

    }


    // ------------------------------------------------------ Instance Variables


    // The row index for the row whose column values may be read or written
    private int current = -1;


    // The current row index (one relative)
    private int index = 0;


    // The DataModelListeners interested in our events
    private List listeners = null;


    // The open flag
    private boolean open = false;


    // The ResultSet we are wrapping
    private ResultSet resultSet = null;


    // The number of rows in this ResultSet, or Integer.MIN_VALUE if unknown yet
    private int size = Integer.MIN_VALUE;


    // ------------------------------------------------------- Lifecycle Methods


    public void close() {

        if (!open) {
            throw new IllegalStateException();
        }
        if (listeners != null) {
            DataModelEvent event = new DataModelEvent(this);
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).modelClosed(event);
            }
        }
        // PENDING(craigmcc) - Delegate close() call to resultSet?
        index = 0;
        open = false;
        resultSet = null;
        size = Integer.MIN_VALUE;

    }


    public void open() throws FacesException {

        if (open) {
            throw new IllegalStateException();
        }
        index = 0;
        open = true;
        size = Integer.MIN_VALUE;
        if (listeners != null) {
            DataModelEvent event = new DataModelEvent(this);
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).modelOpened(event);
            }
        }

    }


    // -------------------------------------------------------------- Properties


    public boolean isOpen() {

        return (open);

    }


    public int getRowCount() {

        if (!open) {
            throw new IllegalStateException();
        }
        if (size == Integer.MIN_VALUE) {
            size = 0;
            try {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    size++;
                }
            } catch (SQLException e) {
                throw new FacesException(e);
            }
        }
        return (size);

    }


    public Object getRowData() {

        if (!open) {
            throw new IllegalStateException();
        }
        if ((index < 0) || (index > getRowCount())) {
            throw new IllegalArgumentException();
        }
        if (index == 0) {
            return (null);
        } else {
            current = index;
            return (this);
        }

    }


    public int getRowIndex() {

        if (!open) {
            throw new IllegalStateException();
        }
        return (index);

    }


    public void setRowIndex(int rowIndex) {

        if (!open) {
            throw new IllegalStateException();
        }
        if (rowIndex < 0) {
            throw new IllegalArgumentException();
        }
        int old = index;
        index = rowIndex;
        if ((old != index) && (listeners != null)) {
            DataModelEvent event =
                event = new DataModelEvent(this, index, getRowData());
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).modelSelected(event);
            }
        }

    }


    // --------------------------------------------- Event Listener Registration


    public void addDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);

    }


    public void removeDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners != null) {
            listeners.remove(listener);
        }

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the value of the specified column index, for the current row.
     * </p>
     *
     * @param column One-relative index of the column whose value is to be
     *  retrieved
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied name
     */
    public Object value(int column) throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            return (resultSet.getObject(column));
        } catch (SQLException e) {
            throw new PropertyNotFoundException("" + column);
        }

    }



    /**
     * <p>Return the value of the specified column name, for the currrent row.
     * </p>
     *
     * @param name Name of the column whose value is to be retrieved
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied name
     */
    public Object value(String name) throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            return (resultSet.getObject(name));
        } catch (SQLException e) {
            throw new PropertyNotFoundException(name);
        }

    }


    /**
     * <p>Set the value of the specified column index, for the current row.
     * </p>
     *
     * @param column One-relative index of the column whose value is to be set
     * @param value New value for this column
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied name
     */
    public void value(int column, Object value)
        throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            resultSet.updateObject(column, value);
        } catch (SQLException e) {
            throw new PropertyNotFoundException("" + column);
        }

    }



    /**
     * <p>Set the value of the specified column name, for the currrent row.
     * </p>
     *
     * @param name Name of the column whose value is to be set
     * @param value New value for this column
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied name
     */
    public void value(String name, Object value)
        throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            resultSet.updateObject(name, value);
        } catch (SQLException e) {
            throw new PropertyNotFoundException(name);
        }

    }


}
