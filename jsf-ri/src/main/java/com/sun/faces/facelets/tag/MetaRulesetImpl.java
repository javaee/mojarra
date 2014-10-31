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

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import javax.faces.view.facelets.*;
import java.beans.IntrospectionException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.ref.WeakReference;

/**
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public class MetaRulesetImpl extends MetaRuleset {

    private final static Logger LOGGER = FacesLogger.FACELETS_META.getLogger();
    private final static Map<Class, WeakReference<MetadataTarget>> metadata =
         Collections.synchronizedMap(new WeakHashMap<Class, WeakReference<MetadataTarget>>());

    private final Tag tag;
    private final Class type;
    private final Map<String,TagAttribute> attributes;
    private final List<Metadata> mappers;
    private final List<MetaRule> rules;


    // ------------------------------------------------------------ Constructors


    public MetaRulesetImpl(Tag tag, Class<?> type) {

        this.tag = tag;
        this.type = type;
        this.attributes = new HashMap<String,TagAttribute>();
        this.mappers = new ArrayList<Metadata>();
        this.rules = new ArrayList<MetaRule>();

        // setup attributes
        TagAttribute[] attrs = this.tag.getAttributes().getAll();
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].getLocalName().equals("class")) {
                attributes.put("styleClass", attrs[i]);
            } else {
                attributes.put(attrs[i].getLocalName(), attrs[i]);
            }
        }

        // add default rules
        this.rules.add(BeanPropertyTagRule.Instance);

    }


    // ---------------------------------------------------------- Public Methods


    public MetaRuleset ignore(String attribute) {

        Util.notNull("attribute", attribute);
        this.attributes.remove(attribute);
        return this;

    }


    public MetaRuleset alias(String attribute, String property) {

        Util.notNull("attribute", attribute);
        Util.notNull("property", property);
        TagAttribute attr = this.attributes.remove(attribute);
        if (attr != null) {
            this.attributes.put(property, attr);
        }
        return this;

    }

    public MetaRuleset add(Metadata mapper) {

        Util.notNull("mapper", mapper);
        if (!this.mappers.contains(mapper)) {
            this.mappers.add(mapper);
        }
        return this;

    }

    public MetaRuleset addRule(MetaRule rule) {

        Util.notNull("rule", rule);
        this.rules.add(rule);
        return this;

    }


     public Metadata finish() {

        if (!this.attributes.isEmpty()) {
            if (this.rules.isEmpty()) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    for (Iterator<TagAttribute> itr = this.attributes.values().iterator(); itr.hasNext(); ) {
                        LOGGER.severe(itr.next() + " Unhandled by MetaTagHandler for type "+this.type.getName());
                    }
                }
            } else {
                MetadataTarget target = this.getMetadataTarget();
                // now iterate over attributes
                int ruleEnd = this.rules.size() - 1;
                for (Map.Entry<String,TagAttribute> entry : attributes.entrySet()) {
                    Metadata data = null;
                    int i = ruleEnd;
                    while (data == null && i >= 0) {
                        MetaRule rule = this.rules.get(i);
                        data = rule.applyRule(entry.getKey(), entry.getValue(), target);
                        i--;
                    }
                    if (data == null) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.severe(entry.getValue() + " Unhandled by MetaTagHandler for type "+this.type.getName());
                        }
                    } else {
                        this.mappers.add(data);
                    }
                }
            }
        }

        if (this.mappers.isEmpty()) {
            return NONE;
        } else {
            return new MetadataImpl(this.mappers.toArray(new Metadata[this.mappers.size()]));
        }

    }

    public MetaRuleset ignoreAll() {

        this.attributes.clear();
        return this;

    }


    // ------------------------------------------------------- Protected Methods


    protected MetadataTarget getMetadataTarget() {
        WeakReference<MetadataTarget> metaRef = metadata.get(type);
        MetadataTarget meta = metaRef == null ? null : metaRef.get();
        if (meta == null) {
            try {
                meta = new MetadataTargetImpl(type);
            } catch (IntrospectionException e) {
                throw new TagException(this.tag,
                        "Error Creating TargetMetadata", e);
            }
            metadata.put(type, new WeakReference<MetadataTarget>(meta));
        }
        return meta;

    }



    // --------------------------------------------------------- Private Methods

    
    private final static Metadata NONE = new Metadata() {

        public void applyMetadata(FaceletContext ctx, Object instance) {
            // do nothing
        }

    };

}
