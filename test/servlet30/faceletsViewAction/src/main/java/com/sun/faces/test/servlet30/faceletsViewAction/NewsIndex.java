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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
 */

package com.sun.faces.test.servlet30.faceletsViewAction;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


@ApplicationScoped @ManagedBean(eager = true)
public class NewsIndex {

    private AtomicLong sequenceGenerator;
    private Map<Long, NewsStory> entries;

    @PostConstruct
    public void postContruct() {
        sequenceGenerator = new AtomicLong();
        entries = new TreeMap<Long, NewsStory>();

        entries.put(sequenceGenerator.incrementAndGet(), new NewsStory(sequenceGenerator.get(), "Story 1 Headline: Glassfish V3 released", "Story 1 Content: After much anticipation, Glassfish V3 has finally been released. And it's a really great piece of engineering."));
        entries.put(sequenceGenerator.incrementAndGet(), new NewsStory(sequenceGenerator.get(), "Story 2 Headline: ICEfaces evolves integration with NetBeans IDE and GlassFish", "Story 2 Content: The most recent release of ICEfaces (v1.7.2SP1) enhances the migration of existing Project Woodstock applications to ICEfaces. With the latest ICEfaces NetBeans plugin, it's now possible to add the ICEfaces framework to an existing Woodstock project, and begin to develop ICEfaces pages along side existing Woodstock pages."));
    }

    public Map<Long, NewsStory> getEntries() {
        return entries;
    }

    public NewsStory getStory(Long id) {
        if (id == null) {
            return null;
        }
        
        return entries.get(id);
    }

}
