/*
 * $Id: MockResult.java,v 1.2 2004/02/04 23:39:14 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
