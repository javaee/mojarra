/*
 * $Id: HtmlBasicRenderer.java,v 1.111 2006/07/25 21:06:04 rlubke Exp $
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

// HtmlBasicRenderer.java

package com.sun.faces.renderkit.html_basic;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.MessageFactory;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>HtmlBasicRenderer</B> is a base class for implementing renderers
 * for HtmlBasicRenderKit.
 */

public abstract class HtmlBasicRenderer extends Renderer {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //
    // Log instance for this class
    protected static final Logger logger = 
            Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);
   
    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public static final String SCRIPT_ELEMENT = "script";
    public static final String SCRIPT_LANGUAGE = "language";
    public static final String SCRIPT_TYPE = "type";
    public static final String SCRIPT_LANGUAGE_JAVASCRIPT = "JavaScript";

    public static final String CLEAR_HIDDEN_FIELD_FN_NAME = 
         "clearFormHiddenParams";       
      
    public HtmlBasicRenderer() {
        super();
    }   

    //
    // Methods From Renderer

    public void addGenericErrorMessage(FacesContext facesContext,
                                       UIComponent component,
                                       String messageId, String param) {
        Object[] params = new Object[3];
        params[0] = param;
        facesContext.addMessage(component.getClientId(facesContext),
            MessageFactory.getMessage(facesContext, messageId, params));
    }


    public void decode(FacesContext context, UIComponent component) {

        if (context == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "component"));
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "Begin decoding component " + component.getId());
        }

        if (!(component instanceof UIInput)) {
            // decode needs to be invoked only for components that are
            // instances or subclasses of UIInput.
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No decoding necessary since the component "
                          + component.getId() +
                          " is not an instance or a sub class of UIInput");
            }
            return;
        }    

        // If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
        if (Util.componentIsDisabledOrReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No decoding necessary since the component " +
                          component.getId() + " is disabled");
            }
            return;
        }

        String clientId = component.getClientId(context);
        assert (clientId != null);
        Map<String,String> requestMap = context.getExternalContext().getRequestParameterMap();
        // Don't overwrite the value unless you have to!
        if (requestMap.containsKey(clientId)) {
            String newValue = requestMap.get(clientId);
            setSubmittedValue(component, newValue);
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("new value after decoding" + newValue);
            }
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "End decoding component " + component.getId());
        }
    }

    public boolean getRendersChildren() {
	return true;
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        String currentValue = null;
        ResponseWriter writer = null;       

        if (context == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "component"));
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
                          " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        writer = context.getResponseWriter();
        assert (writer != null);

        currentValue = getCurrentValue(context, component);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Value to be rendered " + currentValue);
        }
        getEndTextToRender(context, component, currentValue);
    }


    /**
     * <p>Render nested child components by invoking the encode methods
     * on those components, but only when the <code>rendered</code>
     * property is <code>true</code>.</p>
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
     * <p>Return an Iterator over the children of the specified
     * component, selecting only those that have a
     * <code>rendered</code> property of <code>true</code>.</p>
     *
     * @param component <code>UIComponent</code> for which to extract children
     */
    protected Iterator getChildren(UIComponent component) {
        int childCount = component.getChildCount();
        if (childCount > 0) {
            return new RenderedChildIterator(component
                  .getChildren().iterator());
        } else {
            return Collections.EMPTY_LIST.iterator();
        }
    }

    



    /**
     * Gets value to be rendered and formats it if required. Sets to empty
     * string if value is null.
     */
    protected String getCurrentValue(FacesContext context, UIComponent component) {

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


    protected Object getValue(UIComponent component) {
        // Make sure this method isn't being called except 
        // from subclasses that override getValue()!
        throw new UnsupportedOperationException();
    }


    /**
     * Renderers override this method to write appropriate HTML content into
     * the buffer.
     */
    protected void getEndTextToRender(FacesContext context, UIComponent component,
                                      String currentValue) throws IOException {
        return;
    }


    /**
     * Renderers override this method to store the previous value
     * of the associated component.
     */
    protected void setSubmittedValue(UIComponent component, Object value) {
    }


    /**
     * Renderers override this method in case output value needs to be
     * formatted
     */
    protected String getFormattedValue(FacesContext context, UIComponent component,
                                       Object currentValue)
        throws ConverterException {

        String result = null;
        // formatting is supported only for components that support
        // converting value attributes.
        if (!(component instanceof ValueHolder)) {
            if (currentValue != null) {
                result = currentValue.toString();
            }
            return result;
        }

        Converter converter = null;

        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        converter = ((ValueHolder) component).getConverter();


        // if value is null and no converter attribute is specified, then
        // return a zero length String.
        if (converter == null && currentValue == null) {
            return "";
        }

        if (converter == null) {
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
                result = currentValue.toString();
                return result;
            }
        }

        return converter.getAsString(context, component, currentValue);

    }


    public String convertClientId(FacesContext context, String clientId) {
        return clientId;
    }


    protected Iterator getMessageIter(FacesContext context,
                                      String forComponent,
                                      UIComponent component) {
        Iterator messageIter = null;
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
     * Locates the component identified by <code>forComponent</code>
     *
     * @param forComponent - the component to search for
     * @param component    - the starting point in which to begin the search
     * @return the component with the the <code>id</code that matches
     *         <code>forComponent</code> otheriwse null if no match is found.
     */
    protected UIComponent getForComponent(FacesContext context,
                                          String forComponent, UIComponent component) {
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
                if (result != null)
                    break;
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
                    new Object[]{forComponent}));
            }
        }
        return result;
    }


    /**
     * <p>Recursively searches for {@link NamingContainer}s from the
     * given start point looking for the component with the <code>id</code>
     * specified by <code>forComponent</code>.
     *
     * @param startPoint   - the starting point in which to begin the search
     * @param forComponent - the component to search for
     * @return the component with the the <code>id</code that matches
     *         <code>forComponent</code> otheriwse null if no match is found.
     */
    private UIComponent findUIComponentBelow(UIComponent startPoint, String forComponent) {
        UIComponent retComp = null;
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

            if (retComp != null)
                break;
        }
        return retComp;
    }

    /**
     * <p>Return the specified facet from the specified component, but
     * <strong>only</strong> if its <code>rendered</code> property is
     * set to <code>true</code>.
     *
     * @param component Component from which to return a facet
     * @param name      Name of the desired facet
     */
    protected UIComponent getFacet(UIComponent component, String name) {

        UIComponent facet = component.getFacet(name);
        if ((facet != null) && !facet.isRendered()) {
            facet = null;
        }
        return (facet);

    }


    /**
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
                	    (MessageUtils.CANT_WRITE_ID_ATTRIBUTE_ERROR_MESSAGE_ID, e.getMessage());
                	logger.warning(message);
                }
            }
        }
        return id;
    }

    /**
     * @param command the command which may have parameters
     * @return an array of parameters
     */
    protected Param[] getParamList(UIComponent command) {
        
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
        
    }

    /**
     * <p>Simple class to encapsulate the name and value of a 
     * <code>UIParameeter</code>.
     */
    public static class Param {
        
        public String name;
        public String value;
        
        public Param(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }



   
    
    /**
     * <p>This <code>Iterator</code> is used to Iterator over
     * children components that are set to be rendered.</p>
     */
    private static class RenderedChildIterator implements Iterator {
        
        Iterator childIterator;
        boolean hasNext;
        Object child;
        
        // -------------------------------------------------------- Constructors
        
        
        private RenderedChildIterator(Iterator childIterator) {
            
            this.childIterator = childIterator;
            update();
            
        }
        
        
        // ----------------------------------------------- Methods from Iterator


        public void remove() {
            
            throw new UnsupportedOperationException();
            
        }

        public boolean hasNext() {
            
            return hasNext;
            
        }

        public Object next() {
            
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            Object temp = child;
            update();                         
            return temp;
            
        }
        
        // ----------------------------------------------------- Private Methods

        /**
         * <p>Moves the internal pointer to the next renderable
         * component skipping any that are not to be rendered.</p>
         */
        private void update() {

            while (childIterator.hasNext()) {
                UIComponent comp = (UIComponent) childIterator.next();
                if (comp.isRendered()) {
                    child = comp;
                    hasNext = true;
                    return;
                }
            }

            hasNext = false;
            child = null;

        }
    }

} // end of class HtmlBasicRenderer
