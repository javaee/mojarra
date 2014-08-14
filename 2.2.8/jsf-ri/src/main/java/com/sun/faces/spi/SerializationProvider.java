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

package com.sun.faces.spi;

import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 * <p>This interface provides a mechanism to allow the use
 * of alternate Java Serialization implementations.</p>
 * 
 * <p>The implementation of this interface *must* be thread-safe and must
 * have a no-arg constructor.</p>
 */
public interface SerializationProvider {

    /**
     * <p>Creates a new <code>ObjectInputStream</code> wrapping the specified
     * <code>source</code>.</p>
     * 
     * <p>It's <em>extremely important</em> that the ObjectInputStream
     * returned by this method extends the serialization implementation's ObjectInputStream 
     * and overrides the {@link ObjectInputStream#resolveClass(java.io.ObjectStreamClass)}
     * of  to perform the following or the equivalent thereof: 
     * <br>
     * <pre>
     *     return Class.forName(desc.getName(),true, 
                Thread.currentThread().getContextClassLoader());
     * </pre>
     * <br>
     * 
     * If this step isn't done, there may be problems when deserializing.</p>
     * 
     * @param source the source stream from which to read the Object(s)
     *  from
     * @return an <code>ObjectInputStream</code>
     * @throws IOException if an error occurs when creating the input stream
     */
    public ObjectInputStream createObjectInputStream(InputStream source)
    throws IOException;


    /**
     * <p>Creates a new <code>ObjectOutputStream</code> wrapping the
     * specified <code>destination</code>.</p>
     * @param destination the destination of the serialized Object(s)
     * @return an <code>ObjectOutputStream</code>
     * @throws IOException if an error occurs when creating the output stream
     */
    public ObjectOutputStream createObjectOutputStream(OutputStream destination)
    throws IOException;


} // END SerializationProvider
