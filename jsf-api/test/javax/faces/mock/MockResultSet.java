/*
 * $Id: MockResultSet.java,v 1.5 2004/02/26 20:31:54 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
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

package javax.faces.mock;


import java.io.InputStream;
import java.io.Reader;
import java.util.Calendar;
import java.util.Map;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import javax.faces.model.TestBean;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * <p>Mock object that implements enough of <code>java.sql.ResultSet</code>
 * to exercise the <code>ResultSetDataModel</code> functionality.  It wraps
 * an array of JavaBeans objects that are passed to the constructor.</p>
 */

public class MockResultSet implements ResultSet {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new <code>MockResultSet</code> instance wrapping the
     * specified array of beans.</p>
     *
     * @param beans Array of beans representing the content of the result set
     */
    public MockResultSet(Object beans[]) {

        if (beans == null) {
            throw new NullPointerException();
        }
        this.beans = beans;
        this.clazz = beans.getClass().getComponentType();

    }


    // ------------------------------------------------------ Instance Variables


    // Array of beans representing our underlying data
    private Object beans[] = null;


    // Class representing the underlying data type we are wrapping
    private Class clazz = null;


    // ResultSetMetaData for this ResultSet
    private MockResultSetMetaData metadata = null;


    // Current row number (0 means "before the first row"
    private int row = 0;


    // ----------------------------------------------------- Implemented Methods


    public boolean absolute(int row) throws SQLException {

        if (row == 0) {
            this.row = 0;
            return (false);
        } else if (row > 0) {
            if (row > beans.length) {
                this.row = beans.length + 1;
                return (false);
            } else {
                this.row = row;
                return (true);
            }
        } else {
            this.row = (beans.length + 1) - row;
            if (row < 1) {
                row = 0;
                return (false);
            } else {
                return (true);
            }
        }

    }


    public void beforeFirst() throws SQLException {

        absolute(0);

    }


    public void close() throws SQLException {

        ; // No action required

    }


    public int getConcurrency() throws SQLException {

        return (ResultSet.CONCUR_UPDATABLE);

    }


    public ResultSetMetaData getMetaData() throws SQLException {

        if (metadata == null) {
            metadata = new MockResultSetMetaData(clazz);
        }
        return (metadata);

    }


    public Object getObject(int columnIndex) throws SQLException {

        return (getObject(getMetaData().getColumnName(columnIndex)));

    }


    public Object getObject(String columnName) throws SQLException {

        if ((row <= 0) || (row > beans.length)) {
            throw new SQLException("Invalid row number " + row);
        }
        try {
            if (columnName.equals("writeOnlyProperty") &&
                (beans[row - 1] instanceof TestBean)) {
                return (((TestBean) beans[row - 1]).getWriteOnlyPropertyValue());
            } else {
                return (PropertyUtils.getSimpleProperty(beans[row - 1],
                                                        columnName));
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }


    public int getRow() throws SQLException {

        return (this.row);

    }


    public int getType() throws SQLException {

        return (ResultSet.TYPE_SCROLL_INSENSITIVE);

    }


    public boolean last() throws SQLException {

        return (absolute(beans.length));

    }


    public void updateObject(int columnIndex, Object value)
        throws SQLException {

        updateObject(getMetaData().getColumnName(columnIndex), value);

    }


    public void updateObject(String columnName, Object value)
        throws SQLException {

        if ((row <= 0) || (row > beans.length)) {
            throw new SQLException("Invalid row number " + row);
        }
        try {
            PropertyUtils.setSimpleProperty(beans[row - 1], columnName, value);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }


    // --------------------------------------------------- Unimplemented Methods


    public void afterLast() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void cancelRowUpdates() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void deleteRow() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int findColumn(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean first() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Array getArray(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Array getArray(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public InputStream getAsciiStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /** @deprecated */
    public BigDecimal getBigDecimal(int columnIndex, int scale)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    /** @deprecated */
    public BigDecimal getBigDecimal(String columnName, int scale)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public InputStream getBinaryStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Blob getBlob(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Blob getBlob(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean getBoolean(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean getBoolean(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public byte getByte(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public byte getByte(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public byte[] getBytes(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public byte[] getBytes(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Reader getCharacterStream(int columnIndex)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Reader getCharacterStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Clob getClob(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Clob getClob(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getCursorName() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Date getDate(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Date getDate(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Date getDate(String columnName, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public double getDouble(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public double getDouble(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getFetchDirection() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getFetchSize() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public float getFloat(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public float getFloat(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getInt(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getInt(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public long getLong(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public long getLong(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Object getObject(int columnIndex, Map map) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Object getObject(String columnName, Map map) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Ref getRef(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Ref getRef(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public short getShort(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public short getShort(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Statement getStatement() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getString(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getString(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Time getTime(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Time getTime(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Time getTime(String columnName, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Timestamp getTimestamp(int columnIndex, Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Timestamp getTimestamp(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public Timestamp getTimestamp(String columnName, Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    /** @deprecated */
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    /** @deprecated */
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public URL getURL(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public URL getURL(String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void insertRow() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isAfterLast() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isBeforeFirst() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isFirst() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isLast() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void moveToCurrentRow() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void moveToInsertRow() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean next() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean previous() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void refreshRow() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean relative(int rows) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean rowDeleted() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean rowInserted() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean rowUpdated() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void setFetchDirection(int direction) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void setFetchSize(int size) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateArray(int columnPosition, Array x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateArray(String columnName, Array x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateAsciiStream(int columnPosition, InputStream x, int len)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateAsciiStream(String columnName, InputStream x, int len)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBigDecimal(int columnPosition, BigDecimal x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBigDecimal(String columnName, BigDecimal x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBinaryStream(int columnPosition, InputStream x, int len)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBinaryStream(String columnName, InputStream x, int len)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBlob(int columnPosition, Blob x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBlob(String columnName, Blob x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBoolean(int columnPosition, boolean x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBoolean(String columnName, boolean x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateByte(int columnPosition, byte x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateByte(String columnName, byte x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBytes(int columnPosition, byte x[])
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateBytes(String columnName, byte x[])
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateCharacterStream(int columnPosition, Reader x, int len)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateCharacterStream(String columnName, Reader x, int len)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateClob(int columnPosition, Clob x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateClob(String columnName, Clob x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateDate(int columnPosition, Date x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateDate(String columnName, Date x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateDouble(int columnPosition, double x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateDouble(String columnName, double x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateFloat(int columnPosition, float x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateFloat(String columnName, float x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateInt(int columnPosition, int x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateInt(String columnName, int x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateLong(int columnPosition, long x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateLong(String columnName, long x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateNull(int columnPosition)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateNull(String columnName)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateObject(int columnPosition, Object x, int scale)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateObject(String columnName, Object x, int scale)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateRef(int columnPosition, Ref x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateRef(String columnName, Ref x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateRow() throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateShort(int columnPosition, short x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateShort(String columnName, short x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateString(int columnPosition, String x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateString(String columnName, String x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateTime(int columnPosition, Time x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateTime(String columnName, Time x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateTimestamp(int columnPosition, Timestamp x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public void updateTimestamp(String columnName, Timestamp x)
        throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean wasNull() throws SQLException {
        throw new UnsupportedOperationException();
    }


}
