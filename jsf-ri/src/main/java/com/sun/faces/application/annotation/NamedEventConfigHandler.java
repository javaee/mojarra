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

package com.sun.faces.application.annotation;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.NamedEventManager;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.NamedEvent;
import javax.faces.event.SystemEvent;

/**
 * This class handles the processing the NamedEvent annotation.  For each class
 * with this annotation, the following logic is applied:
 * <ol>
 *      <li>Get the unqualified class name (e.g., UserLoginEvent)</li>
 *      <li>Strip off the trailing "Event", if present (e.g., UserLogin)</li>
 *      <li>Convert the first character to lower-case (e.g., userLogin)</li>
 *      <li>Prepend the package name to the lower-cased name</li>
 *      <li>If the <code>shortName</code> attribute is specified, register the
 *          event by that name as well.</li>
 * </ol>
 */
public class NamedEventConfigHandler implements ConfigAnnotationHandler {

    private Map<Class<?>, Annotation> namedEvents;
    private static final Collection<Class<? extends Annotation>> HANDLES;


    static {
        Collection<Class<? extends Annotation>> handles =
                new ArrayList<Class<? extends Annotation>>(2);
        handles.add(NamedEvent.class);
        HANDLES = Collections.unmodifiableCollection(handles);
    }

    public Collection<Class<? extends Annotation>> getHandledAnnotations() {
        return HANDLES;
    }

    public void collect(Class<?> target, Annotation annotation) {
        if (namedEvents == null) {
            namedEvents = new HashMap<Class<?>, Annotation>();
        }
        namedEvents.put(target, annotation);
    }

    public void push(FacesContext ctx) {
        if (namedEvents != null) {
            ApplicationAssociate associate =
                    ApplicationAssociate.getInstance(ctx.getExternalContext());
            if (associate != null) {
                NamedEventManager nem = associate.getNamedEventManager();
                for (Map.Entry<Class<?>, Annotation> entry : namedEvents.entrySet()) {
                    process(nem, entry.getKey(), entry.getValue());
                }
            }
        }
    }

    // --------------------------------------------------------- Private Methods
    /*
     */
    private void process(NamedEventManager nem,
            Class<?> annotatedClass,
            Annotation annotation) {
        String name = annotatedClass.getSimpleName();
        int index = name.lastIndexOf("Event");
        if (index > -1) {
            name = name.substring(0, index);
        }

        name = annotatedClass.getPackage().getName() + ("." + name.charAt(0)).toLowerCase() + name.substring(1);
        nem.addNamedEvent(name, (Class<? extends SystemEvent>) annotatedClass);

        String shortName = ((NamedEvent) annotation).shortName();

        if (!"".equals(shortName)) {
            if (nem.isDuplicateNamedEvent(shortName)) {
                nem.addDuplicateName(shortName, (Class<? extends SystemEvent>) annotatedClass);
            } else {
                nem.addNamedEvent(shortName, (Class<? extends SystemEvent>) annotatedClass);
            }
        }
    }
}
