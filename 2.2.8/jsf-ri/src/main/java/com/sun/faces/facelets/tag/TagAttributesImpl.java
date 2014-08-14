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

package com.sun.faces.facelets.tag;

import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;
import java.util.*;
import javax.faces.view.facelets.Tag;

/**
 * A set of TagAttributesImpl, usually representing all attributes on a Tag.
 * 
 * @see javax.faces.view.facelets.TagAttribute
 * @author Jacob Hookom
 * @version $Id$
 */
public final class TagAttributesImpl extends TagAttributes {
    private final static TagAttribute[] EMPTY = new TagAttribute[0];

    private final TagAttribute[] attrs;

    private final String[] ns;

    private final List nsattrs;
    
    private Tag tag;

    /**
     * 
     */
    public TagAttributesImpl(TagAttribute[] attrs) {
        this.attrs = attrs;

        // grab namespaces
        int i = 0;
        Set set = new HashSet();
        for (i = 0; i < this.attrs.length; i++) {
            set.add(this.attrs[i].getNamespace());
        }
        this.ns = (String[]) set.toArray(new String[set.size()]);
        Arrays.sort(ns);

        // assign attrs
        this.nsattrs = new ArrayList();
        for (i = 0; i < ns.length; i++) {
            nsattrs.add(i, new ArrayList());
        }
        int nsIdx = 0;
        for (i = 0; i < this.attrs.length; i++) {
            nsIdx = Arrays.binarySearch(ns, this.attrs[i].getNamespace());
            ((List) nsattrs.get(nsIdx)).add(this.attrs[i]);
        }
        for (i = 0; i < ns.length; i++) {
            List r = (List) nsattrs.get(i);
            nsattrs.set(i, r.toArray(new TagAttribute[r.size()]));
        }
    }

    /**
     * Return an array of all TagAttributesImpl in this set
     * 
     * @return a non-null array of TagAttributesImpl
     */
    @Override
    public TagAttribute[] getAll() {
        return this.attrs;
    }

    /**
     * Using no namespace, find the TagAttribute
     * 
     * @see #get(String, String)
     * @param localName
     *            tag attribute name
     * @return the TagAttribute found, otherwise null
     */
    @Override
    public TagAttribute get(String localName) {
        return get("", localName);
    }

    /**
     * Find a TagAttribute that matches the passed namespace and local name.
     * 
     * @param ns
     *            namespace of the desired attribute
     * @param localName
     *            local name of the attribute
     * @return a TagAttribute found, otherwise null
     */
    @Override
    public TagAttribute get(String ns, String localName) {
        if (ns != null && localName != null) {
            int idx = Arrays.binarySearch(this.ns, ns);
            if (idx >= 0) {
                TagAttribute[] uia = (TagAttribute[]) this.nsattrs.get(idx);
                for (int i = 0; i < uia.length; i++) {
                    if (localName.equals(uia[i].getLocalName())) {
                        return uia[i];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get all TagAttributesImpl for the passed namespace
     * 
     * @param namespace
     *            namespace to search
     * @return a non-null array of TagAttributesImpl
     */
    @Override
    public TagAttribute[] getAll(String namespace) {
        int idx = 0;
        if (namespace == null) {
            idx = Arrays.binarySearch(this.ns, "");
        } else {
            idx = Arrays.binarySearch(this.ns, namespace);
        }
        if (idx >= 0) {
            return (TagAttribute[]) this.nsattrs.get(idx);
        }
        return EMPTY;
    }

    /**
     * A list of Namespaces found in this set
     * 
     * @return a list of Namespaces found in this set
     */
    @Override
    public String[] getNamespaces() {
        return this.ns;
    }

    @Override
    public Tag getTag() {
        return this.tag;
    }
    
    public void setTag(Tag tag) {
        this.tag = tag;
        for (TagAttribute cur : attrs) {
            cur.setTag(tag);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.attrs.length; i++) {
            sb.append(this.attrs[i]);
            sb.append(' ');
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
