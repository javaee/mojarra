/*
 * $Id: SelectItemsIterator.java,v 1.6 2004/07/12 14:26:04 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.ArrayList;

import javax.faces.model.SelectItem;


/**
 * <p>Package private class for iterating over the set of {@link SelectItem}s
 * for a parent {@link UISelectMany} or {@link UISelectOne}.</p>
 */

final class SelectItemsIterator implements Iterator {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct an iterator instance for the specified parent component.</p>
     *
     * @param parent The parent {@link UIComponent} whose children will be
     *  processed
     */
    public SelectItemsIterator(UIComponent parent) {

        this.parent = parent;
        this.kids = parent.getChildren().iterator();

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Iterator over the SelectItem elements pointed at by a
     * <code>UISelectItems</code> component, or <code>null</code>.</p>
     */
    private Iterator items = null;


    /**
     * <p>Iterator over the children of the parent component.</p>
     */
    private Iterator kids = null;


    /**
     * <p>The parent {@link UIComponent} whose children are being iterated.</p>
     */
    private UIComponent parent = null;


    // -------------------------------------------------------- Iterator Methods


    /**
     * <p>Return <code>true</code> if the iteration has more elements.</p>
     */
    public boolean hasNext() {

        if (items != null) {
            if (items.hasNext()) {
                return (true);
            } else {
                items = null;
            }
        }
        return (kids.hasNext());

    }


    /**
     * <p>Return the next element in the iteration.</p>
     *
     * @exception NoSuchElementException if there are no more elements
     */
    public Object next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (items != null) {
            return (items.next());
        }
        UIComponent kid = (UIComponent) kids.next();
        if (kid instanceof UISelectItem) {
            UISelectItem ui = (UISelectItem) kid;
            SelectItem item = (SelectItem) ui.getValue();
            if (item == null) {
                item = new SelectItem(ui.getItemValue(),
                                      ui.getItemLabel(),
                                      ui.getItemDescription(),
                                      ui.isItemDisabled());
            }
            return (item);
        } else if (kid instanceof UISelectItems) {
            UISelectItems ui = (UISelectItems) kid;
            Object value = ui.getValue();
            if (value instanceof SelectItem) {
                return (value);
            } else if (value instanceof SelectItem[]) {
                items = Arrays.asList((Object[]) value).iterator();
                return (next());
            } else if (value instanceof List) {
                items = ((List) value).iterator();
                return (next());
            } else if (value instanceof Map) {
                List list = new ArrayList();
                for (Iterator keys = ((Map) value).keySet().iterator();
                    keys.hasNext(); ) {

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

                items = list.iterator();
                return (next());
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }

    }


    /**
     * <p>Throw UnsupportedOperationException.</p>
     */
    public void remove() {

        throw new UnsupportedOperationException();

    }


}
