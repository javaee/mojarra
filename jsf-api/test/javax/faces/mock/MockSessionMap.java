/*
 * $Id: MockSessionMap.java,v 1.3 2004/02/04 23:39:14 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;


final class MockSessionMap implements Map {


    public MockSessionMap(HttpSession session) {
        this.session = session;
    }


    private HttpSession session = null;
 

    public void clear() {
        Iterator keys = keySet().iterator();
        while (keys.hasNext()) {
            session.removeAttribute((String) keys.next());
        }
    }


    public boolean containsKey(Object key) {
        return (session.getAttribute(key(key)) != null);
    }


    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = session.getAttribute((String) keys.nextElement());
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }


    public Set entrySet() {
        Set set = new HashSet();
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(session.getAttribute((String) keys.nextElement()));
        }
        return (set);
    }


    public boolean equals(Object o) {
        return (session.equals(o));
    }


    public Object get(Object key) {
        return (session.getAttribute(key(key)));
    }


    public int hashCode() {
        return (session.hashCode());
    }


    public boolean isEmpty() {
        return (size() < 1);
    }


    public Set keySet() {
        Set set = new HashSet();
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }


    public Object put(Object key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = session.getAttribute(skey);
        session.setAttribute(skey, value);
        return (previous);
    }


    public void putAll(Map map) {
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            session.setAttribute(key, map.get(key));
        }
    }


    public Object remove(Object key) {
        String skey = key(key);
        Object previous = session.getAttribute(skey);
        session.removeAttribute(skey);
        return (previous);
    }


    public int size() {
        int n = 0;
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }


    public Collection values() {
        List list = new ArrayList();
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(session.getAttribute((String) keys.nextElement()));
        }
        return (list);
    }


    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }
    }


}
