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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
