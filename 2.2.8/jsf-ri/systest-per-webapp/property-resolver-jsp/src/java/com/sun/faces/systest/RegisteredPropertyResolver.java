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

package com.sun.faces.systest;

import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;


public class RegisteredPropertyResolver extends PropertyResolver { 

     private PropertyResolver delegate;

    public RegisteredPropertyResolver(PropertyResolver delegate) {
        this.delegate = delegate;
    }

    public Object getValue(Object object, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getValue(Object,Object) called");
        return delegate.getValue(object, object1);
    }

    public Object getValue(Object object, int i) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getValue(Object,int) called");
        return delegate.getValue(object, i);
    }

    public void setValue(Object object, Object object1, Object object2) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".setValue(Object,Object) called");
        delegate.setValue(object, object1, object2);
    }

    public void setValue(Object object, int i, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".setValue(Object,int) called");
        delegate.setValue(object, i, object1);
    }

    public boolean isReadOnly(Object object, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".isReadOnly(Object,Object) called");
        return delegate.isReadOnly(object, object1);
    }

    public boolean isReadOnly(Object object, int i) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".isReadOnly(Object,int) called");
        return delegate.isReadOnly(object, i);
    }

    public Class getType(Object object, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getType(Object,Object) called");
        return delegate.getType(object, object1);
    }

    public Class getType(Object object, int i) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getValue(Object,int) called");
        return delegate.getType(object, i);
    }
}
