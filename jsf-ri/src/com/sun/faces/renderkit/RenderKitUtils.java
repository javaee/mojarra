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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.renderkit;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.model.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map.Entry;

import com.sun.faces.RIConstants;
import com.sun.faces.util.MessageUtils;

/**
 * <p>A set of utilities for use in {@link RenderKit}s.</p>
 */
public class RenderKitUtils {

    /**
     * <p>The key under which passthrough attributes of a component
     * may be stored under (within the component attribute map).</p>
     */
    private static final String PASSTHROUGH_LIST_ATTR =
          "javax.faces.component.PassThroughAttributes";

    /**
     * <p>A <code>request</code> scope attribute defining the
     * {@link RenderKit} being used for this request.
     */
    private static final String RENDER_KIT_IMPL_REQ =
          RIConstants.FACES_PREFIX + "renderKitImplForRequest";

    /**
     * <p>The prefix to append to certain attributes when renderking
     * <code>XHTML Transitional</code> content.
     */
    private static final String XHTML_ATTR_PREFIX = "xml:";

    /**
     * <p>A placholder value for calling the non-exclude version
     * of @{link #renderPassThruAttributes} and 
     * {@link #renderXHTMLStyleBooleanAttributes}.</p>
     */
    private static final String[] EMPTY_EXCLUDES = new String[0];
    
    /**
     * <p><code>Boolean</code> attributes to be rendered 
     * using <code>XHMTL</code> semantics.
     */
    private static final String[] BOOLEAN_ATTRIBUTES = {
          "disabled", "ismap", "readonly"
    };

    /**
     * <p>An array of all passthrough attributes for the 
     * <code>Standard HTML RenderKit</code>
     */
    private static final String[] PASSTHROUGH_ATTRIBUTES = {
          "accept",
          "accesskey",
          "alt",
          "bgcolor",
          "border",
          "cellpadding",
          "cellspacing",
          "charset",
          "cols",
          "coords",
          "dir",
          "enctype",
          "frame",
          "height",
          "hreflang",
          "lang",
          "longdesc",
          "maxlength",
          "onblur",
          "onchange",
          "onclick",
          "ondblclick",
          "onfocus",
          "onkeydown",
          "onkeypress",
          "onkeyup",
          "onload",
          "onmousedown",
          "onmousemove",
          "onmouseout",
          "onmouseover",
          "onmouseup",
          "onreset",
          "onselect",
          "onsubmit",
          "onunload",
          "rel",
          "rev",
          "rows",
          "rules",
          "shape",
          "size",
          "style",
          "summary",
          "tabindex",
          "target",
          "title",
          "usemap",
          "width",
    };
    static {
        Arrays.sort(PASSTHROUGH_ATTRIBUTES);
    }

    /**
     * <p>An array of attributes that must be prefixed by
     * {@link #XHTML_ATTR_PREFIX} when rendering 
     * <code>XHTML Transitional</code> content.
     */
    private static final String[] XHTML_PREFIX_ATTRIBUTES = {
          "lang"
    };


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
            Map<String, Object> requestMap =
                  context.getExternalContext().getRequestMap();
            RenderKitFactory factory = (RenderKitFactory)
                  requestMap.get(RENDER_KIT_IMPL_REQ);
            if (factory != null) {
                renderKit = factory.getRenderKit(context, renderKitId);
            } else {
                factory = (RenderKitFactory)
                      FactoryFinder
                            .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                if (factory == null) {
                    throw new IllegalStateException();
                } else {
                    requestMap.put(RENDER_KIT_IMPL_REQ, factory);
                }
                renderKit = factory.getRenderKit(context, renderKitId);
            }
        }
        return renderKit.getResponseStateManager();

    }
    
    /**
     * <p>Return an Iterator over {@link javax.faces.model.SelectItem} 
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
     */
    public static Iterator<SelectItem> getSelectItems(FacesContext context,
                                                      UIComponent component) {

        if (context == null) {            
            throw new IllegalArgumentException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        ArrayList<SelectItem> list = new ArrayList<SelectItem>();
        for (UIComponent kid : component.getChildren()) {
            if (kid instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) kid;
                Object value = item.getValue();
                if (value == null) {
                    list.add(new SelectItem(item.getItemValue(),
                                            item.getItemLabel(),
                                            item.getItemDescription(),
                                            item.isItemDisabled()));
                } else if (value instanceof SelectItem) {
                    list.add((SelectItem) value);
                } else {
                    throw new IllegalArgumentException(
                        MessageUtils.getExceptionMessageString(
                            MessageUtils.VALUE_NOT_SELECT_ITEM_ID,
                            new Object[] {
                                  component.getId(),
                                  value.getClass().getName() }));
                }
            } else if (kid instanceof UISelectItems) {
                Object value = ((UISelectItems) kid).getValue();
                if (value instanceof SelectItem) {
                    list.add((SelectItem) value);
                } else if (value instanceof SelectItem[]) {
                    SelectItem[] items = (SelectItem[]) value;
                    for (SelectItem item : items) {
                        list.add(item);
                    }
                } else if (value instanceof Collection) {
                    for (Object element : ((Collection) value)) {
                        if (SelectItem.class.isInstance(element)) {
                            list.add((SelectItem) element);
                        } else {
                            throw new IllegalArgumentException(
                                  MessageUtils.getExceptionMessageString(
                                        MessageUtils.VALUE_NOT_SELECT_ITEM_ID,
                                        new Object[]{
                                              component.getId(),
                                              value.getClass().getName()}));
                        }
                    }
                } else if (value instanceof Map) {
                    Map optionMap = (Map) value;
                    for (Object o : optionMap.entrySet()) {
                        Entry entry = (Entry) o;
                        Object key = entry.getKey();
                        Object val = entry.getValue();
                        if (key == null || val == null) {
                            continue;
                        }
                        list.add(new SelectItem(key.toString(),
                                                val.toString()));
                    }
                } else {
                    throw new IllegalArgumentException(
                          MessageUtils.getExceptionMessageString(
                            MessageUtils.CHILD_NOT_OF_EXPECTED_TYPE_ID,
                            new Object[] {
                                  "UISelectItem/UISelectItems",
                                  component.getFamily(),
                                  component.getId()}));
                }
            }
        }
        return (list.iterator());

    }


    /**
     * <p>Used to determine if the specified #component has any passthrough
     * attributes defined.</p>
     * @param component the component
     * @return <code>true</code> if the component has passthrough attributes, 
     * otherwise <code>false</code>
     */
    public static boolean hasPassThruAttributes(UIComponent component) {

        Map<String, Object> attrMap = component.getAttributes();
        List<String> list = (List<String>) attrMap.get(PASSTHROUGH_LIST_ATTR);
        if (list == null) {
            // didn't find the passthrough list.  Scan the 
            // Component attribute map and store any attributes found
            // in a list for later use
            Set<String> keySet = component.getAttributes().keySet();
            String[] keys = keySet.toArray(new String[keySet.size()]);
            Arrays.sort(keys);
            for (String key : keys) {
                if (Arrays.binarySearch(PASSTHROUGH_ATTRIBUTES, key) > -1) {
                    if (list == null) {
                        list = new ArrayList<String>(8);
                        attrMap.put(PASSTHROUGH_LIST_ATTR, list);
                    }
                    list.add(key);
                }
            }
        }

        return (list != null);

    }


    /**
     * <p>Render any "passthru" attributes, where we simply just output the
     * raw name and value of the attribute.  This method is aware of the
     * set of HTML4 attributes that fall into this bucket.  Examples are
     * all the javascript attributes, alt, rows, cols, etc. </p>
     * 
     * <p>This version of the method allows the user to exclude certain 
     * attributes from being processed.</p>
     * @param context context the {@link FacesContext} of the current request
     * @param writer writer the {@link ResponseWriter} to be used when writing
     *  the attributes
     * @param component the component      
     * @throws IOException if an error occurs writing the attributes
     * @see RenderKitUtils#renderPassThruAttributes(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, String[]) 
     */
    public static void renderPassThruAttributes(FacesContext context,
                                                ResponseWriter writer,
                                                UIComponent component)
          throws IOException {

        renderPassThruAttributes(context, writer, component, EMPTY_EXCLUDES);

    }    

    /**
     * <p>Render any "passthru" attributes, where we simply just output the
     * raw name and value of the attribute.  This method is aware of the
     * set of HTML4 attributes that fall into this bucket.  Examples are
     * all the javascript attributes, alt, rows, cols, etc. </p>
     * 
     * <p>This version of the method allows the user to exclude certain 
     * attributes from being processed.</p>
     * 
     * @param context context the {@link FacesContext} of the current request
     * @param writer writer the {@link ResponseWriter} to be used when writing
     *  the attributes
     * @param component the component
     * @param excludes any attributes that should be excluded from writing to
     *  the response
     * @throws IOException if an error occurs writing the attributes
     */
    public static void renderPassThruAttributes(FacesContext context,
                                                ResponseWriter writer,
                                                UIComponent component,
                                                String[] excludes)
          throws IOException {

        assert (null != writer);
        assert (null != component);
        
        if (excludes == null) {
            throw new IllegalArgumentException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (hasPassThruAttributes(component)) {

            Map<String, Object> attrMap = component.getAttributes();
            List<String> passThroughList = (List<String>)
                  attrMap.get(PASSTHROUGH_LIST_ATTR);
            Collections.sort(passThroughList);
            if (excludes.length > 0) {
                Arrays.sort(excludes);
            }
            for (int i = 0, size = passThroughList.size(); i < size; i++) {
                String attrName = passThroughList.get(i);
                if (excludes.length > 0
                    && Arrays.binarySearch(excludes, attrName) > -1) {
                    continue;
                }
                Object value =
                      attrMap.get(attrName);
                if (value != null && shouldRenderAttribute(value)) {
                    if (context == null) {
                        context = FacesContext.getCurrentInstance();
                    }

                    boolean isXhtml = (context.getExternalContext()
                          .getRequestMap()
                          .get(RIConstants.CONTENT_TYPE_IS_XHTML) != null);
                    if (isXhtml) {
                        String prefixName = attrName;
                        if (Arrays.binarySearch(XHTML_PREFIX_ATTRIBUTES,
                                                attrName) > -1) {
                            prefixName = XHTML_ATTR_PREFIX + attrName;
                        }
                        writer.writeAttribute(prefixName, value, attrName);
                    } else {
                        writer.writeAttribute(attrName, value, attrName);
                    }
                }

            }
        }

    }


    /**
     * <p>Renders the attributes from {@link #BOOLEAN_ATTRIBUTES} 
     * using <code>XHMTL</code> semantics (i.e., disabled="disabled").</p>
     * @param writer writer the {@link ResponseWriter} to be used when writing
     *  the attributes
     * @param component the component
     * @throws IOException if an error occurs writing the attributes
     */
    public static void renderXHTMLStyleBooleanAttributes(ResponseWriter writer,
                                                         UIComponent component)
          throws IOException {

        renderXHTMLStyleBooleanAttributes(writer, component, EMPTY_EXCLUDES);

    }

    /**
     * <p>Renders the attributes from {@link #BOOLEAN_ATTRIBUTES} 
     * using <code>XHMTL</code> semantics (i.e., disabled="disabled").</p>
     * 
     * <p>This version of the method allows the user to exclude certain 
     * attributes from being processed.</p>
     * 
     * @param writer writer the {@link ResponseWriter} to be used when writing
     *  the attributes
     * @param component the component
     * @param excludes any attributes that should be excluded from writing to
     *  the response
     * @throws IOException if an error occurs writing the attributes
     */
    public static void renderXHTMLStyleBooleanAttributes(ResponseWriter writer,
                                                         UIComponent component,
                                                         String[] excludes)
          throws IOException {

        assert (writer != null);
        assert (component != null);

        if (excludes == null) {
            throw new IllegalArgumentException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (excludes.length > 0) {
            Arrays.sort(excludes);
        }

        Map attrMap = component.getAttributes();
        for (String attrName : BOOLEAN_ATTRIBUTES) {
            Object val = attrMap.get(attrName);
            if (val == null ||
                (excludes.length > 0
                 && Arrays.binarySearch(excludes, attrName) > -1)) {
                continue;
            }

            Boolean bool;
            if (!(val instanceof Boolean)) {
                bool = Boolean.valueOf(val.toString());
            } else {
                bool = (Boolean) val;
            }

            if (bool) {
                writer.writeAttribute(attrName,
                                      true,
                                      attrName);
            }
        }

    }


    // --------------------------------------------------------- Private Methods


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

        if (attributeVal instanceof Boolean &&
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
    
} // END RenderKitUtils