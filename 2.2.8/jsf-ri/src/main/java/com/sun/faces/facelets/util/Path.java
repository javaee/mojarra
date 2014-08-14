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

/**
 * @author Jacob Hookom
 */
public final class Path {

    public static String normalize(String path) {
        if (path.length() == 0)
            return path;
        String n = path;
        boolean abs = false;
        while (n.indexOf('\\') >= 0) {
            n = n.replace('\\', '/');
        }
        if (n.charAt(0) != '/') {
            n = '/' + n;
            abs = true;
        }
        int idx = 0;
        while (true) {
            idx = n.indexOf("%20");
            if (idx == -1) {
                break;
            }
            n = n.substring(0, idx) + " " + n.substring(idx + 3);
        }
        while (true) {
            idx = n.indexOf("/./");
            if (idx == -1) {
                break;
            }
            n = n.substring(0, idx) + n.substring(idx + 2);
        }
        if (abs) {
            n = n.substring(1);
        }
        return n;
    }

    public static String relative(String ctx, String path) {
        if (path.length() == 0) {
            return context(ctx);
        }
        String c = context(normalize(ctx));
        String p = normalize(path);
        p = c + p;

        int idx = 0;
        while (true) {
            idx = p.indexOf("/../");
            if (idx == -1) {
                break;
            }
            int s = p.lastIndexOf('/', idx - 3);
            if (s == -1) {
                break;
            }
            p = p.substring(0, s) + p.substring(idx + 3);
        }
        return p;
    }

    public static String context(String path) {
        int idx = path.lastIndexOf('/');
        if (idx == -1) {
            return "/";
        }
        return path.substring(0, idx + 1);
    }

}
