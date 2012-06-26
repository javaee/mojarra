/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.facelets.util.FunctionLibrary;
import com.sun.faces.util.FacesLogger;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public final class PassThroughAttributeLibrary extends FunctionLibrary {

    public final static String Namespace = "http://java.sun.com/jsf/passthrough";
    private final static Logger LOGGER = FacesLogger.FACELETS_COMPONENT.getLogger();


    public final static PassThroughAttributeLibrary Instance = new PassThroughAttributeLibrary();

    public PassThroughAttributeLibrary() {
        super(PassThroughAttributeLibrary.class, Namespace);
    }
    
    public static Map<String, Object> jsonToMap(String dataValue) {
        Map<String, Object> attrs = new HashMap<String, Object>();
        JSONObject json = null;
        
        try { 
            json = new JSONObject(dataValue);
            Iterator<String> keys = json.keys();
            Object value = null;
            boolean isSimple = true;
            while (keys.hasNext()) {
                value = json.get(keys.next());
                if (value instanceof JSONObject) {
                    isSimple = false;
                    break;
                }
            }
            if (isSimple) {
                keys = json.keys();
                String key = null;
                String attrName = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    attrs.put(attrName, json.get(key));
                }
            } else {
                Deque<String> stack = new ArrayDeque<String>();
                renderNestedAttribute(stack, attrs, json);
            }
            
        } catch (JSONException je) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to render JSON", je);
            }
            
        }
        return attrs;
    }
    
    private static void renderNestedAttribute(Deque<String> nameStack,
            Map<String, Object> attrs, JSONObject json) throws JSONException {
        Iterator<String> keys = json.keys();
        String key = null;
        Object value = null;
        while (keys.hasNext()) {
            key = keys.next();
            nameStack.push(key);
            value = json.get(key);
            if (value instanceof JSONObject) {
                renderNestedAttribute(nameStack, attrs, (JSONObject)value);
            } else {
                StringBuilder attrNameBuilder = new StringBuilder();
                Iterator<String> attrNames = nameStack.descendingIterator();
                while (attrNames.hasNext()) {
                    attrNameBuilder.append("-").append(attrNames.next());
                }
                String attrName = attrNameBuilder.substring(1);
                attrs.put(attrName, value.toString());
                nameStack.pop();
            }
        }
    }
    
    
}
