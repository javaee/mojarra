/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
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
     */
    public ObjectInputStream createObjectInputStream(InputStream source)
    throws IOException;


    /**
     * <p>Creates a new <code>ObjectOutputStream</code> wrapping the
     * specified <code>destination</code>.</p>
     * @param destination the destination of the serialized Object(s)
     * @return an <code>ObjectOutputStream</code>
     */
    public ObjectOutputStream createObjectOutputStream(OutputStream destination)
    throws IOException;


} // END SerializationProvider