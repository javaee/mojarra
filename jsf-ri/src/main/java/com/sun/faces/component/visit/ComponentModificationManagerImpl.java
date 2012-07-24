/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.component.visit;

import java.util.Collection;
import java.util.LinkedList;
import javax.faces.component.visit.ComponentModification;
import javax.faces.component.visit.ComponentModificationManager;
import javax.faces.context.FacesContext;

/**
 * The default implementation of the ComponentModificationManager.
 * 
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public final class ComponentModificationManagerImpl extends ComponentModificationManager {

    /**
     * Stores the component modifications.
     */
    private LinkedList<ComponentModification> modifications = new LinkedList<ComponentModification>();
    
    /**
     * Push the component modification.
     * 
     * @param modification the component modification. 
     */
    @Override
    public void push(ComponentModification modification) {
        this.modifications.add(modification);
    }

    /**
     * Peek the last component modification.
     * 
     * @return the last component modification.
     */
    @Override
    public ComponentModification peek() {
        return this.modifications.peekLast();
    }

    /**
     * Pop the component modification.
     * 
     * @return the component modification.
     */
    @Override
    public ComponentModification pop() {
        return this.modifications.pollLast();
    }

    /**
     * Suspend component modifications.
     * 
     * @param context the Faces context.
     * @return the component modification.
     */
    @Override
    public Collection<ComponentModification> suspend(FacesContext context) {
        Collection<ComponentModification> result = this.modifications;
        this.modifications = new LinkedList<ComponentModification>();
        
        for(ComponentModification modification : result) {
            modification.suspend(context);
        }
        
        return result;
    }

    /**
     * Resume the component modification.
     * 
     * @param context the Faces context.
     * @param modifications the component modifications.
     */
    @Override
    public void resume(FacesContext context, Collection<ComponentModification> modifications) {
        for(ComponentModification modification : modifications) {
            modification.resume(context);
            this.modifications.add(modification);
        }
    }
}
