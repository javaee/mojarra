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
 * <p class="changed_added_2_0">A mutable set of rules to be used in
 * auto-wiring state to a particular object instance. Rules assigned to
 * this object will be composed into a single Metadata instance which
 * will encapsulate the ruleset.</p>
 *
 * @since 2.0
 */
public abstract class MetaRuleset {


    /**
     * <p class="changed_added_2_0">Customize this
     * <code>MetaRuleset</code> instance to advise it to ignore the
     * attribute named by the <code>attribute</code> argument, returning
     * <code>this</code>.</p>
     * @param attribute the name of the attribute to ignore.
     * 
     * @since 2.0
     */
    public abstract MetaRuleset ignore(String attribute);

    /**
     * <p class="changed_added_2_0">Customize this
     * <code>MetaRuleset</code> instance to advise it to ignore all
     * attributes, returning
     * <code>this</code>.</p>
     * @since 2.0
     */
    public abstract MetaRuleset ignoreAll();

    /**
     * <p class="changed_added_2_0">Customize this
     * <code>MetaRuleset</code> by removing the attribute named by
     * argument <code>attribute</code> and re-adding it under the name
     * given by the argument <code>property</code>, returning
     * <code>this</code>.</p>
     * @since 2.0
     */
    public abstract MetaRuleset alias(String attribute, String property);

    /**
     * <p class="changed_added_2_0">Add another {@link Metadata} to this
     * ruleset, returning <code>this</code>.</p>
     * @since 2.0
     */
    public abstract MetaRuleset add(Metadata mapper);

    /**
     * <p class="changed_added_2_0">Add another {@link MetaRule} to this
     * ruleset, returning <code>this</code>.</p>
     * @since 2.0
     */
    public abstract MetaRuleset addRule(MetaRule rule);

    /**
     * <p class="changed_added_2_0">Take actions to apply the rule.</p>
     * 
     */
    public abstract Metadata finish();
}
