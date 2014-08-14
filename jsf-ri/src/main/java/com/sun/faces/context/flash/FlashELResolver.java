/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.context.flash;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.context.Flash;
import javax.faces.context.FlashFactory;

/**
 * <p>Provide a feature semantically identical to the <a target="_"
 * href="http://api.rubyonrails.com/classes/ActionController/Flash.html">
 * "flash" concept in Ruby on Rails</a>.</p>
 * <p/>
 * <p>The feature is exposed to users via a custom
 * <code>ELResolver</code> which introduces a new implicit object,
 * <code>flash</code>.  The flash functions as <code>Map</code> and
 * can be used in <code>getValue( )</code> or <code>setValue(
 * )</code> expressions.</p>
 * <p/>
 * <p>Usage</p>
 * <p/>
 * <ul>
 * <p/>
 * <p>Consider three JSF views: viewA, viewB, and viewC.  The user
 * first views viewA, then clicks a button and is shown viewB, where
 * she clicks a button and is shown viewC.  If values are stored
 * into the flash during the rendering or postback phases of viewA,
 * they are available to during the rendering phase of viewB, but
 * are not available during the rendering or postback phases of
 * viewC.  In other words, values stored into the flash on "this"
 * request are accessible for the "next" request, but not
 * thereafter.</p>
 * <p/>
 * <p>There are three ways to access the flash.</p>
 * <p/>
 * <ol>
 * <p/>
 * <li><p>Using an EL Expression, such as using
 * <code>#{flash.foo}</code> as the value of an attribute in a JSP
 * page.</p></li>
 * <p/>
 * <li><p>Using the EL API, such as:</p>
 * <p/>
 * <p><code><pre>
 * FacesContext context = FacesContext.getCurrentInstance();
 * ValueExpression flashExpression = context.getApplication().
 *    createValueExpression(context.getELContext(), "#{flash.foo}",
 *                          null, Object.class);
 * flashExpression.setValue(context.getELContext(), "Foo's new value");
 * </pre></code></p>
 * <p/>
 * </li>
 * <p/>
 * <li><p>Using getting the {@link ELFlash} directly, such as:</p>
 * <p/>
 * <p><code><pre>
 * Map&lt;String,Object&gt; flash = ELFlash.getFlash();
 * flash.put("foo", "Foo's new value");
 * </pre></code></p>
 * <p/>
 * </li>
 * <p/>
 * </ol>
 * <p/>
 * <p>The main entry point to this feature is the first one.  This
 * library includes a simple custom tag, <code><a target="_"
 * href="../../../../tlddoc/jsfExt/set.html">jsfExt:set</a></code>, that
 * evaluates an expression and sets its value into another expression.
 * <code>jsfExt:set</code> can be used to store values into the flash
 * from JSP pages, like this:</p>
 * <p/>
 * <p><code>&lt;jsfExt:set var="#{flash.foo}" value="fooValue"
 * /&gt;</code></p>
 * <p/>
 * <p>or this:</p>
 * <p/>
 * <p><code>&lt;jsfExt:set var="#{flash.keep.bar}" value="#{user.name}"
 * /&gt;</code></p>
 * <p/>
 * <p>or even this:</p>
 * <p/>
 * <p><code><pre>
 * &lt;jsfExt:set var="#{flash.now.baz}" value="#{cookie.userCookie}" /&gt;
 * <p/>
 * &lt;h:outputText value="#{flash.now.baz}" /&gt;
 * <p/>
 * </pre></code></p>
 * <p/>
 * </ul>
 * <p/>
 * <p>Related Classes</p>
 * <p/>
 * <p>The complete list of classes that make up this feature is</p>
 * <p/>
 * <ul><code>
 * <p/>
 * <li><p>FlashELResolver</p></li>
 * <p/>
 * <li><p>{@link ELFlash}</p></li>
 * <p/>
 * </code></ul>
 */

public class FlashELResolver extends ELResolver {

    /**
     * <p>Not intended for manual invocation.  Only called by the JSF
     * Runtime.</p>
     */

    public FlashELResolver() {

    }

    // ------------------------------------------------------ Manifest Constants

    private static final String FLASH_VARIABLE_NAME = "flash";

    private static final String FLASH_NOW_VARIABLE_NAME = "now";

    private static final String FLASH_KEEP_VARIABLE_NAME = "keep";

    // ------------------------------------------------ VariableResolver Methods

    /**
     * <p>Hook into the EL resolution process to introduce the
     * <code>flash</code> implicit object.  If
     * <code>property</code> is <code>null</code>, take no action
     * and return <code>null</code>.  if <code>base</code> is null, return null.
     * If
     * <code>base</code> is an instance of <code>ELFlash</code> and
     * property is the literal string "keep", set a ThreadLocal property
     * that will be inspected by the flash on the next link in the
     * resolution chain and return the <code>ELFlash</code> instance.
     * If <code>base</code> is an instance of <code>ELFlash</code> and
     * <code>property</code> is the literal string "now", return the
     * result of calling <code>getRequestMap( )</code> on the
     * <code>ExternalContext</code> for the <code>FacesContext</code>
     * for this request.  Call <code>setPropertyResolved(true)</code> on
     * the <code>ELContext</code> where appropriate.</p>
     *
     * @throws PropertyNotFoundException if <code>property</code> is
     *                                   <code>null</code>.
     */

  public Object getValue(ELContext elContext, Object base, Object property)
  {
    if (null == property)
    {
      // take no action.
      return null;
    }

    Object result = null;

    if (null == base)
    {
      return null;
    }
    // If the base argument is the flash itself...
    else if (base instanceof Flash)
    {
        FacesContext facesContext =
                (FacesContext) elContext.getContext(FacesContext.class);
        ExternalContext extCtx = facesContext.getExternalContext();
        
        // and the property argument is "keep"...
        if (property.toString().equals(FLASH_KEEP_VARIABLE_NAME))
        {
            elContext.setPropertyResolved(true);
          
            // then this is a request to promote the value
            // "property", which is assumed to have been previously
            // stored in request scope via the "flash.now"
            // expression, to flash scope.
            result = base;
            // Set a flag so the flash itself can look in the request
            // and promote the value to the next request
            FlashFactory ff = (FlashFactory) 
                    FactoryFinder.getFactory(FactoryFinder.FLASH_FACTORY);
            ff.getFlash(true);
            ELFlash.setKeepFlag(facesContext);
        }
        // Otherwise, if base is the flash, and property is "now"...
        else if (property.toString().equals(FLASH_NOW_VARIABLE_NAME))
        {
            // PENDING(edburns): use FacesContext.getAttributes() instead of 
            // request scope.
            Map<String, Object> requestMap = extCtx.getRequestMap();
            requestMap.put(ELFlash.FLASH_NOW_REQUEST_KEY, property);
            elContext.setPropertyResolved(true);
            result = requestMap;
        }
        else
        {
            result = null;
        }
    }

    return result;
  }

    /**
     * <p>Return the valid <code>Class</code> for a future set
     * operation, which will always be <code>null</code> because sets
     * happen via the <code>MapELResolver</code> operating on the {@link
     * ELFlash} instance as a <code>Map</code>.</p>
     *
     * @throws PropertyNotFoundException if property is <code>null</code>.
     */

    public Class<?> getType(ELContext elContext,
                            Object base,
                            Object property) {

        if (null != base) {
            return null;
        }
        if (null == property) {
            String message = " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        if (property.toString().equals(FLASH_VARIABLE_NAME)) {
            elContext.setPropertyResolved(true);
        }

        return null;
    }

    /**
     * <p>This method will throw
     * <code>PropertyNotWritableException</code> if called with a
     * <code>null</code> <code>base</code> and a <code>property</code>
     * value equal to the literal string "flash".  This is because set
     * operations normally go through the <code>MapELResolver</code> via
     * the <code>ELFlash</code> <code>Map</code>.</p>
     * <p/>
     * <p>In other words, do not call this method directly to set a
     * value into the flash!  The only way to access the flash is either
     * through JSP or via the EL API.</p>
     *
     * @throws PropertyNotFoundException    if <code>base</code> is
     *                                      <code>null</code> and <code>property</code> is <code>null</code>.
     * @throws PropertyNotWritableException if <code>base</code> is
     *                                      <code>null</code> and <code>property</code> is the literal string
     *                                      "flash".
     */

    public void setValue(ELContext elContext,
                         Object base,
                         Object property,
                         Object value) {
        if (null != base) {
            return;
        }
        if (null == property) {
            String message = " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        if (property.toString().equals(FLASH_VARIABLE_NAME)) {
            elContext.setPropertyResolved(true);
            throw new PropertyNotWritableException(property.toString());
        }

    }

    /**
     * <p>Returns <code>true</code> because write operations take place
     * via the <code>MapELResolver</code> on the actual {@link ELFlash}
     * instance.</p>
     *
     * @throws PropertyNotFoundException if <code>base</code> is
     *                                   <code>null</code> and <code>property</code> is <code>null</code>.
     */

    public boolean isReadOnly(ELContext elContext,
                              Object base,
                              Object property) {
        if (base != null) {
            return false;
        }
        if (property == null) {
            String message = " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }

        if (property.toString().equals(FLASH_VARIABLE_NAME)) {
            elContext.setPropertyResolved(true);
            return true;
        }

        return false;
    }

    /**
     * <p>Returns an iterator of <code>FeatureDescriptors</code> for the
     * current contents of the flash.</p>
     */

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext,
                                                             Object base) {
        if (null != base) {
            return null;
        }
        Iterator<FeatureDescriptor> result = null;
        Map<String, Object> flash;
        FacesContext facesContext =
                (FacesContext) elContext.getContext(FacesContext.class);
        ExternalContext extCtx = facesContext.getExternalContext();

        //noinspection unchecked
        if (null != (flash = extCtx.getFlash())) {
            Iterator<Map.Entry<String, Object>> iter = flash.entrySet().iterator();
            Map.Entry<String, Object> cur;
            ArrayList<FeatureDescriptor> fds;
            FeatureDescriptor fd;
            if (iter.hasNext()) {
                fds = new ArrayList<FeatureDescriptor>(flash.size());
                while (iter.hasNext()) {
                    cur = iter.next();
                    fd = new FeatureDescriptor();
                    fd.setName(cur.getKey());
                    fds.add(fd);
                }
                result = fds.iterator();
            }
        }

        return result;
    }

    /**
     * <p>If <code>base</code> is non-<code>null</code> and is the
     * literal string "flash", return <code>Object.class</code>.</p>
     */

    public Class<?> getCommonPropertyType(ELContext context,
                                          Object base) {
        Class<?> result = null;
        if (null != base) {
            if (FLASH_VARIABLE_NAME.equals(base.toString())) {
                result = Object.class;
            }
        }
        return result;
    }


}
