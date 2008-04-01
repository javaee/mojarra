/*
 * $Id: ServletResponseWriter.java,v 1.1 2002/06/05 03:01:56 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.ResponseWriter;


/**
 * <p><strong>ServletResponseWriter</strong> is a concrete implementation of
 * {@link ResponseWriter} that wraps a <code>PrintWriter</code> passed to its
 * constructor.</p>
 *
 * <p><strong>FIXME</strong> - Should this implementation be in jsf-api?</p>
 */

public final class ServletResponseWriter extends ResponseWriter {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a <code>ServletResponseWriter</code> that wraps the specified
     * <code>PrintWriter</code>.</p>
     *
     * @param writer The <code>PrintWriter</code> to be wrapped
     *
     * @exception NullPointerException if <code>writer</code> is null
     */
    public ServletResponseWriter(PrintWriter writer) {

        if (writer == null) {
            throw new NullPointerException();
        }
        this.writer = writer;

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The <code>PrintWriter</code> that we are wrapping.</p>
     */
    private PrintWriter writer = null;


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

        writer.write(cbuf);

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

        writer.write(cbuf, off, len);

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

        writer.write(c);

    }


    /**
     * <p>Write a String of characters.</p>
     *
     * @param s A <code>String</code> to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(String s) throws IOException {

        writer.write(s);

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

        writer.write(s, off, len);

    }


}
