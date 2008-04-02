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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.lifecycle;

import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.el.FacesResourceBundleELResolver;
import com.sun.faces.el.ImplicitObjectELResolverForJsp;
import com.sun.faces.el.ManagedBeanELResolver;
import com.sun.faces.el.PropertyResolverChainWrapper;
import com.sun.faces.el.VariableResolverChainWrapper;
import com.sun.faces.util.Util;

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

    private static Logger LOGGER = Util.getLogger(Util.FACES_LOGGER
                                                  + Util.LIFECYCLE_LOGGER);

    boolean preInitCompleted;
    boolean postInitCompleted;

    /**
     * <p>Handle a notification that the processing for a particular
     * phase has just been completed.</p>
     * 
     * <p>When invoked, this phase listener will remove itself
     * as a registered <code>PhaseListener</code> with all
     * <code>Lifecycle</code> instances.
     */
    public synchronized void afterPhase(PhaseEvent event) {
        if (!postInitCompleted) {
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
            populateFacesELResolverForJsp(event.getFacesContext());
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
        CompositeELResolver compositeELResolverForJsp =
              appAssociate.getFacesELResolverForJsp();
        if (compositeELResolverForJsp == null) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                           "jsf.lifecycle.initphaselistener.resolvers_not_registered",
                           new Object[] { appAssociate.getContextName() });
            }
            return;
        }
        compositeELResolverForJsp.add(new ImplicitObjectELResolverForJsp());
        compositeELResolverForJsp.add(new ManagedBeanELResolver());
        compositeELResolverForJsp.add(new FacesResourceBundleELResolver());

        // add ELResolvers from faces-config.xml
        List<ELResolver> elResolversFromFacesConfig =
              appAssociate.getELResolversFromFacesConfig();
        if (elResolversFromFacesConfig != null) {
            for (ELResolver resolver : elResolversFromFacesConfig) {
                compositeELResolverForJsp.add(resolver);
            }
        }

        // register legacy VariableResolver if any.
        if (appAssociate.getLegacyVariableResolver() != null) {
            compositeELResolverForJsp.add(new VariableResolverChainWrapper(
                  appAssociate.getLegacyVariableResolver()));
        } else if (appAssociate.getLegacyVRChainHead() != null) {
            compositeELResolverForJsp.add(new VariableResolverChainWrapper(
                  appAssociate.getLegacyVRChainHead()));
        }

        // add legacy PropertyResolvers if any
        if (appAssociate.getLegacyPropertyResolver() != null) {
            compositeELResolverForJsp.add(new PropertyResolverChainWrapper(
                  appAssociate.getLegacyPropertyResolver()));
        } else if (appAssociate.getLegacyPRChainHead() != null) {
            compositeELResolverForJsp.add(new PropertyResolverChainWrapper(
                  appAssociate.getLegacyPRChainHead()));
        }

        // add ELResolvers added via Application.addELResolver()
        List<ELResolver> elResolversFromApplication =
              appAssociate.getApplicationELResolvers();
        if (elResolversFromApplication != null) {
            for (ELResolver resolver : elResolversFromApplication) {
                compositeELResolverForJsp.add(resolver);
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.FINE,
                       "jsf.lifecycle.initphaselistener.resolvers_registered",
                       new Object[] { appAssociate.getContextName() });
        }
    }

} // END InitializingPhaseListener