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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

import com.sun.faces.RIConstants;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.util.Util;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Custom {@link Writer} to efficiently handle the state manager replacement
 * marker written out by {@link MultiViewHandler#writeState(javax.faces.context.FacesContext)}.
 */
final class WriteBehindStateWriter extends Writer {

    // length of the state marker
    private static final int STATE_MARKER_LEN =
          RIConstants.SAVESTATE_FIELD_MARKER.length();

    private static final ThreadLocal<WriteBehindStateWriter> CUR_WRITER =
          new ThreadLocal<>();
    private Writer out;
    private Writer orig;
    private FastStringWriter fWriter;
    private boolean stateWritten;
    private int bufSize;
    private char[] buf;
    private FacesContext context;
    private Object state;


    // -------------------------------------------------------- Constructors


    /**
     * Constructs a new <code>WriteBehindStateWriter</code> instance.
     *
     * @param out the writer we write non-buffered content to
     * @param context the {@link FacesContext} for the current request
     * @param bufSize the buffer size for post-processing buffered content
     */
    public WriteBehindStateWriter(Writer out,
                                  FacesContext context,
                                  int bufSize) {
        this.out = out;
        this.orig = out;
        this.context = context;
        this.bufSize = bufSize;
        this.buf = new char[bufSize];
        CUR_WRITER.set(this);
        
    }


    // ------------------------------------------------- Methods from Writer


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(int)
     */
    @Override
    public void write(int c) throws IOException {
        out.write(c);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(char[])
     */
    @Override
    public void write(char cbuf[]) throws IOException {
        out.write(cbuf);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(String)
     */
    @Override
    public void write(String str) throws IOException {
        out.write(str);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(String, int, int)
     */
    @Override
    public void write(String str, int off, int len) throws IOException {
        out.write(str, off, len);
    }


    /**
     * Writes directly to the current <code>out</code>.
     *
     * @see Writer#write(char[], int, int)
     */
    @Override
    public void write(char cbuf[], int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }


    /**
     * This is a no-op.
     */
    @Override
    public void flush() throws IOException {
        // no-op
    }


    /**
     * This is a no-op.
     */
    @Override
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
    
    public static void setCurrentInstance(WriteBehindStateWriter writer) {
        CUR_WRITER.set(writer);
    }


    /**
     * Clear the ThreadLocal state.
     */
    public void release() {
        CUR_WRITER.remove();
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
            out = fWriter = new FastStringWriter(1024);
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
        StateManager stateManager = Util.getStateManager(context);
        ResponseWriter origWriter = context.getResponseWriter();
        StringBuilder stateBuilder = getState(stateManager, origWriter);
        StringBuilder builder = fWriter.getBuffer();
        // begin writing...
        int totalLen = builder.length();
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
                        
                        stateBuilder = getState(stateManager, origWriter);
                        stateLen = stateBuilder.length();        
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
     * Get the state.
     * 
     * <p>
     *  In JSF 2.2 it is required by the specification that the view state hidden
     *  input in each h:form has a unique id. So we have to call this method
     *  multiple times as each h:form needs to generate the element id
     *  for itself.
     * </p>
     * 
     * @param stateManager the state manager.
     * @param origWriter the original response writer.
     * @return the state.
     * @throws IOException when an I/O error occurs. 
     */
    private StringBuilder getState(StateManager stateManager, ResponseWriter origWriter) throws IOException {
        FastStringWriter stateWriter =
                new FastStringWriter((stateManager.isSavingStateInClient(
                        context)) ? bufSize : 128);
        context.setResponseWriter(origWriter.cloneWithWriter(stateWriter));
        if(state == null) {
            state = stateManager.saveView(context);
        }
        stateManager.writeState(context, state);
        context.setResponseWriter(origWriter);
        StringBuilder stateBuilder = stateWriter.getBuffer();
        return stateBuilder;
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

