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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.el;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import javax.faces.view.facelets.TagAttribute;

/**
 * 
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public final class TagValueExpression extends ValueExpression implements Externalizable {

    private static final long serialVersionUID = 1L;

    private ValueExpression wrapped;
    private String tagAttribute;

    public TagValueExpression() {
        super();
    }

    public TagValueExpression(TagAttribute tagAttribute, ValueExpression wrapped) {
        this.tagAttribute = tagAttribute.toString();
        this.wrapped = wrapped;
    }

    @Override
    public Class<?> getExpectedType() {
        return wrapped.getExpectedType();
    }

    @Override
    public Class<?> getType(ELContext context) {
        try {
            return wrapped.getType(context);
        } catch (PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(tagAttribute + ": " + pnfe.getMessage(), pnfe);
        } catch (ELException e) {
            throw new ELException(tagAttribute + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Object getValue(ELContext context) {
        try {
            return wrapped.getValue(context);
        } catch (PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(tagAttribute + ": " + pnfe.getMessage(), pnfe);
        } catch (ELException e) {
            throw new ELException(this.tagAttribute + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isReadOnly(ELContext context) {
        try {
            return wrapped.isReadOnly(context);
        } catch (PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(this.tagAttribute + ": " + pnfe.getMessage(), pnfe);
        } catch (ELException e) {
            throw new ELException(this.tagAttribute + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void setValue(ELContext context, Object value) {
        try {
            wrapped.setValue(context, value);
        } catch (PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(tagAttribute + ": " + pnfe.getMessage(), pnfe);
        } catch (PropertyNotWritableException pnwe) {
            throw new PropertyNotWritableException(tagAttribute + ": " + pnwe.getMessage(), pnwe);
        } catch (ELException e) {
            throw new ELException(tagAttribute + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagValueExpression that = (TagValueExpression) o;

        if (tagAttribute != null ? !tagAttribute.equals(that.tagAttribute) : that.tagAttribute != null) {
            return false;
        }
        if (wrapped != null ? !wrapped.equals(that.wrapped) : that.wrapped != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = wrapped != null ? wrapped.hashCode() : 0;
        result = 31 * result + (tagAttribute != null ? tagAttribute.hashCode() : 0);
        return result;
    }

    @Override
    public String getExpressionString() {
        return wrapped.getExpressionString();
    }

    @Override
    public boolean isLiteralText() {
        return wrapped.isLiteralText();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        wrapped = (ValueExpression) in.readObject();
        tagAttribute = in.readUTF();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(wrapped);
        out.writeUTF(tagAttribute);
    }

    public ValueExpression getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return tagAttribute;
    }
}
