/* 
 * $Id: FocusHTMLRenderer.java,v 1.1.2.1 2008/04/10 16:56:49 driscoll Exp $
 */
package com.sun.faces.ext.render;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * Renderer class that emits HTML and JavaScript to set the focus to a given field.
 * 
 * @author driscoll
 */
public class FocusHTMLRenderer extends Renderer {
    
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        String forID = (String) component.getAttributes().get("for");
        ResponseWriter writer = context.getResponseWriter();
        // XXX - I'd still like to get the parentID, but need to add a check if it's a form or not...
        //UIComponent parentComponent = component.getParent();
        //String parentID = parentComponent.getClientId(context);
        //String targetID = parentID+":"+forID;
        String targetID = forID;
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("setFocus('",null);
        writer.writeText(targetID, null);
        writer.writeText("');\n",null);
        writer.writeText("function setFocus(elementId) { var element = " +
                "document.getElementById(elementId); if (element && element.focus) " +
                "{ element.focus(); } }",null);
        writer.endElement("script");        
    }

}
