/*
 * $Id: ButtonRenderer.java,v 1.90 2005/08/26 15:27:12 rlubke Exp $
 */

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

// ButtonRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.sun.faces.util.Util;

import java.util.logging.Level;

/**
 * <B>ButtonRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a Button.
 */

public class ButtonRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    //
    // Class Variables
    //
    private static final String FORM_HAS_COMMAND_LINK_ATTR = 
         "com.sun.faces.FORM_HAS_COMMAND_LINK_ATTR";

    private static final String NO_COMMAND_LINK_FOUND_VALUE = 
         "com.sun.faces.NO_COMMAND_LINK_FOUND";

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    //
    // Class methods
    //

    //
    // General Methods
    //
    
    //
    // Methods From Renderer
    //

    public void decode(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "Begin decoding component " + component.getId());
        }
        
        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("No decoding necessary since the component " +
                          component.getId() + " is disabled");
            }
            return;
        } 


        // Was our command the one that caused this submission?
        // we don' have to worry about getting the value from request parameter
        // because we just need to know if this command caused the submission. We
        // can get the command name by calling currentValue. This way we can 
        // get around the IE bug.
        String clientId = component.getClientId(context);
        Map<String,String> requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        String value = requestParameterMap.get(clientId);
        if (value == null) {
            if (requestParameterMap.get(clientId + ".x") == null &&
                requestParameterMap.get(clientId + ".y") == null) {
                return;
            }
        }

        String type = (String) component.getAttributes().get("type");
        if ((type != null) && (type.toLowerCase().equals("reset"))) {
            return;
        }
        ActionEvent actionEvent = new ActionEvent(component);
        component.queueEvent(actionEvent);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("This command resulted in form submission " +
                      " ActionEvent queued " + actionEvent);
        }
         if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                    "End decoding component " + component.getId());
        }        
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
         if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "Begin encoding component " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
           if (logger.isLoggable(Level.FINE)) {
               logger.fine("End encoding component " + component.getId() +
                          " since rendered attribute is set to false ");
            }
            return;
        }
        
        // Which button type (SUBMIT, RESET, or BUTTON) should we generate?
        String type = (String) component.getAttributes().get("type");
        String styleClass = null;
        if (type == null) {
            type = "submit";
            // This is needed in the decode method
            component.getAttributes().put("type", type);
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String label = "";
        Object value = ((UICommand) component).getValue();
        if (value != null) {
            label = value.toString();
        }
        String imageSrc = (String) component.getAttributes().get("image");
        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
	String clientId = component.getClientId(context);
        if (imageSrc != null) {
            writer.writeAttribute("type", "image", "type");
            writer.writeURIAttribute("src", src(context, imageSrc), "image");
            writer.writeAttribute("name", clientId, "clientId");
        } else {
            writer.writeAttribute("type", type.toLowerCase(), "type");
            writer.writeAttribute("name", clientId, "clientId");
            writer.writeAttribute("value", label, "value");
        }

	StringBuffer sb = new StringBuffer();
	// get the clearHiddenField script, if necessary
	String clearScript = getClearHiddenFieldScript(context, component);
        if (clearScript != null && clearScript.length() != 0) {
	    sb.append(clearScript);
	}
        
        // append user specified script for onclick if any.
        String onclickAttr = (String)component.getAttributes().get("onclick");
        if (onclickAttr != null && onclickAttr.length() != 0) {
            sb.append(onclickAttr);
            
        }
	// only output the attribute if necessary
	clearScript = sb.toString();
        if (clearScript != null && clearScript.length() != 0) {
	    writer.writeAttribute("onclick", clearScript, null);
	}

        Util.renderPassThruAttributes(context, writer, component, 
				      new String[]{"onclick"});
        Util.renderBooleanPassThruAttributes(writer, component);

        if (null != (styleClass = (String)
            component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        writer.endElement("input");
         if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "End encoding component " + component.getId());
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    //
    // General Methods
    //

    /**
     *
     * <p>Return the script to invoke to clear the hidden fields related
     * to commandLink components in the same form as the argument button
     * component.</p>
     *
     * <p>Algorithm:</p>
     *
     * <p>Find the form in which the argument button component resides
     * by going up the parent tree until you find the form.  If you hit
     * the root, return <code>null</code>. </p>
     *
     * <p>Get the form's <code>clientId</code>.</p>
     *
     * <p>Discover if there are one or more commandLink components in
     * this form.  If not, return <code>null</code>.  Do this by first
     * looking in the request scope for the attr
     * <code>FORM_HAS_COMMAND_LINK_ATTR</code>.  If found, see if the
     * value is equal to the clientId of the current form.  If so,
     * continue to the next step.  If not found, or the value is not
     * equal to the current form's clientId, start traversing the
     * children of this form until you find a component whose
     * renderer-type is <code>javax.faces.Link</code> and family is
     * <code>javax.faces.Command</code>.  If such a component is found
     * set the <code>FORM_HAS_COMMAND_LINK_ATTR</code> into request
     * scope (with the value being the form clientId) so subsequent
     * buttons in this form don't have to perform the search.</p>
     *
     * <p>At this point, we know we have one or more commandLink
     * components in this form, so we need to generate the script.</p>
     */

    private String getClearHiddenFieldScript(FacesContext context, 
					     UIComponent component) {
        Map<String,Object> requestMap = 
            context.getExternalContext().getRequestMap();
	UIComponent 
	    myForm = component,
	    root = context.getViewRoot();
        String 
	    formClientId = null,
	    commandLinkAttrValue = null;
	String result = null;
	boolean formHasCommandLink = false;

	//
	// find the form
	//

	while (!(myForm instanceof UIForm) && root != myForm) {
	    myForm = myForm.getParent();
	}

	if (root == myForm) {
	    return null;
	}

	formClientId = myForm.getClientId(context);

	assert(null != formClientId);

	// 
	// Inspect the form for command link instances
	//
	if (null == (commandLinkAttrValue = 
		     (String) requestMap.get(FORM_HAS_COMMAND_LINK_ATTR))) {
	    Util.TreeTraversalCallback callback = 
		new Util.TreeTraversalCallback() {
		    public boolean takeActionOnNode(FacesContext 
						    context,
						    UIComponent 
						    curNode) throws FacesException {
			boolean keepGoing = true;
			String 
			    rendererType = curNode.getRendererType(),
			    family = curNode.getFamily();
			if ("javax.faces.Link".equals(rendererType) &&   
                 "javax.faces.Command".equals(family)) {
			    keepGoing = false;
			    
			}
			return keepGoing;
		    }
		};
	    // if the traversal aborted early due to a match being found
	    if (formHasCommandLink = 
		(!Util.prefixViewTraversal(context, myForm, callback))) {
		requestMap.put(FORM_HAS_COMMAND_LINK_ATTR, formClientId);
	    }
	    else {
		requestMap.put(FORM_HAS_COMMAND_LINK_ATTR, 
			       NO_COMMAND_LINK_FOUND_VALUE);
	    }
	}
	else {
	    // if there is an entry in the map, but it is not equal to
	    // the id for this form,
	    if (!(formHasCommandLink = 
		  commandLinkAttrValue.equals(formClientId))) {
		// see if it is the NO_COMMAND_LINK_FOUND_VALUE
		formHasCommandLink = 
		    !commandLinkAttrValue.equals(NO_COMMAND_LINK_FOUND_VALUE);
	    }
	}
		
	if (!formHasCommandLink) {
	    return null;
	}
	
	result = CLEAR_HIDDEN_FIELD_FN_NAME +
	    "_" + formClientId.replace(NamingContainer.SEPARATOR_CHAR, '_') +
	    "(this.form.id);";
	
	return result;
    }

       
    private String src(FacesContext context, String value) {
        if (value == null) {
            return "";
        }
        value = context.getApplication().getViewHandler().
            getResourceURL(context, value);
        return (context.getExternalContext().encodeResourceURL(value));
    }


} // end of class ButtonRenderer
