/*
 * $Id: ResourceInjectionBean.java,v 1.1 2005/07/29 17:42:22 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import java.io.IOException;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.sql.DataSource;

@Resource(name="myDataSource4", type=DataSource.class)
@Resources({ @Resource(name="myDataSource5", type=DataSource.class),
             @Resource(name="jdbc/myDataSource6", type=DataSource.class) })
  
public class ResourceInjectionBean {

    private @Resource DataSource ds1;
    private @Resource(name="myDataSource2") DataSource ds2;
    private DataSource ds3;
    
    @Resource(name="jdbc/myDataSource3")
    private void setDataSource(DataSource ds) {
        ds3 = ds;
    }
    
    int injectedData1 = 100;
    int injectedData2 = 100;
    int injectedData3 = 100;
    int injectedData4 = 100;
    int injectedData5 = 100;
    int injectedData6 = 100;
   
    public ResourceInjectionBean() {        
    }
    
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
}
