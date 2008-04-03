/*
 * $Id: AttributeManagerGenerator.java,v 1.2 2007/08/28 10:47:37 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

package com.sun.faces.generate;

import com.sun.faces.config.beans.AttributeBean;
import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.beans.PropertyBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * PENDING
 */
public class AttributeManagerGenerator extends AbstractGenerator {

    private static final String TARGET_PACKAGE =
          "com.sun.faces.renderkit";
    private static final String TARGET_CLASSNAME =
          "AttributeManager";

    private PropertyManager manager;
    private List<String> imports;

    // ------------------------------------------------------------ Constructors


    public AttributeManagerGenerator(PropertyManager manager) {
        this.manager = manager;
    }

    // ------------------------------------------ Methods from AbstractGenerator


    public void generate(FacesConfigBean configBean) {
        try {
            CodeWriter writer = getCodeWriter();
            writeCopyright(writer);
            writePackage(writer);
            writeImports(writer);
            writeClassDocumentation(writer);
            writeClassDeclaration(writer);
            writeClassBody(writer, configBean);
            writeClassEnd(writer);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // ------------------------------------------------------------- Main Method


    public static void main(String[] args) throws Exception {

        PropertyManager propManager = PropertyManager.newInstance(args[0]);
        Generator generator = new AttributeManagerGenerator(propManager);
        generator.generate(GeneratorUtil.getConfigBean(args[1]));

    }

    // --------------------------------------------------------- Private Methods


    private CodeWriter getCodeWriter() throws IOException {
        FileWriter fWriter = new FileWriter(new File(getClassPackageDirectory(),
                                                     TARGET_CLASSNAME
                                                     + ".java"));
        return new CodeWriter(fWriter);
    }


    private File getClassPackageDirectory() {

        String packagePath = TARGET_PACKAGE.replace('.', File.separatorChar);
        File packageDir = new File(getBaseOutputDirectory(),
                                   packagePath);
        System.out.println("PACKAGE DIR: " + packageDir.getAbsolutePath());
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }

        return packageDir;

    } // END getClassPackageDirectory


    private File getBaseOutputDirectory() {

        File outputDir = new File(System.getProperty("user.dir") +
                                  File.separatorChar +
                                  manager
                                        .getProperty(PropertyManager.BASE_OUTPUT_DIR));
        System.out.println("BASE OUTPUT DIR: " + outputDir.getAbsolutePath());

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        return outputDir;

    } // END getBaseOutputDirectory


    private void addImport(String fullyQualClassName) {

        if (imports == null) {
            imports = new ArrayList<String>();
        }
        imports.add(fullyQualClassName);

    }

    private void writeImports(CodeWriter writer) throws Exception {
        addImport("java.util.Map");
        addImport("java.util.HashMap");
        addImport("java.util.Collections");
        Collections.sort(imports);

        for (Iterator i = imports.iterator(); i.hasNext();) {
            writer.writeImport((String) i.next());
        }
        writer.write('\n');

    } // END writeImports


    private void writeCopyright(CodeWriter writer) throws Exception {

        writer.writeBlockComment(
              manager.getProperty(PropertyManager.COPYRIGHT));
        writer.write('\n');

    } // END writeCopyright


    private void writePackage(CodeWriter writer) throws Exception {

        // Generate the package declaration
        writer.writePackage(TARGET_PACKAGE);
        writer.write('\n');

    } // END writePackage


    private void writeClassDocumentation(CodeWriter writer) throws Exception {

        writer.writeJavadocComment("This class contains mappings between the standard components\n"
                                   + "and the passthrough attributes associated with them.");

    } // END writeClassDocumentation


    private void writeClassDeclaration(CodeWriter writer) throws Exception {

        // Generate the class declaration
        writer.writePublicClassDeclaration(TARGET_CLASSNAME,
                                           null,
                                           null,
                                           false,
                                           false);

    } // END writeClassDeclaration


    private void writeClassBody(CodeWriter writer, FacesConfigBean bean)
    throws Exception {
        writer.indent();
        writer.fwrite("private static Map<String,String[]> ATTRIBUTE_LOOKUP;\n");
        writer.indent();
        writer.fwrite("static {\n");
        writer.indent();
        writer.fwrite("HashMap<String,String[]> map = new HashMap<String,String[]>();\n");

        ComponentBean[] components = bean.getComponents();
        List<String> keys = new ArrayList<String>();
        for (int i = 0, len = components.length; i < len; i ++) {
            ComponentBean comp = components[i];
            if (!comp.getComponentClass().contains("Html")) {
                continue;
            }
            String type = comp.getRendererType();
            if (type != null) {
                String family = comp.getBaseComponentType();
                type = type.substring(type.lastIndexOf('.') + 1);
                family = family.substring(family.lastIndexOf('.') + 1);
                String key = family + type;
                keys.add(key);
                writer.fwrite("map.put(\"");
                writer.write(key);
                writer.write("\",\n");
                writer.indent();
                writer.indent();
                writer.fwrite("new String[] {\n");
                writer.indent();
                writer.indent();
                PropertyBean[] props = comp.getProperties();
                boolean attributeWritten = false;
                for (int ii = 0, llen = props.length; ii < llen; ii++) {
                    PropertyBean aBean = props[ii];
                    // The RI doesn't use any JS for the command button, so
                    // treat
                    if (key.contains("Button") && "onclick".equals(aBean.getPropertyName())) {
                        aBean.setPassThrough(true);
                    }
                    if (aBean.isPassThrough()) {
                        if ((key.contains("Radio") || "SelectManyCheckbox".equals(key))
                            && ("style".equals(aBean.getPropertyName())
                                || ("border".equals(aBean.getPropertyName())))) {
                            continue;
                        }                        
                        writer.fwrite("\"");
                        writer.write(aBean.getPropertyName());
                        writer.write("\"");
                        attributeWritten = true;
                        if (attributeWritten) {
                            writer.write(",\n");
                        }
                    }
                    if (key.contains("Button") && "onclick".equals(aBean.getPropertyName())) {
                        // reset to the original state
                        aBean.setPassThrough(false);
                    }

                }
                writer.outdent();
                writer.outdent();
                writer.outdent();
                writer.fwrite("});\n");
                writer.outdent();
            }
        }

        writer.fwrite("ATTRIBUTE_LOOKUP = Collections.unmodifiableMap(map);\n");
        writer.outdent();
        writer.fwrite("}\n\n");
        writer.outdent();
        writer.fwrite("public enum Key {\n");
        writer.indent();
        for (int i = 0, len = keys.size(); i < len; i++) {
            String key = keys.get(i);
            writer.fwrite(key.toUpperCase() + "(\"" + key + "\")");
            if (i == (len - 1)) {
                writer.write(";\n");
            } else {
                writer.write(",\n");
            }
        }
        writer.fwrite("private String key;\n");
        writer.fwrite("Key(String key) {\n");
        writer.indent();
        writer.fwrite("this.key = key;\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("public String value() {\n");
        writer.indent();
        writer.fwrite("return this.key;\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.write("\n\n");
        writer.fwrite("public static String[] getAttributes(Key key) {\n");
        writer.indent();
        writer.fwrite("return ATTRIBUTE_LOOKUP.get(key.value());\n");
        writer.outdent();
        writer.fwrite("}\n");
    }


    private void writeClassEnd(CodeWriter writer) throws Exception {
        writer.outdent();
        writer.fwrite("}\n");
        writer.flush();
        writer.close();
    }

}
