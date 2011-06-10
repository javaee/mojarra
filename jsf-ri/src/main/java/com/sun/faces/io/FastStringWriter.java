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

package com.sun.faces.io;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>This is based on {@link java.io.StringWriter} but backed by a
 * {@link StringBuilder} instead.</p>
 * <p/>
 * <p>This class is not thread safe.</p>
 */
public class FastStringWriter extends Writer {

    protected StringBuilder builder;

    // ------------------------------------------------------------ Constructors

    /**
     * <p>Constructs a new <code>FastStringWriter</code> instance
     * using the default capacity of <code>16</code>.</p>
     */
    public FastStringWriter() {
        builder = new StringBuilder();
    }

    /**
     * <p>Constructs a new <code>FastStringWriter</code> instance
     * using the specified <code>initialCapacity</code>.</p>
     *
     * @param initialCapacity specifies the initial capacity of the buffer
     *
     * @throws IllegalArgumentException if initialCapacity is less than zero
     */
    public FastStringWriter(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        builder = new StringBuilder(initialCapacity);
    }

    // ----------------------------------------------------- Methods from Writer

    /**
     * <p>Write a portion of an array of characters.</p>
     *
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     *
     * @throws IOException
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        builder.append(cbuf, off, len);
    }

    /**
     * <p>This is a no-op.</p>
     *
     * @throws IOException
     */
    public void flush() throws IOException {
    }

    /**
     * <p>This is a no-op.</p>
     *
     * @throws IOException
     */
    public void close() throws IOException {
    }

    // ---------------------------------------------------------- Public Methods

    /**
     * Write a string.
     *
     * @param str String to be written
     */
    public void write(String str) {
        builder.append(str);
    }

    /**
     * Write a portion of a string.
     *
     * @param str A String
     * @param off Offset from which to start writing characters
     * @param len Number of characters to write
     */
    public void write(String str, int off, int len) {
        builder.append(str.substring(off, off + len));
    }

    /**
     * Return the <code>StringBuilder</code> itself.
     *
     * @return StringBuilder holding the current buffer value.
     */
    public StringBuilder getBuffer() {
        return builder;
    }

    /** @return the buffer's current value as a string. */
    public String toString() {
        return builder.toString();
    }

    public void reset() {
        builder.setLength(0);
    }

}
