/* Copyright 2005-2010 Sun Microsystems, Inc.  All rights reserved.  You may
 * not modify, use, reproduce, or distribute this software except in
 * compliance with the terms of the License at:
 * http://developer.sun.com/berkeley_license.html
 */

package com.sun.faces.run_time_test.model;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

import com.sun.faces.CustomerBean;

/**
 * <p>Backing file bean for <code>ResultSet</code> 
 com.sun.javaee.blueprints.simpleuicomponentsonents.</p>
 */

public class ResultSetBean {

    private List list = null;


    public ResultSetBean() {
    }


    public List getList() {
        // Construct a preconfigured customer list lazily.
        if (list == null) {
            list = new ArrayList();
            for (int i = 0; i < 1000; i++) {
                list.add(new CustomerBean(Integer.toString(i),
                                          "name_" + Integer.toString(i),
                                          "symbol_" + Integer.toString(i), i));
            }
        }
        return list;
    }


    public void setList(List newlist) {
        this.list = newlist;
    }

    // -------------------------------------------------------- Bound Components

    /**
     * <p>The <code>UIData</code> component representing the entire table.</p>
     */
    private UIData data = null;


    public UIData getData() {
        return data;
    }


    public void setData(UIData data) {
        this.data = data;
    }


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


    /**
     * Handles the ActionEvent generated as a result of clicking on a
     * link that points a particular page in the result-set.
     */
    public void processScrollEvent(ActionEvent event) {
        int currentRow = 1;
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = event.getComponent();
        Integer curRow = (Integer) component.getAttributes().get("currentRow");
        if (curRow != null) {
            currentRow = curRow.intValue();
        }
        // scroll to the appropriate page in the ResultSet.
        scroll(currentRow);
    }


}
