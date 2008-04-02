/*
 * $Id: DataModelBean.java,v 1.2 2003/10/17 03:53:46 eburns Exp $
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

package standard;


import java.util.ArrayList;
import java.util.List;
import javax.faces.application.Action;
import javax.faces.component.UISelectOne;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;


/**
 * <p>Backing file class for <code>DataModel.jsp</code>.</p>
 */

public class DataModelBean {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Initialize the select component's value so that the first customer
     * is marked as selected the very first time we execute.</p>
     */
    public DataModelBean() {

        // System.err.println("DataModelBean:  precreating select component");
        accountId = new UISelectOne();
        accountId.setId("accountId");
        accountId.setValue("0");

    }


    // -------------------------------------------------------- Bound Components


    /**
     * <p>The <code>accountId</code> field for the current row.</p>
     */
    private UISelectOne accountId = null;
    public UISelectOne getAccountId() {
        // System.err.println("getAccountId(): returning " + accountId);
        return accountId;
    }
    public void setAccountId(UISelectOne accountId) {
        // System.err.println("setAccountId(): setting " + accountId);
        this.accountId = accountId;
    }


    // -------------------------------------------------------------- Properties


    private List accountIds = null;


    /**
     * <p>Return an array of <code>SelectItem</code>s representing the
     * account identifiers of all our customers.</p>
     */
    public List getAccountIds() {
        if (accountIds == null) {
            // System.err.println("getAccountIds(): creating list");
            getCustomers();
            accountIds = new ArrayList();
            int n = list.size();
            for (int i = 0; i < n; i++) {
                accountIds.add(new SelectItem
                               ("" + i,
                                ((CustomerBean) list.get(i)).getAccountId(),
                                null));
            }
        }
        // System.err.println("getAccountIds(): returning list");
        return (accountIds);
    }


    private DataModel customers = null;
    private List list = null;


    /**
     * <p>Return a <code>DataModel</code> containing our customer list.</p>
     */
    public DataModel getCustomers() {
        if (list == null ) {
            // System.err.println("getCustomers(): creating list");
            list = new ArrayList();
            list.add(new CustomerBean
                     ("123456", "Alpha Beta Company", "ABC", 1234.56));
            list.add(new CustomerBean
                     ("445566", "General Services, Ltd.", "GS", 33.33));
            list.add(new CustomerBean
                     ("654321", "Summa Cum Laude, Inc.", "SCL", 76543.21));
            list.add(new CustomerBean
                     ("333333", "Yabba Dabba Doo", "YDD",  333.33));
            for (int i = 10; i < 20; i++) {
                list.add(new CustomerBean("8888" + i,
                                          "Customer " + i,
                                          "CU" + i,
                                          ((double) i) * 10.0));
            }
        }
        if (customers == null) {
            // System.err.println("getCustomers(): creating DataModel");
            customers = new ListDataModel(list);
        }
        // System.err.println("getCustomers(): returning DataModel");
        return (customers);
    }


    // ---------------------------------------------------------- Event Handlers


    /**
     * <p>Select the customer whose account id was specified.</p>
     */
    private String select() {
        String value = (String) getAccountId().getValue();
        // System.err.println("select(" + value + ")");
        int rowIndex = Integer.parseInt(value);
        // System.err.println("setting rowIndex to " + rowIndex);
        getCustomers().setRowIndex(rowIndex);
        return (null); // Stay on the same page
    }


    // --------------------------------------------------------- Private Methods


    // ------------------------------------------------------- Action Properties


    public Action getSelect() {
	return new Action() {
		public String invoke() {
		    return (select());
		}
	    };
    }


}
