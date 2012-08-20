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

package com.sun.faces.application.view;

import java.io.Writer;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.StateManager;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.io.FastStringWriter;

/**
 * Custom {@link Writer} to efficiently handle the state manager replacement
 * marker written out by {@link MultiViewHandler#writeState(javax.faces.context.FacesContext)}.
 */
final class WriteBehindStateWriter extends Writer {

    // length of the state marker
    private static final int STATE_MARKER_LEN =
          RIConstants.SAVESTATE_FIELD_MARKER.length();

    private static final ThreadLocal<WriteBehindStateWriter> CUR_WRITER =
          new ThreadLocal<WriteBehindStateWriter>();
    private Writer out;
    private Writer orig;
    private FastStringWriter fWriter;
    private FastStringWriter state;
    private StateManager stateManager;
    private boolean stateWritten;
    private int bufSize;
    private char[] buf;
    private FacesContext context;
    
    
    // -------------------------------------------------------- Constructors


    /**
     * Constructs a new <code>WriteBehindStateWriter</code> instance.
     *
     * @param out the writer we write non-buffered content to
     * @param context the {@link FacesContext} for the current request
     * @param bufSize the buffer size for post-processing buffered content
     */
    private WriteBehindStateWriter(Writer out,
                                  FacesContext context,
                                  int bufSize) {
        this.init(out, context, bufSize);
        stateManager = Util.getStateManager(context);
        this.buf = new char[bufSize];
        this.fWriter = new FastStringWriter(1024);
        this.state = new FastStringWriter((stateManager.isSavingStateInClient(
                context)) ? bufSize : 128);        
        CUR_WRITER.set(this);
        
    }
    
    private void init(Writer out,
            FacesContext context,
            int bufSize) {
        this.out = out;
        this.orig = out;
        this.context = context;
        this.bufSize = bufSize;
    }
    
    private void resetBuf() {
        for (int i = 0; i < this.bufSize; i++) {
            this.buf[i] = 0;
        }
    }


    // ------------------------------------------------- Methods from Writer


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(int)
     */
    public void write(int c) throws IOException {
        out.write(c);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(char[])
     */
    public void write(char cbuf[]) throws IOException {
        out.write(cbuf);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(String)
     */
    public void write(String str) throws IOException {
        out.write(str);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(String, int, int)
     */
    public void write(String str, int off, int len) throws IOException {
        out.write(str, off, len);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(char[], int, int)
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }


    /**
     * This is a no-op.
     */
    public void flush() throws IOException {
        // no-op
    }


    /**
     * This is a no-op.
     */
    public void close() throws IOException {
        // no-op
    }


    // ------------------------------------------------------ Public Methods


    /**
     * @return the <code>WriteBehindStateWriter</code> being used for processing
     *  this request
     */
    public static WriteBehindStateWriter getCurrentInstance() {
        return CUR_WRITER.get();
    }

    public static WriteBehindStateWriter getCurrentInstance(Writer out,
                                  FacesContext context,
                                  int bufSize) {
        WriteBehindStateWriter result = CUR_WRITER.get();
        if (null == result) {
            result = new WriteBehindStateWriter(out, context, bufSize);
        } else {
            result.init(out, context, bufSize);
        }
        return result;
    }
    
    /**
     * Clear the ThreadLocal state.
     */
    public void release() {
        this.resetBuf();
        this.bufSize = -1;
        this.context = null;
        this.fWriter.reset();
        this.orig = null;
        this.out = null;
        this.state.reset();
        this.stateWritten = false;
    }


    /**
     * When called, the original writer is backed up and replaced
     * with a new FastStringWriter.  All content written after this method
     * is called will then be buffered and written out later after the
     * entire view has been rendered.
     */
    public void writingState() {
        if (!stateWritten) {
            this.stateWritten = true;
            fWriter.reset();
            out = fWriter;
        }
    }

    /**
     * @return <code>true</code> if {@link #writingState()} has been called,
     *  otherwise returns <code>false</code>
     */
    public boolean stateWritten() {
        return stateWritten;
    }


    /**
     * <p> Write directly from our FastStringWriter to the provided writer.</p>
     *
     * @throws IOException if an error occurs
     */
    public void flushToWriter() throws IOException {

        // Save the state to a new instance of StringWriter to
        // avoid multiple serialization steps if the view contains
        // multiple forms.
        ResponseWriter origWriter = context.getResponseWriter();
        state.reset();
        context.setResponseWriter(origWriter.cloneWithWriter(state));
        stateManager.writeState(context, stateManager.saveView(context));
        context.setResponseWriter(origWriter);
        StringBuilder builder = fWriter.getBuffer();
        // begin writing...
        int totalLen = builder.length();
        StringBuilder stateBuilder = state.getBuffer();
        int stateLen = stateBuilder.length();
        int pos = 0;
        int tildeIdx = getNextDelimiterIndex(builder, pos);
        while (pos < totalLen) {
            if (tildeIdx != -1) {
                if (tildeIdx > pos && (tildeIdx - pos) > bufSize) {
                    // there's enough content before the first ~
                    // to fill the entire buffer
                    builder.getChars(pos, (pos + bufSize), buf, 0);
                    orig.write(buf);
                    pos += bufSize;
                } else {
                    // write all content up to the first '~'
                    builder.getChars(pos, tildeIdx, buf, 0);
                    int len = (tildeIdx - pos);
                    orig.write(buf, 0, len);
                    // now check to see if the state saving string is
                    // at the begining of pos, if so, write our
                    // state out.
                    if (builder.indexOf(
                          RIConstants.SAVESTATE_FIELD_MARKER,
                          pos) == tildeIdx) {
                        this.resetBuf();
                        // buf is effectively zero'd out at this point
                        int statePos = 0;
                        while (statePos < stateLen) {
                            if ((stateLen - statePos) > bufSize) {
                                // enough state to fill the buffer
                                stateBuilder.getChars(statePos,
                                                      (statePos + bufSize),
                                                      buf,
                                                      0);
                                orig.write(buf);
                                statePos += bufSize;
                            } else {
                                int slen = (stateLen - statePos);
                                stateBuilder.getChars(statePos,
                                                      stateLen,
                                                      buf,
                                                      0);
                                orig.write(buf, 0, slen);
                                statePos += slen;
                            }

                        }
                        // push us past the last '~' at the end of the marker
                        pos += (len + STATE_MARKER_LEN);
                        tildeIdx = getNextDelimiterIndex(builder, pos);
                    } else {
                        pos = tildeIdx;
                        tildeIdx = getNextDelimiterIndex(builder,
                                                         tildeIdx + 1);

                    }
                }
            } else {
                // we've written all of the state field markers.
                // finish writing content
                if (totalLen - pos > bufSize) {
                    // there's enough content to fill the buffer
                    builder.getChars(pos, (pos + bufSize), buf, 0);
                    orig.write(buf);
                    pos += bufSize;
                } else {
                    // we're near the end of the response
                    builder.getChars(pos, totalLen, buf, 0);
                    int len = (totalLen - pos);
                    orig.write(buf, 0, len);
                    pos += (len + 1);
                }
            }
        }

        // all state has been written.  Have 'out' point to the
        // response so that all subsequent writes will make it to the
        // browser.
        out = orig;

    }


    /**
     * @param builder buffered content
     * @param offset the offset to start the search from
     * @return the index of the next delimiter, if any
     */
    private static int getNextDelimiterIndex(StringBuilder builder,
                                             int offset) {

        return builder.indexOf(RIConstants.SAVESTATE_FIELD_DELIMITER,
                               offset);

    }

}

