<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/plain"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.context.FacesContextFactory"
%><%@ page import="javax.faces.render.RenderKitFactory"
%><%@ page import="javax.faces.component.UIViewRoot"
%><%@ page import="javax.faces.lifecycle.Lifecycle"
%><%@ page import="javax.faces.lifecycle.LifecycleFactory"
%><%@ page import="javax.faces.event.PhaseId"
%><%@ page import="javax.faces.event.PhaseListener"
%><%@ page import="com.sun.faces.systest.lifecycle.ReloadPhaseListenerImpl"
%><%

// This test demonstrates the request processing lifecycle of 
// a "non-faces" request --->  faces response
// It uses the "default" renderkit to show how a renderkit can be
// set.
//
    // Create a Lifecycle
    //
    LifecycleFactory lFactory = (LifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    if (lifecycle == null) {
        out.println("/phaseListener01.jsp FAILED - Could not create Lifecycle");
        return;
    }

    // Create a FacesContext 
    //
    FacesContextFactory facesContextFactory = (FacesContextFactory)
        FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext facesContext = facesContextFactory.getFacesContext(
        config.getServletContext(), request, response, lifecycle);
    if (facesContext == null) {
        out.println("/phaseListener01.jsp FAILED - Could not create FacesContext");
        return;
    }

    // Acquire a View..
    //
    UIViewRoot view = facesContext.getApplication().getViewHandler().
        createView(facesContext, "/phaseListener01A.jsp");

    // Set the RenderKitFactory.HTML_BASIC_RENDER_KIT renderkit Id
    //
    view.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    facesContext.setViewRoot(view);

    PhaseListener phaseListener = new ReloadPhaseListenerImpl(PhaseId.ANY_PHASE);
    lifecycle.addPhaseListener(phaseListener);
    lifecycle.execute(facesContext);
    lifecycle.render(facesContext);

    String pageRefresh = System.getProperty("PageRefreshPhases");
    // All tests passed
    //
    out.println("/phaseListener01.jsp PASSED");
%>
