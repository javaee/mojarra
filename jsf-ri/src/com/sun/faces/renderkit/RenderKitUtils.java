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
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer.Param;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <p>A set of utilities for use in {@link RenderKit}s.</p>
 */
public class RenderKitUtils {    

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
          "rows",
          "rev",         
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
     * <p>JavaScript to be rendered when a commandLink is used.
     * This may be expaned to include other uses.</p>
     */
    private static volatile String SUN_JSF_JS = null;        
                          
    
    protected static final Logger LOGGER = 
            Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);
          

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
                    MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
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
                                            item.isItemDisabled(),
                                            item.isItemEscaped()));
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
                        list.add(new SelectItem(val, 
                                                key.toString()));
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

        // didn't find the passthrough list.  Scan the 
        // Component attribute map and store any attributes found
        // in a list for later use
        
        boolean attrFound = false;
        for (String attr : PASSTHROUGH_ATTRIBUTES) {
            Object value = attrMap.get(attr);
            attrFound = value != null;
            if (attrFound) {
                break;
            }

        }

        return (attrFound);

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
                        MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "excludes"));
        }

        if (hasPassThruAttributes(component)) {

            Map<String, Object> attrMap = component.getAttributes();
           
            if (excludes.length > 0) {
                Arrays.sort(excludes);
            }
            for (String attrName : PASSTHROUGH_ATTRIBUTES) {                
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

                    boolean isXhtml = writer.getContentType().equals(RIConstants.XHTML_CONTENT_TYPE);
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
                        MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "excludes"));
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
     */
    private static String[][] buildTypeArrayFromString(String accept) {
        String[][] arrayAccept = new String[MAX_CONTENT_TYPES][MAX_CONTENT_TYPE_PARTS];
        // return if empty
        if ((accept == null) || (accept.length() == 0))
            return arrayAccept;
        // some helper variables
        String token = null;
        StringBuilder typeSubType = null;
        String type = null;
        String subtype = null;
        String level = null;
        String quality = null;

        // Parse "types"        
        String[] types = Util.split(accept, CONTENT_TYPE_DELIMITER);
        int index = -1;
        for (int i=0; i<types.length; i++) {
            token = types[i].trim();
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
                type = typeSubTypeParts[0].trim();
                subtype = typeSubTypeParts[1].trim();
            } else {
                type = typeSubType.toString();
                subtype = "";
            }
            // check quality and assign values
            if (quality.equals("not set")) {
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
    private static String[][] findMatch(String[][] clientContentTypes, String[][] serverSupportedContentTypes,
        String[][] preferredContentType) {
        // client/server array index variables
        int cidx = 0;
        int sidx = 0;
                                                                                                                         
        String browserType = "";
        String serverType = "";
        // result array
        String[][] results = new String[MAX_CONTENT_TYPES][MAX_CONTENT_TYPE_PARTS];
        int resultidx = -1;
        // the highest quality
        double highestQFactor = 0;
        // the record with the highest quality
        int idx = 0;
        for (sidx = 0; sidx < MAX_CONTENT_TYPES; sidx++) {
            // get server type
            serverType = serverSupportedContentTypes[sidx][1];
            if (serverType != null) {
                for (cidx = 0; cidx < MAX_CONTENT_TYPES; cidx++) {
                    // get browser type
                    browserType = clientContentTypes[cidx][1];
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
            for (int i=0; i<=resultidx; i++) {
                if ((Double.parseDouble(results[i][0]) == highestQFactor) &&
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
     * @param writer the <code>ResponseWriter</code>
     * @param context the <code>FacesContext</code> for the current request
     * @throws java.io.IOException if an error occurs writing to the response
     */
    public static void renderFormInitScript(ResponseWriter writer,
                                            FacesContext context)
          throws IOException {
        WebConfiguration webConfig =
              WebConfiguration.getInstance(context.getExternalContext());

        if (webConfig
              .getBooleanContextInitParameter(BooleanWebContextInitParameter.ExternalizeJavaScript)) {
            // PENDING
            // We need to look into how to make this work in a portlet environment.
            // For the time being, this feature will need to be disabled when running
            // in a portlet.
            String mapping = Util.getFacesMapping(context);
            String uri = null;
            if ((mapping != null) && (Util.isPrefixMapped(mapping))) {
                uri = mapping + '/' + RIConstants.SUN_JSF_JS_URI;
            } else {
                uri = '/' + RIConstants.SUN_JSF_JS_URI + mapping;
            }
            writer.write('\n');
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src",
                                  context.getExternalContext()
                                        .getRequestContextPath() + uri,
                                  null);
            writer.endElement("script");
            writer.write("\n");
        } else {
            boolean isXhtml =
                  RIConstants.XHTML_CONTENT_TYPE
                        .equals(writer.getContentType());

            writer.write('\n');
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("language", "Javascript", null);
            if (isXhtml) {
                writer.write("\n//<![CDATA[");
            } else {
                writer.write("\n<!--");
            }

            writeSunJS(context, writer);

            if (isXhtml) {
                writer.write("\n//]]>\n");
            } else {
                writer.write("\n//-->\n");
            }
            writer.endElement("script");
            writer.write("\n");
        }
    }

    /**
     * <p>Returns a string that can be inserted into the <code>onclick</code>
     * handler of a command.  This string will add all request parameters
     * as well as the client ID of the activated command to the form as
     * hidden input parameters, update the target of the link if necessary,
     * and handle the form submission.  The content of {@link #SUN_JSF_JS}
     * must be rendered prior to using this method.</p>
     * @param formClientId the client ID of the form
     * @param commandClientId the client ID of the command
     * @param target
     *@param params the nested parameters, if any @return a String suitable for the <code>onclick</code> handler
     *  of a command
     */
    public static String getCommandLinkOnClickScript(String formClientId,
                                                     String commandClientId,
                                                     String target,
                                                     Param[] params) {

        StringBuilder sb = new StringBuilder(256);    
        sb.append("if(typeof jsfcljs == 'function'){jsfcljs(document.forms['");
        sb.append(formClientId);        
        sb.append("'],'");
        sb.append(commandClientId).append(',').append(commandClientId);
        for (Param param : params) {         
            sb.append(',');
            sb.append(param.name);
            sb.append(',');
            sb.append(param.value);            
        }          
        sb.append("','");
        sb.append(target);
        sb.append("');}return false");

        return sb.toString();
        
    }


    /**
     * <p>This is a utility method for compressing multi-lined javascript.
     * In the case of {@link #SUN_JSF_JS} it offers about a 47% decrease
     * in length.</p>
     * 
     * <p>For our purposes, compression is just trimming each line and 
     * then writing it out.  It's pretty simplistic, but it works.</p>
     * 
     * @param JSString the string to compress
     * @return the compressed string
     */
    public static String compressJS(String JSString) {

        BufferedReader reader = new BufferedReader(new StringReader(JSString));
        StringWriter writer = new StringWriter(1024);   
        writer.write('\n');
        try {
            for (String line = reader.readLine();
                 line != null;
                 line = reader.readLine()) {

                line = line.trim();
                writer.write(line);
            }
            return writer.toString();
        } catch (IOException ioe) {
            // won't happen
        }
        return null;

    }


    /**
     * <p>Return the implementation JavaScript.  If compression
     * is enabled, the result will be compressed.</p>
     * 
     * @param context - the <code>FacesContext</code> for the current request
     * @param writer - the <code>Writer</code> to write the JS to
     * 
     * @return the implemenation javascript as a String
     */
    public static void writeSunJS(FacesContext context, Writer writer) 
    throws IOException {
        loadSunJsfJs(context);
        writer.write(SUN_JSF_JS);
    }
    
    
    // --------------------------------------------------------- Private Methods


    /**
     * <p>Loads the contents of the sunjsf.js file into memory removing any
     * comments/empty lines it encoutners, and, if enabled, compressing the 
     * result.</p>
     * @return the JavaScript sans comments and blank lines
     */
    private static void loadSunJsfJs(FacesContext context) {

        if (SUN_JSF_JS == null) {
            synchronized (XHTML_ATTR_PREFIX) {
                if (SUN_JSF_JS == null) {
                    BufferedReader reader = null;
                    try {
                        URL url = Util.getCurrentLoader(null)
                              .getResource("com/sun/faces/sunjsf.js");
                        if (url == null) {
                            LOGGER.severe(
                                  "jsf.renderkit.resstatemgr.clientbuf_not_integer");
                        }
                        URLConnection conn = url.openConnection();
                        conn.setUseCaches(false);
                        InputStream input = conn.getInputStream();
                        reader = new BufferedReader(
                                    new InputStreamReader(input));                        
                        StringBuilder builder = new StringBuilder(128);
                        builder.append('\n');
                        for (String line = reader.readLine();
                             line != null;
                             line = reader.readLine()) {

                            String temp = line.trim();
                            if (temp.length() == 0
                                || temp.startsWith("/*")
                                || temp.startsWith("*")
                                || temp.startsWith("*/")
                                || temp.startsWith("//")) {
                                continue;
                            }
                            builder.append(line).append('\n');
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        if (WebConfiguration
                              .getInstance(context.getExternalContext())
                              .getBooleanContextInitParameter(
                                    BooleanWebContextInitParameter.CompressJavaScript)) {
                            SUN_JSF_JS = compressJS(builder.toString());
                        } else {
                            SUN_JSF_JS = builder.toString();
                        }
                    } catch (IOException ioe) {
                        LOGGER.log(Level.SEVERE,
                                   "jsf.renderkit.resstatemgr.clientbuf_not_integer",
                                   ioe);
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException ioe) {
                                // ignore                    
                            }
                        }
                    }
                }
            }
        }
    }
                           
} // END RenderKitUtils