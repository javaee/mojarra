package com.sun.faces.render;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;


public class OutputScriptStyleTestCase extends AbstractTestCase {

    public OutputScriptStyleTestCase(String name) {
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
        return (new TestSuite(OutputScriptStyleTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testOutputScriptStyle() throws Exception {
        HtmlPage page = getPage("/faces/render/outputScriptStyleNested.xhtml");

        String text = page.asXml();

        // case 1
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case1.js\">.*" + 
                "</script>.*" + 
                "</head>.*"
                ));
        
        assertTrue(!text.matches("(?s).*alert\\(\"case1\"\\);.*"));
        
        // case 2
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\">.*" + 
                "alert\\(\"case2\"\\);.*" +
                "</script>.*" + 
                "</body>.*"
                ));
        
        // case 3
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case3.js\">.*" + 
                "</script>.*" + 
                "</body>.*"
                ));
        
        assertTrue(!text.matches("(?s).*alert\\(\"case3\"\\);.*"));
        
        // case 4
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case4.js\">.*" + 
                "</script>.*" + 
                "</head>.*"
                ));

        // case 5, if not satisfied, would cause the page to fail.

        // case 6
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case6.js\">.*" + 
                "</script>.*" + 
                "</body>.*"
                ));
        
        // case 7, if not satisfied, would cause the page to fail
        
        // case 8
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\">.*" + 
                "alert\\(\"case8\"\\);.*" +
                "</script>.*" + 
                "</body>.*"
                ));
        
        // case 9
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case9.css\"\\s*/>.*" + 
                "</head>.*"
                ));
        
        assertTrue(!text.matches("(?s).*\\.case9.*"));
        
        // case 10
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<style\\s*type=\"text/css\">.*" +
                "\\.case10\\s*\\{.*" +
                "color: blue;.*" +
                "\\}.*" +
                "</style>.*" +
                "</head>.*"
                ));
        

        // case 11
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case11.css\"\\s*/>.*" + 
                "</head>.*"
                ));
        
        assertTrue(!text.matches("(?s).*\\.case11.*"));
        
        // case 12
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case12.css\"\\s*/>.*" + 
                "</head>.*"
                ));

        // case 13, if not satisfied, would cause the page to fail.

        // case 14
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case14.css\"\\s*/>.*" + 
                "</head>.*"
                ));

        // case 15, if not satisfied, would cause the page to fail.
        
        // case 16
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<style\\s*type=\"text/css\">.*" +
                "\\.case16\\s*\\{.*" +
                "color: orange;.*" +
                "\\}.*" +
                "</style>.*" +
                "</head>.*"
                ));

    }

    public void testScriptQuery() throws Exception {
        lastpage = getPage("/faces/render/outputScriptQuery.xhtml");
        String text = lastpage.asXml();

        assertTrue(text.matches(
                "(?s).*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/simple.js\\?mod=test\">.*" +
                "</script>.*"
                ));

        assertTrue(text.matches(
                "(?s).*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/simple2.js\">.*" +
                "</script>.*"
                ));

        assertTrue(text.matches(
                "(?s).*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/jsf-uncompressed.js\\?ln=javax.faces\">.*" +
                "</script>.*"
                ));

    }

    public void testSheetMedia() throws Exception {
        lastpage = getPage("/faces/render/outputSheetMedia.xhtml");
        String text = lastpage.asXml();
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".* rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case9.css\".* media=\"print\"\\s*/>.*" + 
                "</head>.*"
                ));
    }
}