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

package renderkits.renderkit.xul;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import renderkits.util.Util;

/**
 * <B>BaseRenderer</B> is a base class for implementing renderers
 * for <code>SVGRenderKit</code>.
 */

public abstract class BaseRenderer extends Renderer {


    protected static Logger logger =
          Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);

    /** @return true if this renderer should render an id attribute. */
    protected boolean shouldWriteIdAttribute(UIComponent component) {
        String id;
        return (null != (id = component.getId()) &&
                !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX));
    }

    protected void writeIdAttributeIfNecessary(FacesContext context,
                                               ResponseWriter writer,
                                               UIComponent component) {
        String id;
        if (shouldWriteIdAttribute(component)) {
            try {
                writer.writeAttribute("id", component.getClientId(context),
                                      "id");
            } catch (IOException e) {
                if (logger.isLoggable(Level.WARNING)) {
                    // PENDING I18N
                    logger.warning("Can't write ID attribute" + e.getMessage());
                }
            }
        }
    }

    /**
     * Gets value to be rendered and formats it if required. Sets to empty
     * string if value is null.
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
     * Renderers override this method in case output value needs to be
     * formatted
     */
    protected String getFormattedValue(FacesContext context,
                                       UIComponent component,
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
            converter = Util.getConverterForClass(converterType);

            // if there is no default converter available for this identifier,
            // assume the model type to be String.
            if (converter == null) {
                result = currentValue.toString();
                return result;
            }
        }

        return converter.getAsString(context, component, currentValue);

    }

    protected Object getValue(UIComponent component) {
        if (component instanceof ValueHolder) {
            Object value = ((ValueHolder) component).getValue();
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("component.getValue() returned " + value);
            }
            return value;
        }

        return null;
    }

    /**
     * Locates the component identified by <code>forComponent</code>
     *
     * @param forComponent - the component to search for
     * @param component    - the starting point in which to begin the search
     *
     * @return the component with the the <code>id</code that matches
     *         <code>forComponent</code> otheriwse null if no match is found.
     */
    private UIComponent getForComponent(FacesContext context,
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
        } catch (Throwable t) {
            //PENDING i18n
            throw new RuntimeException("Component not found:" + forComponent);
        }
        // log a message if we were unable to find the specified
        // component (probably a misconfigured 'for' attribute
        if (result == null) {
            if (logger.isLoggable(Level.WARNING)) {
                //PENDING i18n
                logger.warning("Component not found in view:" + forComponent);
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
     *
     * @return the component with the the <code>id</code that matches
     *         <code>forComponent</code> otheriwse null if no match is found.
     */
    protected UIComponent findUIComponentBelow(UIComponent startPoint,
                                               String forComponent) {
        UIComponent retComp = null;
        List<UIComponent> children = startPoint.getChildren();
        for (int i = 0, size = children.size(); i < size; i++) {
            UIComponent comp = children.get(i);

            if (comp instanceof NamingContainer) {
                retComp = comp.findComponent(forComponent);
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
        return retComp;
    }

    /**
     * <p>Return an Iterator over the children of the specified
     * component, selecting only those that have a
     * <code>rendered</code> property of <code>true</code>.</p>
     *
     * @param component <code>UIComponent</code> for which to extract children
     */
    protected Iterator<UIComponent> getChildren(UIComponent component) {

        List<UIComponent> results = new ArrayList<UIComponent>();
        Iterator<UIComponent> kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (kid.isRendered()) {
                results.add(kid);
            }
        }
        return (results.iterator());

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
}

