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

package com.sun.faces.renderkit;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ProjectStage;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.faces.render.Renderer;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer.Param;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;

import javax.faces.component.*;
import javax.faces.component.html.HtmlMessages;

/**
 * <p>A set of utilities for use in {@link RenderKit}s.</p>
 */
public class RenderKitUtils {

    /**
     * <p>The prefix to append to certain attributes when renderking
     * <code>XHTML Transitional</code> content.
     */
    private static final String XHTML_ATTR_PREFIX = "xml:";


    /**
     * <p><code>Boolean</code> attributes to be rendered
     * using <code>XHMTL</code> semantics.
     */
    private static final String[] BOOLEAN_ATTRIBUTES = {
          "disabled", "ismap", "readonly"
    };


    /**
     * <p>An array of attributes that must be prefixed by
     * {@link #XHTML_ATTR_PREFIX} when rendering
     * <code>XHTML Transitional</code> content.
     */
    private static final String[] XHTML_PREFIX_ATTRIBUTES = {
          "lang"
    };

    /**
     * <p>The maximum number of array elements that can be used
     * to hold content types from an accept String.</p>
     */
    private final static int MAX_CONTENT_TYPES = 50;

    /**
     * <p>The maximum number of content type parts.
     * For example: for the type: "text/html; level=1; q=0.5"
     * The parts of this type would be:
     *      "text" - type
     *      "html; level=1" - subtype
     *      "0.5" - quality value
     *      "1" - level value </p>
     */
    private final static int MAX_CONTENT_TYPE_PARTS = 4;

    /**
     * The character that is used to delimit content types
     * in an accept String.</p>
     */
    private final static String CONTENT_TYPE_DELIMITER = ",";

    /**
     * The character that is used to delimit the type and
     * subtype portions of a content type in an accept String.
     * Example: text/html </p>
     */
    private final static String CONTENT_TYPE_SUBTYPE_DELIMITER = "/";

    /**
     * This represents the base package that can leverage the
     * <code>attributesThatAreSet</code> List for optimized attribute
     * rendering.
     *
     * IMPLEMENTATION NOTE:  This must be kept in sync with the array
     * in UIComponentBase$AttributesMap and HtmlComponentGenerator.
     *
     * Hopefully JSF 2.0 will remove the need for this.
     */
    private static final String OPTIMIZED_PACKAGE = "javax.faces.component.";


    /**
     * IMPLEMENTATION NOTE:  This must be kept in sync with the Key
     * in UIComponentBase$AttributesMap and HtmlComponentGenerator.
     *
     * Hopefully JSF 2.0 will remove the need for this.
     */
    private static final String ATTRIBUTES_THAT_ARE_SET_KEY =
        UIComponentBase.class.getName() + ".attributesThatAreSet";


    protected static final Logger LOGGER = FacesLogger.RENDERKIT.getLogger();
    
    private static final Param[] EMPTY_PARAMS = new Param[0];


    // ------------------------------------------------------------ Constructors


    private RenderKitUtils() {
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the {@link RenderKit} for the current request.</p>
     * @param context the {@link FacesContext} of the current request
     * @return the {@link RenderKit} for the current request.
     */
    public static RenderKit getCurrentRenderKit(FacesContext context) {

        RenderKitFactory renderKitFactory = (RenderKitFactory)
              FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        return renderKitFactory.getRenderKit(context,
                                             context
                                                   .getViewRoot().getRenderKitId());

    }


    /**
     * <p>Obtain and return the {@link ResponseStateManager} for
     * the specified #renderKitId.</p>
     *
     * @param context the {@link FacesContext} of the current request
     * @param renderKitId {@link RenderKit} ID
     * @return the {@link ResponseStateManager} for the specified
     *  #renderKitId
     * @throws FacesException if an exception occurs while trying
     *  to obtain the <code>ResponseStateManager</code>
     */
    public static ResponseStateManager getResponseStateManager(
          FacesContext context, String renderKitId)
          throws FacesException {

        assert (null != renderKitId);
        assert (null != context);

        RenderKit renderKit = context.getRenderKit();
        if (renderKit == null) {
            // check request scope for a RenderKitFactory implementation
            RenderKitFactory factory = (RenderKitFactory)
                  RequestStateManager.get(context, RequestStateManager.RENDER_KIT_IMPL_REQ);
            if (factory != null) {
                renderKit = factory.getRenderKit(context, renderKitId);
            } else {
                factory = (RenderKitFactory)
                      FactoryFinder
                            .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                if (factory == null) {
                    throw new IllegalStateException();
                } else {
                    RequestStateManager.set(context,
                                            RequestStateManager.RENDER_KIT_IMPL_REQ,
                                            factory);
                }
                renderKit = factory.getRenderKit(context, renderKitId);
            }
        }
        return renderKit.getResponseStateManager();

    }

    /**
     * <p>Return a List of {@link javax.faces.model.SelectItem}
     * instances representing the available options for this component,
     * assembled from the set of {@link javax.faces.component.UISelectItem}
     * and/or {@link javax.faces.component.UISelectItems} components that are
     * direct children of this component.  If there are no such children, an
     * empty <code>Iterator</code> is returned.</p>
     *
     * @param context The {@link javax.faces.context.FacesContext} for the current request.
     *                If null, the UISelectItems behavior will not work.
     * @param component the component
     * @throws IllegalArgumentException if <code>context</code>
     *                                  is <code>null</code>
     * @return a List of the select items for the specified component
     */
    public static Iterator<SelectItem> getSelectItems(FacesContext context,
                                                     UIComponent component) {

        Util.notNull("context", context);
        Util.notNull("component", component);

        return new SelectItemsIterator(context, component);
        
    }



    /**
     * <p>Render any "passthru" attributes, where we simply just output the
     * raw name and value of the attribute.  This method is aware of the
     * set of HTML4 attributes that fall into this bucket.  Examples are
     * all the javascript attributes, alt, rows, cols, etc. </p>
     *
     * @param writer writer the {@link javax.faces.context.ResponseWriter} to be used when writing
     *  the attributes
     * @param component the component
     * @param attributes an array of attributes to be processed
     * @throws IOException if an error occurs writing the attributes
     */
    public static void renderPassThruAttributes(ResponseWriter writer,
                                                UIComponent component,
                                                String[] attributes)
    throws IOException {

        assert (null != writer);
        assert (null != component);

        Map<String, Object> attrMap = component.getAttributes();

        if (canBeOptimized(component)) {
            //noinspection unchecked
            List<String> setAttributes = (List<String>)
              component.getAttributes().get(ATTRIBUTES_THAT_ARE_SET_KEY);
            if (setAttributes != null) {
                renderPassThruAttributesOptimized(writer,
                                                  component,
                                                  attributes,
                                                  setAttributes);
            }
        } else {
            // this block should only be hit by custom components leveraging
            // the RI's rendering code.  We make no assumptions and loop through
            // all known attributes.
            boolean isXhtml =
                  RIConstants.XHTML_CONTENT_TYPE.equals(writer.getContentType());
            for (String attrName : attributes) {

                Object value =
                      attrMap.get(attrName);
                if (value != null && shouldRenderAttribute(value)) {
                    writer.writeAttribute(prefixAttribute(attrName, isXhtml),
                                          value,
                                          attrName);
                }
            }

        }
    }

    public static String buildAjaxCommand(FacesContext context,
                                          UIComponent component,
                                          AjaxBehavior ajaxBehavior) {
        final String AJAX_REQUEST = "jsf.ajax.request";
        // Is there already an option written?
        boolean already = false;
        StringBuilder ajaxCommand = new StringBuilder(256);
        Collection<String> execute = ajaxBehavior.getExecute(context);
        Collection<String> render = ajaxBehavior.getRender(context);
        String onevent = ajaxBehavior.getOnEvent(context);
        String onerror = ajaxBehavior.getOnError(context);
        ajaxCommand.append(AJAX_REQUEST);
        ajaxCommand.append("(this, event");
        if (execute != null || render != null) {
            ajaxCommand.append(", {");
        }
        if (execute != null) {
            already = true;
            boolean first = true;
            ajaxCommand.append("execute:'");
            for (String exe : execute) {
                if (!first) {
                    ajaxCommand.append(' ');
                } else {
                    first = false;
                }
                UIComponent resolvedComponent = findComponent(component, exe);
                if (resolvedComponent == null) {
                    // RELEASE_PENDING  i18n
                    throw new FacesException(
                          "'execute' attribute contains unknown id '"
                          + exe
                          + "'");
                }
                ajaxCommand.append(resolvedComponent.getClientId());
            }
            ajaxCommand.append('\'');
        }
        if (render != null) {
            if (already) {
                ajaxCommand.append(',');
            } else {
                already = true;
            }
            boolean first = true;
            ajaxCommand.append("render:'");
            for (String rend : render) {
                if (!first) {
                    ajaxCommand.append(' ');
                } else {
                    first = false;
                }
                UIComponent resolvedComponent = findComponent(component, rend);
                if (resolvedComponent == null) {
                    // RELEASE_PENDING  i18n
                    throw new FacesException("'render' attribute contains unknown id '"+rend+"'");
                }
                ajaxCommand.append(resolvedComponent.getClientId());
            }
            ajaxCommand.append('\'');
        }
        if (onevent != null) {
            if (already) {
                ajaxCommand.append(',');
            } else {
                already = true;
            }
            ajaxCommand.append("onevent:");
            ajaxCommand.append(onevent);
        }
        if (onerror != null) {
            if (already) {
                ajaxCommand.append(',');
            } else {
                already = true;
            }
            ajaxCommand.append("onerror:");
            ajaxCommand.append(onerror);
        }
        if (already) {
            ajaxCommand.append('}');
        }

        ajaxCommand.append(");");

        return ajaxCommand.toString();
    }

    public static void renderOnchange(FacesContext context, UIComponent component)
        throws IOException {

        boolean isCommand = false;
        final String event = "onchange";

        // is there a user Onchange?
        boolean userSpecifiedOnchange = false;
        // do we need to render ajax?
        boolean renderAjax = false;
        // String buffer for final output
        StringBuffer sb;
        // the ajax command to render
        String ajaxCommand = "";
        // the user supplied onchange to render
        String userOnchange;

        ResponseWriter writer = context.getResponseWriter();

        userOnchange = (String) component.getAttributes().get(event);
        userSpecifiedOnchange = (userOnchange != null && !"".equals(userOnchange));

        AjaxBehavior ajaxBehavior = (AjaxBehavior)component.getAttributes().get(AjaxBehavior.AJAX_BEHAVIOR);
        renderAjax = (ajaxBehavior != null && !ajaxBehavior.isDisabled(context));
        if (!userSpecifiedOnchange && !renderAjax) { // nothing to do
            return;  // save the effort of creating the StringBuffer
        }

        if (renderAjax) ajaxCommand = buildAjaxCommand(context, component, ajaxBehavior);

        sb = new StringBuffer(256);

        if (userSpecifiedOnchange && renderAjax) { // Doing both
            // RELEASE_PENDING driscoll this should use the jsfcbk function, move the detection
            // into the relevant components, or figure something better out
            sb.append("var a=function(event){");
            userOnchange = userOnchange.trim();
            sb.append(userOnchange);
            if (userOnchange.charAt(userOnchange.length() - 1) != ';') {
                sb.append(';');
            }
            sb.append("};var b=function(event){");
            sb.append(ajaxCommand);
            sb.append("};return mojarra.jsfcbk(a,this,event) ? mojarra.jsfcbk(b,this,event) : false;");
        } else if (userSpecifiedOnchange) { // do one
            sb.append(userOnchange);
        } else if (renderAjax) { // do the other
            sb.append(ajaxCommand);
        }

        // At last, write out the completed attribute
        if (userSpecifiedOnchange || renderAjax) {
            writer.writeAttribute(event, sb.toString(), event);
        }
    }

    public static void renderOnclick(FacesContext context, 
                                     UIComponent component, Param[] params,
                                     AjaxBehavior ajaxBehavior)
        throws IOException {

        boolean isCommand = true;
        final String event = "onclick";

        // is there a user Onchange?
        boolean userSpecifiedOnclick = false;
        // do we need to render ajax?
        boolean renderAjax = (ajaxBehavior != null && !ajaxBehavior.isDisabled(context));
        // are there parameters to render?
        boolean renderParams = (!Arrays.equals(params,EMPTY_PARAMS));
        // String buffer for final output
        StringBuffer sb;
        // the ajax command to render
        String ajaxCommand = "";
        // the user supplied onchange to render
        String userOnclick;
        // Form Id
        String formClientId = "";
        // Client Id
        String componentClientId = "";


        if (renderParams) {
            formClientId = getFormClientId(component, context);
            componentClientId = component.getClientId(context);
        }
        ResponseWriter writer = context.getResponseWriter();

        userOnclick = (String) component.getAttributes().get(event);
        userSpecifiedOnclick = (userOnclick != null && !"".equals(userOnclick));
        if (userSpecifiedOnclick) {
            userOnclick = userOnclick.trim();
        }

        if (!userSpecifiedOnclick && !renderAjax && !renderParams) { // nothing to do
            return;  // save the effort of creating the StringBuffer
        }

        if (renderAjax) ajaxCommand = buildAjaxCommand(context, component, ajaxBehavior);

        sb = new StringBuffer(256);

        if (userSpecifiedOnclick && renderAjax && renderParams) {
            sb.append("var a=function(event){");
            sb.append(userOnclick);
            if (userOnclick.charAt(userOnclick.length() - 1) != ';') {
                sb.append(';');
            }
            sb.append("};var b=function(event){");
            sb.append(ajaxCommand);
            sb.append("};if (mojarra.jsfcbk(a,this,event)===false) { return false;}else{");
            sb.append("mojarra.apf(document.getElementById('");
            sb.append(formClientId);
            sb.append("'),");
            sb.append(renderParams(componentClientId,params));
            sb.append(");mojarra.jsfcbk(b,this,event); mojarra.dpf(document.getElementById('");
            sb.append(formClientId);
            sb.append("'));return false}");

        } else if (userSpecifiedOnclick && renderAjax) {
            sb.append("var a=function(event){");
            sb.append(userOnclick);
            if (userOnclick.charAt(userOnclick.length() - 1) != ';') {
                sb.append(';');
            }
            sb.append("};var b=function(event){");
            sb.append(ajaxCommand);
            sb.append("return false;");
            sb.append("};return (mojarra.jsfcbk(a,this,event)===false) ? false : mojarra.jsfcbk(b,this,event);");
        } else if (userSpecifiedOnclick && renderParams) {
            sb.append("var a=function(event){");
            sb.append(userOnclick);
            if (userOnclick.charAt(userOnclick.length() - 1) != ';') {
                sb.append(';');
            }
            sb.append("};var b=function(event){");
            sb.append(getCommandOnClickScript(formClientId,
                                              componentClientId,
                                              "",
                                              params,
                                              renderAjax));
            sb.append("};return (mojarra.jsfcbk(a,this,event)===false) ? false : mojarra.jsfcbk(b,this,event);");
        } else if (renderAjax && renderParams) {
            sb.append("var a=function(event){");
            sb.append(ajaxCommand);
            sb.append("};mojarra.apf(document.getElementById('");
            sb.append(formClientId);
            sb.append("'),");
            sb.append(renderParams(componentClientId,params));
            sb.append(");mojarra.jsfcbk(a,this,event); mojarra.dpf(document.getElementById('");
            sb.append(formClientId);
            sb.append("')); return false;");
        } else if (userSpecifiedOnclick) { // do one
            sb.append(userOnclick);
            if (userOnclick.charAt(userOnclick.length() - 1) != ';') {
               sb.append(';');
            }
        } else if (renderAjax) { // do one
            sb.append(ajaxCommand);
            sb.append("return false;");
        } else if (renderParams) { // do one
            sb.append(getCommandOnClickScript(formClientId,
                                              componentClientId,
                                              "",
                                              params,
                                              renderAjax));
        }

        // At last, write out the completed attribute
        if (userSpecifiedOnclick || renderAjax || renderParams) {
            writer.writeAttribute(event, sb.toString(), event);
        }
    }




    public static String prefixAttribute(final String attrName,
                                         final ResponseWriter writer) {

        return (prefixAttribute(attrName,
             RIConstants.XHTML_CONTENT_TYPE.equals(writer.getContentType())));

    }


    public static String prefixAttribute(final String attrName,
                                         boolean isXhtml) {
        if (isXhtml) {
            if (Arrays.binarySearch(XHTML_PREFIX_ATTRIBUTES, attrName) > -1) {
                return XHTML_ATTR_PREFIX + attrName;
            } else {
                return attrName;
            }
        } else {
            return attrName;
        }

    }


    /**
     * <p>Renders the attributes from {@link #BOOLEAN_ATTRIBUTES}
     * using <code>XHMTL</code> semantics (i.e., disabled="disabled").</p>
     *
     * @param writer writer the {@link ResponseWriter} to be used when writing
     *  the attributes
     * @param component the component
     * @throws IOException if an error occurs writing the attributes
     */
    public static void renderXHTMLStyleBooleanAttributes(ResponseWriter writer,
                                                         UIComponent component)
          throws IOException {

        assert (writer != null);
        assert (component != null);

        Map attrMap = component.getAttributes();
        for (String attrName : BOOLEAN_ATTRIBUTES) {
            Object val = attrMap.get(attrName);
            if (val == null) {
                continue;
            }

            if (Boolean.valueOf(val.toString())) {
                writer.writeAttribute(attrName,
                                      true,
                                      attrName);
            }
        }

    }

    /**
     * <p>Given an accept String from the client, and a <code>String</code>
     * of server supported content types, determine the best qualified
     * content type for the client.  If no match is found, or either of the
     * arguments are <code>null</code>,  <code>null</code> is returned.</p>
     *
     * @param accept The client accept String
     * @param serverSupportedTypes The types that the server supports
     * @param preferredType The preferred content type if another type is found
     *        with the same highest quality factor.
     * @return The content type <code>String</code>
     */
    public static String determineContentType(String accept, String serverSupportedTypes, String preferredType) {
        String contentType = null;

        if (null == accept || null == serverSupportedTypes) {
            return contentType;
        }

        String[][] clientContentTypes = buildTypeArrayFromString(accept);
        String[][] serverContentTypes = buildTypeArrayFromString(serverSupportedTypes);
        String[][] preferredContentType = buildTypeArrayFromString(preferredType);
        String[][] matchedInfo = findMatch(clientContentTypes, serverContentTypes, preferredContentType);

        // if best match exits and best match is not some wildcard,
        // return best match
        if ((matchedInfo[0][1] != null) && !(matchedInfo[0][2].equals("*"))) {
            contentType = matchedInfo[0][1] + CONTENT_TYPE_SUBTYPE_DELIMITER + matchedInfo[0][2];
        }

        return contentType;
    }


    /**
     * @param contentType the content type in question
     * @return <code>true</code> if the content type is a known XML-based
     *  content type, otherwise, <code>false</code>
     */
    public static boolean isXml(String contentType) {
        return (RIConstants.XHTML_CONTENT_TYPE.equals(contentType)
                || RIConstants.APPLICATION_XML_CONTENT_TYPE.equals(contentType)
                || RIConstants.TEXT_XML_CONTENT_TYPE.equals(contentType));
    }


    // --------------------------------------------------------- Private Methods


    /**
     * Attempt to find the component assuming the ID is relative to the
     * nearest naming container.  If not found, then search for the component
     * using an absolute component expression.
     */
    private static UIComponent findComponent(UIComponent component,
                                             String exe) {

        // RELEASE_PENDING - perhaps only enable ID validation if ProjectStage
        // is development
        UIComponent resolvedComponent = component.findComponent(exe);
        if (resolvedComponent == null) {
            // not found using a relative search, try an absolute search
            resolvedComponent = component.findComponent(':' + exe);
        }
        return resolvedComponent;

    }

    
    /**
     * @param component the UIComponent in question
     * @return <code>true</code> if the component is within the
     *  <code>javax.faces.component</code> or <code>javax.faces.component.html</code>
     *  packages, otherwise return <code>false</code>
     */
    private static boolean canBeOptimized(UIComponent component) {

        String name = component.getClass().getName();
        return (name != null && name.startsWith(OPTIMIZED_PACKAGE));

    }


    /**
     * <p>For each attribute in <code>setAttributes</code>, perform a binary
     * search against the array of <code>knownAttributes</code>  If a match is found
     * and the value is not <code>null</code>, render the attribute.
     * @param writer the current writer
     * @param component the component whose attributes we're rendering
     * @param knownAttributes an array of pass-through attributes supported by
     *  this component
     * @param setAttributes a <code>List</code> of attributes that have been set
     *  on the provided component
     * @throws IOException if an error occurs during the write
     */
    private static void renderPassThruAttributesOptimized(ResponseWriter writer,
                                                          UIComponent component,
                                                          String[] knownAttributes,
                                                          List<String> setAttributes)
    throws IOException {

        String[] attributes = setAttributes.toArray(new String[setAttributes.size()]);
        Arrays.sort(attributes);
        boolean isXhtml =
              RIConstants.XHTML_CONTENT_TYPE.equals(writer.getContentType());
        Map<String, Object> attrMap = component.getAttributes();
        for (String name : attributes) {
            if (Arrays.binarySearch(knownAttributes, name) >= 0) {
                Object value =
                      attrMap.get(name);
                if (value != null && shouldRenderAttribute(value)) {
                    writer.writeAttribute(prefixAttribute(name, isXhtml),
                                          value,
                                          name);
                }
            }
        }

    }


    /**
     * <p>Determines if an attribute should be rendered based on the
     * specified #attributeVal.</p>
     *
     * @param attributeVal the attribute value
     * @return <code>true</code> if and only if #attributeVal is
     *  an instance of a wrapper for a primitive type and its value is
     *  equal to the default value for that type as given in the specification.
     */
    private static boolean shouldRenderAttribute(Object attributeVal) {

        if (attributeVal instanceof String) {
            return true;
        } else if (attributeVal instanceof Boolean &&
            Boolean.FALSE.equals(attributeVal)) {
            return false;
        } else if (attributeVal instanceof Integer &&
                   (Integer) attributeVal == Integer.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Double &&
                   (Double) attributeVal == Double.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Character &&
                   (Character) attributeVal
                   == Character
                         .MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Float &&
                   (Float) attributeVal == Float.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Short &&
                   (Short) attributeVal == Short.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Byte &&
                   (Byte) attributeVal == Byte.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Long &&
                   (Long) attributeVal == Long.MIN_VALUE) {
            return false;
        }
        return true;

    }

    /**
     * <p>This method builds a two element array structure as follows:
     * Example:
     *     Given the following accept string:
     *       text/html; level=1, text/plain; q=0.5
     *     [0][0] 1  (quality is 1 if none specified)
     *     [0][1] "text"  (type)
     *     [0][2] "html; level=1" (subtype)
     *     [0][3] 1 (level, if specified; null if not)
     *
     *     [1][0] .5
     *     [1][1] "text"
     *     [1][2] "plain"
     *     [1][3] (level, if specified; null if not)
     *
     * The array is used for comparison purposes in the findMatch method.</p>
     *
     * @param accept An accept <code>String</code>
     * @return an two dimensional array containing content-type/quality info
     */
    private static String[][] buildTypeArrayFromString(String accept) {
        String[][] arrayAccept = new String[MAX_CONTENT_TYPES][MAX_CONTENT_TYPE_PARTS];
        // return if empty
        if ((accept == null) || (accept.length() == 0))
            return arrayAccept;
        // some helper variables
        StringBuilder typeSubType;
        String type;
        String subtype;
        String level = null;
        String quality = null;

        // Parse "types"
        String[] types = Util.split(accept, CONTENT_TYPE_DELIMITER);
        int index = -1;
        for (int i=0; i<types.length; i++) {
            String token = types[i].trim();
            index += 1;
            // Check to see if our accept string contains the delimiter that is used
            // to add uniqueness to a type/subtype, and/or delimits a qualifier value:
            //    Example: text/html;level=1,text/html;level=2; q=.5
            if (token.contains(";")) {
                String[] typeParts = Util.split(token, ";");
                typeSubType = new StringBuilder(typeParts[0].trim());
                for (int j=1; j<typeParts.length; j++) {
                    quality = "not set";
                    token = typeParts[j].trim();
                    // if "level" is present, make sure it gets included in the "type/subtype"
                    if (token.contains("level")) {
                        typeSubType.append(';').append(token);
                        String[] levelParts = Util.split(token, "=");
                        level = levelParts[0].trim();
                        if (level.equalsIgnoreCase("level")) {
                            level = levelParts[1].trim();
                        }
                    } else {
                        quality = token;
                        String[] qualityParts = Util.split(quality, "=");
                        quality = qualityParts[0].trim();
                        if (quality.equalsIgnoreCase("q")) {
                            quality = qualityParts[1].trim();
                            break;
                        } else {
                            quality = "not set"; // to identifiy that no quality was supplied
                        }
                    }
                }
            } else {
                typeSubType = new StringBuilder(token);
                quality = "not set"; // to identifiy that no quality was supplied
            }
            // now split type and subtype
            if (typeSubType.indexOf(CONTENT_TYPE_SUBTYPE_DELIMITER) >= 0) {
                String[] typeSubTypeParts = Util.split(typeSubType.toString(), CONTENT_TYPE_SUBTYPE_DELIMITER);
                // Apparently there are user-agents that send invalid
                // Accept headers containing no subtype (i.e. text/).
                // For those cases, assume "*" for the subtype.
                if (typeSubTypeParts.length == 1) {
                    type = typeSubTypeParts[0].trim();
                    subtype = "*";
                } else {
                    type = typeSubTypeParts[0].trim();
                    subtype = typeSubTypeParts[1].trim();
                }

            } else {
                type = typeSubType.toString();
                subtype = "";
            }
            // check quality and assign values
            if ("not set".equals(quality)) {
                if (type.equals("*") && subtype.equals("*")) {
                    quality = "0.01";
                } else if (!type.equals("*") && subtype.equals("*")) {
                    quality = "0.02";
                } else if (type.equals("*") && subtype.length() == 0) {
                    quality = "0.01";
                } else {
                    quality = "1";
                }
            }
            arrayAccept[index][0] = quality;
            arrayAccept[index][1] = type;
            arrayAccept[index][2] = subtype;
            arrayAccept[index][3] = level;
        }
        return (arrayAccept);
    }

    /**
     * <p>For each server supported type, compare client (browser) specified types.
     * If a match is found, keep track of the highest quality factor.
     * The end result is that for all matches, only the one with the highest
     * quality will be returned.</p>
     *
     * @param clientContentTypes An <code>array</code> of accept <code>String</code>
     * information for the client built from @{link #buildTypeArrayFromString}.
     * @param serverSupportedContentTypes An <code>array</code> of accept <code>String</code>
     * information for the server supported types built from @{link #buildTypeArrayFromString}.
     * @param preferredContentType An <code>array</code> of preferred content type information.
     * @return An <code>array</code> containing the parts of the preferred content type for the
     * client.  The information is stored as outlined in @{link #buildTypeArrayFromString}.
     */
    private static String[][] findMatch(String[][] clientContentTypes,
                                        String[][] serverSupportedContentTypes,
                                        String[][] preferredContentType) {

        // result array
        String[][] results = new String[MAX_CONTENT_TYPES][MAX_CONTENT_TYPE_PARTS];
        int resultidx = -1;
        // the highest quality
        double highestQFactor = 0;
        // the record with the highest quality
        int idx = 0;
        for (int sidx = 0; sidx < MAX_CONTENT_TYPES; sidx++) {
            // get server type
            String serverType = serverSupportedContentTypes[sidx][1];
            if (serverType != null) {
                for (int cidx = 0; cidx < MAX_CONTENT_TYPES; cidx++) {
                    // get browser type
                    String browserType = clientContentTypes[cidx][1];
                    if (browserType != null) {
                        // compare them and check for wildcard
                        if ((browserType.equalsIgnoreCase(serverType)) || (browserType.equals("*"))) {
                            // types are equal or browser type is wildcard - compare subtypes
                            if ((clientContentTypes[cidx][2].equalsIgnoreCase(
                                serverSupportedContentTypes[sidx][2])) ||
                                (clientContentTypes[cidx][2].equals("*"))) {
                                // subtypes are equal or browser subtype is wildcard
                                // found match: multiplicate qualities and add to result array
                                // if there was a level associated, this gets higher precedence, so
                                // factor in the level in the calculation.
                                double cLevel = 0.0;
                                double sLevel = 0.0;
                                if (clientContentTypes[cidx][3] != null) {
                                    cLevel = (Double.parseDouble(clientContentTypes[cidx][3]))*.10;
                                }
                                if (serverSupportedContentTypes[sidx][3] != null) {
                                    sLevel = (Double.parseDouble(serverSupportedContentTypes[sidx][3]))*.10;
                                }
                                double cQfactor = Double.parseDouble(clientContentTypes[cidx][0]) + cLevel;
                                double sQfactor = Double.parseDouble(serverSupportedContentTypes[sidx][0]) + sLevel;
                                double resultQuality = cQfactor * sQfactor;
                                resultidx += 1;
                                results[resultidx][0] = String.valueOf(resultQuality);
                                if (clientContentTypes[cidx][2].equals("*")) {
                                    // browser subtype is wildcard
                                    // return type and subtype (wildcard)
                                    results[resultidx][1] = clientContentTypes[cidx][1];
                                    results[resultidx][2] = clientContentTypes[cidx][2];
                                } else {
                                    // return server type and subtype
                                    results[resultidx][1] = serverSupportedContentTypes[sidx][1];
                                    results[resultidx][2] = serverSupportedContentTypes[sidx][2];
                                    results[resultidx][3] = serverSupportedContentTypes[sidx][3];
                                }
                                // check if this was the highest factor
                                if (resultQuality > highestQFactor) {
                                    idx = resultidx;
                                    highestQFactor = resultQuality;
                                }
                            }
                        }
                    }
                }
            }
        }

        // First, determine if we have a type that has the highest quality factor that
        // also matches the preferred type (if there is one):
        String[][] match = new String[1][3];
        if (preferredContentType[0][0] != null) {
            BigDecimal highestQual = BigDecimal.valueOf(highestQFactor);
            for (int i=0; i<=resultidx; i++) {
                if ((BigDecimal.valueOf(Double.parseDouble(results[i][0])).compareTo(highestQual) == 0) &&
                    (results[i][1]).equals(preferredContentType[0][1]) &&
                    (results[i][2]).equals(preferredContentType[0][2])) {
                    match[0][0] = results[i][0];
                    match[0][1] = results[i][1];
                    match[0][2] = results[i][2];
                    return match;
                }
            }
        }

        match[0][0] = results[idx][0];
        match[0][1] = results[idx][1];
        match[0][2] = results[idx][2];
        return match;
    }

    /**
     * <p>Replaces all occurrences of <code>-</code> with <code>$_</code>.</p>
     *
     * @param origIdentifier the original identifer that needs to be
     *  'ECMA-ized'
     * @return an ECMA valid identifer
     */
    public static String createValidECMAIdentifier(String origIdentifier) {
        return origIdentifier.replace("-", "$_");
    }


    /**
     * <p>Renders the Javascript necessary to add and remove request
     * parameters to the current form.</p>
     * @param context the <code>FacesContext</code> for the current request
     * @throws java.io.IOException if an error occurs writing to the response
     */
    public static void renderJsfJs(FacesContext context) throws IOException {

        final String name = "jsf.js";
        final String library = "javax.faces";

        if (context.getAttributes().get(RIConstants.SCRIPT_STATE) != null) {
            // Already included, return
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        UIViewRoot viewRoot = context.getViewRoot();
        ListIterator iter = (viewRoot.getComponentResources(context, "head")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String rname = (String)resource.getAttributes().get("name");
            String rlibrary = (String)resource.getAttributes().get("library");
            if (name.equals(rname) && library.equals(rlibrary)) {
                // Set the context to record script as included
                context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);
                return;
            }
        }
        iter = (viewRoot.getComponentResources(context, "body")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String rname = (String)resource.getAttributes().get("name");
            String rlibrary = (String)resource.getAttributes().get("library");
            if (name.equals(rname) && library.equals(rlibrary)) {
                // Set the context to record script as included
                context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);
                return;
            }
        }
        iter = (viewRoot.getComponentResources(context, "form")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String rname = (String)resource.getAttributes().get("name");
            String rlibrary = (String)resource.getAttributes().get("library");
            if (name.equals(rname) && library.equals(rlibrary)) {
                // Set the context to record script as included
                context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);
                return;
            }
        }

        // Since we've now determined that it's not in the page, we need to add it.

        ResourceHandler handler = context.getApplication().getResourceHandler();
        Resource resource = handler.createResource(name, library);
        writer.write('\n');
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("src", ((resource != null) ? resource.getRequestPath() : ""), null);
        writer.endElement("script");
        writer.append('\r');
        writer.append('\n');

        // Set the context to record script as included
        context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);
    }

    /**
     * <p>Returns a string that can be inserted into the <code>onclick</code>
     * handler of a command.  This string will add all request parameters
     * as well as the client ID of the activated command to the form as
     * hidden input parameters, update the target of the link if necessary,
     * and handle the form submission.  The jsf.js file will be rendered
     * as part of this call.</p>
     * @param formClientId the client ID of the form
     * @param commandClientId the client ID of the command
     * @param target the link target
     * @param params the nested parameters, if any @return a String suitable for the <code>onclick</code> handler
     *  of a command
     * @return the default <code>onclick</code> JavaScript for the default
     *  command link component
     */
    public static String getCommandOnClickScript(String formClientId,
                                                     String commandClientId,
                                                     String target,
                                                     Param[] params,
                                                     boolean isAjax) {

        StringBuilder sb = new StringBuilder(256);
        sb.append("mojarra.jsfcljs(document.getElementById('");
        sb.append(formClientId);
        sb.append("'),");
        sb.append(renderParams(commandClientId, params));
        sb.append(",'");
        sb.append(target);
        sb.append("');return false");

        return sb.toString();
    }

    /*
     *
     */
    private static String renderParams(String commandClientId, Param[] params) {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{'");
        sb.append(commandClientId).append("':'").append(commandClientId);
        for (Param param : params) {
            String pn = param.name;
            if (pn != null && pn.length() != 0) {
                String pv = param.value;
                sb.append("','");
                sb.append(pn.replace("'", "\\\'"));
                sb.append("':'");
                if (pv != null && pv.length() != 0) {
                    sb.append(pv.replace("'", "\\\'"));
                }
            }
        }
        sb.append("'}");
        return sb.toString();
    }

    public static void renderUnhandledMessages(FacesContext ctx) {

        Application app = ctx.getApplication();
        if (ProjectStage.Development.equals(app.getProjectStage())) {
            HtmlMessages messages = (HtmlMessages) app.createComponent(HtmlMessages.COMPONENT_TYPE);
            Renderer messagesRenderer = ctx.getRenderKit().getRenderer(HtmlMessages.COMPONENT_FAMILY, "javax.faces.Messages");
            messages.setErrorStyle("Color: red");
            messages.setWarnStyle("Color: orange");
            messages.setInfoStyle("Color: blue");
            messages.setFatalStyle("Color: red");
            messages.setTooltip(true);
            messages.setTitle("Project Stage[Development]: Unhandled Messages");
            messages.setRedisplay(false);
            try {
                messagesRenderer.encodeBegin(ctx, messages);
                messagesRenderer.encodeEnd(ctx, messages);
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, ioe.toString(), ioe);
                }
            }
        } else {
            Iterator<String> clientIds = ctx.getClientIdsWithMessages();
            if (clientIds.hasNext()) {
                //Display each message possibly not displayed.
                StringBuilder builder = new StringBuilder();
                while (clientIds.hasNext()) {
                    String clientId = clientIds.next();
                    Iterator<FacesMessage> messages =
                          ctx.getMessages(clientId);
                    while (messages.hasNext()) {
                        FacesMessage message = messages.next();
                        if (message.isRendered()) {
                            continue;
                        }
                        builder.append("\n");
                        builder.append("sourceId=").append(clientId);
                        builder.append("[severity=(")
                              .append(message.getSeverity());
                        builder.append("), summary=(")
                              .append(message.getSummary());
                        builder.append("), detail=(")
                              .append(message.getDetail()).append(")]");
                    }
                }
                LOGGER.log(Level.INFO, "jsf.non_displayed_message", builder.toString());
            }
        }

    }


    // --------------------------------------------------------- Private Methods


   /**
     * <p>Utility method to return the client ID of the parent form.</p>
     *
     * @param component typically a command component
     * @param context   the <code>FacesContext</code> for the current request
     *
     * @return the client ID of the parent form, if any
     */
    public static String getFormClientId(UIComponent component,
                                   FacesContext context) {

        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        
        UIForm form = (UIForm) parent;
        if (form != null) {
            return form.getClientId(context);
        }

        return null;
    }

    /**
     * @param context the <code>FacesContext</code> for the current request
     *
     * @return <code>true</code> If the <code>add/remove</code> javascript
     *         has been rendered, otherwise <code>false</code>
     */
    private static boolean hasScriptBeenRendered(FacesContext context) {

        return (context.getAttributes()
              .get(RIConstants.SCRIPT_STATE) != null);

    }


    /**
     * <p>Set a flag to indicate that the <code>add/remove</code> javascript
     * has been rendered for the current form.
     *
     * @param context the <code>FacesContext</code> of the current request
     */
    private static void setScriptAsRendered(FacesContext context) {

        context.getAttributes()
              .put(RIConstants.SCRIPT_STATE, Boolean.TRUE);

    }


    // ---------------------------------------------------------- Nested Classes


} // END RenderKitUtils
