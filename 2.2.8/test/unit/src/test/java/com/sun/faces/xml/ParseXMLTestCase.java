/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
 */
package com.sun.faces.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;

public class ParseXMLTestCase extends TestCase {

    List list = new ArrayList();
    private final static String xmlDir = "/conf/share";
    private final static String jsfri = "/jsf-ri";

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ParseXMLTestCase(String name) {
        super(name);
    }

    /**
     * Set up instance variables required by this test case.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Return the tests included in this test suite.
     * @return 
     */
    public static Test suite() {
        return (new TestSuite(ParseXMLTestCase.class));
    }

    // ------------------------------------------------------------ Test Methods
    /**
     * Added for issue 904.
     */
    public void testParseXML() throws Exception {

        String curDir = System.getProperty("user.dir");
        File baseDir = new File(curDir);
        System.out.println("current dir = " + curDir);
        System.out.flush();
        visitAllDirsAndFiles(new File(baseDir, xmlDir));
        //printAllXMLFiles();
        for (Object file : list) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                factory.setValidating(true);
                SAXParser saxParser = factory.newSAXParser();

                System.out.println("XML file to be parsed : file://" + file.toString());
                System.out.flush();
                saxParser.parse(new InputSource(new FileInputStream(file.toString())), new XHTMLResolvingHandler());
                System.out.println("parsing complete.");
                System.out.flush();
            } catch (Exception e) {
                System.out.println("Parse error for " + file.toString() + " " + e.toString());
                System.out.flush();
                fail();
            }
        }

    }

    // Process all files and directories under dir
    public void visitAllDirsAndFiles(File dir) {

        if (dir.isFile()) {
            if (isXML(dir)) {
                //add it to the list
                list.add(dir);
            }
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                visitAllDirsAndFiles(new File(dir, children[i]));
            }
        }
    }

    public boolean isXML(File file) {
        String name = file.getName();
        if (name.endsWith(".xml")) {
            //it is an xml file
            //add to list
            return true;
        } else {
            return false;
        }
    }

    private void printAllXMLFiles() {
        for (Object l : list) {
            System.out.println("XML file : " + l);
        }
    }
}
