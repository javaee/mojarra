/*
 * $Id: MockResult.java,v 1.3 2004/02/26 20:31:54 eburns Exp $
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
