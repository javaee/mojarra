/*
 * $Id: ResultSetDataModel.java,v 1.5 2003/10/15 02:02:15 craigmcc Exp $
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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
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

public class ResultSetDataModel extends DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ResultSetDataModel} wrapping the specified
     * <code>ResultSet</code>.</p>
     *
     * @param resultSet <code>ResultSet</code> to be wrapped
     *
     * @exception IllegalArgumentException if <code>resultSet</code> is of
     *  type <code>ResultSet.TYPE_FORWARD_ONLY</code>
     * @exception NullPointerException if <code>resultSet</code>
     *  is <code>null</code>
     */
    public ResultSetDataModel(ResultSet resultSet) {

        if (resultSet == null) {
            throw new NullPointerException();
        }
        try {
            if (ResultSet.TYPE_FORWARD_ONLY == resultSet.getType()) {
                throw new IllegalArgumentException();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
        this.resultSet = resultSet;

    }


    // ------------------------------------------------------ Instance Variables


    // The row index for the row whose column values may be read or written
    private int current = -1;


    // The current row index (one relative)
    private int index = -1;


    // The metadata for the ResultSet we are wrapping (lazily instantiated)
    private ResultSetMetaData metadata = null;


    // The ResultSet we are wrapping
    private ResultSet resultSet = null;


    // The number of rows in this ResultSet, or Integer.MIN_VALUE if unknown yet
    private int size = Integer.MIN_VALUE;


    // -------------------------------------------------------------- Properties


    /**
     * @exception FacesException {@inheritDoc}     
     */ 
    public int getRowCount() {

        if (size == Integer.MIN_VALUE) {
            try {
                size = 0;
                while (resultSet.absolute(size + 1)) {
                    size++;
                }
            } catch (SQLException e) {
                throw new FacesException(e);
            }
        }
        return (size);

    }


    /**
     * @exception FacesException {@inheritDoc}     
     */ 
    public Object getRowData() {

        if (index == -1) {
            return (null);
        } else {
            current = index;
            return (this);
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

        if ((rowIndex < -1) || (rowIndex >= getRowCount())) {
            throw new IllegalArgumentException();
        }
        try {
            if ((rowIndex >= 0) && (!resultSet.absolute(rowIndex + 1))) {
                throw new IllegalArgumentException();
            }
        } catch (SQLException e) {
            throw new FacesException(e);
        }
        int old = index;
        index = rowIndex;
        if ((old != index) && (listeners != null)) {
            DataModelEvent event =
                event = new DataModelEvent(this, index, getRowData());
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).rowSelected(event);
            }
        }

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return <code>true</code> if the <code>ResultSet</code> we are wrapping
     * is of concurrency <code>ResultSet.CONCUR_READ_ONLY</code>, or if the
     * column metadata indicates that the individual column is read only;
     * otherwise, return <code>false</code>.</p>
     *
     * @param column One-relative index of the column whose state is to be
     *  retrieved
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied index
     */
    public boolean readOnly(int column) throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            ResultSetMetaData metadata = metadata();
            if (metadata.isReadOnly(column)) {
                return (true);
            } else if (ResultSet.CONCUR_READ_ONLY == resultSet.getConcurrency()) {
                return (true);
            } else {
                return (false);
            }
        } catch (SQLException e) {
            throw new PropertyNotFoundException("" + column);
        }

    }


    /**
     * <p>Return <code>true</code> if the <code>ResultSet</code> we are wrapping
     * is of concurrency <code>ResultSet.CONCUR_READ_ONLY</code>, or if the
     * column metadata indicates that the individual column is read only;
     * otherwise, return <code>false</code>.</p>
     *
     * @param name Name of the column whose state is to be retrieved
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied name
     */
    public boolean readOnly(String name) throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            ResultSetMetaData metadata = metadata();
            int n = metadata.getColumnCount();
            int j = -1;
            for (int i = 1; i <= n; i++) {
                if (metadata.getColumnName(i).equals(name)) {
                    j = i;
                    break;
                }
            }
            if (metadata.isReadOnly(j)) {
                return (true);
            } else if (ResultSet.CONCUR_READ_ONLY == resultSet.getConcurrency()) {
                return (true);
            } else {
                return (false);
            }
        } catch (SQLException e) {
            throw new PropertyNotFoundException(name);
        }
    }


    /**
     * <p>Return the Java <code>Class</code> representing the data type of
     * the specified column.</p>
     *
     * @param column One-relative index of the column whose type is to be
     *  returned
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied index
     */
    public Class type(int column) throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            ResultSetMetaData metadata = metadata();
            String cname = metadata.getColumnClassName(column);
            ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }
            return (loader.loadClass(cname));
        } catch (Exception e) {
            throw new PropertyNotFoundException("" + column);
        }

    }


    /**
     * <p>Return the Java <code>Class</code> representing the data type of
     * the specified column.</p>
     *
     * @param name Name of the column whose type is to be returned
     *  returned
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied index
     */
    public Class type(String name) throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            ResultSetMetaData metadata = metadata();
            int n = metadata.getColumnCount();
            int j = -1;
            for (int i = 1; i <= n; i++) {
                if (metadata.getColumnName(i).equals(name)) {
                    j = i;
                    break;
                }
            }
            String cname = metadata.getColumnClassName(j);
            ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }
            return (loader.loadClass(cname));
        } catch (Exception e) {
            throw new PropertyNotFoundException(name);
        }

    }


    /**
     * <p>Return the value of the specified column index, for the current row.
     * </p>
     *
     * @param column One-relative index of the column whose value is to be
     *  retrieved
     *
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied index
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
     * @exception IllegalArgumentException if the underlying result set
     *  is read only
     * @exception IllegalStateException if the row index has been moved
     * @exception PropertyNotFoundException if there is no column with
     *  the specfied index
     */
    public void value(int column, Object value)
        throws PropertyNotFoundException {

        if (current != index) {
            throw new IllegalStateException();
        }
        try {
            if (ResultSet.CONCUR_READ_ONLY == resultSet.getConcurrency()) {
                throw new IllegalArgumentException();
            }
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
     * @exception IllegalArgumentException if the underlying result set
     *  is read only
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
            if (ResultSet.CONCUR_READ_ONLY == resultSet.getConcurrency()) {
                throw new IllegalArgumentException();
            }
            resultSet.updateObject(name, value);
        } catch (SQLException e) {
            throw new PropertyNotFoundException(name);
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>ResultSetMetaData</code> for the
     * <code>ResultSet</code> we are wrapping.</p>
     *
     * @exception SQLException if we cannot return the metadata
     */
    private ResultSetMetaData metadata() throws SQLException {

        if (metadata == null) {
            metadata = resultSet.getMetaData();
        }
        return (metadata);

    }


}
