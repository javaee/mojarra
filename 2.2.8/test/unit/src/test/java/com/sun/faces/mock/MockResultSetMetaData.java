/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * <p>
 * Mock object that implements enough of <code>java.sql.ResultSetMetaData</code>
 * to exercise the <code>ResultSetDataModel</code> functionality.</p>
 */
public class MockResultSetMetaData implements ResultSetMetaData {

    // ------------------------------------------------------------ Constructors
    /**
     * <p>
     * Construct a new <code>ResultSetMetaData</code> object wrapping the
     * properties of the specified Java class.</p>
     *
     * @param clazz Class whose properties we treat like columns
     */
    public MockResultSetMetaData(Class clazz) throws SQLException {
        this.clazz = clazz;
        try {
            descriptors
                    = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
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
    @Override
    public String getColumnClassName(int columnIndex) throws SQLException {
        return (getDescriptor(columnIndex).getPropertyType().getName());
    }

    @Override
    public int getColumnCount() throws SQLException {
        return (descriptors.length);
    }

    @Override
    public String getColumnName(int columnIndex) throws SQLException {
        return (getDescriptor(columnIndex).getName());
    }

    @Override
    public boolean isReadOnly(int columnIndex) throws SQLException {
        return (getDescriptor(columnIndex).getWriteMethod() == null);
    }

    // --------------------------------------------------- Unimplemented Methods
    @Override
    public String getCatalogName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnDisplaySize(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnLabel(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnType(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnTypeName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPrecision(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getScale(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSchemaName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTableName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAutoIncrement(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCaseSensitive(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCurrency(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDefinitelyWritable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int isNullable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSearchable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSigned(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWritable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
