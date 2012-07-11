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

package javax.faces.view.facelets;

/**
 * <p class="changed_added_2_0"><span class="changed_modified_2_2">A</span> set of 
 * TagAttributes, usually representing all attributes on a Tag.</p>
 *
 * @since 2.0
 */
public abstract class TagAttributes {

    /**
     * Return an array of all TagAttributes in this set
     * 
     * @return a non-null array of TagAttributes
     */
    public abstract TagAttribute[] getAll();

    /**
     * Using no namespace, find the TagAttribute
     * 
     * @see #get(String, String)
     * @param localName
     *            tag attribute name
     * @return the TagAttribute found, otherwise null
     */
    public abstract TagAttribute get(String localName);

    /**
     * Find a TagAttribute that matches the passed namespace and local name.
     * 
     * @param ns
     *            namespace of the desired attribute
     * @param localName
     *            local name of the attribute
     * @return a TagAttribute found, otherwise null
     */
    public abstract TagAttribute get(String ns, String localName);

    /**
     * Get all TagAttributes for the passed namespace
     * 
     * @param namespace
     *            namespace to search
     * @return a non-null array of TagAttributes
     */
    public abstract TagAttribute[] getAll(String namespace);

    /**
     * A list of Namespaces found in this set
     * 
     * @return a list of Namespaces found in this set
     */
    public abstract String[] getNamespaces();
    
    /**
     * <p class="changed_added_2_2">A reference to the Tag for which this class
     * represents the attributes.  For compatibility with previous implementations,
     * an implementation is provided that returns {@code null}.</p>
     * 
     * @since 2.2
     * 
     * @return the {@link Tag} for which this class represents the attributes.
     */
    
    public Tag getTag() {
        return null;
    }
    
    /**
     * <p class="changed_added_2_2">Set a reference to the Tag for which
     * this class represents the attributes.  The VDL runtime must
     * ensure that this method is called before any {@link
     * FaceletHandler}s for this element are instantiated. For
     * compatibility with previous implementations, a no-op
     * implementation is provided.</p>
     * 
     * @since 2.2
     * 
     */
    public void setTag(Tag tag) {
        
    }

}
