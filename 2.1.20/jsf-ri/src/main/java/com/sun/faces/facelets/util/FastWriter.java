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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.util;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Jacob Hookom
 */
public final class FastWriter extends Writer {
    
    private char[] buff;
    private int size;
    
    public FastWriter() {
        this(1024);
    }
    
    public FastWriter(int initialSize) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("Initial Size cannot be less than 0");
        }
        this.buff = new char[initialSize];
    }

    public void close() throws IOException {
        // do nothing
    }

    public void flush() throws IOException {
        // do nothing
    }
    
    private void overflow(int len) {
        if (this.size + len > this.buff.length) {
            char[] next = new char[(this.size + len) * 2];
            System.arraycopy(this.buff, 0, next, 0, this.size);
            this.buff = next;    
        }
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        overflow(len);
        System.arraycopy(cbuf, off, this.buff, this.size, len);
        this.size += len;
    }

    public void write(char[] cbuf) throws IOException {
        this.write(cbuf, 0, cbuf.length);
    }

    public void write(int c) throws IOException {
        this.overflow(1);
        this.buff[this.size] = (char) c;
        this.size++;
    }

    public void write(String str, int off, int len) throws IOException {
        this.write(str.toCharArray(), off, len);
    }

    public void write(String str) throws IOException {
        this.write(str.toCharArray(), 0, str.length());
    }
    
    public void reset() {
        this.size = 0;
    }

    public String toString() {
        return new String(this.buff, 0, this.size);
    }
}
