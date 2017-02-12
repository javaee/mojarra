/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
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
 */
package com.sun.faces.util.copier;

import static com.sun.faces.util.ReflectionUtils.instance;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.faces.context.FacesContext;

public class CopierUtils {
    
    private static final String ERROR_COPIER_NAME = "The copier name should be a Java valid simple/qualified name.";
    private static final String COPIER_PREFIX = "com.sun.faces.util.copier.";
    
    private final static Set<String> keywords;

    static {
        Set<String> s = new HashSet<String>();
        String[] kws = {
            "abstract", "continue", "for", "new", "switch",
            "assert", "default", "if", "package", "synchronized",
            "boolean", "do", "goto", "private", "this",
            "break", "double", "implements", "protected", "throw",
            "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void",
            "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while",
            // literals
            "null", "true", "false"
        };
        for (String kw : kws) {
            s.add(kw);
        }
        keywords = Collections.unmodifiableSet(s);
    }    
    
    public static Copier getCopier(FacesContext context, String copierType) {
        Copier copier = null;

        if (!isEmpty(copierType)) {

            // TODO: or should validate only against {"MultiStrategyCopier", "SerializationCopier",
            // "NewInstanceCopier", "CopyCtorCopier", "CloneCopier"} strings / enum
            
            if (isCopierTypeSimpleName(copierType)) {                
                copierType = COPIER_PREFIX.concat(copierType);
            } else if (!isName(copierType)) {
                throw new IllegalArgumentException(ERROR_COPIER_NAME);
            }

            Object expressionResult = evaluateExpressionGet(context, copierType);

            if (expressionResult instanceof Copier) {
                copier = (Copier) expressionResult;
            } else if (expressionResult instanceof String) {
                copier = instance((String) expressionResult);
            }
        }

        if (copier == null) {
            copier = new MultiStrategyCopier();
        }
        
        return copier;
    }

    @SuppressWarnings("unchecked")
    private static <T> T evaluateExpressionGet(FacesContext context, String expression) {
        if (expression == null) {
            return null;
        }

        return (T) context.getApplication().evaluateExpressionGet(context, expression, Object.class);
    }

    private static boolean isCopierTypeSimpleName(String copierType) {
        return (isIdentifier(copierType) && !(isKeyword(copierType)));
    }

    // maybe the following four methods should be moved in com.sun.faces.util   
    private static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private static boolean isName(CharSequence name) {
        String id = name.toString();

        for (String s : id.split("\\.", -1)) {
            if (!isIdentifier(s) || isKeyword(s)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isIdentifier(CharSequence name) {
        String id = name.toString();

        if (id.length() == 0) {
            return false;
        }
        int cp = id.codePointAt(0);
        if (!Character.isJavaIdentifierStart(cp)) {
            return false;
        }
        for (int i = Character.charCount(cp);
                i < id.length();
                i += Character.charCount(cp)) {
            cp = id.codePointAt(i);
            if (!Character.isJavaIdentifierPart(cp)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isKeyword(CharSequence s) {
        String keywordOrLiteral = s.toString();
        return keywords.contains(keywordOrLiteral);
    }

}
