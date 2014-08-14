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

package com.sun.faces.facelets.tag.jstl.fn;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Implementations of JSTL Functions
 * 
 * @author Jacob Hookom
 */
public final class JstlFunction {

    private JstlFunction() {
    }

    public static boolean contains(String name, String searchString) {
        if (name == null) {
            name = "";
        }
        if (searchString == null) {
            searchString = "";
        }
        return name.contains(searchString);
    }

    public static boolean containsIgnoreCase(String name, String searchString) {
        if (name == null) {
            name = "";
        }
        if (searchString == null) {
            searchString = "";
        }
        return name.toLowerCase().contains(searchString.toLowerCase());
    }

    public static boolean endsWith(String name, String searchString) {
        if (name == null) {
            name = "";
        }
        if (searchString == null) {
            searchString = "";
        }
        return name.endsWith(searchString);
    }

    public static String escapeXml(String value) {
        if (value == null || value.length() == 0) {
            value = "";
        }
        StringBuilder b = new StringBuilder(value.length());
        for (int i = 0, len = value.length(); i < len; i++) {
            char c = value.charAt(i);
            if (c == '<') {
                b.append("&lt;");
            } else if (c == '>') {
                b.append("&gt;");
            } else if (c == '\'') {
                b.append("&#039;");
            } else if (c == '"') {
                b.append("&#034;");
            } else if (c == '&') {
                b.append("&amp;");
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }

    public static int indexOf(String name, String searchString) {
        if (name == null) {
            name = "";
        }
        if (searchString == null) {
            searchString = "";
        }
        return name.indexOf(searchString);
    }

    public static String join(String[] a, String delim) {
        if (a == null|| a.length == 0) {
            return "";
        }
        boolean skipDelim = false;
        if (delim == null || delim.length() == 0) {
            skipDelim = true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = a.length, delimCount = (len - 1); i < len; i++) {
            sb.append(a[i]);
            if (!skipDelim && (i < delimCount)) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        if (obj instanceof String) {
            return ((String) obj).length();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }
        if (obj instanceof Enumeration) {
            Enumeration e = (Enumeration) obj;
            int count = 0;
            while (e.hasMoreElements()) {
                e.nextElement();
                count++;
            }
            return count;
        }
        if (obj instanceof Iterator) {
            Iterator i = (Iterator) obj;
            int count = 0;
            while (i.hasNext()) {
                i.next();
                count++;
            }
            return count;
        }
        throw new IllegalArgumentException("Object type not supported: "
                + obj.getClass().getName());
    }
    
    public static String replace(String value, String before, String after) {
        if (value == null) {
            value = "";
        }
        if (before == null) {
            before = "";
        }
        if (before.length() == 0) {
            return value;
        }
        if (value.length() == 0) {
            return "";
        }
        if (after == null) {
            after = "";
        }

        return value.replaceAll(before, after);
    }
    
    public static String[] split(String value, String d) {
        if (value == null) {
            value = "";
        }
        if (value.length() == 0) {
            return new String[]{ "" };
        }
        if (d == null) {
            d = "";
        }
        if (d.length() == 0) {
            return new String[] { value };
        }

        List<String> tokens = new ArrayList<String>();
        for (StringTokenizer st = new StringTokenizer(value, d); st.hasMoreTokens(); ) {
            tokens.add(st.nextToken());
        }

        return tokens.toArray(new String[tokens.size()]);
    }
    
    public static boolean startsWith(String value, String p) {
        if (value == null) {
            value = "";
        }
        if (p == null) {
            p = "";
        }
        return value.startsWith(p);
    }
    
    public static String substring(String v, int s, int e) {
        if (v == null) {
            v = "";
        }
        if (s >= v.length()) {
            return "";            
        }
        if (s < 0) {
            s = 0;
        }
        if (e < 0 || e >= v.length()) {
            e = v.length();
        }
        if (e < s) {
            return "";
        }
        return v.substring(s, e);
    }
    
    public static String substringAfter(String v, String p) {
        if (v == null) {
            v = "";
        }
        if (v.length() == 0) {
            return "";
        }
        if (p == null) {
            p = "";
        }
        int i = v.indexOf(p);
        if (i == -1) {
            return "";
        }
        return v.substring(i+p.length());
    }
    
    public static String substringBefore(String v, String s) {
        if (v == null) {
            v = "";
        }
        if (v.length() == 0) {
            return "";
        }
        if (s == null) {
            s = "";
        }
        int i = v.indexOf(s);
        if (i == -1) {
            return "";
        }
        return v.substring(0, i);
    }
    
    public static String toLowerCase(String v) {
        if (v == null || v.length() == 0) {
            return "";
        }
        return v.toLowerCase();
    }
    
    public static String toUpperCase(String v) {
        if (v == null || v.length() == 0) {
            return "";
        }
        return v.toUpperCase();
    }
    
    public static String trim(String v) {
        if (v == null || v.length() == 0) {
            return "";
        }
        return v.trim();
    }

}
