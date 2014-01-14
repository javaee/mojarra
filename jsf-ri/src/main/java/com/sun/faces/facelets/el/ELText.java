/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.el;

import com.sun.faces.el.ELUtils;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.sun.faces.util.HtmlUtils;
import com.sun.faces.util.MessageUtils;
import javax.faces.context.FacesContext;
import javax.faces.view.Location;

/**
 * Handles parsing EL Strings in accordance with the EL-API Specification. The
 * parser accepts either <code>${..}</code> or <code>#{..}</code>.
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public class ELText {

    private static final class LiteralValueExpression extends ValueExpression {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        private final String text;

        public LiteralValueExpression(String text) {
            this.text = text;
        }

        public boolean isLiteralText() {
            return false;
        }

        public int hashCode() {
            return 0;
        }

        public String getExpressionString() {
            return this.text;
        }

        public boolean equals(Object obj) {
            return false;
        }

        public void setValue(ELContext context, Object value) {
        }

        public boolean isReadOnly(ELContext context) {
            return false;
        }

        public Object getValue(ELContext context) {
            return null;
        }

        public Class getType(ELContext context) {
            return null;
        }

        public Class getExpectedType() {
            return null;
        }

    }

    private static final class ELTextComposite extends ELText {
        private final ELText[] txt;

        public ELTextComposite(ELText[] txt) {
            super(null);
            this.txt = txt;
        }

        public void write(Writer out, ELContext ctx) throws ELException,
                IOException {
            for (int i = 0; i < this.txt.length; i++) {
                this.txt[i].write(out, ctx);
            }
        }

        public void writeText(ResponseWriter out, ELContext ctx)
                throws ELException, IOException {
            for (int i = 0; i < this.txt.length; i++) {
                this.txt[i].writeText(out, ctx);
            }
        }

        public String toString(ELContext ctx) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < this.txt.length; i++) {
                sb.append(this.txt[i].toString(ctx));
            }
            return sb.toString();
        }

        /*
         * public String toString(ELContext ctx) { StringBuffer sb = new
         * StringBuffer(); for (int i = 0; i < this.txt.length; i++) {
         * sb.append(this.txt[i].toString(ctx)); } return sb.toString(); }
         */

        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < this.txt.length; i++) {
                sb.append(this.txt[i].toString());
            }
            return sb.toString();
        }

        public boolean isLiteral() {
            return false;
        }

        public ELText apply(ExpressionFactory factory, ELContext ctx) {
            int len = this.txt.length;
            ELText[] nt = new ELText[len];
            for (int i = 0; i < len; i++) {
                nt[i] = this.txt[i].apply(factory, ctx);
            }
            return new ELTextComposite(nt);
        }
    }

    private static final class ELTextVariable extends ELText {
        private final ValueExpression ve;

        public ELTextVariable(ValueExpression ve) {
            super(ve.getExpressionString());
            this.ve = ve;
        }

        public boolean isLiteral() {
            return false;
        }

        public ELText apply(ExpressionFactory factory, ELContext ctx) {
            ELText result = null;
            if (this.ve instanceof ContextualCompositeValueExpression) {
                result = new ELTextVariable(ve);
            } else {
                result = new ELTextVariable(factory.createValueExpression(ctx,
                    this.ve.getExpressionString(), String.class));
            }
            
            return result;
        }

        public void write(Writer out, ELContext ctx) throws ELException,
                IOException {
            Object v = this.ve.getValue(ctx);
            if (v != null) {
                char[] buffer = new char[1028];
                HtmlUtils.writeTextForXML(out, v.toString(), buffer);
            }
        }

        public String toString(ELContext ctx) throws ELException {
            Object v = this.ve.getValue(ctx);
            if (v != null) {
                return v.toString();
            }

            return null;
        }

        public void writeText(ResponseWriter out, ELContext ctx)
                throws ELException, IOException {
            Object v = this.ve.getValue(ctx);
            if (v != null) {
                out.writeText(v.toString(), null);
            }
        }
    }

    protected final String literal;

    public ELText(String literal) {
        this.literal = literal;
    }

    /**
     * If it's literal text
     * 
     * @return true if the String is literal (doesn't contain <code>#{..}</code>
     *         or <code>${..}</code>)
     */
    public boolean isLiteral() {
        return true;
    }

    /**
     * Return an instance of <code>this</code> that is applicable given the
     * ELContext and ExpressionFactory state.
     * 
     * @param factory
     *            the ExpressionFactory to use
     * @param ctx
     *            the ELContext to use
     * @return an ELText instance
     */
    public ELText apply(ExpressionFactory factory, ELContext ctx) {
        return this;
    }

    /**
     * Allow this instance to write to the passed Writer, given the ELContext
     * state
     * 
     * @param out
     *            Writer to write to
     * @param ctx
     *            current ELContext state
     * @throws ELException
     * @throws IOException
     */
    public void write(Writer out, ELContext ctx) throws ELException,
            IOException {
        out.write(this.literal);
    }

    public void writeText(ResponseWriter out, ELContext ctx)
            throws ELException, IOException {
        out.writeText(this.literal, null);
    }

    /**
     * Evaluates the ELText to a String
     * 
     * @param ctx
     *            current ELContext state
     * @throws ELException
     * @return the evaluated String
     */
    public String toString(ELContext ctx) throws ELException {
        return this.literal;
    }

    public String toString() {
        return this.literal;
    }

    /**
     * Parses the passed string to determine if it's literal or not
     * 
     * @param in
     *            input String
     * @return true if the String is literal (doesn't contain <code>#{..}</code>
     *         or <code>${..}</code>)
     */
    public static boolean isLiteral(String in) {
        ELText txt = parse(in);
        return txt == null || txt.isLiteral();
    }

    /**
     * Factory method for creating an unvalidated ELText instance. NOTE: All
     * expressions in the passed String are treated as
     * {@link com.sun.faces.facelets.el.ELText.LiteralValueExpression}, with one
     * exception: composite component expressions.  These are treated as
     * ContextualCompositeValueExpressions.
     * 
     * @param in
     *            String to parse
     * @return ELText instance that knows if the String was literal or not
     * @throws javax.el.ELException
     */
    public static ELText parse(String in) throws ELException {
        return parse(null, null, in);
    }
    
    public static ELText parse(String in, String alias) throws ELException {
        return parse(null, null, in, alias);
    }
    
    public static ELText parse(ExpressionFactory fact, ELContext ctx, String in)
            throws ELException {
        return parse(null, null, in, null);
    }    

    /**
     * Factory method for creating a validated ELText instance. When an
     * Expression is hit, it will use the ExpressionFactory to create a
     * ValueExpression instance, resolving any functions at that time. <p/>
     * Variables and properties will not be evaluated.
     * 
     * @param fact
     *            ExpressionFactory to use
     * @param ctx
     *            ELContext to validate against
     * @param in
     *            String to parse
     * @return ELText that can be re-applied later
     * @throws javax.el.ELException
     */
    public static ELText parse(ExpressionFactory fact, ELContext ctx, String in,
            String alias)
            throws ELException {
        char[] ca = in.toCharArray();
        int i = 0;
        char c = 0;
        int len = ca.length;
        int end = len - 1;
        boolean esc = false;
        int vlen = 0;

        StringBuffer buff = new StringBuffer(128);
        List text = new ArrayList();
        ELText t = null;
        ValueExpression ve = null;

        while (i < len) {
            c = ca[i];
            if ('\\' == c) {
                esc = !esc;
                if (esc && i < end && (ca[i + 1] == '$' || ca[i + 1] == '#')) {
                    i++;
                    continue;
                }
            } else if (!esc && ('$' == c || '#' == c)) {
                if (i < end) {
                    if ('{' == ca[i + 1]) {
                        if (buff.length() > 0) {
                            text.add(new ELText(buff.toString()));
                            buff.setLength(0);
                        }
                        vlen = findVarLength(ca, i);
                        if (ctx != null && fact != null) {
                            ve = fact.createValueExpression(ctx, new String(ca,
                                    i, vlen), String.class);
                            t = new ELTextVariable(ve);
                        } else {
                            String expr = new String(ca, i, vlen);
                            if (null != alias && ELUtils.isCompositeComponentExpr(expr)) {
                                if (ELUtils.isCompositeComponentLookupWithArgs(expr)) {
                                    String message =
                                            MessageUtils.getExceptionMessageString(MessageUtils.ARGUMENTS_NOT_LEGAL_CC_ATTRS_EXPR);
                                    throw new ELException(message);
                                }    
                                FacesContext context = FacesContext.getCurrentInstance();
                                ELContext elContext = context.getELContext();
                                ValueExpression delegate = 
                                        context.getApplication().getExpressionFactory().
                                        createValueExpression(elContext, expr, Object.class);
                                Location location = new Location(alias, -1, -1);                                
                                ve = new ContextualCompositeValueExpression(location,
                                                                            delegate);
                                
                            } else {
                                ve = new LiteralValueExpression(expr);
                            }
                            t = new ELTextVariable(ve);
                        }
                        text.add(t);
                        i += vlen;
                        continue;
                    }
                }
            }
            esc = false;
            buff.append(c);
            i++;
        }

        if (buff.length() > 0) {
            text.add(new ELText(buff.toString()));
            buff.setLength(0);
        }

        if (text.isEmpty()) {
            return new ELText("");
        } else if (text.size() == 1) {
            return (ELText) text.get(0);
        } else {
            ELText[] ta = (ELText[]) text.toArray(new ELText[text.size()]);
            return new ELTextComposite(ta);
        }
    }

    private static int findVarLength(char[] ca, int s) throws ELException {
        int i = s;
        int len = ca.length;
        char c = 0;
        int str = 0;
        int nested = 0;
        boolean insideString = false;
        while (i < len) {
            c = ca[i];
            if ('\\' == c && i<len-1) {
                i++;
            } else if ('\'' == c || '"' == c) {
                if (str == c) {
                    insideString = false;
                    str = 0;
                } else {
                    insideString = true;
                    str = c;
                }
            } else if ('{' == c && !insideString) {
                nested++;
            } else if (str == 0 && ('}' == c)) {
                if (nested > 1) {
                    nested--;
                } else {
                    return i - s + 1;
                }
            } else if ('}' == c && !insideString) {
                nested--;
            }
            i++;
        }
        throw new ELException("EL Expression Unbalanced: ... "
                + new String(ca, s, i - s));
    }

}
