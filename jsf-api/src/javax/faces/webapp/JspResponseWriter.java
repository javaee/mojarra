/*
 * $Id: JspResponseWriter.java,v 1.5 2003/02/20 22:46:45 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import javax.faces.context.ResponseWriter;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;


/**
 * <p><strong>JspResponseWriter</strong> is a concrete implementation of
 * {@link ResponseWriter} that wraps a <code>PageContext</code> passed to its
 * constructor.</p>
 */

public final class JspResponseWriter extends ResponseWriterBase {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a <code>JspResponseWriter</code> that wraps the specified
     * <code>PageContext</code>.</p>
     *
     * @param writer The <code>JspWriter</code> to be wrapped
     *
     * @exception NullPointerException if <code>pageContext</code> is null
     */
    public JspResponseWriter(PageContext pageContext) {

        if (pageContext == null) {
            throw new NullPointerException();
        }
        this.pageContext = pageContext;

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The <code>PageContext</code> that we are wrapping.</p>
     */
    private PageContext pageContext = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Close the underlying writer.</p>
     *
     * @exception IOException if an input/output error occurs
     */
    public void close() throws IOException {

        pageContext.getOut().close();

    }


    /**
     * <p>Flush the underlying writer's buffer, if any.</p>
     *
     * @exception IOException if an input/output error occurs
     */
    public void flush() throws IOException {

        pageContext.getOut().flush();

    }


    /**
     * <p>Write an array of characters.</p>
     *
     * @param cbuf An array of characters to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(char cbuf[]) throws IOException {

        pageContext.getOut().print(cbuf);

    }


    /**
     * <p>Write a portion of an array of characters.</p>
     *
     * @param cbuf An array of characters to be written
     * @param off Starting offset (zero-relative)
     * @param len Number of characters to write
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(char cbuf[], int off, int len) throws IOException {

        pageContext.getOut().print(new String(cbuf, off, len));

    }


    /**
     * <p>Write a single character from the low-order 16 bits of the
     * specified integer.</p>
     *
     * @param c An <code>int</code> specifying the character to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int c) throws IOException {

        pageContext.getOut().print((char) (c & 0xffff));

    }


    /**
     * <p>Write a String of characters.</p>
     *
     * @param s A <code>String</code> to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(String s) throws IOException {

        pageContext.getOut().print(s);

    }


    /**
     * <p>Write a portion of a String of characters.</p>
     *
     * @param s A String of characters to be written
     * @param off Starting offset (zero-relative)
     * @param len Number of characters to write
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(String s, int off, int len) throws IOException {

        pageContext.getOut().print(s.substring(off, len));

    }


}
