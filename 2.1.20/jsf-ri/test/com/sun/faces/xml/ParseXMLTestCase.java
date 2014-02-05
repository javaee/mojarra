/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Locale;
import java.util.ResourceBundle;
import junit.framework.TestCase;

/**
 *
 * @author sheetalv
 */
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
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
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
