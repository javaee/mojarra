/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.util.copier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Copier that copies an object by serializing and subsequently deserializing it
 * again.
 * <p>
 * As per the platform serialization rules, the object and all its non transient
 * dependencies have to implement the {@link Serializable} interface.
 *
 * @since 2.3
 * @author Arjan Tijms
 *
 */
public class SerializationCopier implements Copier {

    private static final String SERIALIZATION_COPIER_ERROR
            = "SerializationCopier cannot be used in this case. Please try other copier (e.g. MultiStrategyCopier, NewInstanceCopier, CopyCtorCopier, CloneCopier).";

    @Override
    public Object copy(Object object) {

        if (!(object instanceof Serializable)) {
            throw new IllegalStateException("Can't copy object of type " + object.getClass() + " since it doesn't implement Serializable");
        }

        try {
            return copyOutIn(object);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException(SERIALIZATION_COPIER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T copyOutIn(T object) throws ClassNotFoundException, IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Out out = new Out(byteArrayOutputStream);

        out.writeObject(object);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        In in = new In(byteArrayInputStream, out);

        @SuppressWarnings("unchecked")
        T cloned = (T) in.readObject();

        return cloned;
    }

    private static class In extends ObjectInputStream {

        private final Out out;

        In(InputStream inputStream, Out out) throws IOException {
            super(inputStream);
            this.out = out;
        }

        @Override
        protected Class<?> resolveProxyClass(String[] interfaceNames)
                throws IOException, ClassNotFoundException {
            return out.queue.poll();
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass objectStreamClass)
                throws IOException, ClassNotFoundException {

            String actuallyfound = null;
            Class<?> pollclass = out.queue.poll();

            if (pollclass != null) {
                actuallyfound = pollclass.getName();
            }

            if (!objectStreamClass.getName().equals(actuallyfound)) {
                throw new IllegalArgumentException(SERIALIZATION_COPIER_ERROR);
            }
            return pollclass;
        }
    }

    private static class Out extends ObjectOutputStream {

        Queue<Class<?>> queue = new LinkedList<>();

        Out(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void annotateClass(Class<?> c) {
            queue.add(c);
        }

        @Override
        protected void annotateProxyClass(Class<?> c) {
            queue.add(c);
        }
    }
}
