
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

package com.sun.faces.systest;

import java.util.Collections;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;


/*
 * This class is responsible for storing an instance of S
 * in a ServletContext instance such that the instance is a singleton
 * within that ServletContext instance.
 *
 */
class SingletonStore<S> {


    // <editor-fold defaultstate="collapsed" desc="ivars">

    private final String key;
    private final String keyForKey;
    private Map<String, Object> appMap;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="constants">

    private static final String KEY_BASE =
            SingletonStore.class.getName();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="constructors">

    public SingletonStore(String singletonKey) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        if (null != facesContext) {
            appMap = extContext.getApplicationMap();
        } else {
            assert(false);
            appMap = Collections.emptyMap();
        }
        Object context = extContext.getContext();

        keyForKey = KEY_BASE + singletonKey;
        String candidateKey = (String) appMap.get(keyForKey);
        // Is this the first time that this kind of singleton has been created
        // for this ServletContext instance or any wrapped instance?
        if (null == candidateKey) {
            // if so, create the key...
            this.key = singletonKey + context.hashCode();
            appMap.put(keyForKey, this.key);
        } else {
            this.key = candidateKey;
        }

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="static methods">

    static Object removeSingletonReference(ExternalContext extContext, String singletonKey) {
        Object result = null;
        Map<String, Object> map = extContext.getApplicationMap();

        String keyForKey = KEY_BASE + singletonKey;
        String keyVal = (String) map.get(keyForKey);

        result = map.remove(keyVal);
        map.remove(keyForKey);

        return result;
    }

    static void removeSingletonReference(ServletContext context, String singletonKey) {
        String keyForKey = KEY_BASE + singletonKey;
        String keyVal = (String) context.getAttribute(keyForKey);

        context.removeAttribute(keyVal);
        context.removeAttribute(keyForKey);
    }

    static Object getReferenceToSingleton(ExternalContext extContext, String singletonKey) {
        Object result = null;
        Map<String, Object> map = extContext.getApplicationMap();

        String keyForKey = KEY_BASE + singletonKey;
        String keyVal = (String) map.get(keyForKey);

        if (null != keyVal) {
            result = map.get(keyVal);
        }

        return result;
    }

    static Object getReferenceToSingleton(ServletContext context, String singletonKey) {
        Object result = null;

        String keyForKey = KEY_BASE + singletonKey;
        String keyVal = (String) context.getAttribute(keyForKey);

        if (null != keyVal) {
            result = context.getAttribute(keyVal);
        }

        return result;
    }

    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="instance methods">

    S getReferenceToSingleton() {
        S result = (S) appMap.get(key);

        return result;
    }

    S removeSingletonReference() {
        S result = null;
        if (null != appMap) {
            appMap.remove(keyForKey);
            result = (S) appMap.remove(key);
        }
        return result;
    }


    void putSingletonReference(S instance) {
        appMap.put(key, instance);
    }


    // </editor-fold>
}
