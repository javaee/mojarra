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

package com.sun.faces.lifecycle;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.el.ELUtils;

import com.sun.faces.el.FacesCompositeELResolver;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.Application;

/**
 * <p>This class is used to register the JSF <code>ELResolver</code>
 * stack with the JSP container.</p>
 *
 * <p>We overload it a bit to set a bit on the ApplicationAssociate
 * stating we've processed a request to indicate the appliation is fully
 * initialized.</p>
 * 
 * <p>After the first request, this <code>PhaseListener</code> will remove
 * itself from all registered lifecycle instances registered with the 
 * application.</p>
 * @since 1.2
 */
public class ELResolverInitPhaseListener implements PhaseListener {


    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();
    private boolean postInitCompleted;

    private boolean preInitCompleted;

    // ---------------------------------------------- Methods From PhaseListener

    /**
     * <p>Handle a notification that the processing for a particular
     * phase has just been completed.</p>
     *
     * <p>When invoked, this phase listener will remove itself
     * as a registered <code>PhaseListener</code> with all
     * <code>Lifecycle</code> instances.
     */
    public synchronized void afterPhase(PhaseEvent event) {

        if (!postInitCompleted && PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
            ApplicationAssociate associate =
                 ApplicationAssociate.getInstance(
                      event.getFacesContext().getExternalContext());
            associate.setRequestServiced();
            LifecycleFactory factory = (LifecycleFactory)
                  FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            // remove ourselves from the list of listeners maintained by
            // the lifecycle instances
            for(Iterator<String> i = factory.getLifecycleIds(); i.hasNext(); ) {
                Lifecycle lifecycle = factory.getLifecycle(i.next());
                lifecycle.removePhaseListener(this);
            }
            postInitCompleted = true;
        }

    }


    /**
     * <p>Handle a notification that the processing for a particular
     * phase of the request processing lifecycle is about to begin.</p>
     *
     * <p>The implementation of this method currently calls through to
     * {@link #populateFacesELResolverForJsp(javax.faces.context.FacesContext)}.<p/>
     */

    public synchronized void beforePhase(PhaseEvent event) {

        if (!preInitCompleted) {
            ApplicationAssociate associate =
                 ApplicationAssociate.getInstance(
                      FacesContext.getCurrentInstance().getExternalContext());
            associate.setRequestServiced();
            associate.initializeELResolverChains();
            associate.installProgrammaticallyAddedResolvers();
            preInitCompleted = true;

        }

    }


    /**
     * <p>Return the identifier of the request processing phase during
     * which this listener is interested in processing {@link javax.faces.event.PhaseEvent}
     * events.  Legal values are the singleton instances defined by the
     * {@link javax.faces.event.PhaseId} class, including <code>PhaseId.ANY_PHASE</code>
     * to indicate an interest in being notified for all standard phases.</p>
     *
     * <p>We return <code>PhaseId.ANY_PHASE</code>.
     */
    public PhaseId getPhaseId() {

        return PhaseId.ANY_PHASE;

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * Populate the FacesCompositeELResolver stack registered with JSP
     * if a request is being processed for the very first time. At the
     * application initialiazation time, an empty CompositeELResolver is
     * registered with JSP because ELResolvers can be added until the first
     * request is serviced.
     *
     * @param context - the <code>FacesContext</code> for the current request
     */
    protected void populateFacesELResolverForJsp(FacesContext context) {

        ApplicationAssociate appAssociate =
              ApplicationAssociate.getInstance(context.getExternalContext());
        populateFacesELResolverForJsp(context.getApplication(), appAssociate);

    }

    public static void populateFacesELResolverForJsp(Application app,
            ApplicationAssociate appAssociate) {
        FacesCompositeELResolver compositeELResolverForJsp =
              appAssociate.getFacesELResolverForJsp();
        if (compositeELResolverForJsp == null) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                           "jsf.lifecycle.initphaselistener.resolvers_not_registered",
                           new Object[] { appAssociate.getContextName() });
            }
            return;
        }

        ELUtils.buildJSPResolver(compositeELResolverForJsp, appAssociate);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       "jsf.lifecycle.initphaselistener.resolvers_registered",
                       new Object[] { appAssociate.getContextName() });
        }

    }

    public static void removeELResolverInitPhaseListener() {
        LifecycleFactory factory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        // remove ourselves from the list of listeners maintained by
        // the lifecycle instances
        for (Iterator<String> i = factory.getLifecycleIds(); i.hasNext();) {
            Lifecycle lifecycle = factory.getLifecycle(i.next());
            for (PhaseListener cur : lifecycle.getPhaseListeners()) {
                if (cur instanceof ELResolverInitPhaseListener) {
                    lifecycle.removePhaseListener(cur);
                }
            }
        }

    }

} // END InitializingPhaseListener
