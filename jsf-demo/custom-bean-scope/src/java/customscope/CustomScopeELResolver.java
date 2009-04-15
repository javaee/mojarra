/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package customscope;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.beans.FeatureDescriptor;

/**
 * This ELResolver handles the resolution of entities within a custom EL scope
 * called <code>customScope</code>.
 */
public class CustomScopeELResolver extends ELResolver {

    private static final String SCOPE_NAME = "customScope";


    // ------------------------------------------------- Methods From ELResolver


    public Object getValue(ELContext elContext, Object base, Object property) {
        if (property == null) {
            throw new PropertyNotFoundException();
        }
        if (base == null && SCOPE_NAME.equals(property.toString())) {
            // explicit scope lookup request
            Map<String,Object> customScope = getScope(elContext);
            elContext.setPropertyResolved(true);
            return customScope;
        } else if (base != null
                   && base instanceof Map
                   && ((Map) base).containsKey(SCOPE_NAME)) {
            // We're dealing with the custom scope that has been explicity referenced
            // by an expression.  'property' will be the name of some entity
            // within the scope.
            //noinspection unchecked
            return lookup(elContext, (Map<String,Object>) base, property.toString());
        } else if (base == null) {
            // bean has already been created and is in scope.
            // check to see if the bean is present
            return lookup(elContext, getScope(elContext), property.toString());
        }
        return null;
    }

    public Class<?> getType(ELContext elContext, Object base, Object property) {
        return Object.class;
    }

    public void setValue(ELContext elContext, Object base, Object property, Object value) {
        // this scope isn't writable in the strict sense, so do nothing.
    }

    public boolean isReadOnly(ELContext elContext, Object base, Object property) {
        return true;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object base) {
        return Collections.<FeatureDescriptor>emptyList().iterator();
    }

    public Class<?> getCommonPropertyType(ELContext elContext, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }


    // --------------------------------------------------------- Private Methods


    private Map<String,Object> getScope(ELContext elContext) {

        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
        Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
        @SuppressWarnings({"unchecked"})
        Map<String,Object> customScope = (Map<String,Object>) sessionMap.get(SCOPE_NAME);
        if (customScope == null) {
            customScope = new ConcurrentHashMap<String,Object>();
            customScope.put(SCOPE_NAME, SCOPE_NAME);
            sessionMap.put(SCOPE_NAME, customScope);
        }
        return customScope;

    }

    
    private Object lookup(ELContext elContext,
                          Map<String,Object> scope,
                          String key) {

        Object value = scope.get(key);
        elContext.setPropertyResolved(value != null);
        return value;

    }
}
