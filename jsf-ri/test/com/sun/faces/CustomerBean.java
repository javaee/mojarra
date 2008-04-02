/*
 * $Id: CustomerBean.java,v 1.8 2006/03/29 22:39:32 rlubke Exp $
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

package com.sun.faces;


/**
 * <p>JavaBean represented the data for an individual customer.</p>
 */

public class CustomerBean implements java.io.Serializable {


    private String accountId = null;
    private String name = null;
    private String symbol = null;
    private double totalSales = 0.0;


    // ------------------------------------------------------------ Constructors


    public CustomerBean() {

        this(null, null, null, 0.0);

    }


    public CustomerBean(String accountId, String name,
                        String symbol, double totalSales) {

        System.out.println("Created CustomerBean");
        this.accountId = accountId;
        this.name = name;
        this.symbol = symbol;
        this.totalSales = totalSales;

    }


    // ---------------------------------------------------------- Public Methods


    public void setAccountId(String accountId) {

        this.accountId = accountId;

    }


    public void setName(String name) {

        this.name = name;

    }


    public void setSymbol(String symbol) {

        this.symbol = symbol;

    }


    public void setTotalSales(double totalSales) {

        this.totalSales = totalSales;

    }


    public String getAccountId() {

        return (this.accountId);

    }


    public String getName() {

        return (this.name);

    }


    public String getSymbol() {

        return (this.symbol);

    }


    public double getTotalSales() {

        return (this.totalSales);

    }


    public String toString() {

        StringBuffer sb = new StringBuffer("CustomerBean[accountId=");
        sb.append(accountId);
        sb.append(",name=");
        sb.append(name);
        sb.append(",symbol=");
        sb.append(symbol);
        sb.append(",totalSales=");
        sb.append(totalSales);
        sb.append("]");
        return (sb.toString());

    }

}
