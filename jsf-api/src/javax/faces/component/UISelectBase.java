/*
 * $Id: UISelectBase.java,v 1.1 2002/06/14 21:31:14 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * <p><strong>UISelectBase</strong> is a convenience base class for
 * {@link UISelectMany} and {@link UISelectOne} that abstracts out the
 * common behavior of these classes.</p>
 */

abstract class UISelectBase extends UIComponentBase {


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return the available items list for this component.</p>
     */
    public Object getItems() {

        return (getAttribute("items"));

    }


    /**
     * <p>Set the available items for this component, which must be one of
     * the following.</p>
     * <ul>
     * <li>Array of {@link SelectItem}</li>
     * <li>Array of objects (typically Strings)</li>
     * <li>Array of Java primitive items (such as <code>int[]</code>)</li>
     * <li>Implementation of <code>java.util.List</code></li>
     * </ul>
     *
     * @param items The new available items
     *
     * @exception IllegalArgumentException if the specified items list is
     *  not one of the valid object types
     */
    public void setItems(Object items) {

        // FIXME - type check on the argument
        setAttribute("items", items);

    }


    /**
     * <p>Return the model reference expression for the available items
     * for this component.</p>
     */
    public String getItemsModelReference() {

        return ((String) getAttribute("itemsModelReference"));

    }


    /**
     * <p>Set the model reference expression for the available items
     * for this component.</p>
     *
     * @param itemsModelReference The new model reference expression (if any)
     */
    public void setItemsModelReference(String itemsModelReference) {

        setAttribute("itemsModelReference", itemsModelReference);

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Return the values of the requested attribute (or the values from
     * the corresponding model reference expression if the attribute is not
     * defined), as an array of {@link SelectItem}s.  If no data can be
     * acquired, return a zero-length array.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param attributeName Attribute name for the attribute defining the local
     *  values of the requested data (if any)
     * @param modelReference Model reference expression defining the external
     *  source of the requested data (if any)
     */
    protected SelectItem[] getAsItems(FacesContext context,
                                      String attributeName,
                                      String modelReference) {

        // Acquire the raw values of the requested data
        Object values = null;
        if (attributeName != null) {
            values = getAttribute(attributeName);
        }
        if ((values == null) && (modelReference != null)) {
            values = context.getModelValue(modelReference);
        }

        // Special case for array of SelectItem
        if (values instanceof SelectItem[]) {
            return ((SelectItem[]) values);
        }

        // Convert to a SelectItem array as required
        SelectItem results[] = new SelectItem[0];
        if (values == null) {
            ; // No change needed
        } else if (values instanceof List) {
            results = new SelectItem[((List) values).size()];
            Iterator items = ((List) values).iterator();
            int n = 0;
            while (items.hasNext()) {
                Object item = items.next();
                if (item == null) {
                    item = "";
                } else {
                    item = item.toString();
                }
                results[n++] = new SelectItem((String) item, (String) item,
                                              null);
            }
        } else if (values.getClass().isArray()) { // FIXME - primitives???
            results = new SelectItem[Array.getLength(values)];
            for (int i = 0; i < results.length; i++) {
                Object item = Array.get(values, i);
                if (item == null) {
                    item = "";
                } else {
                    item = item.toString();
                }
                results[i] = new SelectItem((String) item, (String) item,
                                            null);
            }
        } else {
            values = values.toString();
            results = new SelectItem[1];
            results[0] = new SelectItem((String) values, (String) values,
                                        null);
        }
        return (results);

    }


    /**
     * <p>Return the value of the requested attribute (or the value from the
     * corresponding model reference expression if the attribute is not
     * defined), as a single String.  If no data can be acquired, return
     * a zero-length String.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param attributeName Attribute name of the attribute defining the local
     *  value of the requested data (if any)
     * @param modelReference Model reference expression defining the external
     *  source of the requested data (if any)
     */
    protected String getAsString(FacesContext context, String attributeName,
                                 String modelReference) {

        // Acquire the raw value of the requested data
        Object value = null;
        if (attributeName != null) {
            value = getAttribute(attributeName);
        }
        if ((value == null) && (modelReference != null)) {
            value = context.getModelValue(modelReference);
        }

        // Convert to a String as required
        String result = "";
        if (value != null) {
            result = value.toString();
        }
        return (result);

    }


    /**
     * <p>Return the values of the requested attribute (or the values from
     * the corresponding model reference expression if the attribute is not
     * defined), as an array of Strings.  If no data can be acquired, return
     * a zero-length array of Strings.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param attributeName Attribute name for the attribute defining the local
     *  values of the requested data (if any)
     * @param modelReference Model reference expression defining the external
     *  source of the requested data (if any)
     */
    protected String[] getAsStrings(FacesContext context, String attributeName,
                                    String modelReference) {

        // Acquire the raw values of the requested data
        Object values = null;
        if (attributeName != null) {
            values = getAttribute(attributeName);
        }
        if ((values == null) && (modelReference != null)) {
            values = context.getModelValue(modelReference);
        }

        // Convert to a String array as required
        String results[] = new String[0];
        if (values == null) {
            ; // No change needed
        } else if (values instanceof List) {
            results = new String[((List) values).size()];
            Iterator items = ((List) values).iterator();
            int n = 0;
            while (items.hasNext()) {
                Object item = items.next();
                if (item == null) {
                    results[n++] = "";
                } else {
                    results[n++] = item.toString();
                }
            }
        } else if (values.getClass().isArray()) { // FIXME - primitives???
            results = new String[Array.getLength(values)];
            for (int i = 0; i < results.length; i++) {
                Object item = Array.get(values, i);
                if (item == null) {
                    results[i] = "";
                } else {
                    results[i] = item.toString();
                }
            }
        } else {
            results = new String[1];
            results[0] = values.toString();
        }
        return (results);

    }


}
