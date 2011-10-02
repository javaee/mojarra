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

package com.sun.faces.facelets.tag;

import com.sun.faces.util.Util;

import java.lang.reflect.Method;
import java.net.URL;

/**
 * Concrete implementation for defining Facelet tag libraries in Java.
 */
public class TagLibraryImpl extends AbstractTagLibrary {
        public TagLibraryImpl(String namespace) {
            super(namespace);
        }

        public void putConverter(String name, String id) {
            Util.notNull("name", name);
            Util.notNull("id", id);
            this.addConverter(name, id);
        }

        public void putConverter(String name, String id, Class handlerClass) {
            Util.notNull("name", name);
            Util.notNull("id", id);
            Util.notNull("handlerClass", handlerClass);
            this.addConverter(name, id, handlerClass);
        }


        public void putValidator(String name, String id) {
            Util.notNull("name", name);
            Util.notNull("id", id);
            this.addValidator(name, id);
        }


        public void putValidator(String name, String id, Class handlerClass) {
            Util.notNull("name", name);
            Util.notNull("id", id);
            Util.notNull("handlerClass", handlerClass);
            this.addValidator(name, id, handlerClass);
        }

        public void putBehavior(String name, String id) {
            Util.notNull("name", name);
            Util.notNull("id", id);
            this.addBehavior(name, id);
        }

        public void putBehavior(String name, String id, Class handlerClass) {
            Util.notNull("name", name);
            Util.notNull("id", id);
            Util.notNull("handlerClass", handlerClass);
            this.addBehavior(name, id, handlerClass);
        }

        public void putTagHandler(String name, Class type) {
            Util.notNull("name", name);
            Util.notNull("type", type);
            this.addTagHandler(name, type);
        }

        public void putComponent(String name, String componentType,
                String rendererType) {
            Util.notNull("name", name);
            Util.notNull("componentType", componentType);
            this.addComponent(name, componentType, rendererType);
        }

        public void putComponent(String name, String componentType,
                String rendererType, Class handlerClass) {
            Util.notNull("name", name);
            Util.notNull("handlerClass", handlerClass);
            this.addComponent(name, componentType, rendererType, handlerClass);
        }


        public void putUserTag(String name, URL source) {
            Util.notNull("name", name);
            Util.notNull("source", source);
            this.addUserTag(name, source);
        }

        public void putCompositeComponentTag(String name, String resourceId) {
            Util.notNull("name", name);
            Util.notNull("resourceId", resourceId);
            this.addCompositeComponentTag(name, resourceId);
        }

        public void putFunction(String name, Method method) {
            Util.notNull("name", name);
            Util.notNull("method", method);
            this.addFunction(name, method);
        }
    }
