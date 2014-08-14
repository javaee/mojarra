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

import java.util.SortedMap;
import java.util.TreeMap;
import javax.faces.FacesException;
import javax.servlet.jsp.jstl.sql.Result;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>
 * Mock object that implements enough of
 * <code>javax.servlet.jsp.jstl.sql.ResultSet</code> to exercise the
 * <code>ResultDataModel</code> functionality. It wraps an array of JavaBeans
 * objects that are passed to the constructor.</p>
 *
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - The <code>SortedMap</code> objects
 * returned by <code>getRows()</code> do <strong>NOT</strong>
 * support case-insensitive key comparisons, as required by the JSTL
 * specification. Therefore, key values in value reference expressions will be
 * matched case sensitively in unit tests.</p>
 */
public class MockResult implements Result {

    // ------------------------------------------------------------ Constructors
    /**
     * <p>
     * Construct a new <code>MockResult</code> instance wrapping the specified
     * array of beans.</p>
     *
     * @param beans Array of beans representing the content of the result set
     */
    public MockResult(Object beans[]) {
        if (beans == null) {
            throw new NullPointerException();
        }
        this.beans = beans;
    }

    // ------------------------------------------------------ Instance Variables
    // Array of beans representing our underlying data
    private Object beans[] = null;

    // ----------------------------------------------------- Implemented Methods
    @Override
    public SortedMap[] getRows() {
        TreeMap results[] = new TreeMap[beans.length];
        for (int i = 0; i < results.length; i++) {
            try {
                results[i] = new TreeMap(PropertyUtils.describe(beans[i]));
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return (results);
    }

    @Override
    public int getRowCount() {
        return (beans.length);
    }

    // --------------------------------------------------- Unimplemented Methods
    @Override
    public Object[][] getRowsByIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getColumnNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLimitedByMaxRows() {
        throw new UnsupportedOperationException();
    }
}
