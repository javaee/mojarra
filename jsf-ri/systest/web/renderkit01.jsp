<%@ page contentType="text/plain"
%><%@ page import="java.util.Iterator"
%><%@ page import="java.util.Map"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.render.RenderKit"
%><%@ page import="javax.faces.render.RenderKitFactory"
%><%@ page import="javax.faces.render.Renderer"
%><%

// This test goes through the config system to test the loading of 
// the default renderkit information as well as a custom renderkit
// consisting of one renderer.

    // Initialize list of Renderer types
    //
    String defaultList[] = {
        "Button","Checkbox","Data","Date","DateTime","Errors",
        "Form","Grid","Group","Hidden","Hyperlink","Image",
        "Label","List","Listbox","Menu","Message","Number",
        "Radio","Secret","SelectManyCheckbox","Textarea",
        "Text","Time"};
    String customList[] = {"Text"};

    // Acquire RenderKits and check RenderKitId(s)
    //
    RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    Iterator renderKitIds = renderKitFactory.getRenderKitIds();

    boolean foundDefault = false; 
    boolean foundCustom= false; 
    while (renderKitIds.hasNext()) {
        String renderKitId = (String)renderKitIds.next();
        if (renderKitId.equals("DEFAULT")) {
            foundDefault = true;
        } else if (renderKitId.equals("CUSTOM")) {
            foundCustom = true;
        }
    }
    if (!foundDefault || !foundCustom) {
        out.println("/renderkit01.jsp FAILED - all renderkit ids not found");
	return;
    }

    // Check Renderers For Each RenderKit
    //
    while (renderKitIds.hasNext()) {
        String renderKitId = (String)renderKitIds.next();
	RenderKit rKit = renderKitFactory.getRenderKit(renderKitId);
	if (rKit == null) {
	    out.println("/renderkit01.jsp FAILED - renderkit not found");
	    return;
	}
	Renderer renderer = null;
	if (renderKitId.equals("DEFAULT")) {
	    for (int i=0; i<defaultList.length; i++) {
	        try {
	            renderer = rKit.getRenderer(defaultList[i]);
	        } catch (IllegalArgumentException ia) {
	            out.println("/renderkit01.jsp FAILED - renderer not found for type:"+
		        defaultList[i]+" in renderkit 'DEFAULT'");
		    return;
	        }
	    }
	} else if (renderKitId.equals("CUSTOM")) {
	    for (int i=0; i<customList.length; i++) {
	        try {
	            renderer = rKit.getRenderer(customList[i]);
	        } catch (IllegalArgumentException ia) {
	            out.println("/renderkit01.jsp FAILED - renderer not found for type:"+
		        customList[i]+" in renderkit 'CUSTOM'");
		    return;
	        }
	    }
	}
    }

    // All tests passed
    //
    out.println("/renderkit01.jsp PASSED");
%>
