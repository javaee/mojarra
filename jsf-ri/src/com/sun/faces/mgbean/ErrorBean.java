/*
 * $Id: ErrorBean.java,v 1.1 2007/04/22 21:41:05 rlubke Exp $
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
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.mgbean;

import javax.faces.context.FacesContext;
import java.util.List;

/**
 * <p>This doesn't really do anything, aside from being a place holder
 * if the managed bean is configured incorrectly.</p>
 */
public class ErrorBean extends BeanBuilder {


    // ------------------------------------------------------------ Constructors


    public ErrorBean(ManagedBeanInfo beanInfo, String message) {
        super(beanInfo);
        if (message == null || message.length() == 0) {
            throw new IllegalArgumentException();
        }
        queueMessage(message);
    }

    
    public ErrorBean(ManagedBeanInfo beanInfo, List<String> messages) {
        super(beanInfo);
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException();
        }
        queueMessages(messages);
    }


    // ------------------------------------------------ Methods from BeanBuilder

    @Override
    synchronized void bake() {
        // no-op
    }


    protected void buildBean(Object bean, FacesContext context) {
        // no-op
    }
}
