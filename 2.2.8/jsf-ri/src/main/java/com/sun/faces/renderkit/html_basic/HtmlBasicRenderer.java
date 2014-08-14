/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
import java.util.Collection;
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
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
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

    protected static final Param[] EMPTY_PARAMS = new Param[0];

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

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

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

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

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


    // Decodes Behaviors if any match the behavior source/event.
    // As a convenience, returns component id, but only if it
    // was retrieved.  This allows us to avoid duplicating
    // calls to getClientId(), which can be expensive for 
    // deep component trees.
    protected final String decodeBehaviors(FacesContext context, 
                                           UIComponent component)  {

        if (!(component instanceof ClientBehaviorHolder)) {
            return null;
        }

        ClientBehaviorHolder holder = (ClientBehaviorHolder)component;
        Map<String, List<ClientBehavior>> behaviors = holder.getClientBehaviors();
        if (behaviors.isEmpty()) {
            return null;
        }

        ExternalContext external = context.getExternalContext();
        Map<String, String> params = external.getRequestParameterMap();
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if (null != behaviorEvent) {
            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if (behaviorsForEvent != null && behaviorsForEvent.size() > 0) {
               String behaviorSource = params.get("javax.faces.source");
               String clientId = component.getClientId();
               if (isBehaviorSource(context, behaviorSource, clientId)) {
                   for (ClientBehavior behavior: behaviorsForEvent) {
                       behavior.decode(context, component);
                   }
               }

               return clientId;
            }
        }

        return null;
    }


    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @param behaviorSourceId the ID of the behavior source
     * @param componentClientId the client ID of the component being decoded
     * @return <code>true</code> if the behavior source is for the component
     *  being decoded, otherwise <code>false</code>
     */
    protected boolean isBehaviorSource(FacesContext ctx,
                                       String behaviorSourceId,
                                       String componentClientId) {

        return (behaviorSourceId != null && behaviorSourceId.equals(componentClientId));

    }


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
            if (id != null) {
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
                // value may not be a String...
                return submittedValue.toString();
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
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "Unable to find for component", e);
            }
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
                    if (!uiParam.isDisable()) {
                        Object value = uiParam.getValue();
                        Param param = new Param(uiParam.getName(),
                                                (value == null ? null :
                                                 value.toString()));
                        parameterList.add(param);
                    }
                }
            }
            return parameterList.toArray(new Param[parameterList.size()]);
        } else {
            return EMPTY_PARAMS;
        }


    }

    /**
     * Collections parameters for use with Behavior script rendering.
     * Similar to getParamList(), but returns a collection of 
     * ClientBehaviorContext.Parameter instances.
     *
     * @param command the command which may have parameters
     *
     * @return a collection of ClientBehaviorContext.Parameter instances.
     */
    protected Collection<ClientBehaviorContext.Parameter> getBehaviorParameters(
        UIComponent command) {

        ArrayList<ClientBehaviorContext.Parameter> params = null;
        int childCount = command.getChildCount();

        if (childCount > 0) {

            for (UIComponent kid : command.getChildren()) {
                if (kid instanceof UIParameter) {
                    UIParameter uiParam = (UIParameter) kid;
                    String name = uiParam.getName();
                    Object value = uiParam.getValue();

                    if ((name != null) && (name.length() > 0)) {

                        if (params == null) {
                            params = new ArrayList<ClientBehaviorContext.Parameter>(childCount);
                        }

                        params.add(new ClientBehaviorContext.Parameter(name, value));
                    }
                }
            }
        }

        return (params == null) ? Collections.<ClientBehaviorContext.Parameter>emptyList() : params;

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

        // By default we only write the id attribute if:
        //
        // - We have a non-auto-generated id, or...
        // - We have client behaviors.
        //
        // We assume that if client behaviors are present, they
        // may need access to the id (AjaxBehavior certainly does).

        String id;
        return (null != (id = component.getId()) &&
                    (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX) ||
                        ((component instanceof ClientBehaviorHolder) &&
                          !((ClientBehaviorHolder)component).getClientBehaviors().isEmpty())));
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

    /**
     * When rendering pass thru attributes, we need to take any
     * attached Behaviors into account.  The presence of a non-empty
     * Behaviors map can cause us to switch from optimized pass thru
     * attribute rendering to the unoptimized code path.  However,
     * in two very common cases - attaching action behaviors to 
     * commands and attaching value change behaviors to editable value
     * holders - the behaviors map is populated with behaviors that
     * are not handled by the pass thru attribute code - ie. the
     * behaviors are handled locally by the renderer.
     *
     * In order to optimize such cases, we check to see whether the
     * component's behaviors map actually contains behaviors only 
     * for these non-pass thru attributes.  If so, we can pass a
     * null behavior map into renderPassThruAttributes(), thus ensuring
     * that we can take advantage of the optimized pass thru rendering
     * logic.
     *
     * Note that in all cases where we use this method, we actually have
     * two behavior events that we want to check for - a low-level/dom
     * event (eg. "click", or "change") plus a high-level component
     * event (eg. "action", or "valueChange").
     *
     * @param component the component that we are rendering
     * @param domEventName the name of the dom-level event
     * @param componentEventName the name of the component-level event
     */
    protected static Map<String, List<ClientBehavior>> getPassThruBehaviors(
        UIComponent component,
        String domEventName,
        String componentEventName) {

        if (!(component instanceof ClientBehaviorHolder)) {
            return null;
        }

        Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder)component).getClientBehaviors();

        int size = behaviors.size();

        if ((size == 1) || (size == 2)) {
            boolean hasDomBehavior = behaviors.containsKey(domEventName);
            boolean hasComponentBehavior = behaviors.containsKey(componentEventName);

            // If the behavior map only contains behaviors for non-pass
            // thru attributes, return null.
            if (((size == 1) && (hasDomBehavior || hasComponentBehavior)) ||
                ((size == 2) && hasDomBehavior && hasComponentBehavior)) {
                return null;
            }
        }

        return behaviors;
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
     * <code>UIParameter</code>.
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


    /**
     * Structure to hold common info used by Select* components
     * to reduce the number of times component attributes are evaluated
     * when rendering options.
     */
    public static class OptionComponentInfo {

        String disabledClass;
        String enabledClass;
        String selectedClass;
        String unselectedClass;
        boolean disabled;
        boolean hideNoSelection;

        public OptionComponentInfo(String disabledClass,
                                   String enabledClass,
                                   boolean disabled,
                                   boolean hideNoSelection) {

            this(disabledClass, enabledClass, null, null, disabled, hideNoSelection);

        }


        public OptionComponentInfo(String disabledClass,
                                   String enabledClass,
                                   String unselectedClass,
                                   String selectedClass,
                                   boolean disabled,
                                   boolean hideNoSelection) {

            this.disabledClass = disabledClass;
            this.enabledClass = enabledClass;
            this.unselectedClass = unselectedClass;
            this.selectedClass = selectedClass;
            this.disabled = disabled;
            this.hideNoSelection = hideNoSelection;
            
        }

        public String getDisabledClass() {
            return disabledClass;
        }

        public String getEnabledClass() {
            return enabledClass;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public boolean isHideNoSelection() {
            return hideNoSelection;
        }

        public String getSelectedClass() {
            return selectedClass;
        }

        public String getUnselectedClass() {
            return unselectedClass;
        }
        
    }
    

} // end of class HtmlBasicRenderer
