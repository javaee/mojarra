/*
 * $Id: ResourceInjectionBean.java,v 1.3 2006/03/29 22:38:53 rlubke Exp $
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

package com.sun.faces.systest.model;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@Resource(name = "myDataSource4", type = DataSource.class)
@Resources({@Resource(name = "myDataSource5", type = DataSource.class),
@Resource(name = "jdbc/myDataSource6", type = DataSource.class)})

public class ResourceInjectionBean {


    int injectedData1 = 100;
    int injectedData2 = 100;
    int injectedData3 = 100;
    int injectedData4 = 100;
    int injectedData5 = 100;
    int injectedData6 = 100;

    private @Resource DataSource ds1;
    private @Resource(name = "myDataSource2") DataSource ds2;
    private DataSource ds3;

    // ------------------------------------------------------------ Constructors


    public ResourceInjectionBean() {
    }

    // ---------------------------------------------------------- Public Methods


    public int getInjectedData1() {

        initializeData();
        return injectedData1;

    }


    public int getInjectedData2() {

        initializeData();
        return injectedData2;

    }


    public int getInjectedData3() {

        initializeData();
        return injectedData3;

    }


    public int getInjectedData4() {

        initializeData();
        return injectedData4;

    }


    public int getInjectedData5() {

        initializeData();
        return injectedData5;

    }


    public int getInjectedData6() {

        initializeData();
        return injectedData6;

    }


    public void initializeData() {

        try {

            injectedData1 = ds1.getLoginTimeout();
            injectedData2 = ds2.getLoginTimeout();
            injectedData3 = ds3.getLoginTimeout();

            InitialContext ic = new InitialContext();

            DataSource ds4 = (DataSource)
                  ic.lookup("java:comp/env/myDataSource4");
            injectedData4 = ds4.getLoginTimeout();
            System.out.println("timeout for 4" + ds4.getLoginTimeout());
            DataSource ds5 = (DataSource)
                  ic.lookup("java:comp/env/myDataSource5");
            injectedData5 = ds5.getLoginTimeout();

            DataSource ds6 = (DataSource)
                  ic.lookup("java:comp/env/jdbc/myDataSource6");
            injectedData6 = ds6.getLoginTimeout();

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    // --------------------------------------------------------- Private Methods


    @Resource(name = "jdbc/myDataSource3")
    private void setDataSource(DataSource ds) {

        ds3 = ds;

    }

}
