/*
 * $Id: LifecycleManagedBeanFactory.java,v 1.1 2005/08/29 19:40:23 edburns Exp $
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

package components.model;
import com.sun.faces.config.beans.ManagedBeanBean;
import com.sun.faces.spi.ManagedBeanFactory;
import com.sun.faces.spi.ManagedBeanFactory.Scope;
import com.sun.faces.spi.ManagedBeanFactoryWrapper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.PostConstruct;
import javax.ejb.PreDestroy;
import javax.faces.context.FacesContext;

/**
 *
 * @author edburns
 */
public class LifecycleManagedBeanFactory extends ManagedBeanFactoryWrapper {
    
    private ManagedBeanFactory parent = null;
    
    public static ManagedBeanFactory mostRecentMBF = null;
    
    /** Creates a new instance of NewManagedBeanFactory */
    public LifecycleManagedBeanFactory(ManagedBeanFactory old) {
        this.parent = old;
        mostRecentMBF = this;
    }
    
    public ManagedBeanFactory getWrapped() {
        return parent;
    }
    
    public Object newInstance(FacesContext context) {
        Object result = getWrapped().newInstance(context);
        if (null != result) {
            Class resultClass = result.getClass();
            Method [] methods = resultClass.getMethods();
            PostConstruct postAnno = null;
            PreDestroy preAnno = null;
            List<Object []> scopeDestroyList = null;
            
            for (int i = 0; i < methods.length; i++) {
                String name = methods[i].getName();
                // If the bean wants a call on create
                if (null != (postAnno =
                        methods[i].getAnnotation(PostConstruct.class))) {
                    try {
                        methods[i].invoke(result);
                        
                    } catch (IllegalAccessException accessE) {
                        
                    } catch (IllegalArgumentException argumentE) {
                        
                    } catch (InvocationTargetException ite) {
                        
                    }
                }
                // If the bean wants a call on destroy 
                if (null != (preAnno =
                        methods[i].getAnnotation(PreDestroy.class))) {
                    if (null != (scopeDestroyList =
                            getScopeDestroyList(context))) {
                        Object methodAndInstance[] = { methods[i], result };
                        scopeDestroyList.add(methodAndInstance);
                    }
                }
                
            }
        }
           
        return result;
    }
    
    private List<Object []> getScopeDestroyList(FacesContext context) {
        Scope scope = getWrapped().getScope();
        List result = null;
        Map scopeMap = null;
        
        if (scope == Scope.REQUEST) {
            scopeMap = context.getExternalContext().getRequestMap();
        }
        else if (scope == Scope.SESSION) {
            scopeMap = context.getExternalContext().getSessionMap();
        }
        else if (scope == Scope.APPLICATION) {
            scopeMap = context.getExternalContext().getApplicationMap();
        }
        if (null != scopeMap) {
            if (null == (result = (List) scopeMap.get("components.model"))) {
                result = new ArrayList<Object []>();
                scopeMap.put("components.model", result);
            }
        }
        return result;        
    }
    
}

