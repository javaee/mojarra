/*
 * $Id: ResponseStateManager.java,v 1.13.28.2 2007/04/27 21:26:54 ofung Exp $
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

package javax.faces.render;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.StateManager.SerializedView;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;



/**
 * <p><strong>ResponseStateManager</strong> is the helper class to
 * {@link javax.faces.application.StateManager} that knows the specific
 * rendering technology being used to generate the response.  It is a
 * singleton abstract class, vended by the {@link RenderKit}.  This
 * class knows the mechanics of saving state, whether it be in hidden
 * fields, session, or some combination of the two.</p>
 */

public abstract class ResponseStateManager {


    /**
     *       
     * <p>Take the argument <code>state</code> and write it into
     * the output using the current {@link ResponseWriter}, which
     * must be correctly positioned already.</p>
     *
     * <p>If the {@link
     * javax.faces.application.StateManager.SerializedView} is to be
     * written out to hidden fields, the implementation must take care
     * to make all necessary character replacements to make the Strings
     * suitable for inclusion as an HTTP request paramater.</p>
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param state The serialized state information previously saved
     *
     */
    public abstract void writeState(FacesContext context,
				    SerializedView state) throws IOException;
    

    /**
     * <p>The implementation must inspect the current request and return
     * the tree structure Object passed to it on a previous invocation of
     * <code>writeState()</code>.</p>
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param viewId View identifier of the view to be restored
     *
     * @return the tree structure Object passed in to
     * <code>writeState</code>.  If this is the initial request, this
     * method returns null.
     */
    public abstract Object getTreeStructureToRestore(FacesContext context, 
						     String viewId);


    /**
     * <p>The implementation must inspect the current request and return
     * the component state Object passed to it on a previous invocation
     * of <code>writeState()</code>.</p>
     *
     * @param context The {@link FacesContext} instance for the current request
     *
     * @return the component state Object passed in to
     * <code>writeState</code>.
     */
    public abstract Object getComponentStateToRestore(FacesContext context);


}
