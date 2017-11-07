/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application.view;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.unmodifiableMap;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIImportConstants;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewMetadata;
import javax.faces.view.facelets.Facelet;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.context.FacesFileNotFoundException;
import com.sun.faces.facelets.impl.DefaultFaceletFactory;


/**
 * @see javax.faces.view.ViewMetadata
 */
public class ViewMetadataImpl extends ViewMetadata {

    private String viewId;
    private DefaultFaceletFactory faceletFactory;


    // ---------------------------------------------------------------------------------------------------- Constructors


    public ViewMetadataImpl(String viewId) {

        this.viewId = viewId;

    }


    // --------------------------------------------------------------------------------------- Methods from ViewMetadata


    /**
     * @see javax.faces.view.ViewMetadata#getViewId()
     */
    @Override
    public String getViewId() {

        return viewId;

    }

    /**
     * @see javax.faces.view.ViewMetadata#createMetadataView(javax.faces.context.FacesContext)
     */
    @Override
    public UIViewRoot createMetadataView(FacesContext context) {

        UIViewRoot result = null;
        UIViewRoot currentViewRoot = context.getViewRoot();
        Map<String, Object> currentViewMapShallowCopy = Collections.emptyMap();
        
        try {
            context.setProcessingEvents(false);
            if (faceletFactory == null) {
                ApplicationAssociate associate = ApplicationAssociate
                      .getInstance(context.getExternalContext());
                faceletFactory = associate.getFaceletFactory();
                assert (faceletFactory != null);
            }
            ViewHandler vh = context.getApplication().getViewHandler();
            result = vh.createView(context, viewId);

            // Stash away view id before invoking handlers so that 
            // StateContext.partialStateSaving() can determine the current
            // view. 
            context.getAttributes().put(RIConstants.VIEWID_KEY_NAME, viewId);
            // If the currentViewRoot has a viewMap, make sure the entries are
            // copied to the temporary UIViewRoot before invoking handlers.
            if (null != currentViewRoot) {
                Map<String, Object> currentViewMap = currentViewRoot.getViewMap(false);

                if (null != currentViewMap && !currentViewMap.isEmpty()) {
                    currentViewMapShallowCopy = new HashMap<>(currentViewMap);
                    Map<String, Object> resultViewMap = result.getViewMap(true);
                    resultViewMap.putAll(currentViewMapShallowCopy);
                }
            }
            
            // Only replace the current context's UIViewRoot if there is 
            // one to replace.
            if (null != currentViewRoot) {
                // This clear's the ViewMap of the current UIViewRoot before
                // setting the argument as the new UIViewRoot.
                context.setViewRoot(result);
            }

            Facelet f = faceletFactory.getMetadataFacelet(context, result.getViewId());

            f.apply(context, result);

            importConstantsIfNecessary(context, result);

        } catch (FacesFileNotFoundException ffnfe) {
            try {
                context.getExternalContext().responseSendError(404, ffnfe.getMessage());
            } catch(IOException ioe) {}
            context.responseComplete();
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        } finally {
            context.getAttributes().remove(RIConstants.VIEWID_KEY_NAME);
            context.setProcessingEvents(true);
            if (null != currentViewRoot) {
                context.setViewRoot(currentViewRoot);
                if (!currentViewMapShallowCopy.isEmpty()) {
                    currentViewRoot.getViewMap(true).putAll(currentViewMapShallowCopy);
                    currentViewMapShallowCopy.clear();
                }
            }
            
        }

        return result;
        
    }

    // ----------------------------------------------------------------------------------------------- UIImportConstants

    private static void importConstantsIfNecessary(FacesContext context, UIViewRoot root) {
        for (UIImportConstants importConstants : getImportConstants(root)) {
            String type = importConstants.getType();

            if (type == null) {
                throw new IllegalArgumentException("UIImportConstants type attribute is required.");
            }

            String var = importConstants.getVar();

            if (var == null) {
                int innerClass = type.lastIndexOf('$');
                int outerClass = type.lastIndexOf('.');
                var = type.substring(Math.max(innerClass, outerClass) + 1);
            }

            Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();

            if (!applicationMap.containsKey(type)) {
                applicationMap.putIfAbsent(var, collectConstants(type));
            }
        }
    }

    /**
     * Collect constants of the given type. That are, all public static final fields of the given type.
     * @param type The fully qualified name of the type to collect constants for.
     * @return Constants of the given type.
     */
    private static Map<String, Object> collectConstants(String type) {
        Map<String, Object> constants = new LinkedHashMap<>();

        for (Field field : toClass(type).getFields()) {
            int modifiers = field.getModifiers();

            if (isPublic(modifiers) && isStatic(modifiers) && isFinal(modifiers)) {
                try {
                    constants.put(field.getName(), field.get(null));
                }
                catch (Exception e) {
                    throw new IllegalArgumentException(
                        String.format("UIImportConstants cannot access constant field '%s' of type '%s'.", type, field.getName()), e);
                }
            }
        }

        return unmodifiableMap(new ConstantsMap(constants, type));
    }

    /**
     * Convert the given type, which should represent a fully qualified name, to a concrete {@link Class} instance.
     * @param type The fully qualified name of the class.
     * @return The concrete {@link Class} instance.
     * @throws IllegalArgumentException When it is missing in the classpath.
     */
    private static Class<?> toClass(String type) {
        try {
            return Class.forName(type, true, Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e) {
            // Perhaps it's an inner enum which is incorrectly specified as com.example.SomeClass.SomeEnum.
            // Let's be lenient on that although the proper type notation should be com.example.SomeClass$SomeEnum.
            int i = type.lastIndexOf('.');

            if (i > 0) {
                try {
                    return toClass(new StringBuilder(type).replace(i, i + 1, "$").toString());
                }
                catch (Exception ignore) {
                    ignore = null; // Just continue to IllegalArgumentException on original ClassNotFoundException.
                }
            }

            throw new IllegalArgumentException(
                String.format("UIImportConstants cannot find type '%s' in classpath.", type), e);
        }
    }

    /**
     * Specific map implementation which wraps the given map in {@link Collections#unmodifiableMap(Map)} and throws an
     * {@link IllegalArgumentException} in {@link ConstantsMap#get(Object)} method when the key doesn't exist at all.
     *
     * @author Bauke Scholtz
     * @since 2.3
     */
    private static class ConstantsMap extends HashMap<String, Object> {

        private static final long serialVersionUID = 7036447585721834948L;
        private String type;

        public ConstantsMap(Map<String, Object> map, String type) {
            this.type = type;
            putAll(map);
        }

        @Override
        public Object get(Object key) {
            if (!containsKey(key)) {
                throw new IllegalArgumentException(
                    String.format("UIImportConstants type '%s' does not have the constant '%s'.", type, key));
            }

            return super.get(key);
        }

        @Override
        public boolean equals(Object object) {
            return super.equals(object) && type.equals(((ConstantsMap) object).type);
        }

        @Override
        public int hashCode() {
            return super.hashCode() + type.hashCode();
        }

    }

}
