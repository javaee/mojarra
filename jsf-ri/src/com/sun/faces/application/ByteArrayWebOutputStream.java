/*
 * $Id: ByteArrayWebOutputStream.java,v 1.2 2007/05/17 14:26:30 rlubke Exp $ 
 */

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
* Copyright 2007 Sun Microsystems Inc. All Rights Reserved
*/


package com.sun.faces.application;

import com.sun.faces.util.Util;

import javax.servlet.ServletOutputStream;
import javax.faces.FacesException;
import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;

/**
 * This steam convers byte content to character.
 */
class ByteArrayWebOutputStream extends ServletOutputStream {

        // Log instance for this class
    private static final Logger LOGGER =
                Util.getLogger(Util.FACES_LOGGER + Util.APPLICATION_LOGGER);

    private DirectByteArrayOutputStream baos;

    public ByteArrayWebOutputStream() {
        baos = new DirectByteArrayOutputStream(1024);
    }

    public void write(int n) {
        baos.write(n);
    }

    /**
     * <p>It's important to not expose this as reset.</p>
     */

    public void resetByteArray() {
        baos.reset();
    }

    public byte[] toByteArray() {
        return baos.toByteArray();
    }


    /**
     * Converts the buffered bytes into chars based on the
     * specified encoding and writes them to the provided Writer.
     *
     * @param writer   target Writer
     * @param encoding character encoding
     */
    public void writeTo(Writer writer, String encoding) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Converting buffered ServletOutputStream bytes"
                    + " to chars using " + encoding);
        }

        ByteBuffer bBuff = baos.getByteBuffer();
        CharsetDecoder decoder = Charset.forName(encoding).newDecoder();

        try {
            CharBuffer cBuff = decoder.decode(bBuff);
            writer.write(cBuff.array());
        } catch (CharacterCodingException cce) {
            throw new FacesException(cce);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
    }


    /**
     * <p>Write the buffered bytes to the provided OutputStream.</p>
     *
     * @param stream the stream to write to
     */
    public void writeTo(OutputStream stream) {
        try {
            stream.write(baos.getByteBuffer().array());
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
    }

    private static class DirectByteArrayOutputStream extends ByteArrayOutputStream {

        // -------------------------------------------------------- Constructors


        public DirectByteArrayOutputStream(int initialCapacity) {
            super(initialCapacity);
        }

        // ------------------------------------------------------- PublicMethods


        /**
         * Return the buffer backing this ByteArrayOutputStream as a
         * ByteBuffer.
         *
         * @return buf wrapped in a ByteBuffer
         */
        public ByteBuffer getByteBuffer() {
            return (ByteBuffer.wrap(buf, 0, count));
        }

    }
}




