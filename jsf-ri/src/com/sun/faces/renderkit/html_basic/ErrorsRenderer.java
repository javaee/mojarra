/*
 * $Id: ErrorsRenderer.java,v 1.21 2003/08/28 15:52:33 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ErrorsRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.util.Assert;

import javax.faces.application.Message;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * <p><B>ErrorsRenderer</B> handles rendering for the Output_ErrorsTag<p>. 
 *
 * @version $Id: ErrorsRenderer.java,v 1.21 2003/08/28 15:52:33 rlubke Exp $*
 */

public class ErrorsRenderer extends HtmlBasicRenderer {
    //
    // Prviate/Protected Constants
    //
    private static final Log log = LogFactory.getLog(ErrorsRenderer.class);
   
    
    /**
     * <p>Recursively searches for {@link NamingContainer}s from the
     * given start point looking for the component with the <code>id</code>
     * specified by <code>forComponent</code>.
     * @param startPoint - the starting point in which to begin the search
     * @param forComponent - the component to search for
     * @return the component with the the <code>id</code that matches 
     *         <code>forComponent</code> otheriwse null if no match is found.
     */
    private UIComponent findUIComponentBelow(UIComponent startPoint, String forComponent) {        
        UIComponent retComp = null;
        List children = startPoint.getChildren();
        for (int i = 0, size = children.size(); i < size; i++) {
            UIComponent comp = (UIComponent) children.get(i);
            
            if (comp instanceof NamingContainer) {
                retComp = ((NamingContainer) comp).findComponentInNamespace(forComponent);    
            }
            
            if (retComp == null) {
                if (comp.getChildren().size() > 0) {                
                    retComp = findUIComponentBelow(comp, forComponent);
                }  
            }                        
            
            if (retComp != null)
                break;
        }
        return retComp;
    }

    
    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
        Iterator messageIter = null;        
        Message curMessage = null;
        ResponseWriter writer = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
        // Attempt to use the "for" attribute to locate 
        // messages.  Threee possible scenarios here:
        // 1. valid "for" attribute - messages returned
        //    for valid component identified by "for" expression.
        // 2. zero length "for" expression - global errors
        //    not associated with any component returned
        // 3. no "for" expression - all messages returned.
        // 
        String forComponent = (String)component.getAttribute("for");
        if (null != forComponent) {
          
            if (forComponent.length() == 0) {
                messageIter = context.getMessages(null);
            } else {               
                UIComponent comp = null;
                UIComponent currentParent = component;
                try {
                    // Check the naming container of the current 
                    // component for component identified by
                    // 'forComponent'
                    while (currentParent != null) {       
                        // If the current component is a NamingContainer,
                        // see if it contains what we're looking for.
                        comp = currentParent.findComponent(forComponent);
                        if (comp != null)
                            break;
                        // if not, start checking further up in the view
                        currentParent = currentParent.getParent();
                    }                   
                    
                    // no hit from above, scan for a NamingContainer
                    // that contains the component we're looking for from the root.    
                    if (comp == null) {                                                                                             
                        comp = findUIComponentBelow(context.getViewRoot(), forComponent);                                     
                    }
                } catch (Throwable t) {
                    Object[] params = {forComponent};
                    throw new RuntimeException(Util.getExceptionMessage(
                        Util.COMPONENT_NOT_FOUND_ERROR_MESSAGE_ID, params));
                }
                // log a message if we were unable to find the specified component
                // (probably a misconfigured 'for' attribute
                if (comp == null) {
                    if (log.isWarnEnabled()) {
                        log.warn(Util.getExceptionMessage(
                               Util.COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID, 
                               new Object[]{ forComponent }));
                    }
                }
                messageIter = context.getMessages(comp);
            }
        } else {
            messageIter = context.getMessages();
        }
        Assert.assert_it(null != messageIter);

        String color = (String)component.getAttribute("color");
        if (null == color) {
            color = "RED";
        }
	String outputClass = null;
        boolean wroteIt = false;
        if (messageIter.hasNext()) {
	    writer.writeText("\n", null);
	    writer.startElement("font", component);
	    writer.writeAttribute("color", color, "color");
            wroteIt = true;
        }
	if (null != (outputClass = (String) 
		     component.getAttribute("outputClass"))) {
            writer.startElement("span", component);
	    writer.writeAttribute("class", outputClass, "outputClass");
	}
        while (messageIter.hasNext()) {
            curMessage = (Message) messageIter.next();
	    //PENDING(rogerk)null 2nd arg?
	    writer.writeText("\t", null);
	    writer.writeText(curMessage.getSummary(), null);
        }
	if (null != outputClass) {
            writer.endElement("span");
	}
        if (wroteIt) {
	    writer.endElement("font");
        }
    }
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class ErrorsRenderer


