/*
 * $Id: CustomerBean.java,v 1.10 2007/04/27 22:02:02 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces;


/**
 * <p>JavaBean represented the data for an individual customer.</p>
 */

public class CustomerBean implements java.io.Serializable {


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
