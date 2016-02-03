/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

package com.sun.faces.config;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIViewRoot;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.FactoryFinder;
import javax.faces.render.RenderKit;
import javax.servlet.ServletContext;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * A special, minimal implementation of FacesContext used at application initialization time.
 * The ExternalContext returned by this FacesContext only exposes the ApplicationMap.
 */
class InitFacesContext extends FacesContext {

    private ExternalContext ec;
    private UIViewRoot viewRoot;
    private FacesContext orig;

    public InitFacesContext(ServletContext sc) {
        ec = new ServletContextAdapter(sc);
        orig = FacesContext.getCurrentInstance();
        setCurrentInstance(this);
    }

    public Application getApplication() {
        ApplicationFactory factory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return factory.getApplication();
    }

    public Iterator<String> getClientIdsWithMessages() {
        List<String> list = Collections.emptyList();
        return list.iterator();
    }

    public ExternalContext getExternalContext() {
        return ec;
    }

    public FacesMessage.Severity getMaximumSeverity() {
        return FacesMessage.SEVERITY_INFO;
    }

    public Iterator<FacesMessage> getMessages() {
        List<FacesMessage> list = Collections.emptyList();
        return list.iterator();
    }

    public Iterator<FacesMessage> getMessages(String clientId) {
        return getMessages();
    }

    public RenderKit getRenderKit() {
        return null;
    }

    public boolean getRenderResponse() {
        return true;
    }

    public boolean getResponseComplete() {
        return true;
    }

    public ResponseStream getResponseStream() {
        return null;
    }

    public void setResponseStream(ResponseStream responseStream) { }

    public ResponseWriter getResponseWriter() {
        return null;
    }

    public void setResponseWriter(ResponseWriter responseWriter) { }

    public UIViewRoot getViewRoot() {
        if (viewRoot == null) {
            viewRoot = new UIViewRoot();
            viewRoot.setLocale(Locale.getDefault());
        }
        return viewRoot;
    }

    public void setViewRoot(UIViewRoot root) { }

    public void addMessage(String clientId, FacesMessage message) { }

    public void release() {
        setCurrentInstance(orig);
    }

    public void renderResponse() { }

    public void responseComplete() { }

    @Override
    public ELContext getELContext() {
        return new ELContext() {
            public ELResolver getELResolver() {
                return null;
            }

            public FunctionMapper getFunctionMapper() {
                return null;
            }

            public VariableMapper getVariableMapper() {
                return null;
            }
        };
    }

    private static class ServletContextAdapter extends ExternalContext {

        private ServletContext servletContext = null;
        private ApplicationMap applicationMap = null;

        public ServletContextAdapter(ServletContext sc) {
            this.servletContext = sc;
        }

        public void dispatch(String path) throws IOException {
        }

        public String encodeActionURL(String url) {
            return null;
        }

        public String encodeNamespace(String name) {
            return null;
        }


        public String encodeResourceURL(String url) {
            return null;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> getApplicationMap() {
            if (applicationMap == null) {
                applicationMap =
                        new ApplicationMap(servletContext);
            }
            return applicationMap;
        }

        public String getAuthType() {
            return null;
        }

        public Object getContext() {
            return servletContext;
        }

        public String getInitParameter(String name) {
            return null;
        }

        public Map getInitParameterMap() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }


        public Object getRequest() {
            return null;
        }

        @Override
        public void setRequest(Object request) {
        }

        public String getRequestContextPath() {
            return null;
        }

        public Map<String, Object> getRequestCookieMap() {
            return null;
        }

        public Map<String, String> getRequestHeaderMap() {
            return null;
        }


        public Map<String, String[]> getRequestHeaderValuesMap() {
            return null;
        }


        public Locale getRequestLocale() {
            return null;
        }

        public Iterator<Locale> getRequestLocales() {
            return null;
        }


        public Map<String, Object> getRequestMap() {
            return null;
        }


        public Map<String, String> getRequestParameterMap() {
            return null;
        }


        public Iterator<String> getRequestParameterNames() {
            return null;
        }


        public Map<String, String[]> getRequestParameterValuesMap() {
            return null;
        }


        public String getRequestPathInfo() {
            return null;
        }


        public String getRequestServletPath() {
            return null;
        }

        @Override
        public String getRequestContentType() {
            return null;
        }

        @Override
        public String getResponseContentType() {
            return null;
        }

        public URL getResource(String path) throws MalformedURLException {
           return servletContext.getResource(path);
        }


        public InputStream getResourceAsStream(String path) {
            return servletContext.getResourceAsStream(path);
        }

        public Set<String> getResourcePaths(String path) {
            return servletContext.getResourcePaths(path);
        }

        public Object getResponse() {
            return null;
        }

        @Override
        public void setResponse(Object response) {
        }

        public Object getSession(boolean create) {
            return null;
        }

        public Map<String, Object> getSessionMap() {
            return null;
        }

        public java.security.Principal getUserPrincipal() {
            return null;
        }

        public boolean isUserInRole(String role) {
            return false;
        }

        public void log(String message) {
            servletContext.log(message);
        }

        public void log(String message, Throwable exception) {
            servletContext.log(message, exception);
        }

        public void redirect(String url) throws IOException {
        }

        @Override
        public String getRequestCharacterEncoding() {
            return null;
        }

        @Override
        public void setRequestCharacterEncoding(String requestCharacterEncoding) throws UnsupportedEncodingException {

        }

        @Override
        public String getResponseCharacterEncoding() {
            return null;
        }

        @Override
        public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        }

    } // END ServletContextAdapter

    static class ApplicationMap extends java.util.AbstractMap {

        private final ServletContext servletContext;

        ApplicationMap(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override
        public Object get(Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            return servletContext.getAttribute(key.toString());
        }


        @Override
        public Object put(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException();
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.setAttribute(keyString, value);
            return (result);
        }


        @Override
        public Object remove(Object key) {
            if (key == null) {
                return null;
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.removeAttribute(keyString);
            return (result);
        }


        public Set entrySet() {
            Set result = new Set<Map.Entry<String, Object>>() {

                public boolean add(Entry<String, Object> e) {
                    boolean didContain = (null != servletContext.getAttribute(e.getKey()));
                    servletContext.setAttribute(e.getKey(), e.getValue());
                    return !didContain;
                }

                public boolean addAll(Collection<? extends Entry<String, Object>> c) {
                    boolean curModified = false, modified = false;
                    for (Entry cur : c) {
                        curModified = this.add(cur);
                        if (curModified) {
                            modified = true;
                        }
                    }

                    return modified;
                }

                public void clear() {
                    Enumeration<String> names = servletContext.getAttributeNames();
                    String cur = null;
                    while (names.hasMoreElements()) {
                        cur = names.nextElement();
                        servletContext.removeAttribute(cur);
                    }
                }

                public boolean contains(Object o) {
                    Entry<String, Object> e = (Entry<String, Object>) o;
                    return (null != servletContext.getAttribute(e.getKey()));
                }

                public boolean containsAll(Collection<?> o) {
                    Collection<Entry<String, Object>> c = (Collection<Entry<String, Object>>) o;
                    for (Entry<String, Object> cur : c) {
                        if (null == servletContext.getAttribute(cur.getKey())) {
                            return false;
                        }
                    }
                    return true;
                }

                public boolean isEmpty() {
                    return servletContext.getAttributeNames().hasMoreElements();
                }

                public Iterator<Entry<String, Object>> iterator() {
                    return new Iterator<Entry<String, Object>>() {

                        private Enumeration<String> enumer = servletContext.getAttributeNames();
                        String key;

                        public boolean hasNext() {
                            boolean result = false;
                            if (null != enumer) {
                                result = enumer.hasMoreElements();
                            }
                            return result;
                        }

                        public Entry<String, Object> next() {
                            if (null == enumer) {
                                throw new NoSuchElementException();
                            }
                            key = enumer.nextElement();
                            Object value = servletContext.getAttribute(key);
                            Entry<String, Object> result =
                                    new SimpleEntry<String, Object>(key, value);
                            return result;
                        }

                        public void remove() {
                            if (null == key) {
                                this.next();
                            }
                            servletContext.removeAttribute(key);
                        }

                    };
                }

                public boolean remove(Object o) {
                    Entry<String, Object> e = (Entry<String, Object>) o;
                    boolean result = (null != servletContext.getAttribute(e.getKey()));
                    if (result) {
                        servletContext.removeAttribute(e.getKey());
                    }
                    return result;
                }

                public boolean removeAll(Collection<?> c) {
                    boolean modified = false;
                    Collection<Entry<String, Object>> toRemove =
                            (Collection<Entry<String, Object>>) c;
                    for (Entry<String, Object> cur : toRemove) {
                        if (null != servletContext.getAttribute(cur.getKey())) {
                            modified = true;
                            servletContext.removeAttribute(cur.getKey());
                        }
                    }
                    return modified;
                }



                public boolean retainAll(Collection<?> c) {
                    boolean modified = false;
                    Collection<Entry<String, Object>> toRetain =
                            (Collection<Entry<String, Object>>) c;
                    Map<String, Boolean> attrNames = new HashMap<String, Boolean>();
                    Enumeration<String> enumer = servletContext.getAttributeNames();
                    while (enumer.hasMoreElements()) {
                        attrNames.put(enumer.nextElement(), Boolean.TRUE);
                    }
                    for (Entry<String, Object> cur : toRetain) {
                        attrNames.remove(cur.getKey());
                    }
                    for (String cur : attrNames.keySet()) {
                        if (null != servletContext.getAttribute(cur)) {
                            modified = true;
                            servletContext.removeAttribute(cur);
                        }
                    }

                    return modified;
                }

                public int size() {
                    int size = 0;
                    Enumeration<String> enumer = servletContext.getAttributeNames();
                    while (enumer.hasMoreElements()) {
                        enumer.nextElement();
                        size++;
                    }
                    return size;
                }

                public Object[] toArray() {
                    Object [] result = new Object[size()];
                    Enumeration<String> enumer = servletContext.getAttributeNames();
                    int i = 0;
                    while (enumer.hasMoreElements()) {
                        String cur = enumer.nextElement();
                        result[i] = new SimpleEntry<String, Object>(cur,
                                servletContext.getAttribute(cur));

                    }

                    return result;
                }

                public <T> T[] toArray(T[] t) {
                    Object[] result = (Object [])t;

                    Enumeration<String> enumer = servletContext.getAttributeNames();
                    int i = 0;
                    while (enumer.hasMoreElements()) {
                        String cur = enumer.nextElement();
                        result[i] = new SimpleEntry<String, Object>(cur,
                                servletContext.getAttribute(cur));

                    }

                    return t;
                }



            };
            return result;
        }


        @Override
        public boolean equals(Object obj) {
            return !(obj == null || !(obj instanceof ApplicationMap))
                    && super.equals(obj);
        }


        @Override
        public int hashCode() {
            int hashCode = 7 * servletContext.hashCode();
            for (Object o : entrySet()) {
                hashCode += o.hashCode();
            }
            return hashCode;
        }


        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }


        @Override
        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }


        @Override
        public boolean containsKey(Object key) {
            return (servletContext.getAttribute(key.toString()) != null);
        }


        // Copy from JDK for running on Java 5.
    public static class SimpleEntry<K,V>
	implements Entry<K,V>, java.io.Serializable
    {

        // <editor-fold defaultstate="collapsed" desc="SimpleEntry inner class">

	private static final long serialVersionUID = -8499721149061103585L;

	private final K key;
	private V value;

        /**
         * Utility method for SimpleEntry and SimpleImmutableEntry.
         * Test for equality, checking for nulls.
         */
        private static boolean eq(Object o1, Object o2) {
            return o1 == null ? o2 == null : o1.equals(o2);
        }

        /**
         * Creates an entry representing a mapping from the specified
         * key to the specified value.
         *
         * @param key the key represented by this entry
         * @param value the value represented by this entry
         */
	public SimpleEntry(K key, V value) {
	    this.key   = key;
            this.value = value;
	}

        /**
         * Creates an entry representing the same mapping as the
         * specified entry.
         *
         * @param entry the entry to copy
         */
	public SimpleEntry(Entry<? extends K, ? extends V> entry) {
	    this.key   = entry.getKey();
            this.value = entry.getValue();
	}

    	/**
	 * Returns the key corresponding to this entry.
	 *
	 * @return the key corresponding to this entry
	 */
	public K getKey() {
	    return key;
	}

    	/**
	 * Returns the value corresponding to this entry.
	 *
	 * @return the value corresponding to this entry
	 */
	public V getValue() {
	    return value;
	}

    	/**
	 * Replaces the value corresponding to this entry with the specified
	 * value.
	 *
	 * @param value new value to be stored in this entry
	 * @return the old value corresponding to the entry
         */
	public V setValue(V value) {
	    V oldValue = this.value;
	    this.value = value;
	    return oldValue;
	}

	/**
	 * Compares the specified object with this entry for equality.
	 * Returns {@code true} if the given object is also a map entry and
	 * the two entries represent the same mapping.	More formally, two
	 * entries {@code e1} and {@code e2} represent the same mapping
	 * if<pre>
	 *   (e1.getKey()==null ?
	 *    e2.getKey()==null :
	 *    e1.getKey().equals(e2.getKey()))
	 *   &amp;&amp;
	 *   (e1.getValue()==null ?
	 *    e2.getValue()==null :
	 *    e1.getValue().equals(e2.getValue()))</pre>
	 * This ensures that the {@code equals} method works properly across
	 * different implementations of the {@code Map.Entry} interface.
	 *
	 * @param o object to be compared for equality with this map entry
	 * @return {@code true} if the specified object is equal to this map
	 *	   entry
	 * @see    #hashCode
	 */
	public boolean equals(Object o) {
	    if (!(o instanceof Map.Entry))
		return false;
	    Map.Entry e = (Map.Entry)o;
	    return eq(key, e.getKey()) && eq(value, e.getValue());
	}

	/**
	 * Returns the hash code value for this map entry.  The hash code
	 * of a map entry {@code e} is defined to be: <pre>
	 *   (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
	 *   (e.getValue()==null ? 0 : e.getValue().hashCode())</pre>
	 * This ensures that {@code e1.equals(e2)} implies that
	 * {@code e1.hashCode()==e2.hashCode()} for any two Entries
	 * {@code e1} and {@code e2}, as required by the general
	 * contract of {@link Object#hashCode}.
	 *
	 * @return the hash code value for this map entry
	 * @see    #equals
	 */
	public int hashCode() {
	    return (key   == null ? 0 :   key.hashCode()) ^
		   (value == null ? 0 : value.hashCode());
	}

        /**
         * Returns a String representation of this map entry.  This
         * implementation returns the string representation of this
         * entry's key followed by the equals character ("<tt>=</tt>")
         * followed by the string representation of this entry's value.
         *
         * @return a String representation of this map entry
         */
	public String toString() {
	    return key + "=" + value;
	}

        // </editor-fold>

    }



    } // END ApplicationMap
}
