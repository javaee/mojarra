/*
 * $Id: ManagedBeanFactoryWrapper.java,v 1.8 2007/07/31 18:17:55 rlubke Exp $
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

package com.sun.faces.spi;

import com.sun.faces.config.beans.ManagedBeanBean;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 *
 * <p>This is intended to be the main access point to the pluggable
 * {@link ManagedBeanFactory} mechanism.  Subclasses must provide a
 * public constructor that takes a single
 * <code>ManagedBeanFactory</code> argument and stores it as an ivar,
 * returning it from the {@link #getWrapped} method.</p>
 * 
 * @author edburns, rlubke
 * @deprecated
 */
public abstract class ManagedBeanFactoryWrapper extends ManagedBeanFactory {
    
    public abstract ManagedBeanFactory getWrapped();

    /**
     * @see ManagedBeanFactory#getScope
     */
    
    public Scope getScope() {
        return getWrapped().getScope();
    }
    
    /**
     * @see ManagedBeanFactory#newInstance
     */
    
    public Object newInstance(FacesContext context) {
        return getWrapped().newInstance(context);
    }
    
    
    /**
     * @see ManagedBeanFactory#setManagedBeanBean
     */
    
    public void setManagedBeanBean(ManagedBeanBean bean) {
        getWrapped().setManagedBeanBean(bean);
    }
    
    /**
     * @see ManagedBeanFactory#getManagedBeanBean
     */
    
    public ManagedBeanBean getManagedBeanBean() {
        return getWrapped().getManagedBeanBean();
    }
    
    /**
     * @see ManagedBeanFactory#setManagedBeanFactoryMap(java.util.Map<java.lang.String,com.sun.faces.spi.ManagedBeanFactory>) 
     */
    
    public void setManagedBeanFactoryMap(Map<String, ManagedBeanFactory> others) {
        getWrapped().setManagedBeanFactoryMap(others);
    }
    
    /**
     * @see ManagedBeanFactory#getManagedBeanFactoryMap() 
     */
    
    public Map<String, ManagedBeanFactory> getManagedBeanFactoryMap() {
        return getWrapped().getManagedBeanFactoryMap();
    }


    /**
     * @see com.sun.faces.spi.ManagedBeanFactory#isInjectable() 
     */
    public boolean isInjectable() {
        return getWrapped().isInjectable();
    }
}
