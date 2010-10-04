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
 * <p class="changed_added_2_0">Every kind of markup element in Facelets
 * VDL that has attributes that need to take action on a JSF Java API
 * artifact is associated with an instance of this class.  This class is
 * an abstraction to enable a rule based method for directing how
 * different kinds of elements take different kinds of actions in the
 * JSF Java API.  For example, consider this markup:</p>
 *
 * <div class="changed_added_2_0">

<code><pre>&lt;h:inputText value="#{user.userid}" 
                valueChangeListener="#{user.newUserId}" /&gt;</pre></code>

 * <p>This markup element corresponds to an instance of {@link
 * javax.faces.component.html.HtmlInputText} in the view.
 * <code>HtmlImputText</code> has a number of attributes that are to be
 * exposed to the page author.  <code>HtmlInputText</code> also
 * implements {@link javax.faces.component.EditableValueHolder}, which
 * extends {@link javax.faces.component.ValueHolder}.  Each of these
 * interfaces also expose a number of attributes to the page author.</p>

 * <p>Facelets employes the strategy pattern to allow the manner in
 * which all possible attributes are handled based on the nature of the
 * JSF Java API artifact associated with the markup element.</p>

 * <p>Subclasses override the {@link #createMetaRuleset} method to
 * return a {@link MetaRuleset} instance encapsulating all the strategies
 * for all the attributes that make sense for this particular markup
 * element.  The runtime calls the {@link #setAttributes(FaceletContext, Object)}
 * method to cause those rules to be executed and applied.</p>
 *
 * </div>
 * 
 * @since 2.0
 */
public abstract class MetaTagHandler extends TagHandler {

    private Class lastType = Object.class;

    private Metadata mapper;

    public MetaTagHandler(TagConfig config) {
        super(config);
    }

    /**
     * Extend this method in order to add your own rules.
     * 
     * @param type
     */
    protected abstract MetaRuleset createMetaRuleset(Class type);

    /**
     * Invoking/extending this method will cause the results of the created
     * MetaRuleset to auto-wire state to the passed instance.
     * 
     * @param ctx
     * @param instance
     */
    protected void setAttributes(FaceletContext ctx, Object instance) {
        if (instance != null) {
            Class type = instance.getClass();
            if (mapper == null || !this.lastType.equals(type)) {
                this.lastType = type;
                this.mapper = this.createMetaRuleset(type).finish();
            }
            this.mapper.applyMetadata(ctx, instance);
        }
    }
}
