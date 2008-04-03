/*
 * $Id: ManagedMapBeanBuilder.java,v 1.1 2007/04/22 21:41:05 rlubke Exp $
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
import java.util.Map;

/**
 * <p>This builder builds beans that are defined as <code>Map</code>
 * instances.</p>
 */
public class ManagedMapBeanBuilder extends BeanBuilder {

    private Map<Expression,Expression> mapEntries;

    // ------------------------------------------------------------ Constructors


    public ManagedMapBeanBuilder(ManagedBeanInfo beanInfo) {

        super(beanInfo);
        
    }


    // ------------------------------------------------ Methods from BeanBuilder


    @Override
    void bake() {

        if (!isBaked()) {
            super.bake();
            ManagedBeanInfo.MapEntry entry = beanInfo.getMapEntry();
            mapEntries = getBakedMap(entry.getKeyClass(),
                                     entry.getValueClass(),
                                     entry.getEntries().entrySet());
            baked();
        }

    }


    protected void buildBean(Object bean, FacesContext context) {

        initMap(mapEntries, (Map) bean, context);
        
    }
}
