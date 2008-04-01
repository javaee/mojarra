package com.sun.faces.renderkit.html_basic;

import javax.faces.OutputMethod;

import javax.servlet.jsp.PageContext;

public class JspOutputMethod implements OutputMethod {

    private PageContext pageContext;

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public String getContentType() {
        return null;
    }

    public void startDocument() throws java.io.IOException {

    }

    public void endDocument() throws java.io.IOException {

    }

    public void flush() throws java.io.IOException {

    }

    public void writeComment(String text) throws java.io.IOException {

    }

    public void writeText(String text) throws java.io.IOException {
        pageContext.getOut().print(text);
    }

    public void writeText(char[] c, int start, int len) 
        throws java.io.IOException {

    }

    public void writeText(char c) throws java.io.IOException {

    }

    public void writeRawText(String text) throws java.io.IOException {

    }

    public void writeRawText(char[] c, int start, int len) 
        throws java.io.IOException {

    }

    public void writeRawText(char c) throws java.io.IOException {

    }

    public void startElement(String name) throws java.io.IOException {

    }

    public void endElement(String name) throws java.io.IOException {

    }

    public void writeAttribute(String name, Object value)
        throws java.io.IOException {

    }

    public void writeURIAttribute(String name, Object value)
        throws java.io.IOException {

    }
}
