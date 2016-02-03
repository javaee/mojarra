/*
 * $Id: LabelRenderer.java,v 1.31.30.2 2007/04/27 21:27:45 ofung Exp $
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

// LabelRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

/**
 * <p><B>LabelRenderer</B> renders Label element.<p>.
 */
public class LabelRenderer extends HtmlBasicInputRenderer {

    //
    // Protected Constants
    //
    private static final Log log = LogFactory.getLog(LabelRenderer.class);
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private static final String RENDER_END_ELEMENT = "com.sun.faces.RENDER_END_ELEMENT";
    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public LabelRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }
        ResponseWriter writer = null;
        String
            forValue = null,
            style = (String) component.getAttributes().get("style"),
            styleClass = (String) component.getAttributes().get("styleClass");
	
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        writer = context.getResponseWriter();
        Util.doAssert(writer != null);

        UIComponent forComponent = null;
        String forClientId = null;
        forValue = (String) component.getAttributes().get("for");
        if ( forValue != null ) {
            forComponent = getForComponent(context, forValue, component);
            if (forComponent == null ) {
                // it could that the component hasn't been created yet. So
                // construct the clientId for component.
                forClientId = getForComponentClientId(component, context, 
                        forValue);
            } else {
                forClientId = forComponent.getClientId(context);   
            }
        }
        
        // set a temporary attribute on the component to indicate that
        // label end element needs to be rendered.
        component.getAttributes().put(RENDER_END_ELEMENT, "yes");
        writer.startElement("label", component);
        writeIdAttributeIfNecessary(context, writer, component);
        if ( forClientId != null ) {
            writer.writeAttribute("for", forClientId, "for");
        }

        Util.renderPassThruAttributes(writer, component);
        if (null != styleClass) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        writer.writeText("\n", null);
        
        // render the curentValue as label text if specified.
        String value = getCurrentValue(context, component);
        if (log.isTraceEnabled()) {
            log.trace("Value to be rendered " + value);
        }
        if (value != null && value.length() != 0) {
            writer.write(value);
        }
        writer.flush();
    }


    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // render label end element if RENDER_END_ELEMENT is set.
        String render = (String) component.getAttributes().get(
            RENDER_END_ELEMENT);
        if (render != null && render.equals("yes")) {
            component.getAttributes().remove(RENDER_END_ELEMENT);
            ResponseWriter writer = context.getResponseWriter();
            Util.doAssert(writer != null);
            writer.endElement("label");
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }
    }
    
    /**
     * Builds and returns the clientId of the component that is
     * represented by the forValue. Since the component has not been created
     * yet, invoking <code>getClientId(context)</code> is not possible.
     *
     * @param component UIComponent that represents the label
     * @param context FacesContext for this request
     * @param forValue String representing the "id" of the component
     *        that this label represents.
     * @return String clientId of the component represented by the forValue.
     */
    protected String getForComponentClientId(UIComponent component, 
            FacesContext context, String forValue) {
        String result = null;
        // ASSUMPTION: The component for which this acts as the label
        // as well ths label component are part of the same form.
        // locate the nearest NamingContainer and get its clientId.
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof NamingContainer) {
                break;
            }
            parent = parent.getParent();
        }
        if (parent == null) {
            if (log.isErrorEnabled()) {
                log.error("component " + component.getId() +
                          " must be enclosed inside a form ");
            }
            return result;
        }
        String parentClientId = parent.getClientId(context);
        // prepend the clientId of the nearest container to the forValue.
        result = parentClientId + NamingContainer.SEPARATOR_CHAR + forValue;
        return result;
    }

    // The testcase for this class is TestRenderResponsePhase.java

} // end of class LabelRenderer
