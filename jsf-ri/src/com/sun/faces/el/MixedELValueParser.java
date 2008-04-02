/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.util.Util;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MixedELValueParser {

    private boolean inExpr = false;
    private boolean inLiteral = false;
    private char literalDelimiter = '"';
    private StringBuffer sb = new StringBuffer();
    private int currentPosition = 0;
    private String exprString;


    /**
     * Creates an instance of the parser. Note that it's not
     * thread safe but the same instance can be used to parse
     * multiple expression strings.
     */
    public MixedELValueParser() {
    }


    /**
     * This constructor is just for testing the parser logic without
     * access to a FacesContext. Use the empty constructor and the
     * parse() method for regular parsing.
     */
    public MixedELValueParser(String exprString) {
        this.exprString = exprString;
    }


    /**
     * This method is just for testing the parser.
     */
    public static void main(String[] args) throws Exception {
        List l = new ArrayList();
        l.add("#{foo}");
        l.add("foo#{foo}bar");
        l.add("foo\\#{foo}");
        l.add("foo#{foo = '#{foo}'}");
        l.add("foo#{foo = '\\'#{foo}'}");
        l.add("foo#{foo = \"\\\"#{foo}\"}");
        l.add("foo's#{bar}");
        l.add("foo#{bar");
        l.add("foo#{bar}#{baz}");
        l.add("foo#{bar}{baz}");
        l.add("foo#{'}'}");
        l.add("foo#{'}'");
        Iterator i = l.iterator();
        while (i.hasNext()) {
            String test = (String) i.next();
            System.out.println("Parsing " + test);

            MixedELValueParser p = new MixedELValueParser(test);
            boolean done = false;
            System.out.println("Result:");
            while (!done) {
                Token t = null;
                try {
                    t = p.getNextToken();
                } catch (ElException e) {
                    System.out.println("  Exception: " + e);
                    break;
                }
                if (t == null) {
                    done = true;
                } else if (t instanceof ExprToken) {
                    System.out.println("  Expression: " + t.getValue());
                } else {
                    System.out.println("  Literal: " + t.getValue());
                }
            }
        }
    }


    /**
     * <p>Parses a string that may contain JSF EL expressions
     * delimited by "#{" and "}" character sequences and returns
     * a List of String instances for non-expressions and
     * an Expression instance for each EL expression in the
     * argument value.</p>
     * <p/>
     * <p>The start delimiter can be quoted as "\#{", and within
     * an expression, single and double quotes in a literal can
     * be quoted as "\'" or "\".</p>
     *
     * @param exprString A String with literal text and one or
     *                   more JSF EL expression
     * @return A List with String and Expression instances
     * @throws ElException Thrown if parsing errors were found
     */
    public List parse(FacesContext context, String exprString)
        throws ElException {

        // Reset the state
        this.exprString = exprString;
        inExpr = false;
        inLiteral = false;
        sb = new StringBuffer();
        currentPosition = 0;

        boolean done = false;
        List l = new ArrayList();
        while (!done) {
            Token t = getNextToken();
            if (t == null) {
                done = true;
            } else if (t instanceof ExprToken) {
                l.add(toExpression(context, t.getValue()));
            } else {
                l.add(t.getValue());
            }
        }
        return l;
    }


    private Token getNextToken() throws ElException {
        Token t = null;
        int i = currentPosition;

        for (; i < exprString.length(); i++) {
            char c = exprString.charAt(i);
            if (!inLiteral && c == '#' &&
                (i == 0 || exprString.charAt(i - 1) != '\\') &&
                (i + 1 < exprString.length() &&
                exprString.charAt(i + 1) == '{')) {
                inExpr = true;
                i++;
                if (sb.length() > 0) {
                    t = new Token(sb.toString());
                    sb = new StringBuffer();
                    currentPosition = i + 1;
                    return t;
                }
            } else if (!inLiteral && inExpr && c == '}') {
                inExpr = false;
                t = new ExprToken(sb.toString());
                sb = new StringBuffer();
                currentPosition = i + 1;
                return t;
            } else if (inExpr && (c == '\'' || c == '"') &&
                i > 0 && exprString.charAt(i - 1) != '\\') {
                if (inLiteral && c == literalDelimiter) {
                    inLiteral = false;
                } else if (!inLiteral) {
                    inLiteral = true;
                    literalDelimiter = c;
                }
                sb.append(c);
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            if (inExpr) {
                throw new ElException("Missing closing delimiter: " +
                                      exprString);
            }
            t = new Token(sb.toString());
            sb = new StringBuffer();
            currentPosition = i + 1;
            return t;
        }
        return null;
    }


    private static Expression toExpression(FacesContext context,
                                           String exprString)
        throws ElException {
        Application application = context.getApplication();
        ExpressionInfo info = new ExpressionInfo();
        info.setExpressionString(exprString);
        info.setFacesContext(context);
        info.setVariableResolver(application.getVariableResolver());
        info.setPropertyResolver(application.getPropertyResolver());
        return Util.getExpressionEvaluator().parseExpression(info);
    }


    private static class Token {

        String value;


        public Token(String value) {
            this.value = value;
        }


        public String getValue() {
            return value;
        }
    }

    private static class ExprToken extends Token {

        public ExprToken(String value) {
            super(value);
        }
    }
}
