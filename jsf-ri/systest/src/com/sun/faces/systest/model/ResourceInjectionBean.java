/*
 * $Id: ResourceInjectionBean.java,v 1.5 2007/04/27 22:01:13 ofung Exp $
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
