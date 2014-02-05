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
 */

package com.sun.faces.facelets.tag;

import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author edburns
 */
public abstract class TagHandlerImpl extends TagHandler {

    public TagHandlerImpl(TagConfig config) {
        super(config);
    }
    
    /**
     * Searches child handlers, starting at the 'nextHandler' for all
     * instances of the passed type.  This process will stop searching
     * a branch if an instance is found.
     * 
     * @param type Class type to search for
     * @return iterator over instances of FaceletHandlers of the matching type
     */
    protected final Iterator findNextByType(Class type) {
        List found = new ArrayList();
        if (type.isAssignableFrom(this.nextHandler.getClass())) {
            found.add(this.nextHandler);
        } else if (this.nextHandler instanceof CompositeFaceletHandler) {
            FaceletHandler[] h = ((CompositeFaceletHandler) this.nextHandler).getHandlers();
            for (int i = 0; i < h.length; i++) {
                if (type.isAssignableFrom(h[i].getClass())) {
                    found.add(h[i]);
                }
            }
        }
        return found.iterator();
    }
    
    public final static Iterator findNextByType(FaceletHandler nextHandler,
            Class type) {
        List found = new ArrayList();
        if (type.isAssignableFrom(nextHandler.getClass())) {
            found.add(nextHandler);
        } else if (nextHandler instanceof CompositeFaceletHandler) {
            FaceletHandler[] h = ((CompositeFaceletHandler) nextHandler).getHandlers();
            for (int i = 0; i < h.length; i++) {
                if (type.isAssignableFrom(h[i].getClass())) {
                    found.add(h[i]);
                }
            }
        }
        return found.iterator();
        
    }

}
