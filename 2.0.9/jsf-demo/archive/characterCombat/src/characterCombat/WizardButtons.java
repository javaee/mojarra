/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package characterCombat;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

import java.util.Iterator;

/**
 * <p>Backing bean for wizard style navigation.  This class provides
 * methods that you can point to from your wizard buttons that will
 * return true or false depending on the current page in the
 * application.</p>
 */
public class WizardButtons {

    /**
     * <p>Check to see whether the current page should have a back button</p>
     *
     * @return true if the current page has a "back" page.
     */
    public boolean isHasBack() {
        FacesContext
              realContext = FacesContext.getCurrentInstance(),
              copyContext = createShadowFacesContext(realContext);
        NavigationHandler nav =
              copyContext.getApplication().getNavigationHandler();
        nav.handleNavigation(copyContext, null, "back");
        return compareUIViewRoots(realContext.getViewRoot(),
                                  copyContext.getViewRoot());
    }

    /**
     * <p>Check to see whether the current page should have a next button</p>
     *
     * @return true if the current page has a "next" page.
     */
    public boolean isHasNext() {
        FacesContext
              realContext = FacesContext.getCurrentInstance(),
              copyContext = createShadowFacesContext(realContext);
        NavigationHandler nav =
              copyContext.getApplication().getNavigationHandler();
        nav.handleNavigation(copyContext, null, "next");
        return compareUIViewRoots(realContext.getViewRoot(),
                                  copyContext.getViewRoot());
    }

    /**
     * <p>Check to see whether the current page should have a finish button</p>
     *
     * @return true if the current page should have a "finish" button
     *         instead of a "next" button
     */
    public boolean isFinishPage() {
        FacesContext
              realContext = FacesContext.getCurrentInstance(),
              copyContext = createShadowFacesContext(realContext),
              nextCopyContext;
        NavigationHandler nav =
              copyContext.getApplication().getNavigationHandler();
        // get the next outcome
        nav.handleNavigation(copyContext, null, "next");
        nextCopyContext = createShadowFacesContext(copyContext);
        nav.handleNavigation(nextCopyContext, null, "next");
        return compareUIViewRoots(copyContext.getViewRoot(),
                                  nextCopyContext.getViewRoot());
    }

    /**
     * <p>Get the label for the "next" button.</p>
     *
     * @return String next button label
     */
    public String getNextLabel() {
        String result = "Next >";
        if (isFinishPage()) {
            result = "Finish";
        }
        return result;
    }

    /**
     * <p>Take two View roots and compare them.</p>
     *
     * @param one the first ViewRoot
     * @param two the second ViewRoot
     *
     * @return boolean the result of the comparison.
     */
    public boolean compareUIViewRoots(UIViewRoot one, UIViewRoot two) {
        if (null == one && null == two) {
            return true;
        }
        if (null != one && null != two) {
            if (null == one.getViewId() && null == two.getViewId()) {
                return true;
            }
            if (null != one.getViewId() && null != two.getViewId()) {
                return one.getViewId().equals(two.getViewId());
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * <p>createShadowFacesContext creates a shallow copy of the
     * argument FacesContext, but with a deep copy of the viewRoot
     * property.  This allows us to call the NavigationHandler.handleNavigaton
     * method without modifying the real FacesContext.</p>
     *
     * @param context the FacesContext to be copied
     *
     * @return FacesContext shallow copy of FacesContext
     */
    public FacesContext createShadowFacesContext(FacesContext context) {
        final FacesContext oldContext = context;

        return new FacesContext() {
            private Application application = oldContext.getApplication();

            public Application getApplication() {
                return application;
            }

            public Iterator<String> getClientIdsWithMessages() {
                return oldContext.getClientIdsWithMessages();
            }

            public ExternalContext getExternalContext() {
                return oldContext.getExternalContext();
            }

            public Severity getMaximumSeverity() {
                return oldContext.getMaximumSeverity();
            }

            public Iterator<FacesMessage> getMessages() {
                return oldContext.getMessages();
            }

            public Iterator<FacesMessage> getMessages(String clientId) {
                return oldContext.getMessages(clientId);
            }

            public RenderKit getRenderKit() {
                return oldContext.getRenderKit();
            }


            public boolean getRenderResponse() {
                return oldContext.getRenderResponse();
            }


            public boolean getResponseComplete() {
                return oldContext.getResponseComplete();
            }


            public ResponseStream getResponseStream() {
                return oldContext.getResponseStream();
            }


            public void setResponseStream(ResponseStream responseStream) {
                oldContext.setResponseStream(responseStream);
            }

            public ResponseWriter getResponseWriter() {
                return oldContext.getResponseWriter();
            }


            public void setResponseWriter(ResponseWriter responseWriter) {
                oldContext.setResponseWriter(responseWriter);
            }

            private UIViewRoot root = oldContext.getViewRoot();

            public UIViewRoot getViewRoot() {
                return root;
            }

            public void setViewRoot(UIViewRoot root) {
                this.root = root;
            }

            public void addMessage(String clientId, FacesMessage message) {
                oldContext.addMessage(clientId, message);
            }

            public void release() {
            }

            public void renderResponse() {
            }

            public ELContext getELContext() {
                return null;
            }

            public void responseComplete() {
            }
        };

    }
}
