/*
 * $Id: FacesLogger.java,v 1.5 2008/02/14 00:32:11 rlubke Exp $
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

package com.sun.faces.util;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * <p>
 * An <code>enum</code> of all application <code>Logger</code>s.
 * </p>
 */
public enum FacesLogger {

    APPLICATION("application"),
    APPLICATION_VIEW("application.view"),
    RESOURCE("resource"),
    CONFIG("config"),
    CONTEXT("context"),
    FACELETS_COMPILER("facelets.compiler"),
    FACELETS_COMPONENT("facelets.tag.component"),
    FACELETS_META("facelets.tag.meta"),
    FACELETS_COMPOSITION("facelets.tag.ui.composition"),
    FACELETS_DECORATE("facelets.tag.ui.decorate"),
    FACELETS_INCLUDE("facelets.tag.ui.include"),
    FACELETS_FACELET("faclets.facelet"),
    FACELETS_FACTORY("facelets.factory"),
    LIFECYCLE("lifecycle"),
    MANAGEDBEAN("managedbean"),
    RENDERKIT("renderkit"),
    TAGLIB("taglib"),
    TIMING("timing"),
    UTIL("util");

    private static final String LOGGER_RESOURCES
          = "com.sun.faces.LogStrings";
    public static final String FACES_LOGGER_NAME_PREFIX
          = "javax.enterprise.resource.webcontainer.jsf.";

    private String loggerName;

    private FacesLogger(String loggerName) {

        this.loggerName = loggerName;

    }

    public Logger getLogger() {

        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        ResourceBundle bundle = ResourceBundle.getBundle(LOGGER_RESOURCES,
                                                         Locale.getDefault(),
                                                         getCurrentLoader());
        return new ResourceBundleLogger(
              className,
              Logger.getLogger(FACES_LOGGER_NAME_PREFIX + loggerName),
              bundle
        );

    }


    // ---------------------------------------------------------- Private Methods

    
    private static ClassLoader getCurrentLoader() {

        ClassLoader loader =
              Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = FacesLogger.class.getClassLoader();
        }
        return loader;
        
    }


    // ---------------------------------------------------------- Nested Classes


    private static final class ResourceBundleLogger extends Logger {

        private Logger delegate;
        private ResourceBundle bundle;
        private String className;


        // -------------------------------------------------------- Constructors


        protected ResourceBundleLogger(String className,
                                       Logger delegate,
                                       ResourceBundle bundle) {
            super(null, null);
            this.delegate = delegate;
            this.bundle = bundle;
            this.className = className;
            //this way all messages are passed to the delegate, which will do
            // appropriate filtering to avoid creating unnecessary LogRecords,
            // all logging should be preceded by a call to isLoggable(level)
            super.setLevel(Level.ALL);
        }


        // -------------------------------------- Overridden methods from Logger


        @Override
        public ResourceBundle getResourceBundle() {
            return this.bundle;
        }

        @Override
        public String getResourceBundleName() {
            return LOGGER_RESOURCES;
        }

        @Override
        public void log(LogRecord record) {
            record.setSourceClassName(className);
            record.setResourceBundle(getResourceBundle());
            this.delegate.log(record);
        }


        // ----------------------------------------------  Pure Delegate Methods


        @Override
        public void addHandler(Handler handler) throws SecurityException {
            this.delegate.addHandler(handler);
        }

        @Override
        public Filter getFilter() {
            return this.delegate.getFilter();
        }

        @Override
        public Handler[] getHandlers() {
            return this.delegate.getHandlers();
        }

        @Override
        public Level getLevel() {
            return this.delegate.getLevel();
        }

        @Override
        public String getName() {
            return this.delegate.getName();
        }

        @Override
        public Logger getParent() {
            return this.delegate.getParent();
        }

        @Override
        public boolean getUseParentHandlers() {
            return this.delegate.getUseParentHandlers();
        }

        @Override
        public boolean isLoggable(Level level) {
            return this.delegate.isLoggable(level);
        }

        @Override
        public void removeHandler(Handler handler) throws SecurityException {
            this.delegate.removeHandler(handler);
        }

        @Override
        public void setFilter(Filter newFilter) throws SecurityException {
            this.delegate.setFilter(newFilter);
        }

        @Override
        public void setLevel(Level newLevel) throws SecurityException {
            this.delegate.setLevel(newLevel);
        }

        @Override
        public void setParent(Logger parent) {
            this.delegate.setParent(parent);
        }

        @Override
        public void setUseParentHandlers(boolean useParentHandlers) {
            this.delegate.setUseParentHandlers(useParentHandlers);
        }

    } // END ResourceBundleLogger

}
