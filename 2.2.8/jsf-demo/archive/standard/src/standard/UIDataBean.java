/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package standard;


import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/** <p>Backing file bean for <code>/UIData.jsp</code> demo.</p> */

public class UIDataBean {

    // -------------------------------------------------------- Bound Components


    /** <p>The <code>accountId</code> field for the current row.</p> */
    private UIInput accountId = null;


    public UIInput getAccountId() {
        return accountId;
    }


    public void setAccountId(UIInput accountId) {
        this.accountId = accountId;
    }


    /** <p>The <code>checked</code> field for the current row.</p> */
    private UISelectBoolean checked = null;


    public UISelectBoolean getChecked() {
        return checked;
    }


    public void setChecked(UISelectBoolean checked) {
        this.checked = checked;
    }


    /** <p>The <code>created</code> field for the current row.</p> */
    private UISelectBoolean created = null;


    public UISelectBoolean getCreated() {
        return created;
    }


    public void setCreated(UISelectBoolean created) {
        this.created = created;
    }


    /** <p>The <code>UIData</code> component representing the entire table.</p> */
    private UIData data = null;


    public UIData getData() {
        return data;
    }


    public void setData(UIData data) {
        this.data = data;
    }

    // --------------------------------------------------- Calculated Properties


    /** <p>Return a customized label for the "Click" link.</p> */
    public String getClickLabel() {

        return ("Click");
        /* Causes NPE because data is not initialized yet?
        CustomerBean customer = (CustomerBean) data.getRowData();
        if (customer != null) {
            return ("Click " + customer.getAccountId());
        } else {
            return ("Click");
        }
        */

    }


    /** <p>Return a customized label for the "Press" button.</p> */
    public String getPressLabel() {

        return ("Press");
        /* Causes NPE because data is not initialized yet?
        CustomerBean customer = (CustomerBean) data.getRowData();
        if (customer != null) {
            return ("Press " + customer.getAccountId());
        } else {
            return ("Press");
        }
        */

    }

    // --------------------------------------------------------- Action Handlers


    /** <p>Acknowledge that a row-specific link was clicked.</p> */
    public String click() {

        FacesContext context = FacesContext.getCurrentInstance();
        append("click(rowIndex=" + data.getRowIndex() +
               ",accountId=" +
               accountId.getValue() + ")");
        CustomerBean customer = (CustomerBean) context.getExternalContext()
              .getRequestMap().get("customer");
        System.out.println("accountId: " + customer.getAccountId());
        context.getExternalContext().getSessionMap().put("customer", customer);
        clear();
        return ("click");

    }


    /**
     * <p>Create a new empty row to be filled in for a new record
     * in the database.</p>
     */
    public String create() {

        append("create()");
        clear();

        // Add a new row to the table
        List list = list();
        if (list != null) {
            CustomerBean customer = new CustomerBean();
            list.add(customer);
            int index = data.getRowIndex();
            data.setRowIndex(list.size() - 1);
            created.setSelected(true);
            data.setRowIndex(index);
        }

        // Position so that the new row is visible if necessary
        scroll(list.size());
        return (null);

    }


    /**
     * <p>Delete any customers who have been checked from the list
     * (Immediate Mode).</p>
     */
    public String deleteImmediate() {

        append("delete()");

        // Delete customers for whom the checked field is selected
        List removes = new ArrayList();
        int n = data.getRowCount();
        for (int i = 0; i < n; i++) {
            data.setRowIndex(i);
            System.out.println(
                  "delete(accountId=" + accountId.getValue() + ",checked=" +
                  checked.getSubmittedValue() +
                  ")");
            if ("true".equals(checked.getSubmittedValue())) {
                removes.add(data.getRowData());
                checked.setSelected(false);
                created.setSelected(false);
            }
        }
        if (removes.size() > 0) {
            List list = list();
            Iterator remove = removes.iterator();
            while (remove.hasNext()) {
                list.remove(remove.next());
            }
        }

        clear();

        return (null);
    }


    /**
     * <p>Delete any customers who have been checked from the list
     * (Deferred Mode).</p>
     */
    public String deleteDeferred() {

        append("delete()");

        // Delete customers for whom the checked field is selected
        List removes = new ArrayList();
        int n = data.getRowCount();
        for (int i = 0; i < n; i++) {
            data.setRowIndex(i);
            System.out.println(
                  "delete(accountId=" + accountId.getValue() + ",checked=" +
                  checked.isSelected() +
                  ")");
            if (checked.isSelected()) {
                removes.add(data.getRowData());
                checked.setSelected(false);
                created.setSelected(false);
            }
        }
        if (removes.size() > 0) {
            List list = list();
            Iterator remove = removes.iterator();
            while (remove.hasNext()) {
                list.remove(remove.next());
            }
        }

        clear();

        return (null);
    }


    /** <p>Scroll directly to the first page.</p> */
    public String first() {

        append("first()");
        scroll(0);
        return (null);

    }


    /** <p>Acknowledge that the footer button was pressed.</p> */
    public String footer() {

        FacesContext context = FacesContext.getCurrentInstance();
        append("footer()");
        clear();
        return (null);

    }


    /** <p>Acknowledge that the header button was pressed.</p> */
    public String header() {

        FacesContext context = FacesContext.getCurrentInstance();
        append("header()");
        clear();
        return (null);

    }


    /** <p>Scroll directly to the last page.</p> */
    public String last() {

        append("last()");
        scroll(data.getRowCount() - 1);
        return (null);

    }


    /** <p>Scroll forwards to the next page.</p> */
    public String next() {

        append("next()");
        int first = data.getFirst();
        scroll(first + data.getRows());
        return (null);

    }


    /** <p>Acknowledge that a row-specific button was pressed.</p> */
    public String press() {

        FacesContext context = FacesContext.getCurrentInstance();
        append("press(rowIndex=" + data.getRowIndex() +
               ",accountId=" +
               accountId.getValue() + ")");
        clear();
        return (null);

    }


    /** <p>Scroll backwards to the previous page.</p> */
    public String previous() {

        append("previous()");
        int first = data.getFirst();
        scroll(first - data.getRows());
        return (null);

    }


    /** <p>Handle a "reset" button by clearing local component values.</p> */
    public String reset() {

        append("reset()");
        clear();
        return (null);

    }


    /**
     * <p>Save any changes to the underlying database.  In a real application
     * this would need to distinguish between inserts and updates, based on
     * the state of the "created" property.</p>
     */
    public String update() {

        append("update()");
        ; // Save to database as necessary
        clear();
        created();
        return (null);

    }

    // --------------------------------------------------------- Private Methods


    /**
     * <p>Append the specified text to the message request attribute, creating
     * this attribute if necessary.</p>
     *
     * @param text Text to be appended
     */
    private void append(String text) {

        //        System.out.println("APPEND:  " + text);
        FacesContext context = FacesContext.getCurrentInstance();
        String message = (String)
              context.getExternalContext().getRequestMap().get("message");
        if (message == null) {
            message = "";
        }
        message += "<li>" + text + "</li>";
        context.getExternalContext().getRequestMap().put("message", message);

    }


    /** <p>Clear the checked state for all customers.</p> */
    private void clear() {

        append("clear()");
        int n = data.getRowCount();
        for (int i = 0; i < n; i++) {
            data.setRowIndex(i);
            checked.setSelected(false);
        }

    }


    /** <p>Clear the created state of all customers.</p> */
    private void created() {

        append("created()");
        int n = data.getRowCount();
        for (int i = 0; i < n; i++) {
            data.setRowIndex(i);
            created.setSelected(false);
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
    private void scroll(int row) {

        int rows = data.getRows();
        if (rows < 1) {
            append("scroll(" + row + ") showing entire table already");
            return; // Showing entire table already
        }
        if (row < 0) {
            data.setFirst(0);
        } else if (row >= data.getRowCount()) {
            data.setFirst(data.getRowCount() - 1);
        } else {
            data.setFirst(row - (row % rows));
        }
        append("scroll(" + row + "), first=" + data.getFirst());

    }


}
