/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: TestELImpl.java,v 1.1 2003/08/13 18:21:50 rlubke Exp $
 */

// TestELImpl

package com.sun.faces.el.impl;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.beans.Factory;
import com.sun.faces.el.impl.ExpressionEvaluatorImpl;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.JspVariableResolver;

import javax.servlet.jsp.PageContext;
import javax.faces.FacesException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestELImpl extends JspFacesTestCase {

    //
    // Class Constants
    private static final String PARSER_INPUT_FILE = "/parserTests.txt";
    private static final String EVAL_INPUT_FILE = "/evaluationTests.txt";
    private static final String PARSER_GOLDEN_FILE = "parserTestsOutput.txt";
    private static final String EVAL_GOLDEN_FILE = "evaluationTestsOutput.txt";
    private static final String[] EMPTY_IGNORE_LIST = { };

    // Class Variables
    private String responseFile = null;

    //
    // Constructors
    //
    public TestELImpl() {
        super("TestELImpl");
    }

    public TestELImpl(String name) {
        super(name);
    }


    // Methods from FacesTestCase
    public void setUp() {
        super.setUp();
        assertTrue(getFacesContext().getResponseWriter() != null);
    }

    public boolean sendWriterToFile() {
        return true;
    }

    public String[] getLinesToIgnore() {
        return EMPTY_IGNORE_LIST;
    }

    public String getExpectedOutputFilename() {
        return responseFile;
    }
    // ----------- TEST CASES ----------------------

    public void testELParser() throws Exception {
        responseFile = PARSER_GOLDEN_FILE;
        LineNumberReader reader = getReaderForFile(PARSER_INPUT_FILE);
        Writer writer = getFacesContext().getResponseWriter();
        ExpressionEvaluatorImpl e = new ExpressionEvaluatorImpl(RIConstants.JSP_EL_PARSER);
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("#") ||
                "".equals(line.trim())) {
                writer.write(line + "\n");
            } else {
                // For testing non-ASCII values, the string @@non-ascii gets
                // converted internally to '\u1111'
                if ("@@non-ascii".equals(line)) {
                    line = "\u1111";
                }

                writer.write("Attribute value: " + line + "\n");
                try {                    
                    String result = e.parseAndRender(line);
                    writer.write("Parses to: " + result + "\n");
                } catch (ElException elex) {                    
                    writer.write("Causes an error: " + elex.getMessage() + "\n");
                }
            }
        }
        try {
            writer.flush();
        } catch (Throwable t) {
            throw new FacesException("Exception while flushing buffer");
        }
        assertTrue(verifyExpectedOutput());
    }

    public void testELEvaluation() throws Exception {
        responseFile = EVAL_GOLDEN_FILE;
        PageContext context = createTestContext();
        LineNumberReader reader = getReaderForFile(EVAL_INPUT_FILE);
        Writer writer = getFacesContext().getResponseWriter(); 
        ExpressionEvaluatorImpl e = new ExpressionEvaluatorImpl(RIConstants.JSP_EL_PARSER);
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("#") ||
                "".equals(line.trim())) {
                writer.write(line + "\n");
            } else {
                String typeStr = reader.readLine();
                writer.write("Expression: " + line + "\n");

                try {
                    Class cl = parseClassName(typeStr);
                    writer.write("ExpectedType: " + cl + "\n");                    
                    ExpressionInfo exprInfo = new ExpressionInfo();
                    exprInfo.setExpressionString(line);
                    exprInfo.setExpectedType(cl);
                    exprInfo.setVariableResolver(new JspVariableResolver(context));
                    Object val = e.evaluate(exprInfo);
                    writer.write("Evaluates to: " + val + "\n");
                    if (val != null) {
                        writer.write("With type: " + val.getClass().getName() + "\n");
                    }
                    writer.write("\n");
                } catch (ElException exc) {
                    writer.write("Causes an error: " + exc);
                    writer.write("\n");
                } catch (ClassNotFoundException exc) {
                    writer.write("Causes an error: " + exc);
                    writer.write("\n");
                }
            }
        }
        try {
            writer.flush();
        } catch (Throwable t) {
            throw new FacesException("Exception while flushing buffer");
        }
        assertTrue(verifyExpectedOutput());
    }

    // ----------- END TEST CASES -------------------

    //
    // Instance Methods
    //
    private LineNumberReader getReaderForFile(String file) {
        return new LineNumberReader(
            new InputStreamReader(
                getFacesContext().getExternalContext().getResourceAsStream(file)));
    }

    //
    // Class Methods
    //
    static Class parseClassName(String pClassName)
        throws ClassNotFoundException {
        String c = pClassName.trim();
        if ("boolean".equals(c)) {
            return Boolean.TYPE;
        } else if ("byte".equals(c)) {
            return Byte.TYPE;
        } else if ("char".equals(c)) {
            return Character.TYPE;
        } else if ("short".equals(c)) {
            return Short.TYPE;
        } else if ("int".equals(c)) {
            return Integer.TYPE;
        } else if ("long".equals(c)) {
            return Long.TYPE;
        } else if ("float".equals(c)) {
            return Float.TYPE;
        } else if ("double".equals(c)) {
            return Double.TYPE;
        } else {
            return Class.forName(pClassName);
        }
    }

    static PageContext createTestContext() {
        PageContext ret = new PageContextImpl();

        // Create some basic values for lookups
        ret.setAttribute("val1a", "page-scoped1", ret.PAGE_SCOPE);
        ret.setAttribute("val1b", "request-scoped1", ret.REQUEST_SCOPE);
        ret.setAttribute("val1c", "session-scoped1", ret.SESSION_SCOPE);
        ret.setAttribute("val1d", "app-scoped1", ret.APPLICATION_SCOPE);

        // Create a bean
        {
            Bean1 b1 = new Bean1();
            b1.setBoolean1(true);
            b1.setByte1((byte) 12);
            b1.setShort1((short) -124);
            b1.setChar1('b');
            b1.setInt1(4);
            b1.setLong1(222423);
            b1.setFloat1((float) 12.4);
            b1.setDouble1(89.224);
            b1.setString1("hello");
            b1.setStringArray1(new String[]{
                "string1",
                "string2",
                "string3",
                "string4"
            });
            {
                List l = new ArrayList();
                l.add(new Integer(14));
                l.add("another value");
                l.add(b1.getStringArray1());
                b1.setList1(l);
            }
            {
                Map m = new HashMap();
                m.put("key1", "value1");
                m.put(new Integer(14), "value2");
                m.put(new Long(14), "value3");
                m.put("recurse", b1);
                b1.setMap1(m);
            }
            ret.setAttribute("bean1a", b1);

            Bean1 b2 = new Bean1();
            b2.setInt2(new Integer(-224));
            b2.setString2("bean2's string");
            b1.setBean1(b2);

            Bean1 b3 = new Bean1();
            b3.setDouble1(1422.332);
            b3.setString2("bean3's string");
            b2.setBean2(b3);
        }

        // Create the public/private beans
        {
            ret.setAttribute("pbean1", Factory.createBean1());
            ret.setAttribute("pbean2", Factory.createBean2());
            ret.setAttribute("pbean3", Factory.createBean3());
            ret.setAttribute("pbean4", Factory.createBean4());
            ret.setAttribute("pbean5", Factory.createBean5());
            ret.setAttribute("pbean6", Factory.createBean6());
            ret.setAttribute("pbean7", Factory.createBean7());
        }

        // Create the empty tests
        {
            Map m = new HashMap();
            m.put("emptyArray", new Object[0]);
            m.put("nonemptyArray", new Object[]{"abc"});
            m.put("emptyList", new ArrayList());
            {
                List l = new ArrayList();
                l.add("hello");
                m.put("nonemptyList", l);
            }
            m.put("emptyMap", new HashMap());
            {
                Map m2 = new HashMap();
                m2.put("a", "a");
                m.put("nonemptyMap", m2);
            }
            m.put("emptySet", new HashSet());
            {
                Set s = new HashSet();
                s.add("hello");
                m.put("nonemptySet", s);
            }
            ret.setAttribute("emptyTests", m);
        }

        return ret;
    }
}
