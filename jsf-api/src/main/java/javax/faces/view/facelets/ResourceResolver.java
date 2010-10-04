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

import java.net.URL;

/**
 * <p class="changed_added_2_0">Provide a hook to decorate or override
 * the way that Facelets loads template files.  A default implementation
 * must be provided that satisfies the requirements for loading
 * templates as in Pre-JSF 2.0 Facelets.</p>

 * <div class="changed_added_2_0">

 * <p>If a <code>&lt;context-param&gt;</code> with the param name equal
 * to the value of {@link #FACELETS_RESOURCE_RESOLVER_PARAM_NAME}
 * exists, the runtime must interpret its value as a fully qualified
 * classname of a java class that extends <code>ResourceResolver</code>
 * and has a zero argument public constructor or a one argument public
 * constructor where the type of the argument is
 * <code>ResourceResolver</code>. If this param is set and its value
 * does not conform to those requirements, the runtime must log a
 * message and continue. If it does conform to these requirements and
 * has a one-argument constructor, the default
 * <code>ResourceResolver</code> must be passed to the constructor. If
 * it has a zero argument constructor it is invoked directly. In either
 * case, the new <code>ResourceResolver</code> replaces the old
 * one. </p>

 * </div>

 */

public abstract class ResourceResolver {

    public static final String FACELETS_RESOURCE_RESOLVER_PARAM_NAME = 
        "javax.faces.FACELETS_RESOURCE_RESOLVER";

    /**
     * <p class="changed_added_2_0">Returns the <code>URL</code> of a
     * Facelet template file.  Called by the Facelets Runtime to load a
     * template file referred to in a Facelets page.</p>

     * @param path the internal path to the template resource.

     */
    abstract public URL resolveUrl(String path);
}
