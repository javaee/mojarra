/*
 * $Id: CommandLinkRenderer.java,v 1.22.26.1.2.7 2007/04/27 21:27:44 ofung Exp $
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

// CommandLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.util.Util;

/**
 * <B>CommandLinkRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a HyperLink that acts like a Button.
 */

public class CommandLinkRenderer extends LinkRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(CommandLinkRenderer.class);
     
    // Separator character

    //
    // Class Variables
    //

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

        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }

        UICommand command = (UICommand) component;

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOnReadonly(component)) {
            if (log.isTraceEnabled()) {
                log.trace("No decoding necessary since the component " +
                          component.getId() + " is disabled");
            }
            return;
        } 
	
        String 
	    clientId = command.getClientId(context),
	    paramName = getHiddenFieldName(context, command);
        Map requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        String value = (String) requestParameterMap.get(paramName);
        if (value == null || value.equals("") || !clientId.equals(value)) {
            return;
        }
        ActionEvent actionEvent = new ActionEvent(component);
        component.queueEvent(actionEvent);

        if (log.isDebugEnabled()) {
            log.debug("This command resulted in form submission " +
                      " ActionEvent queued " + actionEvent);
        }
        if (log.isTraceEnabled()) {
            log.trace("End decoding component " + component.getId());
        }
        return;
    }


    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + component.getId());
        }

        UICommand command = (UICommand) component;
        Object value = command.getValue();
        // suppress rendering if "rendered" property on the command is
        // false.
        if (!command.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert (writer != null);

        String clientId = command.getClientId(context);

        UIForm uiform = getMyForm(context, command);
        if ( uiform == null ) {
            if (log.isErrorEnabled()) {
                log.error("component '" + component.getId() +
                          "' must be enclosed inside a form ");
            }   
            if (value != null) {
                writer.write(value.toString());
            }
            return;
        }
        String formClientId = uiform.getClientId(context);

        //Write Anchor attributes

        //make link act as if it's a button using javascript
        Param paramList[] = getParamList(context, command);
        StringBuffer sb = new StringBuffer();
        writer.startElement("a", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("href", "#", "href");
        Util.renderPassThruAttributes(writer, component,
                                      new String[]{"onclick", "target"});
        Util.renderBooleanPassThruAttributes(writer, component);
        sb = new StringBuffer();
        // call the javascript function that clears the all the hidden field
        // parameters in the form.
        sb.append(Util.createValidECMAIdentifier(CLEAR_HIDDEN_FIELD_FN_NAME + "_" + 
                formClientId.replace(NamingContainer.SEPARATOR_CHAR, '_')));
        sb.append("('");
        sb.append(formClientId);
        sb.append("');");
        sb.append("document.forms[");
        sb.append("'");
        sb.append(formClientId);
        sb.append("'");
        sb.append("]['");
        sb.append(getHiddenFieldName(context, command));
        sb.append("'].value='");
        sb.append(clientId);
        sb.append("';");
        for (int i = 0, len = paramList.length; i < len; i++) {
            sb.append("document.forms[");
            sb.append("'");
            sb.append(formClientId);
            sb.append("'");
            sb.append("]['");
            sb.append(paramList[i].getName());
            sb.append("'].value='");
            sb.append(paramList[i].getValue());
            sb.append("';");
        }
        // Set the target attribute on the form element using javascript.
        // Because we treat commandLink as a button,setting target on it,
        // will not have the desired effect since we "return false" for 
        // onclick which would essentially cancel the click.
        String target = (String) component.getAttributes().get("target");
        if (target != null && target.trim().length() > 0) {
            sb.append(" document.forms[");
            sb.append("'");
            sb.append(formClientId);
            sb.append("'");
            sb.append("].target='");
            sb.append(target);
            sb.append("';");
        }
        sb.append(" document.forms[");
        sb.append("'");
        sb.append(formClientId);
        sb.append("'");
        sb.append("].submit()");

        sb.append("; return false;");
        writer.writeAttribute("onclick", sb.toString(), "onclick");
        
        writeCommonLinkAttributes(writer, command);
        
        // render the current value as link text.
        String label = null;
        
        if (value != null) {
            label = value.toString();
        }
        if (log.isTraceEnabled()) {
            log.trace("Value to be rendered " + value);
        }
        if (label != null && label.length() != 0) {
            writer.write(label);
        }
        writer.flush();

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding children " + component.getId());
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        UICommand command = (UICommand) component;                

        // suppress rendering if "rendered" property on the command is
        // false.
        if (!command.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert (writer != null);
        
        if (getMyForm(context, component) == null) {           
            writer.write(Util.getExceptionMessageString(
                  Util.COMMAND_LINK_NO_FORM_MESSAGE_ID));           
            return;
        }

        //Write Anchor inline elements

        //Done writing Anchor element
        writer.endElement("a");

        //Handle hidden fields

        //Only need one hidden field for the link itself per form.
	FormRenderer.addNeededHiddenField(context, 
	    getHiddenFieldName(context, command));

	// PENDING(edburns): not sure if the JSFA59 back button problem
	// manifests itself with param children as well...

        // get UIParameter children...
        Param paramList[] = getParamList(context, command);
        for (int i = 0; i < paramList.length; i++) {
            FormRenderer.addNeededHiddenField(context, paramList[i].getName());
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }

        return;
    }
    
    protected String getHiddenFieldName(FacesContext context, 
					UIComponent component) {
	UIForm uiform = getMyForm(context, component);
	String formClientId = uiform.getClientId(context);
	return (formClientId + NamingContainer.SEPARATOR_CHAR + 
		UIViewRoot.UNIQUE_ID_PREFIX + "cl");
    }
    
    protected UIForm getMyForm(FacesContext context, UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }

} // end of class CommandLinkRenderer
