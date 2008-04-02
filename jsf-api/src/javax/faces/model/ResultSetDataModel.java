/*
 * $Id: ResultSetDataModel.java,v 1.6 2003/10/15 04:17:35 craigmcc Exp $
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
import java.util.TreeMap;
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
     * @exception FacesException if the specified result set cannot be
     *  initialized
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
        try {
            this.metadata = resultSet.getMetaData();
        } catch (SQLException e) {
            throw new FacesException(e);
        }

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
            try {
                resultSet.absolute(index + 1);
                TreeMap map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
                int n = metadata.getColumnCount();
                for (int i = 1; i <= n; i++) {
                    String name = metadata.getColumnName(i);
                    map.put(name, resultSet.getObject(name));
                }
                return (map);
            } catch (SQLException e) {
                throw new FacesException(e);
            }
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


}
