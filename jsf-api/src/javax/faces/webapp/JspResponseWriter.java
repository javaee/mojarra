/*
 * $Id: JspResponseWriter.java,v 1.2 2002/07/16 21:52:04 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import javax.faces.context.ResponseWriter;
import javax.servlet.jsp.JspWriter;


/**
 * <p><strong>JspResponseWriter</strong> is a concrete implementation of
 * {@link ResponseWriter} that wraps a <code>JspWriter</code> passed to its
 * constructor.</p>
 *
 * <p><strong>FIXME</strong> - Should this implementation be in jsf-api?</p>
 */

public final class JspResponseWriter extends ResponseWriterBase {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a <code>JspResponseWriter</code> that wraps the specified
     * <code>JspWriter</code>.</p>
     *
     * @param writer The <code>JspWriter</code> to be wrapped
     *
     * @exception NullPointerException if <code>writer</code> is null
     */
    public JspResponseWriter(JspWriter writer) {

        if (writer == null) {
            throw new NullPointerException();
        }
        this.writer = writer;

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The <code>JspWriter</code> that we are wrapping.</p>
     */
    private JspWriter writer = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Close the underlying writer.</p>
     *
     * @exception IOException if an input/output error occurs
     */
    public void close() throws IOException {

        writer.close();

    }


    /**
     * <p>Flush the underlying writer's buffer, if any.</p>
     *
     * @exception IOException if an input/output error occurs
     */
    public void flush() throws IOException {

        writer.flush();

    }


    /**
     * <p>Write an array of characters.</p>
     *
     * @param cbuf An array of characters to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(char cbuf[]) throws IOException {

        writer.print(cbuf);

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

        writer.print(new String(cbuf, off, len));

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

        writer.print((char) (c & 0xffff));

    }


    /**
     * <p>Write a String of characters.</p>
     *
     * @param s A <code>String</code> to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(String s) throws IOException {

        writer.print(s);

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

        writer.print(s.substring(off, len));

    }


}
