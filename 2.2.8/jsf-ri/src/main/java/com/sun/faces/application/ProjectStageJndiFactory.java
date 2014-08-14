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

package com.sun.faces.application;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.spi.ObjectFactory;
import javax.naming.Name;
import javax.naming.Context;
import javax.naming.Reference;
import javax.naming.RefAddr;
import javax.faces.application.ProjectStage;

import com.sun.faces.util.FacesLogger;

/**
 * Allows configuring ProjectStage at a server (or in GlassFish's case domain)
 * level.  This allows for the concept of development and test servers where
 * each application doesn't need to be individually configured, but will instead
 * rely on global JNDI configuration instead.
 */
public class ProjectStageJndiFactory implements ObjectFactory {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    /**
     * Lookup the configured stage by looking for the parameter <code>stage<code>.
     * If the value of <code>stage</code> cannot be determined, the default
     * {@link javax.faces.application.ProjectStage#Production} is returned.
     *
     * @see ObjectFactory#getObjectInstance(Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)
     */
    public Object getObjectInstance(Object obj,
                                    Name name,
                                    Context nameCtx,
                                    Hashtable<?, ?> environment)
    throws Exception {

        if (obj != null && obj instanceof Reference) {
            Reference ref = (Reference) obj;
            RefAddr addr = ref.get("stage");
            if (addr != null) {
                String val = (String) addr.getContent();
                if (val != null) {
                    return val.trim();
                }
            } else {
            	if (LOGGER.isLoggable(Level.WARNING)) {
            		LOGGER.warning("'stage' property not defined.  Defaulting to Production");
            	}
            }
        }
        return ProjectStage.Production.toString();

    }
}
