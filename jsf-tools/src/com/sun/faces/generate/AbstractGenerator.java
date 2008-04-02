/*
 * $Id: AbstractGenerator.java,v 1.7 2004/10/29 00:56:39 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;


import com.sun.faces.config.DigesterFactory;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.rules.FacesConfigRuleSet;
import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * <p>Abstract base class for Java and TLD generators.</p>
 *
 * <p>The methods in this class presume the following command line option
 * names and corresponding values:</p>
 * <ul>
 * </ul>
 */

public abstract class AbstractGenerator {


    // -------------------------------------------------------- Static Variables


    // The set of default values for primitives, keyed by the primitive type
    protected static Map defaults = new HashMap();
    static {
        defaults.put("boolean", "false");
        defaults.put("byte", "Byte.MIN_VALUE");
        defaults.put("char", "Character.MIN_VALUE");
        defaults.put("double", "Double.MIN_VALUE");
        defaults.put("float", "Float.MIN_VALUE");
        defaults.put("int", "Integer.MIN_VALUE");
        defaults.put("long", "Long.MIN_VALUE");
        defaults.put("short", "Short.MIN_VALUE");
    }


    // The set of reserved keywords in the Java language
    protected static Set keywords = new HashSet();
    static {
        keywords.add("abstract");
        keywords.add("boolean");
        keywords.add("break");
        keywords.add("byte");
        keywords.add("case");
        keywords.add("cast");
        keywords.add("catch");
        keywords.add("char");
        keywords.add("class");
        keywords.add("const");
        keywords.add("continue");
        keywords.add("default");
        keywords.add("do");
        keywords.add("double");
        keywords.add("else");
        keywords.add("enum");
        keywords.add("extends");
        keywords.add("final");
        keywords.add("finally");
        keywords.add("float");
        keywords.add("for");
        keywords.add("future");
        keywords.add("generic");
        keywords.add("goto");
        keywords.add("if");
        keywords.add("implements");
        keywords.add("import");
        keywords.add("inner");
        keywords.add("instanceof");
        keywords.add("int");
        keywords.add("interface");
        keywords.add("long");
        keywords.add("native");
        keywords.add("new");
        keywords.add("null");
        keywords.add("operator");
        keywords.add("outer");
        keywords.add("package");
        keywords.add("private");
        keywords.add("protected");
        keywords.add("public");
        keywords.add("rest");
        keywords.add("return");
        keywords.add("short");
        keywords.add("static");
        keywords.add("strictfp");
        keywords.add("super");
        keywords.add("switch");
        keywords.add("synchronized");
        keywords.add("this");
        keywords.add("throw");
        keywords.add("throws");
        keywords.add("transient");
        keywords.add("try");
        keywords.add("var");
        keywords.add("void");
        keywords.add("volatile");
        keywords.add("while");
    }


    // The set of unwrapper methods for primitives, keyed by the primitive type
    protected static Map unwrappers = new HashMap();
    static {
        unwrappers.put("boolean", "booleanValue");
        unwrappers.put("byte", "byteValue");
        unwrappers.put("char", "charValue");
        unwrappers.put("double", "doubleValue");
        unwrappers.put("float", "floatValue");
        unwrappers.put("int", "intValue");
        unwrappers.put("long", "longValue");
        unwrappers.put("short", "shortValue");
    }


    // The set of wrapper classes for primitives, keyed by the primitive type
    protected static Map wrappers = new HashMap();
    static {
        wrappers.put("boolean", "Boolean");
        wrappers.put("byte", "Byte");
        wrappers.put("char", "Character");
        wrappers.put("double", "Double");
        wrappers.put("float", "Float");
        wrappers.put("int", "Integer");
        wrappers.put("long", "Long");
        wrappers.put("short", "Short");
    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return the capitalized version of the specified property name.</p>
     *
     * @param name Uncapitalized property name
     */
    protected static String capitalize(String name) {

        return (Character.toUpperCase(name.charAt(0)) + name.substring(1));

    }


    /**
     * <p>Render the specified description text to the specified writer,
     * prefixing each line by 'indent' spaces, an asterisk ("*"), and another
     * space.  This rendering is appropriate for the creation of
     * JavaDoc comments on classes, variables, and methods.</p>
     *
     * @param desc Description text to be rendered
     * @param writer Writer to which output should be sent
     * @param indent Number of leading space for each line
     */
    protected static void description(String desc, Writer writer, int indent)
        throws Exception {

        for (int i = 0; i < indent; i++) {
            writer.write(" ");
        }
        writer.write("* ");
        int n = desc.length();
        for (int i = 0; i < n; i++) {
            char ch = desc.charAt(i);
            if (ch == '\r') {
                continue;
            }
            writer.write(ch);
            if (ch == '\n') {
                for (int j = 0; j < indent; j++) {
                    writer.write(" ");
                }
                writer.write("* ");
            }
        }
        writer.write("\n");

    }


    /**
     * <p>Configure and return a <code>Digester</code> instance suitable for
     * use in the environment specified by our parameter flags.</p>
     *
     * @param design Include rules suitable for design time use in a tool
     * @param generate Include rules suitable for generating component,
     *  renderer, and tag classes
     * @param runtime Include rules suitable for runtime execution
     */
    protected static Digester digester(boolean design,
                                       boolean generate, boolean runtime) {

        Digester digester = DigesterFactory.newInstance(true).createDigester();

        // Configure parsing rules
        digester.addRuleSet(new FacesConfigRuleSet(design, generate, runtime));

        // Configure preregistered entities

        return (digester);
    }


    /**
     * <p>Return a mangled version of the specified name if it conflicts with
     * a Java keyword; otherwise, return the specified name unchanged.</p>
     *
     * @param name Name to be potentially mangled
     */
    protected static String mangle(String name) {

        if (keywords.contains(name)) {
            return ('_' + name);
        } else {
            return (name);
        }

    }


    /**
     * <p>Parse the command line options into a <code>Map</code>.</p>
     *
     * @param args Command line arguments passed to this program
     *
     * @exception IllegalArgumentException if an option flag does not start
     *  with a '-' or is missing a corresponding value
     */
    protected static Map options(String args[]) {

        Map options = new HashMap();
        int i = 0;
        while (i < args.length) {
            if (!args[i].startsWith("-")) {
                throw new IllegalArgumentException
                    ("Invalid option name '" + args[i] + '\'');
            } else if ((i + 1) >= args.length) {
                throw new IllegalArgumentException
                    ("Missing value for option '" + args[i] + '\'');
            }
            options.put(args[i], args[i+1]);
            i += 2;
        }
        return (options);

    }


    /**
     * <p>Parse the specified configuration file, and return the root of the
     * resulting tree of configuration beans.</p>
     *
     * @param digester Digester instance to use for parsing
     * @param config Pathname of the configuration file to be parsed
     *
     * @exception IOException an input/output error occurred while parsing
     * @exception SAXException an XML processing error occurred while parsing
     */
    protected static FacesConfigBean parse(Digester digester, String config)
        throws IOException, SAXException {

        File file = null;
        FacesConfigBean fcb = null;
        InputSource source = null;
        InputStream stream = null;
        try {
            file = new File(config);
            stream = new BufferedInputStream(new FileInputStream(file));
            source = new InputSource(file.toURL().toString());
            source.setByteStream(stream);
            fcb = (FacesConfigBean) digester.parse(source);
            stream.close();
            stream = null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    ;
                }
                stream = null;
            }
        }
        return (fcb);

    }


    /**
     * <p>Return <code>true</code> if the specified type is a primitive.</p>
     *
     * @param type Type to be tested
     */
    protected static boolean primitive(String type) {

        return (wrappers.containsKey(type));

    }


    /**
     * <p>Return the short class name from the specified (potentially fully
     * qualified) class name.  If the specified name has no periods, the
     * input value is returned unchanged.</p>
     *
     * @param className Class name that is optionally fully qualified
     */
    protected static String shortName(String className) {

        int index = className.lastIndexOf('.');
        if (index >= 0) {
            return (className.substring(index + 1));
        } else {
            return (className);
        }

    }


}
