/*
 * $Id: CustomerBean.java,v 1.2 2003/10/17 03:53:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;


import java.io.Serializable;


/**
 * <p>JavaBean represented the data for an individual customer.</p>
 *
 * @version $Id: CustomerBean.java,v 1.2 2003/10/17 03:53:46 eburns Exp $
 */

public class CustomerBean implements Serializable {


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


    private String accountId = null;

    public String getAccountId() {
        return (this.accountId);
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


    private String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        System.err.println("setName(" + accountId + "," + this.name +
                           "," + name);
        this.name = name;
    }


    private String symbol = null;

    public String getSymbol() {
        return (this.symbol);
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    private double totalSales = 0.0;

    public double getTotalSales() {
        return (this.totalSales);
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
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
