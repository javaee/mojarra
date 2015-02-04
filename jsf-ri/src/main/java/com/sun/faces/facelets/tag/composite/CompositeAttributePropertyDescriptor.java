/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.facelets.tag.composite;

import com.sun.faces.facelets.util.ReflectionUtil;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 *
 * @author mriem
 */
public class CompositeAttributePropertyDescriptor extends PropertyDescriptor {

        public CompositeAttributePropertyDescriptor(String propertyName, Method readMethod, Method writeMethod) throws IntrospectionException {
            super(propertyName, readMethod, writeMethod);
        }

        @Override
        public Object getValue(String attributeName) {
            Object result = super.getValue(attributeName);
            if ("type".equals(attributeName)) {
                if ((null != result) && !(result instanceof Class)) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ELContext elContext = context.getELContext();
                    String classStr = (String) ((ValueExpression)result).getValue(elContext);
                    if (null != classStr) {
                        try {
                            result = ReflectionUtil.forName(classStr);

                            this.setValue(attributeName, result);
                        } catch (ClassNotFoundException ex) {
                            classStr = "java.lang." + classStr;
                            boolean throwException = false;
                            try {
                                result = ReflectionUtil.forName(classStr);
                                
                                this.setValue(attributeName, result);
                            } catch (ClassNotFoundException ex2) {
                                throwException = true;
                            }
                            if (throwException) {
                                String message = "Unable to obtain class for " + classStr;
                                throw new FacesException(message, ex);
                            }
                        }
                    }
                    
                }
            }
            return result;
        }
        
        
    }
