/*
 * $Id: RenderKitSpecificationGenerator.java,v 1.3 2004/02/04 23:46:35 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.beans.PropertyBean;
import com.sun.faces.config.beans.RenderKitBean;
import com.sun.faces.config.beans.RendererBean;
import com.sun.faces.config.beans.AttributeBean;



/**
 * <p>Generate javadoc style documenation about the render-kits defined
 * in a faces-config.xml file.</p>
 *
 * <p>This application requires the following command line options:</p>
 * <ul>
 * <li><strong>--config</strong> Absolute pathname to an input configuration
 *     file that will be parsed by the <code>parse()</code> method.</li>
 * <li><strong>--dir</strong> Absolute pathname to the directory into which
 *     generated HTML files will be created.</li>
 * <li><strong>--dtd</strong> Absolute pathname to a file containing the DTD
 *     used to validate the input configuration files.</li>
 * </ul>
 */

public class RenderKitSpecificationGenerator extends AbstractGenerator {

    public static String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"\"http://www.w3.org/TR/REC-html40/loose.dtd\">";


    // -------------------------------------------------------- Static Variables


    private static final Log log =
        LogFactory.getLog(RenderKitSpecificationGenerator.class);


    // The component configuration bean for the component class to be generated
    private static ComponentBean cb;


    // The directory into which the HTML will be generated
    private static File directory;

    // The directory into which the individual Renderer HTML will be generated
    private static File renderKitDirectory;


    // Base object of the configuration beans
    private static FacesConfigBean fcb;

    // List of relevant properties for the component class to be generated
    private static List properties;

    // The Writer for each component class to be generated
    private static Writer writer;

    private static final String DEFAULT_RENDERKIT_ID = "HTML_BASIC";

    private static String renderKitId = DEFAULT_RENDERKIT_ID;


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Create directories as needed to contain the output.  Leave a
     * <code>directory</code> object that points at the top level directory
     * for the concrete component classes.</p>
     *
     * @param path Pathname to the base directory
     */
    private static void directories(String path) throws Exception {

        directory = new File(path);
        if (log.isDebugEnabled()) {
            log.debug("Creating output directory " +
                      directory.getAbsolutePath());
        }
        directory.mkdirs();

	// make the directory for our renderKit
	renderKitDirectory = new File(directory, renderKitId);
        if (log.isDebugEnabled()) {
            log.debug("Creating output renderKitDirectory " +
                      renderKitDirectory.getAbsolutePath());
        }
        renderKitDirectory.mkdirs();
    }


    /**
     * <p>Generate the concrete HTML component class based on the current
     * component configuration bean.</p>
     */
    private static void generate() throws Exception {

        if (log.isInfoEnabled()) {
            log.info("Generating RenderKit Documentation " +
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
        if (db != null) {
            String description = db.getDescription();
            if (description == null) {
                description = "";
            }
            description = description.trim();
            if (description.length() > 0) {
                writer.write("/**\n");
                description(description, writer, 1);
                writer.write(" */\n");
            }
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
        String rendererType = cb.getRendererType();
        if (rendererType != null) {
            writer.write("    setRendererType(\"");
            writer.write(rendererType);
            writer.write("\");\n");
        }
        writer.write("  }\n\n\n");

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
                writer.write("        return ");
                writer.write((String) defaults.get(type));
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

    /**
     *
     * @return a SortedMap, where the keys are component-family String
     * entries, and the values are {@link RendererBean} instances
     */

    public static SortedMap getComponentFamilyRendererMap(String rkId) throws IllegalStateException {
	RenderKitBean renderKit = null;
	RendererBean [] renderers = null;
	RendererBean renderer = null;
	TreeMap result = null;
	ArrayList list = null;
	String componentFamily = null;

	if (null == (renderKit = fcb.getRenderKit(rkId))) {
	    RenderKitBean [] kits = null;
	    if (null == (kits = fcb.getRenderKits())) {
		throw new IllegalStateException("no RenderKits");
	    }
	    if (null == (renderKit = kits[0])) {
		throw new IllegalStateException("no RenderKits");
	    }
	}

	if (null == (renderers = renderKit.getRenderers())) {
	    throw new IllegalStateException("no Renderers");
	}

	result = new TreeMap();
	
	for (int i = 0, len = renderers.length; i < len; i++) {
	    if (null == (renderer = renderers[i])) {
		throw new IllegalStateException("no Renderer");
	    }
	    // if this is the first time we've encountered this
	    // componentFamily
	    if (null == (list = (ArrayList)
			 result.get(componentFamily = 
				    renderer.getComponentFamily()))) {
		// create a list for it
		list = new ArrayList();
		list.add(renderer);
		result.put(componentFamily, list);
	    }
	    else {
		list.add(renderer);
	    }
	}
	return result;
    }

    public static String getFirstSentance(String para) throws Exception {
	int dot = para.indexOf(".");
	return para.substring(0, dot + 1);
    }

    public static void copyResourceToFile(String resourceName, File file) throws Exception {
	    FileOutputStream fos = null;
	    BufferedInputStream bis = null;
	    URL url = null;
	    URLConnection conn = null;
	    byte[] bytes = new byte[1024];
	    int len = 0;

	    fos = new FileOutputStream(file);
	    url = getCurrentLoader(fos).getResource(resourceName);
	    conn = url.openConnection();
	    conn.setUseCaches(false);
	    bis = new BufferedInputStream(conn.getInputStream());
	    while (-1 != (len = bis.read(bytes, 0, 1024))) {
		fos.write(bytes, 0, len);
	    }
	    fos.close();
	    bis.close();


    }

    public static void writeStringToFile(String toWrite, 
					 File file) throws Exception {
	    FileOutputStream fos = null;

	    fos = new FileOutputStream(file);
	    byte [] bytes = toWrite.getBytes();
	    fos.write(bytes);
	    fos.close();
    }


    public static void appendResourceToStringBuffer(String resourceName, 
						  StringBuffer sb) throws Exception {
	    InputStreamReader isr = null;
	    URL url = null;
	    URLConnection conn = null;
	    char[] chars = new char[1024];
	    int len = 0;
	    
	    url = getCurrentLoader(sb).getResource(resourceName);
	    conn = url.openConnection();
	    conn.setUseCaches(false);
	    isr = new InputStreamReader(conn.getInputStream());
	    while (-1 != (len = isr.read(chars, 0, 1024))) {
		sb.append(chars, 0, len);
	    }
	    isr.close();
    }

    public static void generateAllRenderersFrame() throws Exception {
	StringBuffer sb = null;
	
	// generate the allrenderers-frame.html
	sb = new StringBuffer(2048);
	appendResourceToStringBuffer("com/sun/faces/generate/facesdoc/allrenderers-frame.top",
				   sb);
	sb.append("<FONT size=\"+1\" CLASS=\"FrameHeadingFont\">\n");
	sb.append("<B>" + renderKitId + " RenderKit</B></FONT>\n");
	sb.append("<BR>\n\n");
	sb.append("<DL CLASS=\"FrameItemFont\">\n\n");
	
	SortedMap renderersByComponentFamily = 
	    getComponentFamilyRendererMap(renderKitId);
	Iterator 
	    rendererIter = null,
	    keyIter = renderersByComponentFamily.keySet().iterator();
	String 
	    curType = null,
	    curFamily = null;
	List renderers = null;
	RendererBean renderer = null;
	
	while (keyIter.hasNext()) {
	    curFamily = (String) keyIter.next();
	    sb.append("  <DT>" + curFamily + "</DT>\n");
	    renderers = (List) renderersByComponentFamily.get(curFamily);
	    rendererIter = renderers.iterator();
	    while (rendererIter.hasNext()) {
		renderer = (RendererBean) rendererIter.next();
		curType = renderer.getRendererType();
		sb.append("  <DD><A HREF=\"" + renderKitId + "/" + 
			  curFamily + curType + 
			  ".html\" TARGET=\"rendererFrame\">" + curType + 
			  "</A><DD>\n");
	    }
	}

	sb.append("</dl>\n");
	
	appendResourceToStringBuffer("com/sun/faces/generate/facesdoc/allrenderers-frame.bottom",
				   sb);
	writeStringToFile(sb.toString(), 
			  new File(directory, 
				   "allrenderers-frame.html"));
    }

    public static void generateRenderKitSummary() throws Exception {
	StringBuffer sb = null;
	
	// generate the renderkit-summary.html
	sb = new StringBuffer(2048);
	appendResourceToStringBuffer("com/sun/faces/generate/facesdoc/renderkit-summary.top",
				   sb);
	sb.append("<H2>" + renderKitId + " RenderKit</H2>\n");
	sb.append("<BR>\n\n");

	RenderKitBean renderKit = null;
	if (null == (renderKit = fcb.getRenderKit(renderKitId))) {
	    RenderKitBean [] kits = null;
	    if (null == (kits = fcb.getRenderKits())) {
		throw new IllegalStateException("no RenderKits");
	    }
	    if (null == (renderKit = kits[0])) {
		throw new IllegalStateException("no RenderKits");
	    }
	}
	
	DescriptionBean descBean = renderKit.getDescription("");
	String description = (null == descBean) ? "" : descBean.getDescription();
	sb.append("<P>" + description + "</P>\n");
	sb.append("<P />");
	sb.append("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">");
	sb.append("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\">\n");
	sb.append("<TD COLSPAN=\"3\"><FONT SIZE=\"+2\">\n");
	sb.append("<B>Renderer Summary</B></FONT></TD>\n");
	sb.append("\n");
	sb.append("<TR>\n");
	sb.append("<TH>component-family</TH>\n");
	sb.append("<TH>renderer-type</TH>\n");
	sb.append("<TH>description</TH>\n");
	sb.append("</TR>\n");
	
	SortedMap renderersByComponentFamily = 
	    getComponentFamilyRendererMap(renderKitId);
	Iterator 
	    rendererIter = null,
	    keyIter = renderersByComponentFamily.keySet().iterator();
	String 
	    curType = null,
	    curFamily = null;
	List renderers = null;
	RendererBean renderer = null;
	
	while (keyIter.hasNext()) {
	    curFamily = (String) keyIter.next();
	    renderers = (List) renderersByComponentFamily.get(curFamily);
	    rendererIter = renderers.iterator();
	    sb.append("  <TR>\n");
	    sb.append("    <TD rowspan=\"" + renderers.size() + "\">" + 
		      curFamily + "</TD>\n");
	    while (rendererIter.hasNext()) {
		renderer = (RendererBean) rendererIter.next();
		curType = renderer.getRendererType();
		sb.append("    <TD><A HREF=\"" + curFamily + curType + 
			  ".html\" TARGET=\"rendererFrame\">" + curType + 
			  "</A></TD>\n");
		descBean = renderer.getDescription("");
		description = (null == descBean) ? "" : descBean.getDescription();
		sb.append("    <TD>" + getFirstSentance(description) + 
			  "</TD>");
		if (rendererIter.hasNext()) {
		    sb.append("  </TR>\n");
		    sb.append("  <TR>\n");
		}
	    }
	    sb.append("  </TR>\n");
	}
	
	sb.append("</TABLE>\n\n");
	
	appendResourceToStringBuffer("com/sun/faces/generate/facesdoc/renderkit-summary.bottom",
				   sb);
	writeStringToFile(sb.toString(), 
			  new File(renderKitDirectory, 
				   "renderkit-summary.html"));
    }

    public static void generateRenderersDocs() throws Exception {
	StringBuffer sb = null;
	RenderKitBean renderKit = null;
	DescriptionBean descBean = null;
	AttributeBean attribute = null;
	String 
	    description,
	    rendererType,
	    componentFamily,
	    defaultValue,
	    title,
	    curAttr;
	
	
	// generate the docus for each renderer

	if (null == (renderKit = fcb.getRenderKit(renderKitId))) {
	    RenderKitBean [] kits = null;
	    if (null == (kits = fcb.getRenderKits())) {
		throw new IllegalStateException("no RenderKits");
	    }
	    if (null == (renderKit = kits[0])) {
		throw new IllegalStateException("no RenderKits");
	    }
	}
	RendererBean [] renderers = renderKit.getRenderers();
	AttributeBean [] attributes = null;
	sb = new StringBuffer(2048);

	for (int i = 0, len = renderers.length; i < len; i++) {
	    if (null == renderers[i]) {
		throw new IllegalStateException("null Renderer at index: "+i);
	    }
	    attributes = renderers[i].getAttributes();
	    
	    sb.append(DOCTYPE + "\n");
	    sb.append("<html>\n");
	    sb.append("<head>\n");
	    // PENDING timestamp
	    sb.append("<title>\n");
	    title = "<font size=\"-1\">component-family:</font> " +
		(componentFamily = renderers[i].getComponentFamily()) + 
		" <font size=\"-1\">renderer-type:</font> " + 
		(rendererType = renderers[i].getRendererType());
	    sb.append(title + "\n");
	    sb.append("</title>\n");
	    // PENDING META tag
	    sb.append("<link REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"../stylesheet.css\" TITLE=\"Style\">\n");
	    sb.append("</head>\n");
	    sb.append("<script>\n");
	    sb.append("function asd()\n");
	    sb.append("{\n");
	    sb.append("  parent.document.title=" + title + "\n");
	    sb.append("}\n");
	    sb.append("</SCRIPT>\n");
	    sb.append("<body BGCOLOR=\"white\" onload=\"asd();\">\n");
	    sb.append("\n");
	    sb.append("<H2><font size=\"-1\">" + renderKitId + 
		      " render-kit</font>\n");
	    sb.append("<br />\n");
	    sb.append(title + "\n");
	    sb.append("</H2>\n");
	    sb.append("<HR />\n");
	    descBean = renderers[i].getDescription("");
	    description = (null == descBean) ? "" : descBean.getDescription();
	    sb.append("<P>" + description + "</P>\n");
	    // render our renders children status

	    if (renderers[i].isRendersChildren()) {
		sb.append("<P>This renderer is responsible for rendering its children.</P>");
	    }
	    else {
		sb.append("<P>This renderer is not responsible for rendering its children.</P>");
	    }

	    // if we have attributes
	    if ((null == attributes) || (0 < attributes.length)) {
		sb.append("<HR />\n");
		sb.append("<a NAME=\"attributes\"><!-- --></a>\n");
		sb.append("\n");
		sb.append("<h3>Note:</h3>\n");
		sb.append("\n");
		sb.append("<p>Attributes with a <code>pass-through</code> value of\n");
		sb.append("<code>true</code> are not interpreted by the renderer and are passed\n");
		sb.append("straight through to the rendered markup, without checking for validity.  Attributes with a\n");
		sb.append("<code>pass-through</code> value of <code>false</code> are interpreted\n");
		sb.append("by the renderer, and may or may not be checked for validity by the renderer.</p>\n");
		sb.append("\n");
		sb.append("<table BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\n");
		sb.append("<tr BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\">\n");
		sb.append("<td COLSPAN=\"5\"><font SIZE=\"+2\">\n");
		sb.append("<b>Attributes</b></font></td>\n");
		sb.append("</tr>\n");
		sb.append("<tr BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\">\n");
		sb.append("<th><b>attribute-name</b></th>\n");
		sb.append("<th><b>pass-through</b></th>\n");
		sb.append("<th><b>attribute-class</b></th>\n");
		sb.append("<th><b>description</b></th>\n");
		sb.append("<th><b>default-value</b></th>\n");
		sb.append("</tr>\n");
		sb.append("	    \n");
		// output each attribute
		for (int j = 0, attrLen = attributes.length; j < attrLen; j++) {
		    sb.append("<tr BGCOLOR=\"white\" CLASS=\"TableRowColor\">\n");
		    sb.append("<td ALIGN=\"right\" VALIGN=\"top\" WIDTH=\"1%\"><code>\n");
		    sb.append("&nbsp;" + attributes[j].getAttributeName() + "\n");
		    sb.append("</td>\n");
		    sb.append("<td ALIGN=\"right\" VALIGN=\"top\">" + 
			      attributes[j].isPassThrough() + "</td>\n");
		    sb.append("<td><code>" + attributes[j].getAttributeClass() +
			      "</code></td>\n");
		    descBean = attributes[j].getDescription("");
		    description = (null == descBean) ? "" : descBean.getDescription();
		    sb.append("<td>" + description + "</td>\n");
		    if (null == (defaultValue = attributes[j].getDefaultValue())) {
			defaultValue= "undefined";
		    }
		    sb.append("<td>" + defaultValue + "<td>\n");
		    sb.append("</tr>\n");
		}
		sb.append("</table>\n");
	    }
	    else {
		sb.append("<p>This renderer-type has no attributes</p>\n");
	    }
	    sb.append("<hr>\n");
	    sb.append("Copyright (c) 2003-2004 Sun Microsystems, Inc. All Rights Reserved.\n");
	    sb.append("</body>\n");
	    sb.append("</html>\n");
	    writeStringToFile(sb.toString(), 
			      new File(renderKitDirectory, 
				       componentFamily + rendererType + 
				       ".html"));
	    sb.delete(0, sb.length());
	}
    }
	    
    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    loader = fallbackClass.getClass().getClassLoader();
	}
	return loader;
    }

    // ------------------------------------------------------------- Main Method


    public static void main(String args[]) throws Exception {

        try {

            // Perform setup operations
            if (log.isDebugEnabled()) {
                log.debug("Processing command line options");
            }
            Map options = options(args);
            String dtd = (String) options.get("--dtd");
            if (log.isDebugEnabled()) {
                log.debug("Configuring digester instance with DTD '" +
                          dtd + "'");
            }
            directories((String) options.get("--dir"));
            Digester digester = digester(dtd, false, true, false);
            String config = (String) options.get("--config");
            if (log.isDebugEnabled()) {
                log.debug("Parsing configuration file '" + config + "'");
            }
            fcb = parse(digester, config);
            if (log.isInfoEnabled()) {
                log.info("Generating HTML component classes");
            }

            // Generate render-kit documentation
	    // copy the static files to the output area
	    copyResourceToFile("com/sun/faces/generate/facesdoc/index.html",
			       new File(directory, "index.html"));
	    copyResourceToFile("com/sun/faces/generate/facesdoc/stylesheet.css",
			       new File(directory, "stylesheet.css"));

	    generateAllRenderersFrame();
	    generateRenderKitSummary();
	    generateRenderersDocs();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);

    }


}
