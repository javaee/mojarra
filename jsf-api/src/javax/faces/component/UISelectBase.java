/*
 * $Id: UISelectBase.java,v 1.6 2003/02/03 22:57:47 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * <p><strong>UISelectBase</strong> is a convenience base class for
 * {@link UISelectMany} and {@link UISelectOne} that abstracts out the
 * common behavior of these classes.</p>
 */

abstract class UISelectBase extends UIInput implements NamingContainer {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The NamingContainer implementation that we delegate to.</p>
     */
    private NamingContainer namespace = new NamingContainerSupport();


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Return an Iterator over {@link SelectItem} instances representing the
     * available options for this component, assembled from the set of
     * {@link UISelectItem} and/or {@link UISelectItems} components that are
     * direct children of this component.  If there are no such children, a
     * zero-length array is returned.</p>
     *
     * @param context The {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    protected Iterator getSelectItems(FacesContext context) {

        List list = new ArrayList();
        Iterator kids = getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) kid;
                list.add(new SelectItem(item.getItemValue(),
                                        item.getItemLabel(),
                                        item.getItemDescription()));
            } else if (kid instanceof UISelectItems) {
                Object value = kid.currentValue(context);
                if (value instanceof SelectItem) {
                    list.add(value);
                } else if (value instanceof SelectItem[]) {
                    SelectItem items[] = (SelectItem[]) value;
                    for (int i = 0; i < items.length; i++) {
                        list.add(items[i]);
                    }
                } else if (value instanceof Collection) {
                    Iterator elements = ((Collection) value).iterator();
                    while (elements.hasNext()) {
                        list.add((SelectItem) elements.next());
                    }
                } else if (value instanceof Map) {
                    Iterator keys = ((Map) value).keySet().iterator();
                    while (keys.hasNext()) {
                        Object key = keys.next();
                        if (key == null) {
                            continue;
                        }
                        Object val = ((Map) value).get(key);
                        if (val == null) {
                            continue;
                        }
                        list.add(new SelectItem(val.toString(), key.toString(),
                                                null));
                    }
                }
            }
        }
        return (list.iterator());

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


    // ------------------------------------------------ NamingContainer Methods


    public void addComponentToNamespace(UIComponent namedComponent) {
	namespace.addComponentToNamespace(namedComponent);
    }


    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namespace.removeComponentFromNamespace(namedComponent);
    }


    public UIComponent findComponentInNamespace(String name) {
	return namespace.findComponentInNamespace(name);
    }


    public synchronized String generateClientId() {
	return namespace.generateClientId();
    }


}
