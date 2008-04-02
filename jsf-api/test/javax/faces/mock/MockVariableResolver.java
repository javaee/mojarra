/*
 * $Id: MockVariableResolver.java,v 1.1 2003/10/21 23:58:23 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
