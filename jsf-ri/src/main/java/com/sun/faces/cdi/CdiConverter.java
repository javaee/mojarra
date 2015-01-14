/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.cdi;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * A delegate to the CDI managed converter.
 */
public class CdiConverter implements Converter, StateHolder {

    /**
     * Stores the converter-id (if any).
     */
    private String converterId;
    
    /**
     * Stores a transient reference to the CDI managed converter.
     */
    private transient Converter delegate;
    
    /**
     * Stores the for-class (if any).
     */
    private Class forClass;
    
    /**
     * Constructor. 
     */
    public CdiConverter() {
    }
    
    /**
     * Constructor.
     * 
     * @param converterId the converter id.
     * @param forClass the for class.
     * @param delegate the delegate.
     */
    public CdiConverter(String converterId, Class forClass, Converter delegate) {
        this.converterId = converterId;
        this.forClass = forClass;
        this.delegate = delegate;
    }
    
    /**
     * Get the object.
     * 
     * @param facesContext the Faces context.
     * @param component the UI component.
     * @param value the value.
     * @return the object.
     */
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        return getDelegate(facesContext).getAsObject(facesContext, component, value);
    }

    /**
     * Get the string representation.
     * 
     * @param facesContext the Faces context.
     * @param component the UI component.
     * @param value the value.
     * @return the string.
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        return getDelegate(facesContext).getAsString(facesContext, component, value);
    }

    /**
     * Save the state.
     * 
     * @param facesContext the Faces context.
     * @return the saved object.
     */
    @Override
    public Object saveState(FacesContext facesContext) {
        return new Object[] {
            converterId,
            forClass
        };
    }

    /**
     * Restore the state.
     * 
     * @param facesContext the Faces context.
     * @param state the state.
     */
    @Override
    public void restoreState(FacesContext facesContext, Object state) {
        Object[] stateArray = (Object[]) state;
        converterId = (String) stateArray[0];
        forClass = (Class) stateArray[1];
    }

    /**
     * Is the converter transient.
     * 
     * @return false
     */
    @Override
    public boolean isTransient() {
        return false;
    }

    /**
     * Set the converter to transient.
     * 
     * <p>
     *  We ignore the call as our proxy is always non-transient.
     * </p>
     * 
     * @param transientValue 
     */
    @Override
    public void setTransient(boolean transientValue) {
    }

    /**
     * Get the delegate.
     * 
     * @param facesContext the Faces context.
     * @return the delegate.
     */
    private Converter getDelegate(FacesContext facesContext) {
        if (delegate == null) {
            if (!converterId.equals("")) {
                delegate = facesContext.getApplication().createConverter(converterId);
            } else {
                delegate = facesContext.getApplication().createConverter(forClass);
            }
        }
        return delegate;
    }
}
