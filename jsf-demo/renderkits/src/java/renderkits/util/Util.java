package renderkits.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Util extends Object {
                                                                                                               
    //
    // Private/Protected Constants
    //
    public static final String FACES_LOGGER = "javax.enterprise.resource.jsf.";
                                                                                                               
    public static final String FACES_LOG_STRINGS =
            "com.sun.faces.LogStrings";
                                                                                                               
    // Log instance for this class
    private static Logger logger;
    static {
        logger = getLogger(FACES_LOGGER);
    }

    public static Logger getLogger( String loggerName ) {
        return Logger.getLogger(loggerName, FACES_LOG_STRINGS );
    }
    
    //----------------------------------------------------------
    // The following is used to verify encodings
    //----------------------------------------------------------
    //
    static public void validateEncoding(String encoding)
        throws UnsupportedEncodingException {
        if (encoding != null) {
            // Try creating a string off of the default encoding
            new String(encodingTestBytes, encoding);
        }
    }
    
    // Private array used simply to verify character encodings
    static private final byte[] encodingTestBytes = new byte[]{(byte) 65};
    
    /**
     * Write a string attribute.  Note that this code
     * is duplicated below for character arrays - change both
     * places if you make any changes!!!
     */
    static public void writeAttribute(Writer out,
                                      char[] buff,
                                      String text) throws IOException {
        int buffLength = buff.length;
        int buffIndex = 0;

        int length = text.length();
        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);
            
            // Tilde or less...
            if (ch < 0xA0) {
                // If "?" or over, no escaping is needed (this covers
                // most of the Latin alphabet)
                if (ch >= 0x3f) {
                    buffIndex = addToBuffer(out, buff, buffIndex,
                                            buffLength, ch);
                } else if (ch >= 0x27) { // If above "'"...
                    // If between "'" and ";", no escaping is needed
                    if (ch < 0x3c) {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                        // Note - "<" isn't escaped in attributes, as per
                        // HTML spec
                    } else if (ch == '>') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&gt;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                } else {
                    if (ch == '&') {
                        buffIndex = flushBuffer(out, buff, buffIndex);
                        
                        // HTML 4.0, section B.7.1: ampersands followed by
                        // an open brace don't get escaped
                        if ((i + 1 < length) && (text.charAt(i + 1) == '{'))
                            out.write(ch);
                        else
                            out.write("&amp;");
                    } else if (ch == '"') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&quot;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                }
            } else {
                buffIndex = flushBuffer(out, buff, buffIndex);
                
                // Double-byte characters to encode.
                // PENDING: when outputting to an encoding that
                // supports double-byte characters (UTF-8, for example),
                // we should not be encoding
                _writeDecRef(out, ch);
            }
        }

        flushBuffer(out, buff, buffIndex);
    }
    
    static public void writeAttribute(Writer out,
                                      char[] buffer,
                                      char[] text) throws IOException {
        writeAttribute(out, buffer, text, 0, text.length);
    }
    
    /**
     * Write a character array attribute.  Note that this code
     * is duplicated above for string - change both places if you make
     * any changes!!!
     */
    static public void writeAttribute(Writer out,
                                      char[] buff,
                                      char[] text,
                                      int start,
                                      int length) throws IOException {
        int buffLength = buff.length;
        int buffIndex = 0;

        int end = start + length;
        for (int i = start; i < end; i++) {
            char ch = text[i];

            // Tilde or less...
            if (ch < 0xA0) {
                // If "?" or over, no escaping is needed (this covers
                // most of the Latin alphabet)
                if (ch >= 0x3f) {
                    buffIndex = addToBuffer(out, buff, buffIndex,
                                            buffLength, ch);
                } else if (ch >= 0x27) { // If above "'"...
                    if (ch < 0x3c) {
                        // If between "'" and ";", no escaping is needed
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                        // Note - "<" isn't escaped in attributes, as per HTML spec
                    } else if (ch == '>') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&gt;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                } else {
                    if (ch == '&') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        // HTML 4.0, section B.7.1: ampersands followed by
                        // an open brace don't get escaped
                        if ((i + 1 < end) && (text[i + 1] == '{'))
                            out.write(ch);
                        else
                            out.write("&amp;");
                    } else if (ch == '"') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&quot;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                }
            } else {
                buffIndex = flushBuffer(out, buff, buffIndex);
                
                // Double-byte characters to encode.
                // PENDING: when outputting to an encoding that
                // supports double-byte characters (UTF-8, for example),
                // we should not be encoding
                _writeDecRef(out, ch);
            }
        }

        flushBuffer(out, buff, buffIndex);
    }
    
    //-------------------------------------------------
    // The following methods include the handling of
    // escape characters....
    //-------------------------------------------------

    static public void writeText(Writer out,
                                 char[] buffer,
                                 char[] text) throws IOException {
        writeText(out, buffer, text, 0, text.length);
    }


    /**
     * Write char array text.  Note that this code is duplicated below
     * for Strings - change both places if you make any changes!!!
     */
    static public void writeText(Writer out,
                                 char[] buff,
                                 char[] text,
                                 int start,
                                 int length) throws IOException {
        int buffLength = buff.length;
        int buffIndex = 0;

        int end = start + length;
        for (int i = start; i < end; i++) {
            char ch = text[i];
            
            // Tilde or less...
            if (ch < 0xA0) {
                // If "?" or over, no escaping is needed (this covers
                // most of the Latin alphabet)
                if (ch >= 0x3f) {
                    buffIndex = addToBuffer(out, buff, buffIndex,
                                            buffLength, ch);
                } else if (ch >= 0x27) { // If above "'"...
                    // If between "'" and ";", no escaping is needed
                    if (ch < 0x3c) {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    } else if (ch == '<') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&lt;");
                    } else if (ch == '>') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&gt;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                } else {
                    if (ch == '&') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&amp;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                }
            } else {
                // Double-byte characters to encode.
                // PENDING: when outputting to an encoding that
                // supports double-byte characters (UTF-8, for example),
                // we should not be encoding
                buffIndex = flushBuffer(out, buff, buffIndex);
                _writeDecRef(out, ch);
            }
        }

        flushBuffer(out, buff, buffIndex);
    }


    /**
     * Write String text.  Note that this code is duplicated above for
     * character arrays - change both places if you make any changes!!!
     */
    static public void writeText(Writer out,
                                 char[] buff,
                                 String text) throws IOException {
        int buffLength = buff.length;
        int buffIndex = 0;

        int length = text.length();

        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);
            
            // Tilde or less...
            if (ch < 0xA0) {
                // If "?" or over, no escaping is needed (this covers
                // most of the Latin alphabet)
                if (ch >= 0x3f) {
                    buffIndex = addToBuffer(out, buff, buffIndex,
                                            buffLength, ch);
                } else if (ch >= 0x27) {  // If above "'"...
                    // If between "'" and ";", no escaping is needed
                    if (ch < 0x3c) {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    } else if (ch == '<') {
                        buffIndex = flushBuffer(out, buff, buffIndex);
                        out.write("&lt;");
                    } else if (ch == '>') {
                        buffIndex = flushBuffer(out, buff, buffIndex);
                        out.write("&gt;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                } else {
                    if (ch == '&') {
                        buffIndex = flushBuffer(out, buff, buffIndex);

                        out.write("&amp;");
                    } else {
                        buffIndex = addToBuffer(out, buff, buffIndex,
                                                buffLength, ch);
                    }
                }
            } else {
                // Double-byte characters to encode.
                // PENDING: when outputting to an encoding that
                // supports double-byte characters (UTF-8, for example),
                // we should not be encoding
                buffIndex = flushBuffer(out, buff, buffIndex);
                _writeDecRef(out, ch);
            }
        }

        flushBuffer(out, buff, buffIndex);
    }
    
    /**
     * Writes a character as a decimal escape.  Hex escapes are smaller than
     * the decimal version, but Netscape didn't support hex escapes until
     * 4.7.4.
     */
    static private void _writeDecRef(Writer out, char ch) throws IOException {
        if (ch == '\u20ac') {
            out.write("&euro;");
            return;
        }
        out.write("&#");
        // Formerly used String.valueOf().  This version tests out
        // about 40% faster in a microbenchmark (and on systems where GC is
        // going gonzo, it should be even better)
        int i = (int) ch;
        if (i > 10000) {
            out.write('0' + (i / 10000));
            i = i % 10000;
            out.write('0' + (i / 1000));
            i = i % 1000;
            out.write('0' + (i / 100));
            i = i % 100;
            out.write('0' + (i / 10));
            i = i % 10;
            out.write('0' + i);
        } else if (i > 1000) {
            out.write('0' + (i / 1000));
            i = i % 1000;
            out.write('0' + (i / 100));
            i = i % 100;
            out.write('0' + (i / 10));
            i = i % 10;
            out.write('0' + i);
        } else {
            out.write('0' + (i / 100));
            i = i % 100;
            out.write('0' + (i / 10));
            i = i % 10;
            out.write('0' + i);
        }

        out.write(';');
    }

    /**
     * Flush the contents of the buffer to the output stream
     * and return the reset buffer index
     */
    private static int flushBuffer(Writer out,
                                   char[] buffer,
                                   int bufferIndex) throws IOException {
        if (bufferIndex > 0)
            out.write(buffer, 0, bufferIndex);

        return 0;
    }

    /**
     * Add a character to the buffer, flushing the buffer if the buffer is
     * full, and returning the new buffer index
     */
    private static int addToBuffer(Writer out,
                                   char[] buffer,
                                   int bufferIndex,
                                   int bufferLength,
                                   char ch) throws IOException {
        if (bufferIndex >= bufferLength) {
            out.write(buffer, 0, bufferIndex);
            bufferIndex = 0;
        }

        buffer[bufferIndex] = ch;

        return bufferIndex + 1;
    }
    
    
}

