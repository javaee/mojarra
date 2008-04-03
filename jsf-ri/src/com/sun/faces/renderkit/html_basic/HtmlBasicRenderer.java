/*
 * $Id: HtmlBasicRenderer.java,v 1.124 2007/11/29 00:51:14 rlubke Exp $
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

// HtmlBasicRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>HtmlBasicRenderer</B> is a base class for implementing renderers
 * for HtmlBasicRenderKit.
 */

public abstract class HtmlBasicRenderer extends Renderer {   


    // Log instance for this class
    protected static final Logger logger = FacesLogger.RENDERKIT.getLogger();

    private static final Param[] EMPTY_PARAMS = new Param[0];

    // ------------------------------------------------------------ Constructors


    public HtmlBasicRenderer() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    @Override
    public String convertClientId(FacesContext context, String clientId) {

        return clientId;

    }


    @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!(component instanceof UIInput)) {
            // decode needs to be invoked only for components that are
            // instances or subclasses of UIInput.
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "No decoding necessary since the component {0} is not an instance or a sub class of UIInput",
                           component.getId());
            }
            return;
        }

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = component.getClientId(context);
        assert(clientId != null);
        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();
        // Don't overwrite the value unless you have to!
        String newValue = requestMap.get(clientId);
        if (newValue != null) {
            setSubmittedValue(component, newValue);
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "new value after decoding {0}",
                           newValue);
            }
        }

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

       rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String currentValue = getCurrentValue(context, component);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE,
                       "Value to be rendered {0}",
                       currentValue);
        }
        getEndTextToRender(context, component, currentValue);

    }


    @Override
    public boolean getRendersChildren() {

        return true;

    }

    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Conditionally augment an id-reference value.</p>
     * <p>If the <code>forValue</code> doesn't already include a generated
     * suffix, but the id of the <code>fromComponent</code> does include a
     * generated suffix, then append the suffix from the
     * <code>fromComponent</code> to the <code>forValue</code>.
     * Otherwise just return the <code>forValue</code> as is.</p>
     *
     * @param forValue      - the basic id-reference value.
     * @param fromComponent - the component that holds the
     *                      code>forValue</code>.
     *
     * @return the (possibly augmented) <code>forValue<code>.
     */
    protected String augmentIdReference(String forValue,
                                        UIComponent fromComponent) {

        int forSuffix = forValue.lastIndexOf(UIViewRoot.UNIQUE_ID_PREFIX);
        if (forSuffix <= 0) {
            // if the for-value doesn't already have a suffix present
            String id = fromComponent.getId();
            int idSuffix = id.lastIndexOf(UIViewRoot.UNIQUE_ID_PREFIX);
            if (idSuffix > 0) {
                // but the component's own id does have a suffix
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Augmenting for attribute with " +
                                id.substring(idSuffix) +
                                " suffix from Id attribute");
                }
                forValue += id.substring(idSuffix);
            }
        }
        return forValue;

    }


    /**
     * <p>Render nested child components by invoking the encode methods
     * on those components, but only when the <code>rendered</code>
     * property is <code>true</code>.</p>
     *
     * @param context FacesContext for the current request
     * @param component the component to recursively encode
     *
     * @throws IOException if an error occurrs during the encode process
     */
    protected void encodeRecursive(FacesContext context, UIComponent component)
          throws IOException {

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

        // Render this component and its children recursively
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator<UIComponent> kids = getChildren(component);
            while (kids.hasNext()) {
                UIComponent kid = kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);

    }


    /**
     * @param component <code>UIComponent</code> for which to extract children
     *
     * @return an Iterator over the children of the specified
     *  component, selecting only those that have a
     *  <code>rendered</code> property of <code>true</code>.
     */
    protected Iterator<UIComponent> getChildren(UIComponent component) {

        int childCount = component.getChildCount();
        if (childCount > 0) {
            return component.getChildren().iterator();
        } else {
            return Collections.<UIComponent>emptyList().iterator();
        }

    }


    /**
     * @param context the FacesContext for the current request
     * @param component the UIComponent whose value we're interested in
     *
     * @return the value to be rendered and formats it if required. Sets to
     *  empty string if value is null.
     */
    protected String getCurrentValue(FacesContext context,
                                     UIComponent component) {

        if (component instanceof UIInput) {
            Object submittedValue = ((UIInput) component).getSubmittedValue();
            if (submittedValue != null) {
                return (String) submittedValue;
            }
        }

        String currentValue = null;
        Object currentObj = getValue(component);
        if (currentObj != null) {
            currentValue = getFormattedValue(context, component, currentObj);
        }
        return currentValue;

    }


    /**
     * Renderers override this method to write appropriate HTML content into
     * the buffer.
     *
     * @param context the FacesContext for the current request
     * @param component the UIComponent of interest
     * @param currentValue <code>component</code>'s current value
     *
     * @throws IOException if an error occurs rendering the text
     */
    protected void getEndTextToRender(FacesContext context,
                                      UIComponent component,
                                      String currentValue) throws IOException {

        // no-op unless overridden

    }


    /**
     * @param component Component from which to return a facet
     * @param name      Name of the desired facet
     *
     * @return the specified facet from the specified component, but
     *  <strong>only</strong> if its <code>rendered</code> property is
     *  set to <code>true</code>.
     */
    protected UIComponent getFacet(UIComponent component, String name) {

        UIComponent facet = null;
        if (component.getFacetCount() > 0) {
            facet = component.getFacet(name);
            if ((facet != null) && !facet.isRendered()) {
                facet = null;
            }
        }
        return (facet);

    }


    /**
     * Locates the component identified by <code>forComponent</code>
     *
     * @param context the FacesContext for the current request
     * @param forComponent - the component to search for
     * @param component    - the starting point in which to begin the search
     *
     * @return the component with the the <code>id</code that matches
     *         <code>forComponent</code> otheriwse null if no match is found.
     */
    protected UIComponent getForComponent(FacesContext context,
                                          String forComponent,
                                          UIComponent component) {

        if (null == forComponent || forComponent.length() == 0) {
            return null;
        }

        UIComponent result = null;
        UIComponent currentParent = component;
        try {
            // Check the naming container of the current 
            // component for component identified by
            // 'forComponent'
            while (currentParent != null) {
                // If the current component is a NamingContainer,
                // see if it contains what we're looking for.
                result = currentParent.findComponent(forComponent);
                if (result != null) {
                    break;
                }
                // if not, start checking further up in the view
                currentParent = currentParent.getParent();
            }

            // no hit from above, scan for a NamingContainer
            // that contains the component we're looking for from the root.    
            if (result == null) {
                result =
                      findUIComponentBelow(context.getViewRoot(), forComponent);
            }
        } catch (Exception e) {
            // ignore - log the warning
        }
        // log a message if we were unable to find the specified
        // component (probably a misconfigured 'for' attribute
        if (result == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(MessageUtils.getExceptionMessageString(
                      MessageUtils.COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID,
                      forComponent));
            }
        }
        return result;

    }

    /**
     * Overloads getFormattedValue to take a advantage of a previously
     * obtained converter.
     * @param context the FacesContext for the current request
     * @param component UIComponent of interest
     * @param currentValue the current value of <code>component</code>
     * @param converter the component's converter
     * @return the currentValue after any associated Converter has been
     *  applied
     *
     * @throws ConverterException if the value cannot be converted
     */
    protected String getFormattedValue(FacesContext context,
                                       UIComponent component,
                                       Object currentValue,
                                       Converter converter)
          throws ConverterException {

        // formatting is supported only for components that support
        // converting value attributes.
        if (!(component instanceof ValueHolder)) {
            if (currentValue != null) {
                return currentValue.toString();
            }
            return null;
        }

        if (converter == null) {
            // If there is a converter attribute, use it to to ask application
            // instance for a converter with this identifer.
            converter = ((ValueHolder) component).getConverter();
        }

        if (converter == null) {
            // if value is null and no converter attribute is specified, then
            // return a zero length String.
            if(currentValue == null) {
        	return "";
            }
            // Do not look for "by-type" converters for Strings
            if (currentValue instanceof String) {
                return (String) currentValue;
            }

            // if converter attribute set, try to acquire a converter
            // using its class type.

            Class converterType = currentValue.getClass();
            converter = Util.getConverterForClass(converterType, context);

            // if there is no default converter available for this identifier,
            // assume the model type to be String.
            if (converter == null) {
                return currentValue.toString();
            }
        }

        return converter.getAsString(context, component, currentValue);
    }


    /**
     * @param context the FacesContext for the current request
     * @param component UIComponent of interest
     * @param currentValue the current value of <code>component</code>
     *
     * @return the currentValue after any associated Converter has been
     *  applied
     *
     * @throws ConverterException if the value cannot be converted
     */
    protected String getFormattedValue(FacesContext context,
                                       UIComponent component,
                                       Object currentValue)
          throws ConverterException {

        return getFormattedValue(context, component, currentValue, null);

    }


    protected Iterator getMessageIter(FacesContext context,
                                      String forComponent,
                                      UIComponent component) {

        Iterator messageIter;
        // Attempt to use the "for" attribute to locate 
        // messages.  Three possible scenarios here:
        // 1. valid "for" attribute - messages returned
        //    for valid component identified by "for" expression.
        // 2. zero length "for" expression - global errors
        //    not associated with any component returned
        // 3. no "for" expression - all messages returned.
        if (null != forComponent) {
            if (forComponent.length() == 0) {
                messageIter = context.getMessages(null);
            } else {
                UIComponent result = getForComponent(context, forComponent,
                                                     component);
                if (result == null) {
                    messageIter = Collections.EMPTY_LIST.iterator();
                } else {
                    messageIter =
                          context.getMessages(result.getClientId(context));
                }
            }
        } else {
            messageIter = context.getMessages();
        }
        return messageIter;

    }


    /**
     * @param command the command which may have parameters
     *
     * @return an array of parameters
     */
    protected Param[] getParamList(UIComponent command) {

        if (command.getChildCount() > 0) {
            ArrayList<Param> parameterList = new ArrayList<Param>();

            for (UIComponent kid : command.getChildren()) {
                if (kid instanceof UIParameter) {
                    UIParameter uiParam = (UIParameter) kid;
                    Object value = uiParam.getValue();
                    Param param = new Param(uiParam.getName(),
                                            (value == null ? null :
                                             value.toString()));
                    parameterList.add(param);
                }
            }
            return parameterList.toArray(new Param[parameterList.size()]);
        } else {
            return EMPTY_PARAMS;
        }


    }


    protected Object getValue(UIComponent component) {

        // Make sure this method isn't being called except 
        // from subclasses that override getValue()!
        throw new UnsupportedOperationException();

    }


    /**
     * Renderers override this method to store the previous value
     * of the associated component.
     *
     * @param component the target component to which the submitted value
     *  will be set
     * @param value the value to set
     */
    protected void setSubmittedValue(UIComponent component, Object value) {

        // no-op unless overridden

    }


    /**
     * @param component the component of interest
     *
     * @return true if this renderer should render an id attribute.
     */
    protected boolean shouldWriteIdAttribute(UIComponent component) {

        String id;
        return (null != (id = component.getId()) &&
                    !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX));

    }


    protected String writeIdAttributeIfNecessary(FacesContext context,
                                                 ResponseWriter writer,
                                                 UIComponent component) {

        String id = null;
        if (shouldWriteIdAttribute(component)) {
            try {
                writer.writeAttribute("id", id = component.getClientId(context),
                                      "id");
            } catch (IOException e) {
                if (logger.isLoggable(Level.WARNING)) {
                    String message = MessageUtils.getExceptionMessageString
                          (MessageUtils.CANT_WRITE_ID_ATTRIBUTE_ERROR_MESSAGE_ID,
                           e.getMessage());
                    logger.warning(message);
                }
            }
        }
        return id;

    }

    protected void rendererParamsNotNull(FacesContext context,
                                         UIComponent component) {

        Util.notNull("context", context);
        Util.notNull("component", component);
        
    }


    protected boolean shouldEncode(UIComponent component) {

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "End encoding component {0} since rendered attribute is set to false",
                           component.getId());
            }
            return false;
        }
        return true;

    }


    protected boolean shouldDecode(UIComponent component) {

        if (Util.componentIsDisabledOrReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "No decoding necessary since the component {0} is disabled or read-only",
                           component.getId());
            }
            return false;
        }
        return true;

    }

    protected boolean shouldEncodeChildren(UIComponent component) {

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                            "Children of component {0} will not be encoded since this component's rendered attribute is false",
                            component.getId());
            }
            return false;
        }
        return true;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Recursively searches for {@link NamingContainer}s from the
     * given start point looking for the component with the <code>id</code>
     * specified by <code>forComponent</code>.
     *
     * @param startPoint   - the starting point in which to begin the search
     * @param forComponent - the component to search for
     *
     * @return the component with the the <code>id</code that matches
     *         <code>forComponent</code> otheriwse null if no match is found.
     */
    private static UIComponent findUIComponentBelow(UIComponent startPoint,
                                                    String forComponent) {

        UIComponent retComp = null;
        if (startPoint.getChildCount() > 0) {
            List<UIComponent> children = startPoint.getChildren();
            for (int i = 0, size = children.size(); i < size; i++) {
                UIComponent comp = children.get(i);

                if (comp instanceof NamingContainer) {
                    try {
                        retComp = comp.findComponent(forComponent);
                    } catch (IllegalArgumentException iae) {
                        continue;
                    }
                }

                if (retComp == null) {
                    if (comp.getChildCount() > 0) {
                        retComp = findUIComponentBelow(comp, forComponent);
                    }
                }

                if (retComp != null) {
                    break;
                }
            }
        }
        return retComp;

    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * <p>Simple class to encapsulate the name and value of a
     * <code>UIParameeter</code>.
     */
    public static class Param {


        public String name;
        public String value;

        
        // -------------------------------------------------------- Constructors


        public Param(String name, String value) {

            this.name = name;
            this.value = value;

        }

    }

} // end of class HtmlBasicRenderer