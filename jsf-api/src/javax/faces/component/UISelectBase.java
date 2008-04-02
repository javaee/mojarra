/*
 * $Id: UISelectBase.java,v 1.10 2003/03/13 01:11:58 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
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
     * @exception IllegalArgumentException if the value of a
     *  {@link UISelectItem} or {@link UISelectItems} is of the wrong type
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    protected Iterator getSelectItems(FacesContext context) {

        List list = new ArrayList();
        Iterator kids = getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UISelectItem) {
                Object value = ((UISelectItem) kid).currentValue(context);
                if (value == null) {
                    UISelectItem item = (UISelectItem) kid;
                    list.add(new SelectItem(item.getItemValue(),
                                            item.getItemLabel(),
                                            item.getItemDescription()));
                } else if (value instanceof SelectItem) {
                    list.add(value);
                } else {
                    throw new IllegalArgumentException // FIXME - i18n
                        ("Value is not a SelectItem");
                }
            } else if (kid instanceof UISelectItems) {
                Object value = ((UISelectItems) kid).currentValue(context);
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
                } else {
                    throw new IllegalArgumentException // FIXME - i18n
                        ("Value is not a SelectItem, SelectItem[], Collection, or Map");
                }
            }
        }
        return (list.iterator());

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
