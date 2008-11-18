/*
 * $Id: PartialTraversal.java,v 1.32 2007/04/27 22:00:09 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 */

package javax.faces.application;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;


/**
 * <p class="changed_added_2_0" >The <strong>PartialTraversal</strong> interface allows implementations
 * to define their own partial traversal strategy for partial view processing and
 * partial view rendering.</p>
 */

public interface PartialTraversal {

    /**
     * <p>Perform partial processing on the current view for the request processing
     * lifecycle phase indicated by {@link javax.faces.event.PhaseId}.  The
     * characteristics of the traversal are defined by 
     * {@link javax.faces.context.PartialViewContext}.</p>
     *
     * @param context The current {@link javax.faces.context.FacesContext}
     * @param phaseId The {@link javax.faces.event.PhaseId} for which the partial 
     *  traversal will occur.
     * @param viewRoot The root for the current view. 
     */
    public void traverse(FacesContext context, PhaseId phaseId, UIViewRoot viewRoot);

}
