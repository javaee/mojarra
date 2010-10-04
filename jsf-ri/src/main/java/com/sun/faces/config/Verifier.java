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

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class backs the <code>com.sun.faces.verifyObjects</code>
 * feature which provides basic validation of Components,
 * Converters, and Validators.
 * <p>
 */
public class Verifier {

    /**
     * Thread local to share the <code>Verifier</code>.
     */
    private static final ThreadLocal<Verifier> VERIFIER =
            new ThreadLocal<Verifier>();

    /**
     * Represent the current Faces object types we validate.
     */
    public enum ObjectType {
        COMPONENT,
        CONVERTER,
        VALIDATOR,       
        BEHAVIOR,       
    }


    /**
     * Container for any messages that may be queued.
     */
    private List<String> messages;


    // ------------------------------------------------------- Constructors


    /**
     * Construct a new <code>Verifier</code> instance.
     */
    Verifier() {

        messages = new ArrayList<String>(4);
        
    }


    // ------------------------------------------------- Public Methods


    /**
     * @return a <code>Verifier</code> for the current web application
     * <em>if</em> <code>com.sun.faces.verifyObjects</code> is enabled
     */
    public static Verifier getCurrentInstance() {

        return VERIFIER.get();

    }


    /**
     * Set the <code>Verifier</code> for this thread (typically the
     * same thread that is used to bootstrap the application).
     * @param verifier the <code>Verifier</code> for this web application
     */
    public static void setCurrentInstance(Verifier verifier) {

        if (verifier == null) {
            VERIFIER.remove();
        } else {
            VERIFIER.set(verifier);
        }

    }


    /**
     * @return <code>true</code> if no messages were queued by the
     *  validation process
     */
    public boolean isApplicationValid() {

        return (messages.isEmpty());

    }


    /**
     * @return a <code>List</code> of all failures found
     */
    public List<String> getMessages() {

        return messages;

    }


    /**
     * Validate the specified faces object by:
     * <ul>
     *   <li>
     *     Ensure the class can be found and loaded
     *   </li>
     *   <li>
     *     Ensure the object has a public, no-argument constructor
     *   </li>
     *   </li>
     *    Ensure the object is an instance of the class represented
     *    by <code>assignableTo</code>
     * </ul>
     * If any of these tests fail, queue a message to be displayed at a
     * later point in time.
     * @param type The type of Faces object we're validating
     * @param className the class name of the Faces object we're validating
     * @param assignableTo the type we expect <code>className</code> to
     *  either implement or extend
     */
    public void validateObject(ObjectType type, String className, Class<?> assignableTo) {

        // temporary hack until we can fix the stylesheets that create
        // the runtime xml
        if ("javax.faces.component.html.HtmlHead".equals(className)
              || "javax.faces.component.html.HtmlBody".equals(className)) {
            return;
        }
        Class<?> c = null;
        try {
            c = Util.loadClass(className, this);
        } catch (ClassNotFoundException cnfe) {
            messages.add(MessageUtils.getExceptionMessageString(
                             MessageUtils.VERIFIER_CLASS_NOT_FOUND_ID,
                             type,
                             className));
        } catch (NoClassDefFoundError ncdfe) {
            messages.add(MessageUtils.getExceptionMessageString(
                             MessageUtils.VERIFIER_CLASS_MISSING_DEP_ID,
                             type,
                             className,
                             ncdfe.getMessage()));
        }
        if (c != null) {
            try {
                Constructor ctor = c.getConstructor(RIConstants.EMPTY_CLASS_ARGS);
                if (!Modifier.isPublic(ctor.getModifiers())) {
                     messages.add(MessageUtils.getExceptionMessageString(
                                      MessageUtils.VERIFIER_CTOR_NOT_PUBLIC_ID,
                                      type,
                                      className));
                }
            } catch (NoSuchMethodException nsme) {
                messages.add(MessageUtils.getExceptionMessageString(
                                 MessageUtils.VERIFIER_NO_DEF_CTOR_ID,
                                 type,
                                 className));
            }
            if (!assignableTo.isAssignableFrom(c)) {
                messages.add(MessageUtils.getExceptionMessageString(
                                 MessageUtils.VERIFIER_WRONG_TYPE_ID,
                                 type,
                                 className));
            }
        }

    }

}
