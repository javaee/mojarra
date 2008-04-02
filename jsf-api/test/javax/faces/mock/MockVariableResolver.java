/*
 * $Id: MockVariableResolver.java,v 1.4 2005/08/22 22:08:27 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.mock;


import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;


/**
 * <p>Mock implementation of {@link VariableResolver} that supports a limited
 * subset of expression evaluation functionality:</p>
 * <ul>
 * <li>Recognizes <code>applicationScope</code>, <code>requestScope</code>,
 *     and <code>sessionScope</code> implicit names.</li>
 * <li>Searches in ascending scopes for non-reserved names.</li>
 * </ul>
 */

public class MockVariableResolver extends VariableResolver {


    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------ VariableResolver Methods


    public Object resolveVariable(FacesContext context, String name) {

        if ((context == null) || (name == null)) {
            throw new NullPointerException();
        }

        // Handle predefined variables
        if ("applicationScope".equals(name)) {
            return (econtext().getApplicationMap());
        } else if ("requestScope".equals(name)) {
            return (econtext().getRequestMap());
        } else if ("sessionScope".equals(name)) {
            return (econtext().getSessionMap());
        }

        // Look up in ascending scopes
        Map map = null;
        map = econtext().getRequestMap();
        if (map.containsKey(name)) {
            return (map.get(name));
        }
        map = econtext().getSessionMap();
        if ((map != null) && (map.containsKey(name))) {
            return (map.get(name));
        }
        map = econtext().getApplicationMap();
        if (map.containsKey(name)) {
            return (map.get(name));
        }

        // Requested object is not found
        return (null);

    }



    // --------------------------------------------------------- Private Methods


    private ExternalContext econtext() {

        return (FacesContext.getCurrentInstance().getExternalContext());

    }


}
