/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
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

import javax.faces.webapp.pdl.facelets.tag.TagHandler;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import java.lang.reflect.Method;

import javax.faces.FacesException;
import javax.faces.webapp.pdl.facelets.tag.Tag;

/**
 * A library of Tags associated with one or more namespaces.
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public interface TagLibrary {

    /**
     * true if the namespace is used in this library
     * 
     * @param ns
     *            namespace
     * @param t the tag instance currently active at the time
     * this method is called.  May be null

     */
    public boolean containsNamespace(String ns, Tag t);

    /**
     * If this library contains a TagHandler for the namespace and local name true if handled by this library
     * 
     * @param ns
     *            namespace
     * @param localName
     *            local name
     * @return
     */
    public boolean containsTagHandler(String ns, String localName);

    /**
     * Create a new instance of a TagHandler, using the passed TagConfig
     * 
     * @param ns
     *            namespace
     * @param localName
     *            local name
     * @param tag
     *            configuration information
     * @return a new TagHandler instance
     * @throws FacesException
     */
    public TagHandler createTagHandler(String ns, String localName,
            TagConfig tag) throws FacesException;

    /**
     * If this library contains the specified function name
     * 
     * @param ns namespace
     * @param name function name
     * @return true if handled
     */
    public boolean containsFunction(String ns, String name);

    /**
     * Return a Method instance for the passed namespace and name
     * 
     * @param ns namespace
     * @param name function name
     * @return
     */
    public Method createFunction(String ns, String name);
}
