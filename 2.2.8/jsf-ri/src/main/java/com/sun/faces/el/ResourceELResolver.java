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

package com.sun.faces.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * ELResolver to resolve expressions like the following:
 * <ul>
 *   <li>#{resource['library:resource']}</li>
 *   <li>#{resource['resource']}
 * </ul>
 */
public class ResourceELResolver extends ELResolver {


    // ------------------------------------------------- Methods from ELResolver


    /**
     * If base and property are not <code>null</code> and base is
     * an instance of {@link ResourceHandler}, perform the following:
     * <ul>
     * <li>
     * If <code>property</code> doesn't contain <code>:</code> treat <code>property</code>
     * as the resource name and pass <code>property</code> to {@link ResourceHandler#createResource(String)}
     * </li>
     * <li>
     * If <code>property</code> contains a single <code>:</code> treat the content
     * before the <code>:</code> as the library name, and the content after the
     * <code>:</code> to be the resource name and pass both to {@link javax.faces.application.ResourceHandler#createResource(String, String)}
     * </li>
     * <li>
     * If <code>property</code> contains more than one <code>:</code> then throw
     * a <code>ELException</code>
     * </li>
     * <li>
     * If one of the above steps resulted in the creation of a {@link Resource}
     * instance, call <code>ELContext.setPropertyResolved(true)</code> and return
     * the result of {@link javax.faces.application.Resource#getRequestPath()}
     * </li>
     * </ul>
     * @see ELResolver#getValue(javax.el.ELContext, Object, Object)
     */
    public Object getValue(ELContext context, Object base, Object property) {
        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }
        Object ret = null;
        if (base instanceof ResourceHandler) {
            ResourceHandler handler = (ResourceHandler) base;
            String prop = property.toString();
            Resource res;
            if (!prop.contains(":")) {
                res = handler.createResource(prop);
            } else {
                if (!isPropertyValid(prop)) {
                    // RELEASE_PENDING i18n
                    throw new ELException("Invalid resource format.  Property " + prop + " contains more than one colon (:)");
                }
                Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();

                String[] parts = Util.split(appMap, prop, ":");
                
                // If the enclosing entity for this expression is itself 
                // a resource, the "this" syntax for the library name must
                // be supported.
                if (null != parts[0] && parts[0].equals("this")) {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    UIComponent currentComponent = UIComponent.getCurrentCompositeComponent(facesContext);
                    Resource componentResource = (Resource)
                                currentComponent.getAttributes().get(Resource.COMPONENT_RESOURCE_KEY);
                    if (null != componentResource) {
                        String libName = null;
                        if (null != (libName = componentResource.getLibraryName())) {
                            parts[0] = libName;
                        }
                    }
                    
                }
                
                res = handler.createResource(parts[1], parts[0]);
            }
            if (res != null) {
                ret = res.getRequestPath();
            }
            context.setPropertyResolved(true);
        }
        return ret;
    }


    /**
     * @return <code>null</code> as this resolver only performs lookups
     * @throws PropertyNotFoundException if base and property are null
     */
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }
        return null;
    }


    /**
     * This is basically a no-op.
     * @throws PropertyNotFoundException if base and property are null
     */
    public void setValue(ELContext context,
                         Object base,
                         Object property,
                         Object value) {
        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }
    }


    /**
     * @return <code>false</code> (basically ignored by the EL system)
     * @throws PropertyNotFoundException if base and property are null
     */
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }

        return false;
    }


    /**
     * @return <code>null</code> - there is no way to query the
     *  <code>ResourceManager</code> for all known resources
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
                                                             Object base) {

        return null;

    }


    /**
     * @return <code>String.class</code> - getType() expects String properties
     */
    public Class<?> getCommonPropertyType(ELContext context, Object base) {

        return String.class;

    }

    
    // --------------------------------------------------------- Private Methods


    private boolean isPropertyValid(String property) {

        int idx = property.indexOf(':');
        return (property.indexOf(':', idx + 1) == -1);

    }


}
