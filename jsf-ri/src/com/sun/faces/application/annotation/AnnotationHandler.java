/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.application.annotation;

import java.util.Set;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.application.FacesAnnotationHandler;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ApplicationPostConstructEvent;
import javax.faces.FacesException;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.util.FacesLogger;

/**
 * <p>
 * Default implementation of {@link FacesAnnotationHandler}.  Note that this
 * implementation does not perform any search for annotated classes, but instead
 * relies on the scan results being pushed to the <code>ServletContext</code>
 * by {@link com.sun.faces.config.ConfigManager}.
 * </p>
 */
public class AnnotationHandler extends FacesAnnotationHandler implements SystemEventListener {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    /**
     * Key under which a Set of annotated classes names will be stored within
     * the <code>ServletContext</code>.
     */
    public static final String ANNOTATIONS_SCAN_TASK_KEY =
          AnnotationHandler.class.getName() + "_ANNOTATION_SCAN_TASK";

    /**
     * <p>
     * The annotated classes.
     * </p>
     */
    private Set<String> annotatedClasses;


    /**
     * Flag indicating the application has been initialized.  When this flag
     * is <code>true</code>, calls to {@link #processAnnotatedClasses(javax.faces.context.FacesContext, java.util.Set)}
     * will be ignored.
     */
    private boolean applicationInitialized;


    /**
     * The <code>Application</code> instance for this web application.
     */
    private Application application;


    // ------------------------------------------------------------ Constructors


    public AnnotationHandler() {

        application = FacesContext.getCurrentInstance().getApplication();
        application.subscribeToEvent(ApplicationPostConstructEvent.class,
                                     Application.class,
                                     this);

    }



    // ------------------------------------- Methods from FacesAnnotationHandler


    /**
     * @see javax.faces.application.FacesAnnotationHandler#getClassNamesWithFacesAnnotations(javax.faces.context.FacesContext)
     */
    public Set<String> getClassNamesWithFacesAnnotations(FacesContext context) {

        if (annotatedClasses == null) {
            annotatedClasses = getAnnotatedClasses(context);
        }
        return annotatedClasses;

    }


    /**
     * @see javax.faces.application.FacesAnnotationHandler#processAnnotatedClasses(javax.faces.context.FacesContext, java.util.Set)
     */
    public void processAnnotatedClasses(FacesContext context,
                                        Set<String> annotatedClassnames) {

        if (!applicationInitialized) {
            if (annotatedClassnames == null || !annotatedClassnames.isEmpty()) {
                ApplicationAssociate associate =
                      ApplicationAssociate
                            .getInstance(context.getExternalContext());
                if (associate != null) {
                    AnnotationManager manager =
                          associate.getAnnotationManager();
                    assert (manager != null);
                    manager.applyConfigAnntations(context, annotatedClassnames);
                } else {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("No ApplicationAssociate available.");
                    }
                }
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("No JSF annotated artifacts provided.");
                }
            }
        } else {
            // PENDING i18n
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Application has been initialized.  Calls to AnnotationHandler.processAnnotatedClasses() will be ignored.");
            }
        }

    }


    // ---------------------------------------- Methods from SystemEventListener


    /**
     * @see javax.faces.event.SystemEventListener#processEvent(javax.faces.event.SystemEvent)
     */
    public void processEvent(SystemEvent event) throws AbortProcessingException {

        applicationInitialized = true;
        application.unsubscribeFromEvent(ApplicationPostConstructEvent.class,
                                         Application.class,
                                         this);
        
    }


    /**
     * @see javax.faces.event.SystemEventListener#isListenerForSource(Object)
     */
    public boolean isListenerForSource(Object source) {

        return (source instanceof Application);

    }


    // --------------------------------------------------------- Private Methods


    private Set<String> getAnnotatedClasses(FacesContext ctx) {

        Map<String,Object> appMap = ctx.getExternalContext().getApplicationMap();
        //noinspection unchecked
        Future<Set<String>> scanTask = (Future<Set<String>>) appMap.remove(ANNOTATIONS_SCAN_TASK_KEY);
        try {
        return ((scanTask != null) ? scanTask.get() : Collections.<String>emptySet());
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }
    
}
