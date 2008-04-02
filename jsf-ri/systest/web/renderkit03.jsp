<%@ page contentType="text/plain"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.context.FacesContextFactory"
%><%@ page import="javax.faces.lifecycle.Lifecycle"
%><%@ page import="javax.faces.lifecycle.LifecycleFactory"
%><%@ page import="javax.faces.tree.Tree"
%><%@ page import="javax.faces.tree.TreeFactory"
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

    // Acquire a tree..
    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Tree requestTree = treeFactory.getTree(facesContext, "/renderkit03A.jsp");

    // Set the "DEFAULT" renderkit Id
    requestTree.setRenderKitId("CUSTOM");

    // Set the tree
    facesContext.setTree(requestTree);
    
    facesContext.renderResponse();

    lifecycle.execute(facesContext);

    // All tests passed
    out.println("/renderkit03.jsp PASSED");
%>
