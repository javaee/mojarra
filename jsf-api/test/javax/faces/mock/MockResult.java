/*
 * $Id: MockResult.java,v 1.5 2005/08/22 22:08:25 ofung Exp $
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


import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.faces.FacesException;
import javax.servlet.jsp.jstl.sql.Result;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * <p>Mock object that implements enough of
 * <code>javax.servlet.jsp.jstl.sql.ResultSet</code> to exercise the
 * <code>ResultDataModel</code> functionality.  It wraps an array of
 * JavaBeans objects that are passed to the constructor.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - The <code>SortedMap</code>
 * objects returned by <code>getRows()</code> do <strong>NOT</strong>
 * support case-insensitive key comparisons, as required by the JSTL
 * specification.  Therefore, key values in value reference expressions
 * will be matched case sensitively in unit tests.</p>
 */

public class MockResult implements Result {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new <code>MockResult</code> instance wrapping the
     * specified array of beans.</p>
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


    public int getRowCount() {

        return (beans.length);

    }


    // --------------------------------------------------- Unimplemented Methods


    public Object[][] getRowsByIndex() {
        throw new UnsupportedOperationException();
    }


    public String[] getColumnNames() {
        throw new UnsupportedOperationException();
    }


    public boolean isLimitedByMaxRows() {
        throw new UnsupportedOperationException();
    }


}
