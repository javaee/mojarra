/*
 * $Id: ResultSetBean.java,v 1.2 2003/10/29 22:48:29 craigmcc Exp $
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

package components.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;


/**
 * <p>Backing file bean for <code>ResultSet</code> demo.</p>
 */

public class ResultSetBean {



    // -------------------------------------------------------- Bound Components



    /**
     * <p>The <code>UIData</code> component representing the entire table.</p>
     */
    private UIData data = null;
    public UIData getData() { return data; }
    public void setData(UIData data) { this.data = data; }


    // ---------------------------------------------------------- Action Methods


    /**
     * <p>Scroll directly to the first page.</p>
     */
    public String first() {
	scroll(0);
	return (null);

    }


    /**
     * <p>Scroll directly to the last page.</p>
     */
    public String last() {
	scroll(data.getRowCount() - 1);
	return (null);

    }


    /**
     * <p>Scroll forwards to the next page.</p>
     */
    public String next() {
        int first = data.getFirst();
        scroll(first + data.getRows());
	return (null);

    }


    /**
     * <p>Scroll backwards to the previous page.</p>
     */
    public String previous() {
	int first = data.getFirst();
        scroll(first - data.getRows());
	return (null);

    }


    /**
     * <p>Handle a "reset" button by clearing local component values.</p>
     */
    public String reset() {
	clear();
	return (null);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Clear the checked state for all customers.</p>
     */
    private void clear() {

	int n = data.getRowCount();
	for (int i = 0; i < n; i++) {
	    data.setRowIndex(i);
	}

    }


    /**
     * <p>Return an <code>Iterator</code> over the customer list, if any;
     * otherwise return <code>null</code>.</p>
     */
    private Iterator iterator() {

	List list = list();
	if (list != null) {
	    return (list.iterator());
	} else {
	    return (null);
	}

    }


    /**
     * <p>Return the <code>List</code> containing our customers, if any;
     * otherwise, return <code>null</code>.</p>
     */
    private List list() {

	List list = (List)
	    FacesContext.getCurrentInstance().getExternalContext().
	    getSessionMap().get("list");
	return (list);

    }


    /**
     * <p>Scroll to the page that contains the specified row number.</p>
     *
     * @param row Desired row number
     */
    public void scroll(int row) {

	int rows = data.getRows();
	if (rows < 1) {
	    return; // Showing entire table already
	}
	if (row < 0) {
	    data.setFirst(0);
	} else if (row >= data.getRowCount()) {
            data.setFirst(data.getRowCount() - 1);
        } else {
            data.setFirst(row - (row % rows));
        }

    }


}
