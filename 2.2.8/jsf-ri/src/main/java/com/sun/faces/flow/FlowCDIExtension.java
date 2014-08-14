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


package com.sun.faces.flow;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.util.cdi11.CDIUtil;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.faces.flow.FlowScoped;

public class FlowCDIExtension implements Extension {


   private Map<Contextual<?>, FlowCDIContext.FlowBeanInfo> flowScopedBeanFlowIds;
   private boolean isCdiOneOneOrGreater = false;
   private CDIUtil cdiUtil = null;

   private static final Logger LOGGER = FacesLogger.FLOW.getLogger();

   public FlowCDIExtension() {
       flowScopedBeanFlowIds = new ConcurrentHashMap<Contextual<?>, FlowCDIContext.FlowBeanInfo>();
       isCdiOneOneOrGreater = Util.isCdiOneOneOrGreater();
   }
   
   public void processBean(@Observes ProcessBean<?> event) {
       FlowScoped flowScoped = event.getAnnotated().getAnnotation(FlowScoped.class);
       if (null != flowScoped) {
           FlowCDIContext.FlowBeanInfo fbi = new FlowCDIContext.FlowBeanInfo();
           fbi.definingDocumentId = flowScoped.definingDocumentId();
           fbi.id = flowScoped.value();
           flowScopedBeanFlowIds.put(event.getBean(), fbi);
       }
   }
   
   public void beforeBeanDiscovery(@Observes final BeforeBeanDiscovery event, BeanManager beanManager) {
       event.addScope(FlowScoped.class, true, true);
   }

   void afterBeanDiscovery(@Observes final AfterBeanDiscovery event, BeanManager beanManager) {
       event.addContext(new FlowCDIContext(flowScopedBeanFlowIds));
       flowScopedBeanFlowIds.clear();
       
       if (isCdiOneOneOrGreater) {
           Class clazz = null;
           try {
               clazz = Class.forName("com.sun.faces.flow.FlowCDIEventFireHelperImpl");
           } catch (ClassNotFoundException ex) {
               if (LOGGER.isLoggable(Level.SEVERE)) {
                   LOGGER.log(Level.SEVERE, "CDI 1.1 events not enabled", ex);
               }
               return;
           }
           if (null == cdiUtil){
               ServiceLoader<CDIUtil> oneCdiUtil = ServiceLoader.load(CDIUtil.class);
               for (CDIUtil oneAndOnly : oneCdiUtil) {
                   if (null != cdiUtil) {
                       String message = "Must only have one implementation of CDIUtil available";
                       if (LOGGER.isLoggable(Level.SEVERE)) {
                           LOGGER.log(Level.SEVERE, message);
                       }
                       throw new IllegalStateException(message);
                   }
                   cdiUtil = oneAndOnly;
               }
               
           }
           if (null != cdiUtil) {
               Bean bean = cdiUtil.createHelperBean(beanManager, clazz);
               event.addBean(bean);
           } else if (LOGGER.isLoggable(Level.SEVERE)) {
               LOGGER.log(Level.SEVERE, "Unable to obtain CDI 1.1 utilities for Mojarra");
           }
           
       }
       
   }
   
}
