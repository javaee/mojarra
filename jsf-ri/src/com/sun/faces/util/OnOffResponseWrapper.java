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

package com.sun.faces.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
* <p>An HttpServletResponseWrapper that can be enabled or disabled
* with respect to the methods that deal with the writing of contents
* to the response.  This is necessary to avoid sending content 
* that appears outside of the <f:view> tag to the Ajax response.</p>
*/
public class OnOffResponseWrapper extends HttpServletResponseWrapper {
    public OnOffResponseWrapper(HttpServletResponse orig) {
        super(orig);
    }        
        
    private ServletOutputStream noOpServletOutputStream = null;

    public ServletOutputStream getOutputStream() throws IOException {
        if (null == noOpServletOutputStream) {
            // We implement *all* the methods of ServetOutputStream that
            // could possibly cause output.  This is because we cannot count
            // on the superclass behavior, so we need to guarantee that 
            // nothing is written unlis our outer class is enabled.
            noOpServletOutputStream = new ServletOutputStream() {
                    
                ServletOutputStream out = 
                        getResponse().getOutputStream();
                    
                public void println(String s) throws IOException {
                    if (isEnabled()) {
                        out.println(s);
                    }
                }

                public void print(String s) throws IOException {
                    if (isEnabled()) {
                        out.print(s);
                    }
                }

                public void write(byte[] b, int off, int len) throws IOException {
                    if (isEnabled()) {
                        out.write(b, off, len);
                    }
                }

                public void write(byte[] b) throws IOException {
                    if (isEnabled()) {
                        out.write(b);
                    }
                }

                public void write(int b) throws IOException {
                    if (isEnabled()) {
                        out.write(b);
                    }
                }

                public void println(int i) throws IOException {
                    if (isEnabled()) {
                        out.println(i);
                    }
                }

                public void print(int i) throws IOException {
                    if (isEnabled()) {
                        out.print(i);
                    }
                }

                public void print(boolean b) throws IOException {
                    if (isEnabled()) {
                        out.print(b);
                    }
                }

                public void println(boolean b) throws IOException {
                    if (isEnabled()) {
                        out.println(b);
                    }
                }

                public void print(long b) throws IOException {
                    if (isEnabled()) {
                        out.print(b);
                    }
                }

                public void println(long b) throws IOException {
                    if (isEnabled()) {
                        out.println(b);
                    }
                }

                public void print(double b) throws IOException {
                    if (isEnabled()) {
                        out.print(b);
                    }
                }

                public void println(double b) throws IOException {
                    if (isEnabled()) {
                        out.println(b);
                    }
                }

                public void print(char b) throws IOException {
                    if (isEnabled()) {
                        out.print(b);
                    }
                }

                public void println(char b) throws IOException {
                    if (isEnabled()) {
                        out.println(b);
                    }
                }

                public void print(float b) throws IOException {
                    if (isEnabled()) {
                        out.print(b);
                    }
                }

                public void println(float b) throws IOException {
                    if (isEnabled()) {
                        out.println(b);
                    }
                }
                public void close() throws IOException {
                    if (isEnabled()) {
                        out.close();
                    }
                }

                public void flush() throws IOException {
                    if (isEnabled()) {
                        out.flush();
                    }
                }

                public void println() throws IOException {
                    if (isEnabled()) {
                        out.println();
                    }
                }
            };
        }
        return noOpServletOutputStream;
    }
        
    private PrintWriter noOpPrintWriter = null;

    public PrintWriter getWriter() throws IOException {
        if (null == noOpPrintWriter) {
            // We implement *all* the methods of PrintWriter that
            // could possibly cause output.  This is because we cannot count
            // on the superclass behavior, so we need to guarantee that 
            // nothing is written unlis our outer class is enabled.
            noOpPrintWriter = new PrintWriter(new Writer() {
                    
                private PrintWriter writer = 
                    getResponse().getWriter();
                    
                    
                public void write(String str) throws IOException {
                    if (isEnabled()) {
                        writer.write(str);
                    }
                }

                public void write(char[] cbuf) throws IOException {
                    if (isEnabled()) {
                        writer.write(cbuf);
                    }
                }

                public Writer append(char c) throws IOException {
                    if (isEnabled()) {
                        writer.append(c);
                    }
                    return this;
                }

                public Writer append(CharSequence csq, int start, int end) throws IOException {
                    if (isEnabled()) {
                        writer.append(csq, start, end);
                    }
                    return this;
                }

                public void write(char[] cbuf, int off, int len) throws IOException {
                    if (isEnabled()) {
                        writer.write(cbuf, off, len);
                    }
                }

                public Writer append(CharSequence csq) throws IOException {
                    if (isEnabled()) {
                        writer.append(csq);
                    }
                    return this;
                }

                public void write(String str, int off, int len) throws IOException {
                    if (isEnabled()) {
                        writer.write(str, off, len);
                    }
                }

                public void write(int c) throws IOException {
                    if (isEnabled()) {
                        writer.write(c);
                    }
                     
                }

                public void close() throws IOException {
                    if (isEnabled()) {
                        writer.close();
                    }
                }

                public void flush() throws IOException {
                    if (isEnabled()) {
                        writer.flush();
                    }
                }
                    
            });
        }
        return noOpPrintWriter;
    }

    private boolean enabled;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setResponse(ServletResponse response) {
        // No-op.
    }

    public int getBufferSize() {
        int result = 0;
        if (this.isEnabled()) {
            result = getResponse().getBufferSize();
        }
        return result;
    }

    public void flushBuffer() throws IOException {
        if (this.isEnabled()) {
            getResponse().flushBuffer();
        }
    }

    public boolean isCommitted() {
        boolean result = false;
        if (this.isEnabled()) {
            result = getResponse().isCommitted();
        }
        return result;
    }

    public void reset() {
        if (this.isEnabled()) {
            getResponse().reset();
        }
    }

    public void resetBuffer() {
        if (this.isEnabled()) {
            getResponse().resetBuffer();
        }
    }
        
}
