/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.facelets.tag;

import java.net.URL;
import java.lang.reflect.Method;

import com.sun.faces.util.Util;

/**
 * RELEASE_PENDING (rlubke,driscoll) docs
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
            Util.notNull("componentType", componentType);
            Util.notNull("handlerClass", handlerClass);
            this.addComponent(name, componentType, rendererType, handlerClass);
        }


        public void putUserTag(String name, URL source) {
            Util.notNull("name", name);
            Util.notNull("source", source);
            this.addUserTag(name, source);
        }

        public void putFunction(String name, Method method) {
            Util.notNull("name", name);
            Util.notNull("method", method);
            this.addFunction(name, method);
        }
    }