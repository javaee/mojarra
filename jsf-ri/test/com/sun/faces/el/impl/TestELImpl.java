/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: TestELImpl.java,v 1.6 2004/02/06 18:56:49 rlubke Exp $
 */

// TestELImpl

package com.sun.faces.el.impl;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.el.impl.beans.Factory;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

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
    private static final String[] EMPTY_IGNORE_LIST = {};

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
        //Writer writer = new OutputStreamWriter(System.out);
        ExpressionEvaluatorImpl e =
            (ExpressionEvaluatorImpl) Util.getExpressionEvaluator();
        for (String line = reader.readLine(); line != null; line =
            reader.readLine()) {
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
                    writer.write(
                        "Causes an error: " + elex.getMessage() + "\n");
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
        FacesContext fc = FacesContext.getCurrentInstance();
        createTestContext(fc);
        LineNumberReader reader = getReaderForFile(EVAL_INPUT_FILE);
        Writer writer = getFacesContext().getResponseWriter();
        for (String line = reader.readLine(); line != null; line =
            reader.readLine()) {
            if (line.startsWith("*") ||
                "".equals(line.trim())) {
                writer.write(line + "\n");
            } else {
                String typeStr = reader.readLine();
                writer.write("Expression: " + line + "\n");

                try {
                    Class cl = parseClassName(typeStr);
                    writer.write("ExpectedType: " + cl + "\n");
                    Object val = null;
                    Class type = null;
                    if (Util.isVBExpression(line)) {
                        ValueBinding vb = Util.getValueBinding(line);
                        val = vb.getValue(fc);
                        type = val == null ? null : val.getClass();
                    } else {
                        val = line;
                        type = String.class;
                    }

                    writer.write("Evaluates to: " + val + "\n");
                    if (val != null) {
                        writer.write("With type: " + type + "\n");
                    }
                    writer.write("\n");
                } catch (Exception exc) {
                    writer.write("Causes an error: " + exc);
                    //exc.printStackTrace(new PrintWriter(writer));
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
                getFacesContext().getExternalContext().getResourceAsStream(
                    file)));
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
        } else if ("null".equals(c)) {
            return null;
        } else {
            return Class.forName(pClassName);
        }
    }


    static void createTestContext(FacesContext context) {
        UIViewRoot root = new UIViewRoot();
        UIInput input = new UIInput();
        input.setValue("inputValue");
        root.getChildren().add(input);
        context.setViewRoot(root);
        ExternalContext ec = context.getExternalContext();
        Map requestMap = ec.getRequestMap();
        Map sessionMap = ec.getSessionMap();
        Map applicationMap = ec.getApplicationMap();

        // Create some basic values for lookups
        requestMap.put("val1b", "request-scoped1");
        sessionMap.put("val1c", "session-scoped1");
        applicationMap.put("val1d", "app-scoped1");

        // Create a bean
        {
            Bean1 b1 = new Bean1();
            b1.setBoolean1(true);
            b1.setByte1((byte) 12);
            b1.setShort1((short) 98);
            b1.setChar1('b');
            b1.setInt1(4);
            b1.setLong1(98);
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
            requestMap.put("bean1a", b1);

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
            requestMap.put("pbean1", Factory.createBean1());
            requestMap.put("pbean2", Factory.createBean2());
            requestMap.put("pbean3", Factory.createBean3());
            requestMap.put("pbean4", Factory.createBean4());
            requestMap.put("pbean5", Factory.createBean5());
            requestMap.put("pbean6", Factory.createBean6());
            requestMap.put("pbean7", Factory.createBean7());
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
            requestMap.put("emptyTests", m);
        }
    }
}
