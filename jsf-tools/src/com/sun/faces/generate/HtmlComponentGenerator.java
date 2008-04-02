/*
 * $Id: HtmlComponentGenerator.java,v 1.9 2004/10/29 00:56:39 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;


import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.beans.PropertyBean;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>Generate concrete HTML component classes.  Classes will be generated
 * for each <code>&lt;component&gt;</code> element in the specified
 * configuration file whose absolute class name is in package
 * <code>javax.faces.component.html</code>.</p>
 *
 * <p>This application requires the following command line options:</p>
 * <ul>
 * <li><strong>--config</strong> Absolute pathname to an input configuration
 *     file that will be parsed by the <code>parse()</code> method.</li>
 * <li><strong>--copyright</strong> Absolute pathname to a file containing the
 *     copyright material for the top of each Java source file.</li>
 * <li><strong>--dir</strong> Absolute pathname to the directory into which
 *     generated Java source code will be created.</li>
 * <li><strong>--dtd</strong> Pipe delimited list of public identifiers and 
 *     absolute pathnames to files containing the DTDs used to validate the 
 *     input configuration files. PRECONDITION: The list is the sequence:
 *     <public id>|<dtd path>|<public id>|<dtd path>...</li>
 * </ul>
 */

public class HtmlComponentGenerator extends AbstractGenerator {


    // -------------------------------------------------------- Static Variables


    private static final Log log =
        LogFactory.getLog(HtmlComponentGenerator.class);


    // The component configuration bean for the component class to be generated
    private static ComponentBean cb;


    // StringBuffer containing the copyright material
    private static String copyright;


    // The directory into which the source code will be generated
    private static File directory;


    // Base object of the configuration beans
    private static FacesConfigBean fcb;

    // List of relevant properties for the component class to be generated
    private static List properties;

    // The Writer for each component class to be generated
    private static Writer writer;


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Load the copyright text for the top of each Java source file.</p>
     *
     * @param path Pathname of the copyright file
     */
    private static void copyright(String path) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Loading copyright text from '" + path + "'");
        }
        StringBuffer sb = new StringBuffer();
        BufferedReader reader =
            new BufferedReader(new FileReader(path));
        int ch;
        while ((ch = reader.read()) >= 0) {
            sb.append((char) ch);
        }
        reader.close();
        if (log.isDebugEnabled()) {
            log.debug("  Copyright text contains " + sb.length() +
                      " characters");
        }
        copyright = sb.toString();

    }


    /**
     * <p>Create directories as needed to contain the output.  Leave a
     * <code>directory</code> object that points at the top level directory
     * for the concrete component classes.</p>
     *
     * @param path Pathname to the base directory
     */
    private static void directories(String path) throws Exception {

        directory = new File(path);
        directory = new File(path, "javax/faces/component/html");
        if (log.isDebugEnabled()) {
            log.debug("Creating output directory " +
                      directory.getAbsolutePath());
        }
        directory.mkdirs();

    }


    /**
     * <p>Generate the concrete HTML component class based on the current
     * component configuration bean.</p>
     */
    private static void generate() throws Exception {

        if (log.isInfoEnabled()) {
            log.info("Generating concrete HTML component class " +
                     cb.getComponentClass());
        }

        // Initialize all per-class static variables
        properties = new ArrayList();

        // Create and open a Writer for generating our output
        File file = new File(directory,
                             shortName(cb.getComponentClass()) + ".java");
        writer = new BufferedWriter(new FileWriter(file));

        // Generate the various portions of each class
        prefix();
        properties();
        suffix();

        // Flush and close the Writer for this class
        writer.flush();
        writer.close();

    }


    /**
     * <p>Generate the prefix for this component class, down to (and including)
     * the class declaration.</p>
     */
    private static void prefix() throws Exception {

        // Acquire the config bean for our base component
        ComponentBean base = fcb.getComponent(cb.getBaseComponentType());
        if (base == null) {
            throw new IllegalArgumentException("No base component type for '" +
                                               cb.getComponentType() + "'");
        }

        // Generate the copyright information
        writer.write(copyright);

        // Generate the package declaration
        writer.write("\npackage javax.faces.component.html;\n");
        writer.write("\n\n");

        // Generate the imports
        writer.write("import java.io.IOException;\n");
        writer.write("import javax.faces.context.FacesContext;\n");
        writer.write("import javax.faces.el.MethodBinding;\n");
        writer.write("import javax.faces.el.ValueBinding;\n");
        writer.write("\n\n");

        // Generate the class JavaDocs (if any)
        DescriptionBean db = cb.getDescription("");
        String 
	    rendererTypeDocs = null,
	    rendererType = cb.getRendererType();

	if (db != null || rendererType != null) {
	    writer.write("/**\n");
	}
	    
        if (db != null) {
            String description = db.getDescription();
            if (description == null) {
                description = "";
            }
            description = description.trim();
            if (description.length() > 0) {
                description(description, writer, 1);

            }
        }

	if (rendererType != null) {
	    rendererTypeDocs = "<p>By default, the <code>rendererType</code> property must be set to \"<code>" + 
		rendererType + 
		"</code>\" This value can be changed by calling the <code>setRendererType()</code> method.</p>";
	    description(rendererTypeDocs, writer, 1);
	}

	if (db != null || rendererType != null) {
	    writer.write("*/\n");
	}

        // Generate the class declaration
        writer.write("public class ");
        writer.write(shortName(cb.getComponentClass()));
        writer.write(" extends ");
        writer.write(base.getComponentClass());
        writer.write(" {\n\n\n");

        // Generate the constructor
        writer.write("  public ");
        writer.write(shortName(cb.getComponentClass()));
        writer.write("() {\n");
        writer.write("    super();\n");
        if (rendererType != null) {
            writer.write("    setRendererType(\"");
            writer.write(rendererType);
            writer.write("\");\n");
        }
        writer.write("  }\n\n\n");

	// Generate the manifest constant for the component type
	writer.write("  /*\n");
	writer.write("   * <p>The standard component type for this component.</p>\n");
	writer.write("   */\n");
	writer.write("   public static final String COMPONENT_TYPE = \"");
	writer.write(cb.getComponentType());
	writer.write("\";\n\n\n");

    }


    /**
     * <p>Generate the property instance variable, and getter and setter
     * methods, for all non-duplicate properties of this component class.</p>
     */
    private static void properties() throws Exception {

        ComponentBean base = fcb.getComponent(cb.getBaseComponentType());
        PropertyBean pbs[] = cb.getProperties();
        for (int i = 0; i < pbs.length; i++) {

            // Should we generate this property?
            PropertyBean pb = pbs[i];
            if (base.getProperty(pb.getPropertyName()) != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Skipping base class property '" +
                              pb.getPropertyName() + "'");
                }
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug("Generating property variable/getter/setter for '" +
                          pb.getPropertyName() + "'");
            }
            properties.add(pb);
            String type = pb.getPropertyClass();
            String var = mangle(pb.getPropertyName());

            // Generate the instance variable
            writer.write("  private ");
            writer.write(type);
            writer.write(" ");
            writer.write(var);
            if (pb.getDefaultValue() != null) {
                writer.write(" = ");
                writer.write(pb.getDefaultValue());
            } else if (primitive(type)) {
                writer.write(" = ");
                writer.write((String) defaults.get(type));
            }
            writer.write(";\n");
            if (primitive(type)) {
                writer.write("  private boolean ");
                writer.write(var);
                writer.write("_set = false;\n");
            }
            writer.write("\n");

            // Document the getter method
            writer.write("  /**\n");
            writer.write("   * <p>Return the value of the <code>");
            writer.write(pb.getPropertyName());
            writer.write("</code> property.");
            DescriptionBean db = pb.getDescription("");
            if (db != null) {
                String description = db.getDescription();
                if (description == null) {
                    description = "";
                }
                description = description.trim();
                if (description.length() > 0) {
                    writer.write("  Contents:</p><p>\n");
                    description(description, writer, 3);
                }
            } else {
                writer.write("\n");
            }
            writer.write("   * </p>\n");
            writer.write("   */\n");

            // Generate the getter method
            writer.write("  public ");
            writer.write(type);
            if ("boolean".equals(type)) {
                writer.write(" is");
            } else {
                writer.write(" get");
            }
            writer.write(capitalize(pb.getPropertyName()));
            writer.write("() {\n");
            if (primitive(type)) {
                writer.write("    if (this.");
                writer.write(var);
                writer.write("_set) {\n");
            } else {
                writer.write("    if (null != this.");
                writer.write(var);
                writer.write(") {\n");
            }
            writer.write("      return this.");
            writer.write(var);
            writer.write(";\n");
            writer.write("    }\n");
            writer.write("    ValueBinding _vb = getValueBinding(\"");
            writer.write(pb.getPropertyName());
            writer.write("\");\n");
            writer.write("    if (_vb != null) {\n");
            if (primitive(type)) {
                writer.write("      Object _result = _vb.getValue(getFacesContext());\n");
                writer.write("      if (_result == null) {\n");
                writer.write("        return ");
                writer.write((String) defaults.get(type));
                writer.write(";\n");
                writer.write("      } else {\n");
                writer.write("        return ((");
                writer.write((String) wrappers.get(type));
                writer.write(") _result).");
                writer.write((String) unwrappers.get(type));
                writer.write("();\n");
                writer.write("      }\n");
            } else {
                writer.write("      return (");
                writer.write(type);
                writer.write(") _vb.getValue(getFacesContext());\n");
            }
            writer.write("    } else {\n");
            if (primitive(type)) {
                writer.write("        return this.");
                writer.write(var);
                writer.write(";\n");
            } else {
                writer.write("      return null;\n");
            }
            writer.write("    }\n");
            writer.write("  }\n\n");

            // Generate the setter method
            writer.write("  /**\n");
            writer.write("   * <p>Set the value of the <code>");
            writer.write(pb.getPropertyName());
            writer.write("</code> property.</p>\n");
            writer.write("   */\n");
            writer.write("  public void set");
            writer.write(capitalize(pb.getPropertyName()));
            writer.write("(");
            writer.write(type);
            writer.write(" ");
            writer.write(var);
            writer.write(") {\n");
            writer.write("    this.");
            writer.write(var);
            writer.write(" = ");
            writer.write(var);
            writer.write(";\n");
            if (primitive(type)) {
                writer.write("    this.");
                writer.write(var);
                writer.write("_set = true;\n");
            }
            writer.write("  }\n\n");

            // Generate spacing between properties
            writer.write("\n");

        }

    }


    /**
     * <p>Generate the suffix for this component class.</p>
     */
    private static void suffix() throws Exception {

        int n = 0; // Index into values array
        int p = 0; // Number of primitive properties
        for (int i = 0; i < properties.size(); i++) {
            PropertyBean pb = (PropertyBean) properties.get(i);
            if (primitive(pb.getPropertyClass())) {
                p++;
            }
        }

        // Generate the saveState() method
        writer.write("  public Object saveState(FacesContext _context) {\n");
        writer.write("    Object _values[] = new Object[");
        writer.write("" + (properties.size() + p + 1));
        writer.write("];\n");
        writer.write("    _values[0] = super.saveState(_context);\n");
        n = 1;
        for (int i = 0; i < properties.size(); i++) {
            PropertyBean pb = (PropertyBean) properties.get(i);
            String name = mangle(pb.getPropertyName());
            String type = pb.getPropertyClass();
            writer.write("    _values[");
            writer.write("" + n++);
            writer.write("] = ");
            if ("boolean".equals(type)) {
                writer.write("this.");
                writer.write(name);
                writer.write(" ? Boolean.TRUE : Boolean.FALSE");
            } else if (primitive(type)) {
                writer.write("new ");
                writer.write((String) wrappers.get(type));
                writer.write("(this.");
                writer.write(name);
                writer.write(")");
            } else {
                writer.write(name);
            }
            writer.write(";\n");
            if (primitive(type)) {
                writer.write("    _values[");
                writer.write("" + n++);
                writer.write("] = this.");
                writer.write(name);
                writer.write("_set ? Boolean.TRUE : Boolean.FALSE;\n");
            }
        }
        writer.write("    return _values;\n");
        writer.write("  }\n\n\n");

        // Generate the restoreState() method
        writer.write("  public void restoreState(FacesContext _context, Object _state) {\n");
        writer.write("    Object _values[] = (Object[]) _state;\n");
        writer.write("    super.restoreState(_context, _values[0]);\n");
        n = 1;
        for (int i = 0; i < properties.size(); i++) {
            PropertyBean pb = (PropertyBean) properties.get(i);
            String name = mangle(pb.getPropertyName());
            String type = pb.getPropertyClass();
            writer.write("    this.");
            writer.write(name);
            writer.write(" = ");
            if (primitive(type)) {
                writer.write("((");
                writer.write((String) wrappers.get(type));
                writer.write(") _values[");
                writer.write("" + n++);
                writer.write("]).");
                writer.write((String) unwrappers.get(type));
                writer.write("()");
            } else {
                writer.write("(");
                writer.write(type);
                writer.write(") _values[");
                writer.write("" + n++);
                writer.write("]");
            }
            writer.write(";\n");
            if (primitive(type)) {
                writer.write("    this.");
                writer.write(name);
                writer.write("_set = ((Boolean) _values[");
                writer.write("" + n++);
                writer.write("]).booleanValue();\n");
            }
        }
        writer.write("  }\n\n\n");

        // Generate the ending of this class
        writer.write("}\n");

    }



    // ------------------------------------------------------------- Main Method


    public static void main(String args[]) throws Exception {

        try {

            // Perform setup operations
            if (log.isDebugEnabled()) {
                log.debug("Processing command line options");
            }
            Map options = options(args);

            copyright((String) options.get("--copyright"));
            directories((String) options.get("--dir"));
            Digester digester = digester(false, true, false);
            String config = (String) options.get("--config");
            if (log.isDebugEnabled()) {
                log.debug("Parsing configuration file '" + config + "'");
            }
            digester.push(new FacesConfigBean());
            fcb = parse(digester, config);
            if (log.isInfoEnabled()) {
                log.info("Generating HTML component classes");
            }

            // Generate concrete HTML component classes
            ComponentBean cbs[] = fcb.getComponents();
            for (int i = 0; i < cbs.length; i++) {
                String componentClass = cbs[i].getComponentClass();
                if (componentClass.startsWith("javax.faces.component.html.")) {
                    cb = cbs[i];
                    generate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);

    }


}
