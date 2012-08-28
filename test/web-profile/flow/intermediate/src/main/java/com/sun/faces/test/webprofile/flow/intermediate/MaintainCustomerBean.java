/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.webprofile.flow.intermediate;

import java.io.Serializable;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.faces.flow.FlowHandler;
import javax.inject.Named;

@Named
@FlowScoped(id="maintain-customer-record")
public class MaintainCustomerBean implements Serializable
{
   public MaintainCustomerBean() {
      super();
   }
   
   public String createCustomer() {
       //  Logic to create a new customer object.
       FacesContext context = FacesContext.getCurrentInstance();
       CustomerBean customer = new CustomerBean();
       FlowHandler flowHandler = context.getApplication().getFlowHandler();
       Map<Object, Object> flowScope = flowHandler.getCurrentFlowScope();
       if (null == flowScope) {
           throw new IllegalStateException("Must have a flow handler when called from within initializer!");
       }
       flowScope.put("customerId", customer.getCustomerId());
       flowScope.put("customerIdValue", customer);
       return "router1";
   }
   
   public String fetchCustomer() {
      //  Logic to fetch a customer.
      return "success";
   }
   
   public void initializeFlow() {
       FacesContext context = FacesContext.getCurrentInstance();
       Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
       requestMap.put("initializerMessage", "Initializer called");

   }
   
   public void cleanUpFlow() {
       Map<String,Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
       requestMap.put("finalizerMessage", "Finalizer called");
   }
}
