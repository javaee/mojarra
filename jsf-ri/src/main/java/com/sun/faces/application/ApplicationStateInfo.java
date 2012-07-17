/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.faces.config.WebConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.PartialStateSaving;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FullStateSavingViewIds;
import com.sun.faces.util.MessageUtils;

/**
 * This class maintains per-application information pertaining
 * to partail or full state saving as a whole or partial state saving
 * with some views using full state saving.
 */
public class ApplicationStateInfo {

    private boolean partialStateSaving;
    private Set<String> fullStateViewIds;


    // ------------------------------------------------------------ Constructors


    public ApplicationStateInfo() {

        WebConfiguration config = WebConfiguration.getInstance();
        partialStateSaving = config.isOptionEnabled(PartialStateSaving);

        if (partialStateSaving) {
            String[] viewIds = config.getOptionValue(FullStateSavingViewIds, ",");
            fullStateViewIds = new HashSet<String>(viewIds.length, 1.0f);
            fullStateViewIds.addAll(Arrays.asList(viewIds));
        }
        
    }


    // --------------------------------------------------------- Private Methods


    /**
     * @param viewId the view ID to check
     * @throws IllegalArgumentException if viewId is null
     * @return <code>true</code> if partial state saving should be used for the
     *  specified view ID, otherwise <code>false</code>
     */
    public boolean usePartialStateSaving(String viewId) {

        if (viewId == null) {
            throw new IllegalArgumentException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_VIEW_ID_ERROR_MESSAGE_ID));
        }

        return (partialStateSaving && !fullStateViewIds.contains(viewId));

    }

}
