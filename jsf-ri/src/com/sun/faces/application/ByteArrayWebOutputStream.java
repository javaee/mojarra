/*
 * $Id: ByteArrayWebOutputStream.java,v 1.4 2007/05/30 16:49:43 rlubke Exp $ 
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.application;

import com.sun.faces.util.FacesLogger;

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
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

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




