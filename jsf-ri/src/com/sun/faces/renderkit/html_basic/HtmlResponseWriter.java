package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.io.Writer;

import javax.faces.context.ResponseWriter;


public class HtmlResponseWriter extends ResponseWriter {

    // Character encoding of that Writer - this may be null
    // if the encoding isn't known, in which case we degrade as
    // gracefully as possible
    //
    private String encoding = null;

    // Writer to use for output;
    //
    private Writer writer = null;

    public HtmlResponseWriter(Writer writer, String encoding) {
        this.writer = writer;
	this.encoding = encoding;
    }

    //PENDING(rogerk) These methods will be implemented as part of Phase 2
    // of the Responsewriter implementation;
    public String getCharacterEncoding() {
        return encoding;
    }

    public void startDocument() throws IOException {
    }

    public void endDocument() throws IOException {
    }

    public void startElement(String name) throws IOException {
    }

    public void endElement(String name) throws IOException {
    }

    public void writeAttribute(String name, Object value) {
    }

    public void writeURIAttribute(String name, Object value) {
    }

    public void writeComment(Object comment) throws IOException {
    }

    public void writeText(Object text) throws IOException {
    }

    public void writeText(char text) throws IOException {
    }

    public void writeText(char text[]) throws IOException {
    }

    public void writeText(char text[], int off, int len)
        throws IOException {
    }

    public ResponseWriter cloneWithWriter(Writer writer) {
        return null;
    }

// PENDING(rogerk) The following methods are temporarily here to get 
// things running and will be either removed and/or corrected with
// correct implementation - rest of ResponseWriter implementation..
    public void close() throws IOException {
        writer.close();
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void write(char cbuf) throws IOException {
        writer.write(cbuf);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    public void write(int c) throws IOException {
        writer.write(c);
    }

    public void write(String str) throws IOException {
        writer.write(str);
    }
    
    public void write(String str, int off, int len) throws IOException {
        writer.write(str, off, len);
    }
}
