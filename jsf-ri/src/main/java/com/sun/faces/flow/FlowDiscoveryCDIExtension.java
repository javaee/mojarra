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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowDefinition;

/*
 *  This is the hook into the bootstrapping of the entire feature.  
 *
 *  Use the CDI anntation scanning feature to find all beans annotated
 *  with @FlowDefinition.  The scanning work done here is leveraged
 *  in FlowDiscoveryCDIHelper.discoverFlows().
 *
 *  Use BeforeBeanDiscovery to manually register an application scoped
 *  bean FlowDiscoveryCDIHelper.  I would rather not do this, but I
 *  couldn't get the system to find this bean in any other way.  I think
 *  this may have something to do with CDI being told not to scan within
 *  javax.faces.jar, or something like that.
 *
 *  Use AfterBeanDiscovery to add a custom Context.  This is necessary
 *  because it was the only way I found that actually worked that let me
 *  pass in the results of the scanning into something I could invoke in
 *  when processing the JSF PostConstructApplicationEvent.
 *
 *  Use ProcessBean to build up a tuple for each bean annotated with
 *  @FlowDefinition { definingClass, flow-id, defining-document-id }.  A
 *  data structure containing all the tuples is passed to
 *  FlowDiscoveryCDIContext.
 * 
 */

public class FlowDiscoveryCDIExtension implements Extension {


    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.FLOW.getLogger();
    private List<Producer<Flow>> flowProducers;
    
    public FlowDiscoveryCDIExtension() {
        flowProducers = new CopyOnWriteArrayList<Producer<Flow>>();
        
    }
    
    public List<Producer<Flow>> getFlowProducers() {
        return flowProducers;
    }
    
    void beforeBeanDiscovery(@Observes final BeforeBeanDiscovery event, BeanManager beanManager) {
        AnnotatedType flowDiscoveryHelper = beanManager.createAnnotatedType(FlowDiscoveryCDIHelper.class);
        event.addAnnotatedType(flowDiscoveryHelper);
        
    }
    
    <T> void findFlowDefiners(@Observes ProcessProducer<T, Flow> pp) {
    	if (pp.getAnnotatedMember().isAnnotationPresent(FlowDefinition.class)) {
            flowProducers.add(pp.getProducer());
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Discovered Flow Producer {0}", pp.getProducer().toString());
            }

    	}
    }
    
    
}
