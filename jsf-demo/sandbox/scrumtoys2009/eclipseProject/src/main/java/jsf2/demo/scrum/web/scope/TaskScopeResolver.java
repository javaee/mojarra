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

package jsf2.demo.scrum.web.scope;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;

/**
 * Resolver to #{taskScope} expression.
 * @author eder
 */
public class TaskScopeResolver extends ELResolver {

    private static final String SCOPE_NAME = "taskScope";

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return Collections.<FeatureDescriptor>emptyList().iterator();
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return Object.class;
    }

    @Override
    public Object getValue(ELContext context, Object scope, Object property) {
        if (property == null) {
            throw new PropertyNotFoundException();
        }
        if (scope == null && SCOPE_NAME.equals(property.toString())) {
            TaskScope scopeManager = getScope(context);
            context.setPropertyResolved(true);
            return scopeManager;
        } else if (scope != null && scope instanceof TaskScope) {
            //looking for bean in scope already created.
            return lookupBean(context, (TaskScope) scope, property.toString());
        } else if (scope == null) {
            return lookupBean(context, getScope(context), property.toString());
        }
        return null;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
    }

    private TaskScope getScope(ELContext context) {
        //looking for custom scope in the session
        //if doesn't exists create and put it in the session
        FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
        Map<String,Object> sessionMap = facesContext.getExternalContext().getSessionMap();

        TaskScope scopeManager = (TaskScope) sessionMap.get(SCOPE_NAME);
        if (scopeManager == null) {
            scopeManager = new TaskScope(facesContext.getApplication());
            sessionMap.put(SCOPE_NAME, scopeManager);
            scopeManager.notifyCreate(SCOPE_NAME,facesContext);
        }
        return scopeManager;
    }

    private Object lookupBean(ELContext context, TaskScope scope, String key) {
        //looking for mbean in taskScope
        Object value = scope.get(key);
        context.setPropertyResolved(value != null);
        return value;
    }

    public static void destroyScope() {
        //remove scope from the session
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
        TaskScope taskScope = (TaskScope) sessionMap.remove(SCOPE_NAME);
        if (taskScope != null)
            taskScope.notifyDestroy(SCOPE_NAME, ctx);
    }

}
