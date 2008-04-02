/* 
 * $Id: ViewHandlerResponseWrapper.java,v 1.10 2006/10/05 20:56:36 rlubke Exp $ 
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
* Copyright 2005 Sun Microsystems Inc. All Rights Reserved
*/

// ViewHandlerResponseWrapper.java 

package com.sun.faces.application;

import javax.faces.FacesException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.util.Util;


/**
 * <p>This class is used by {@link javax.faces.application.ViewHandler#createView} to obtain the
 * text that exists after the &lt;f:view&gt; tag.</p>
 */

public class ViewHandlerResponseWrapper extends HttpServletResponseWrapper {

    // Log instance for this class
    private static final Logger LOGGER = Util.getLogger(Util.FACES_LOGGER
                                                        + Util.APPLICATION_LOGGER);
    
    private ByteArrayServletOutputStream basos;
    private PrintWriter pw ;
    private CharArrayWriter caw;
    private int status = HttpServletResponse.SC_OK;


    public ViewHandlerResponseWrapper(HttpServletResponse wrapped) {
        super(wrapped);
    }


    @Override
    public void sendError(int sc, String msg) throws IOException {
        super.sendError(sc, msg);
        status = sc;
    }

    @Override
    public void sendError(int sc) throws IOException {
        super.sendError(sc);
        status = sc;
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        status = sc;
    }

    @Override
    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);
        status = sc;
    }


    public int getStatus() {
        return status;
    }

    public boolean isBytes() {
        return (null != basos);
    }

    public boolean isChars() {
        return (null != caw);
    }

    public byte[] getBytes() {
        byte[] result = null;
        if (null != basos) {
            result = basos.toByteArray();
        }
        return result;
    }

    public char[] getChars() {
        char[] result = null;
        if (null != caw) {
            result = caw.toCharArray();
        }
        return result;
    }

    public String toString() {
        String result = null;
        if (null != caw) {
            result = caw.toString();
        } else if (null != basos) {
            result = basos.toString();
        }
        return result;
    }

    public void flushContentToWrappedResponse() throws IOException {
        ServletResponse wrapped = this.getResponse();
        if (null != caw) {
            pw.flush();
            caw.writeTo(wrapped.getWriter());
            caw.reset();
        } else if (null != basos) {
            basos.writeTo(wrapped.getWriter(), wrapped.getCharacterEncoding());                       
            basos.resetByteArray();
        }

    }
    
    public void flushToWriter(Writer writer, String encoding) throws IOException {
        if (null != caw) {
            pw.flush();
            caw.writeTo(writer);
            caw.reset();
        } else if (null != basos) {
            basos.writeTo(writer, encoding);
            basos.resetByteArray();
        }
        writer.flush();
    }

    public void clearWrappedResponse() throws IOException {
        if (null != caw) {
            caw.reset();
        } else if (null != basos) {
            basos.resetByteArray();
        }
    }

    public ServletOutputStream getOutputStream() throws IOException {             
        if (pw != null) {
            throw new IllegalStateException();
        }
        if (null == basos) {
            basos = new ByteArrayServletOutputStream();
        }
        return basos;
    }

    public PrintWriter getWriter() throws IOException {
        if (basos != null) {
            throw new IllegalStateException();
        }
        if (null == pw) {
            caw = new CharArrayWriter(1024);
            pw = new PrintWriter(caw);
        }

        return pw;
    }
    
    
    // ----------------------------------------------------------- Inner Classes
    

    static class ByteArrayServletOutputStream extends ServletOutputStream {
        private DirectByteArrayOutputStream baos;        

        public ByteArrayServletOutputStream() {
            baos = new DirectByteArrayOutputStream(1024);
        }

        public void write(int n) {
            baos.write(n);
        }

        /** <p>It's important to not expose this as reset.</p> */

        public void resetByteArray() {
            baos.reset();
        }
        
        public byte[] toByteArray() {
            return baos.toByteArray();
        }


        /**
         * Converters the buffered bytes into chars based on the
         * specified encoding and writes them to the provided Writer.
         * @param writer target Writer
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
         * @return buf wrapped in a ByteBuffer
         */
        public ByteBuffer getByteBuffer() {
            return (ByteBuffer.wrap(buf, 0, count)); 
        }
        
    }


} // end of class ViewHandlerResponseWrapper
