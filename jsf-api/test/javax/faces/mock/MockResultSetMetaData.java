/*
 * $Id: MockResultSetMetaData.java,v 1.5 2005/08/22 22:08:25 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.mock;


import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


/**
 * <p>Mock object that implements enough of
 * <code>java.sql.ResultSetMetaData</code>
 * to exercise the <code>ResultSetDataModel</code> functionality.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.5 $ $Date: 2005/08/22 22:08:25 $
 */

public class MockResultSetMetaData implements ResultSetMetaData {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new <code>ResultSetMetaData</code> object wrapping the
     * properties of the specified Java class.</p>
     *
     * @param clazz Class whose properties we treat like columns
     */
    public MockResultSetMetaData(Class clazz) throws SQLException {

        this.clazz = clazz;
        try {
            descriptors =
                Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }
    

    // ------------------------------------------------------ Instance Variables


    // The Class whose properties we are treating as columns
    private Class clazz = null;


    // The PropertyDescriptors for the Class we are wrapping
    private PropertyDescriptor descriptors[] = null;


    // ---------------------------------------------------------- Public Methods


    public PropertyDescriptor getDescriptor(int columnIndex)
        throws SQLException {

        try {
            return (descriptors[columnIndex - 1]);
        } catch (IndexOutOfBoundsException e) {
            throw new SQLException("Invalid columnIndex " + columnIndex);
        }

    }


    // ----------------------------------------------------- Implemented Methods


    public String getColumnClassName(int columnIndex) throws SQLException {

        return (getDescriptor(columnIndex).getPropertyType().getName());

    }


    public int getColumnCount() throws SQLException {

        return (descriptors.length);

    }

    public String getColumnName(int columnIndex) throws SQLException {

        return (getDescriptor(columnIndex).getName());

    }


    public boolean isReadOnly(int columnIndex) throws SQLException {

        return (getDescriptor(columnIndex).getWriteMethod() == null);

    }



    // --------------------------------------------------- Unimplemented Methods


    public String getCatalogName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getColumnDisplaySize(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getColumnLabel(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getColumnType(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getColumnTypeName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getPrecision(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getScale(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getSchemaName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getTableName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isAutoIncrement(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isCaseSensitive(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isCurrency(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isDefinitelyWritable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int isNullable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isSearchable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isSigned(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isWritable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


}
