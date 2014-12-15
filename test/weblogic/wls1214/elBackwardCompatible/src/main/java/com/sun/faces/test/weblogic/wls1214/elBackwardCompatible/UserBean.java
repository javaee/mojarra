/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.weblogic.wls1214.elBackwardCompatible;

//import com.sun.el.ExpressionFactoryImpl;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ExpressionFactory;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
//import org.jboss.weld.el.WeldExpressionFactory;

@Named
@RequestScoped
public class UserBean implements Serializable {
    
    private final Map<String, Object> m_oNumberFilterBetweenOperatorMinValue;
    private final Map<String, Object> m_oNumberFilterBetweenOperatorMaxValue;
    
    public Map<String, Object> getNumberFilterBetweenOperatorMinValue() {
        return m_oNumberFilterBetweenOperatorMinValue;
    }
    
    public Map<String, Object> getNumberFilterBetweenOperatorMaxValue() {
        return m_oNumberFilterBetweenOperatorMaxValue;
    }
    
    private Integer intProp;

    public Integer getIntProp() {
        return intProp;
    }

    public void setIntProp(Integer intProp) {
        this.intProp = intProp;
    }
    
    private final List<String> listValues;

    public List<String> getListValues() {
        return listValues;
    }
    
    public UserBean() {
        this.listValues = new ArrayList<String>();
        listValues.add("1");
        listValues.add("2");
        listValues.add("3");
        
        m_oNumberFilterBetweenOperatorMinValue = new HashMap<String, Object>();        
        m_oNumberFilterBetweenOperatorMaxValue = new HashMap<String, Object>();
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        final String flagName = "forceSetFlag";

        Map<String, String> params = extContext.getRequestParameterMap();
        boolean forceSetFlag = false;
        if (params.containsKey(flagName)) {
            forceSetFlag = Boolean.valueOf(params.get(flagName));
        }
        
        if (forceSetFlag) {
            try {
                forceSetFlag();
            } catch (Exception ex) {
                throw new FacesException(ex);
            }
        }
    }
    
    private void forceSetFlag() throws Exception{
        final String flagName = "isBackwardCompatible22";
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        ServletContext sc = (ServletContext) extContext.getContext();
        JspApplicationContext jspAppContext = JspFactory.getDefaultFactory()
                .getJspApplicationContext(sc);
        ExpressionFactory ef = jspAppContext.getExpressionFactory();
        
//        Field delegateField = WeldExpressionFactory.class.getDeclaredField("delegate");
//        delegateField.setAccessible(true);
//        Object delegateInstance = delegateField.get(ef);
        
//        if (!(delegateInstance instanceof ExpressionFactoryImpl)) {
        
            // dereference twice to get the true ExpressionFactoryImpl instance
//            delegateInstance = delegateField.get(delegateInstance);
//        }
                
//        Field isBackwardCompatible22Field = ExpressionFactoryImpl.class.getDeclaredField(flagName);
//        isBackwardCompatible22Field.setAccessible(true);
        
        boolean flagValue = true;
//        Map<String, String> params = extContext.getRequestParameterMap();
//        if (params.containsKey(flagName)) {
//            flagValue = Boolean.valueOf(params.get(flagName));
//        }
        
//        isBackwardCompatible22Field.setBoolean(delegateInstance, flagValue);
        
    }
    
}

