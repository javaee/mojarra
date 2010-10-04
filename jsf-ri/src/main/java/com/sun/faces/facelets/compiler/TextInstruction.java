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

package com.sun.faces.facelets.compiler;


import com.sun.faces.facelets.el.ELText;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

final class TextInstruction implements Instruction {
    private final ELText txt;
    
    private final String alias;

    public TextInstruction(String alias, ELText txt) {
        this.alias = alias;
        this.txt = txt;
    }

    public void write(FacesContext context) throws IOException {
        ResponseWriter out = context.getResponseWriter();
        try {
            ELContext elContext = context.getELContext();
            txt.writeText(out, elContext);
            //out.writeText(txt.toString(elContext), null);
        } catch (ELException e) {
            throw new ELException(this.alias + ": " + e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new ELException(this.alias + ": " + e.getMessage(), e);
        }
    }


    public Instruction apply(ExpressionFactory factory, ELContext ctx) {
        ELText nt = this.txt.apply(factory, ctx);
        if (nt == this.txt) {
            return this;
        }

        return new TextInstruction(alias, nt);
    }

    public boolean isLiteral() {
        return txt.isLiteral();
    }
}
