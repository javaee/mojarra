<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<%@ page contentType="text/html"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.context.FacesContextFactory"
%><%@ page import="javax.faces.component.UIViewRoot"
%><%@ page import="javax.faces.lifecycle.Lifecycle"
%><%@ page import="javax.faces.render.RenderKitFactory"
%><%@ page import="javax.faces.lifecycle.LifecycleFactory"
%><%

// This test demonstrates the request processing lifecycle of
// a "non-faces" request --->  faces response
// It uses a "custom" renderkit to show how a renderkit can be
// set.
//
    // Create a Lifecycle
    LifecycleFactory lFactory = (LifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    if (lifecycle == null) {
        out.println("/renderkit03.jsp FAILED - Could not create Lifecycle");
        return;
    }

    // Create a FacesContext 
    FacesContextFactory facesContextFactory = (FacesContextFactory)
        FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext facesContext = facesContextFactory.getFacesContext(
        config.getServletContext(), request, response, lifecycle);
    if (facesContext == null) {
        out.println("/renderkit03.jsp FAILED - Could not create FacesContext");
        return;
    }

    // Acquire a view
    UIViewRoot view = facesContext.getApplication().getViewHandler().restoreView(facesContext, "/renderkit03A.jsp");
    if ( view == null)  {
        view = facesContext.getApplication().getViewHandler().createView(facesContext, "/renderkit03A.jsp");
    }
    // Set the RenderKitFactory.HTML_BASIC_RENDER_KIT renderkit Id
    view.setRenderKitId("CUSTOM");
    facesContext.setViewRoot(view);
    facesContext.renderResponse();

    lifecycle.execute(facesContext);
    lifecycle.render(facesContext);

    // All tests passed
    out.println("/renderkit03.jsp PASSED");
%>
