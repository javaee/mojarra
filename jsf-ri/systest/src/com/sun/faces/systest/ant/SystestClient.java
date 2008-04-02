/* ========================================================================= *
 *                                                                           *
 *                 The Apache Software License,  Version 1.1                 *
 *                                                                           *
 *         Copyright (c) 1999, 2000  The Apache Software Foundation.         *
 *                           All rights reserved.                            *
 *                                                                           *
 * ========================================================================= *
 *                                                                           *
 * Redistribution and use in source and binary forms,  with or without modi- *
 * fication, are permitted provided that the following conditions are met:   *
 *                                                                           *
 * 1. Redistributions of source code  must retain the above copyright notice *
 *    notice, this list of conditions and the following disclaimer.          *
 *                                                                           *
 * 2. Redistributions  in binary  form  must  reproduce the  above copyright *
 *    notice,  this list of conditions  and the following  disclaimer in the *
 *    documentation and/or other materials provided with the distribution.   *
 *                                                                           *
 * 3. The end-user documentation  included with the redistribution,  if any, *
 *    must include the following acknowlegement:                             *
 *                                                                           *
 *       "This product includes  software developed  by the Apache  Software *
 *        Foundation <http://www.apache.org/>."                              *
 *                                                                           *
 *    Alternately, this acknowlegement may appear in the software itself, if *
 *    and wherever such third-party acknowlegements normally appear.         *
 *                                                                           *
 * 4. The names  "The  Jakarta  Project",  "Tomcat",  and  "Apache  Software *
 *    Foundation"  must not be used  to endorse or promote  products derived *
 *    from this  software without  prior  written  permission.  For  written *
 *    permission, please contact <apache@apache.org>.                        *
 *                                                                           *
 * 5. Products derived from this software may not be called "Apache" nor may *
 *    "Apache" appear in their names without prior written permission of the *
 *    Apache Software Foundation.                                            *
 *                                                                           *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES *
 * INCLUDING, BUT NOT LIMITED TO,  THE IMPLIED WARRANTIES OF MERCHANTABILITY *
 * AND FITNESS FOR  A PARTICULAR PURPOSE  ARE DISCLAIMED.  IN NO EVENT SHALL *
 * THE APACHE  SOFTWARE  FOUNDATION OR  ITS CONTRIBUTORS  BE LIABLE  FOR ANY *
 * DIRECT,  INDIRECT,   INCIDENTAL,  SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL *
 * DAMAGES (INCLUDING,  BUT NOT LIMITED TO,  PROCUREMENT OF SUBSTITUTE GOODS *
 * OR SERVICES;  LOSS OF USE,  DATA,  OR PROFITS;  OR BUSINESS INTERRUPTION) *
 * HOWEVER CAUSED AND  ON ANY  THEORY  OF  LIABILITY,  WHETHER IN  CONTRACT, *
 * STRICT LIABILITY, OR TORT  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN *
 * ANY  WAY  OUT OF  THE  USE OF  THIS  SOFTWARE,  EVEN  IF  ADVISED  OF THE *
 * POSSIBILITY OF SUCH DAMAGE.                                               *
 *                                                                           *
 * ========================================================================= *
 *                                                                           *
 * This software  consists of voluntary  contributions made  by many indivi- *
 * duals on behalf of the  Apache Software Foundation.  For more information *
 * on the Apache Software Foundation, please see <http://www.apache.org/>.   *
 *                                                                           *
 * ========================================================================= */

package com.sun.faces.systest.ant;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * <p>This class contains a <strong>Task</strong> for Ant that is used to
 * send HTTP requests to a servlet container, and examine the responses.
 * It is similar in purpose to the <code>GTest</code> task in Watchdog,
 * but uses the JDK's HttpURLConnection for underlying connectivity.</p>
 *
 * <p>The task is registered with Ant using a <code>taskdef</code> directive:
 * <pre>
 *   &lt;taskdef name="systest"
 *       classname="com.sun.faces.systest.ant.SystestClient"&gt;
 * </pre>
 * and accepts the following configuration properties:</p>
 * <ul>
 * <li><strong>golden</strong> - The server-relative path of the static
 *     resource containing the golden file for this request.</li>
 * <li><strong>host</strong> - The server name to which this request will be
 *     sent.  Defaults to <code>localhost</code> if not specified.</li>
 * <li><strong>ignore</strong> - The server-relative path of the static
 *     resource containing lines from the specified golden file that should
 *     not be matched against the actual response.  If a golden file is
 *     specified but not an ignore file, then the contents must match
 *     exactly.</li>
 * <li><strong>inContent</strong> - The data content that will be submitted
 *     with this request.  The test client will transparently add a carriage
 *     return and line feed, and set the content length header, if this is
 *     specified.  Otherwise, no content will be included in the request.</li>
 * <li><strong>inHeaders</strong> - The set of one or more HTTP headers that
 *     will be included on the request, in the format
 *     <code>{name}:{value}{##{name}:{value}...</li>
 * <li><strong>joinSession</strong> - Should we join the session whose session
 *     identifier was returned on the previous request.  [false]</li>
 * <li><strong>message</strong> - The HTTP response message that is expected
 *     in the response from the server.  No check is made if no message
 *     is specified.</li>
 * <li><strong>method</strong> - The HTTP request method to be used on this
 *     request.  Defaults to <ocde>GET</code> if not specified.</li>
 * <li><strong>outContent</strong> - The first line of the response data
 *     content that we expect to receive.  No check is made if no content is
 *     specified.</li>
 * <li><strong>outHeaders</strong> - The set of one or more HTTP headers that
 *     are expected in the response (order independent).</li>
 * <li><strong>port</strong> - The port number to which this request will be
 *     sent.  Defaults to <code>8080</code> if not specified.</li>
 * <li><strong>protocol</strong> - The protocol and version (such as
 *     "HTTP/1.0") to include in the request, if executed as a direct
 *     socket connection.  If not specified, HttpURLConnection will be used
 *     instead.</li>
 * <li><strong>redirect</strong> - If set to true, follow any redirect that
 *     is returned by the server.  (Only works when using HttpURLConnection).
 *     </li>
 * <li><strong>request</strong> - The request URI to be transmitted for this
 *     request.  This value should start with a slash character ("/"), and
 *     be the server-relative URI of the requested resource.</li>
 * <li><strong>status</strong> - The HTTP status code that is expected in the
 *     response from the server.  Defaults to <code>200</code> if not
 *     specified.  Set to zero to disable checking the return value.</li>
 * </ul>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2003/05/13 00:18:57 $
 */

public class SystestClient extends Task {


    // ----------------------------------------------------- Instance Variables


    /**
     * The <code>Log</code> instance for this class.
     */
    protected static final Log log = LogFactory.getLog(SystestClient.class);


    /**
     * The saved golden file we will compare to the response.  Each element
     * contains a line of text without any line delimiters.
     */
    protected List saveGolden = new ArrayList();


    /**
     * The saved headers we received in our response.  The key is the header
     * name (converted to lower case), and the value is an ArrayList of the
     * string value(s) received for that header.
     */
    protected Map saveHeaders = new HashMap();


    /**
     * The saved ignore lines for modifying our golden file comparison to the
     * response.  Each element contains a line of text without any line
     * delimiters.
     */
    protected List saveIgnore = new ArrayList();


    /**
     * The response file to be compared to the golden file.  Each element
     * contains a line of text without any line delimiters.
     */
    protected List saveResponse = new ArrayList();


    // ------------------------------------------------------------- Properties


    /**
     * The server-relative request URI of the golden file for this request.
     */
    protected String golden = null;

    public String getGolden() {
        return (this.golden);
    }

    public void setGolden(String golden) {
        this.golden = golden;
    }


    /**
     * The host name to which we will connect.
     */
    protected String host = "localhost";

    public String getHost() {
        return (this.host);
    }

    public void setHost(String host) {
        this.host = host;
    }


    /**
     * The server-relative request URI of the ignore file for this request.
     */
    protected String ignore = null;

    public String getIgnore() {
        return (this.ignore);
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }


    /**
     * The first line of the request data that will be included on this
     * request.
     */
    protected String inContent = null;

    public String getInContent() {
        return (this.inContent);
    }

    public void setInContent(String inContent) {
        this.inContent = inContent;
    }


    /**
     * The HTTP headers to be included on the request.  Syntax is
     * <code>{name}:{value}[##{name}:{value}] ...</code>.
     */
    protected String inHeaders = null;

    public String getInHeaders() {
        return (this.inHeaders);
    }

    public void setInHeaders(String inHeaders) {
        this.inHeaders = inHeaders;
    }


    /**
     * Should we join the session whose session identifier was returned
     * on the previous request.
     */
    protected boolean joinSession = false;

    public boolean getJoinSession() {
        return (this.joinSession);
    }

    public void setJoinSession(boolean joinSession) {
        this.joinSession = true;
    }


    /**
     * The HTTP response message to be expected in the response.
     */
    protected String message = null;

    public String getMessage() {
        return (this.message);
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * The HTTP request method that will be used.
     */
    protected String method = "GET";

    public String getMethod() {
        return (this.method);
    }

    public void setMethod(String method) {
        this.method = method;
    }


    /**
     * The first line of the response data content that we expect to receive.
     */
    protected String outContent = null;

    public String getOutContent() {
        return (this.outContent);
    }

    public void setOutContent(String outContent) {
        this.outContent = outContent;
    }


    /**
     * The HTTP headers to be checked on the response.  Syntax is
     * <code>{name}:{value}[##{name}:{value}] ...</code>.
     */
    protected String outHeaders = null;

    public String getOutHeaders() {
        return (this.outHeaders);
    }

    public void setOutHeaders(String outHeaders) {
        this.outHeaders = outHeaders;
    }


    /**
     * The port number to which we will connect.
     */
    protected int port = 8080;

    public int getPort() {
        return (this.port);
    }

    public void setPort(int port) {
        this.port = port;
    }


    /**
     * The protocol and version to include in the request, if executed as
     * a direct socket connection.  Lack of a value here indicates that an
     * HttpURLConnection should be used instead.
     */
    protected String protocol = null;

    public String getProtocol() {
        return (this.protocol);
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    /**
     * Should we follow redirects returned by the server?
     */
    protected boolean redirect = false;

    public boolean getRedirect() {
        return (this.redirect);
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }


    /**
     * The request URI to be sent to the server.  This value is required.
     */
    protected String request = null;

    public String getRequest() {
        return (this.request);
    }

    public void setRequest(String request) {
        this.request = request;
    }


    /**
     * The HTTP status code expected on the response.
     */
    protected int status = 200;

    public int getStatus() {
        return (this.status);
    }

    public void setStatus(int status) {
        this.status = status;
    }


    // ------------------------------------------------------- Static Variables


    /**
     * The session identifier returned by the most recent request, or
     * <code>null</code> if the previous request did not specify a session
     * identifier.
     */
    protected static String sessionId = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Execute the test that has been configured by our property settings.
     *
     * @exception BuildException if an exception occurs
     */
    public void execute() throws BuildException {

        saveHeaders.clear();
        try {
            readGolden();
        } catch (IOException e) {
            System.out.println("FAIL:  readGolden(" + golden + ")");
            e.printStackTrace(System.out);
        }
        try {
            readIgnore();
        } catch (IOException e) {
            System.out.println("FAIL:  readIgnore(" + ignore + ")");
            e.printStackTrace(System.out);
        }
        if ((protocol == null) || (protocol.length() == 0)) {
            executeHttp();
        } else {
            executeSocket();
        }

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Execute the test via use of an HttpURLConnection.
     *
     * @exception BuildException if an exception occurs
     */
    protected void executeHttp() throws BuildException {

        // Construct a summary of the request we will be sending
        String summary = "[" + method + " " + request + "]";
        boolean success = true;
        String result = null;
        Throwable throwable = null;
        HttpURLConnection conn = null;

        try {

            // Configure an HttpURLConnection for this request
            if (log.isDebugEnabled()) {
                log.debug("Configuring HttpURLConnection for this request");
            }
            URL url = new URL("http", host, port, request);
            conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setDoInput(true);
            if (inContent != null) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length",
                                        "" + inContent.length());
                if (log.isTraceEnabled()) {
                    log.trace("INPH: Content-Length: " + inContent.length());
                }
            } else {
                conn.setDoOutput(false);
            }

            // Send the session id cookie (if any)
            if (joinSession && (sessionId != null)) {
                conn.setRequestProperty("Cookie",
                                        "JSESSIONID=" + sessionId);
                if (log.isTraceEnabled()) {
                    log.trace("INPH: Cookie: JSESSIONID=" + sessionId);
                }
            }

            if (this.redirect && log.isTraceEnabled()) {
                log.trace("FLAG: setInstanceFollowRedirects(" +
                          this.redirect + ")");
            }
            conn.setInstanceFollowRedirects(this.redirect);
            conn.setRequestMethod(method);
            if (inHeaders != null) {
                String headers = inHeaders;
                while (headers.length() > 0) {
                    int delimiter = headers.indexOf("##");
                    String header = null;
                    if (delimiter < 0) {
                        header = headers;
                        headers = "";
                    } else {
                        header = headers.substring(0, delimiter);
                        headers = headers.substring(delimiter + 2);
                    }
                    int colon = header.indexOf(":");
                    if (colon < 0)
                        break;
                    String name = header.substring(0, colon).trim();
                    String value = header.substring(colon + 1).trim();
                    conn.setRequestProperty(name, value);
                    if (log.isTraceEnabled()) {
                        log.trace("INPH: " + name + ": " + value);
                    }
                }
            }

            // Connect to the server and send our output if necessary
            conn.connect();
            if (inContent != null) {
                if (log.isTraceEnabled()) {
                    log.trace("INPD: " + inContent);
                }
                OutputStream os = conn.getOutputStream();
                for (int i = 0; i < inContent.length(); i++)
                    os.write(inContent.charAt(i));
                os.close();
            }

            // Acquire the response data, if there is any
            String outData = "";
            String outText = "";
            boolean eol = false;
            InputStream is = conn.getInputStream();
            int lines = 0;
            while (true) {
                String line = read(is);
                if (line == null)
                    break;
                if (lines == 0)
                    outData = line;
                else
                    outText += line + "\r\n";
                saveResponse.add(line);
                lines++;
            }
            is.close();

            // Dump out the response stuff
            if (log.isTraceEnabled()) {
                log.trace("RESP: " + conn.getResponseCode() + " " +
                          conn.getResponseMessage());
            }
            for (int i = 1; i < 1000; i++) {
                String name = conn.getHeaderFieldKey(i);
                String value = conn.getHeaderField(i);
                if ((name == null) || (value == null))
                    break;
                if (log.isTraceEnabled()) {
                    log.trace("HEAD: " + name + ": " + value);
                }
                save(name, value);
                if ("Set-Cookie".equals(name))
                    parseSession(value);
            }
            if (log.isTraceEnabled()) {
                log.trace("DATA: " + outData);
                if (outText.length() > 2) {
                    log.trace("TEXT: " + outText);
                }
            }

            // Validate the response against our criteria
            if (success) {
                result = validateStatus(conn.getResponseCode());
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateMessage(conn.getResponseMessage());
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateHeaders();
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateData(outData);
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateGolden();
                if (result != null)
                    success = false;
            }

        } catch (Throwable t) {
            if (t instanceof FileNotFoundException) {
                if (status == 404) {
                    success = true;
                    result = "Not Found";
                    throwable = null;
                } else {
                    success = false;
                    try {
                        result = "Status=" + conn.getResponseCode() +
                            ", Message=" + conn.getResponseMessage();
                    } catch (IOException e) {
                        result = e.toString();
                    }
                    throwable = null;
                }
            } else if (t instanceof ConnectException) {
                success = false;
                result = t.getMessage();
                throwable = null;
            } else {
                success = false;
                result = t.getMessage();
                throwable = t;
            }
        }

        // Log the results of executing this request
        if (success) {
            System.out.println("OK   " + summary);
        } else {
            System.out.println("FAIL " + summary + " " + result);
            if (throwable != null)
                throwable.printStackTrace(System.out);
        }

    }


    /**
     * Execute the test via use of a socket with direct input/output.
     *
     * @exception BuildException if an exception occurs
     */
    protected void executeSocket() throws BuildException {

        // Construct a summary of the request we will be sending
        String command = method + " " + request + " " + protocol;
        String summary = "[" + command + "]";
        if (log.isDebugEnabled()) {
            log.debug("RQST: " + summary);
        }
        boolean success = true;
        String result = null;
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        Throwable throwable = null;
        int outStatus = 0;
        String outMessage = null;

        try {

            // Open a client socket for this request
            socket = new Socket(host, port);
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            is = socket.getInputStream();

            // Send the command and content length header (if any)
            pw.print(command + "\r\n");
            if (inContent != null) {
                if (log.isTraceEnabled()) {
                    log.trace("INPH: " + "Content-Length: " +
                              inContent.length());
                }
                pw.print("Content-Length: " + inContent.length() + "\r\n");
            }

            // Send the session id cookie (if any)
            if (joinSession && (sessionId != null)) {
                pw.println("Cookie: JSESSIONID=" + sessionId);
                if (log.isTraceEnabled()) {
                    log.trace("INPH: Cookie: JSESSIONID=" +
                              sessionId);
                }
            }

            // Send the specified headers (if any)
            if (inHeaders != null) {
                String headers = inHeaders;
                while (headers.length() > 0) {
                    int delimiter = headers.indexOf("##");
                    String header = null;
                    if (delimiter < 0) {
                        header = headers;
                        headers = "";
                    } else {
                        header = headers.substring(0, delimiter);
                        headers = headers.substring(delimiter + 2);
                    }
                    int colon = header.indexOf(":");
                    if (colon < 0)
                        break;
                    String name = header.substring(0, colon).trim();
                    String value = header.substring(colon + 1).trim();
                    if (log.isTraceEnabled()) {
                        log.trace("INPH: " + name + ": " + value);
                    }
                    pw.print(name + ": " + value + "\r\n");
                }
            }
            pw.print("\r\n");

            // Send our content (if any)
            if (inContent != null) {
                if (log.isTraceEnabled()) {
                    log.trace("INPD: " + inContent);
                }
                for (int i = 0; i < inContent.length(); i++)
                    pw.print(inContent.charAt(i));
            }
            pw.flush();

            // Read the response status and associated message
            String line = read(is);
            if (line == null) {
                outStatus = -1;
                outMessage = "NO RESPONSE";
            } else {
                line = line.trim();
                if (log.isTraceEnabled()) {
                    log.trace("RESP: " + line);
                }
                int space = line.indexOf(" ");
                if (space >= 0) {
                    line = line.substring(space + 1).trim();
                    space = line.indexOf(" ");
                }
                try {
                    if (space < 0) {
                        outStatus = Integer.parseInt(line);
                        outMessage = "";
                    } else {
                        outStatus = Integer.parseInt(line.substring(0, space));
                        outMessage = line.substring(space + 1).trim();
                    }
                } catch (NumberFormatException e) {
                    outStatus = -1;
                    outMessage = "NUMBER FORMAT EXCEPTION";
                }
            }
            if (log.isTraceEnabled()) {
                log.trace("STAT: " + outStatus + " MESG: " + outMessage);
            }

            // Read the response headers (if any)
            String headerName = null;
            String headerValue = null;
            while (true) {
                line = read(is);
                if ((line == null) || (line.length() == 0))
                    break;
                int colon = line.indexOf(":");
                if (colon < 0) {
                    if (log.isTraceEnabled()) {
                        log.trace("????: " + line);
                    }
                } else {
                    headerName = line.substring(0, colon).trim();
                    headerValue = line.substring(colon + 1).trim();
                    if (log.isTraceEnabled()) {
                        log.trace("HEAD: " + headerName + ": " +
                                  headerValue);
                    }
                    save(headerName, headerValue);
                    if ("Set-Cookie".equals(headerName))
                        parseSession(headerValue);
                }
            }

            // Acquire the response data (if any)
            String outData = "";
            String outText = "";
            int lines = 0;
            while (true) {
                line = read(is);
                if (line == null)
                    break;                
                if (lines == 0)
                    outData = line;
                else
                    outText += line + "\r\n";
                saveResponse.add(line);
                lines++;
            }
            is.close();
            if (log.isTraceEnabled()) {
                log.trace("DATA: " + outData);
                if (outText.length() > 2) {
                    log.trace("TEXT: " + outText);
                }
            }

            // Validate the response against our criteria
            if (success) {
                result = validateStatus(outStatus);
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateMessage(message);
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateHeaders();
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateData(outData);
                if (result != null)
                    success = false;
            }
            if (success) {
                result = validateGolden();
                if (result != null)
                    success = false;
            }

        } catch (Throwable t) {
            success = false;
            result = "Status=" + outStatus +
                ", Message=" + outMessage;
            throwable = null;
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Throwable w) {
                    ;
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Throwable w) {
                    ;
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable w) {
                    ;
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Throwable w) {
                    ;
                }
            }
        }

        if (success) {
            System.out.println("OK   " + summary);
        } else {
            System.out.println("FAIL " + summary + " " + result);
            if (throwable != null)
                throwable.printStackTrace(System.out);
        }

    }


    /**
     * Parse the session identifier from the specified Set-Cookie value.
     *
     * @param value The Set-Cookie value to parse
     */
    protected void parseSession(String value) {

        if (value == null) {
            return;
        }
        int equals = value.indexOf("JSESSIONID=");
        if (equals < 0) {
            return;
        }
        value = value.substring(equals + "JSESSIONID=".length());
        int semi = value.indexOf(";");
        if (semi >= 0) {
            value = value.substring(0, semi);
        }
        if (log.isTraceEnabled()) {
            log.trace("S ID: " + value);
        }
        sessionId = value;

    }


    /**
     * Read and return the next line from the specified input stream, with
     * no carriage return or line feed delimiters.  If
     * end of file is reached, return <code>null</code> instead.
     *
     * @param stream The input stream to read from
     *
     * @exception IOException if an input/output error occurs
     */
    protected String read(InputStream stream) throws IOException {

        StringBuffer result = new StringBuffer();
        while (true) {
            int b = stream.read();
            if (b < 0) {
                if (result.length() == 0) {
                    return (null);
                } else {
                    break;
                }
            }
            char c = (char) b;
            if (c == '\r') {
                continue;
            } else if (c == '\n') {
                break;
            } else {
                result.append(c);
            }
        }
        return (result.toString());

    }


    /**
     * Read and save the contents of the golden file for this test, if any.
     * Otherwise, the <code>saveGolden</code> list will be empty.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void readGolden() throws IOException {

        // Was a golden file specified?
        saveGolden.clear();
        if (golden == null) {
            return;
        }

        // Create a connection to receive the golden file contents
        URL url = new URL("http", host, port, golden);
        HttpURLConnection conn =
            (HttpURLConnection) url.openConnection();
        conn.setAllowUserInteraction(false);
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setFollowRedirects(true);
        conn.setRequestMethod("GET");

        // Connect to the server and retrieve the golden file
        conn.connect();
        InputStream is = conn.getInputStream();
        while (true) {
            String line = read(is);
            if (line == null) {
                break;
            }
            saveGolden.add(line);
        }
        is.close();
        conn.disconnect();

    }


    /**
     * Read and save the contents of the ignore file for this test, if any.
     * Otherwise, the <code>saveIgnore</code> list will be empty.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void readIgnore() throws IOException {

        // Was an ignore file specified?
        saveIgnore.clear();
        if (ignore == null) {
            return;
        }

        // Create a connection to receive the ignore file contents
        URL url = new URL("http", host, port, ignore);
        HttpURLConnection conn =
            (HttpURLConnection) url.openConnection();
        conn.setAllowUserInteraction(false);
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setFollowRedirects(true);
        conn.setRequestMethod("GET");

        // Connect to the server and retrieve the ignore file
        conn.connect();
        InputStream is = conn.getInputStream();
        while (true) {
            String line = read(is);
            if (line == null) {
                break;
            }
            saveIgnore.add(line);
        }
        is.close();
        conn.disconnect();

    }


    /**
     * Save the specified header name and value in our collection.
     *
     * @param name Header name to save
     * @param value Header value to save
     */
    protected void save(String name, String value) {

        String key = name.toLowerCase();
        ArrayList list = (ArrayList) saveHeaders.get(key);
        if (list == null) {
            list = new ArrayList();
            saveHeaders.put(key, list);
        }
        list.add(value);

    }


    /**
     * Validate the output data against what we expected.  Return
     * <code>null</code> for no problems, or an error message.
     *
     * @param data The output data to be tested
     */
    protected String validateData(String data) {

        if (outContent == null) {
            return (null);
        } else if (data.startsWith(outContent)) {
            return (null);
        } else {
            return ("Expected data '" + outContent + "', got data '" +
                    data + "'");
        }

    }


    /**
     * Validate the response against the golden file (if any), skipping the
     * comparison on any golden file line that is also in the ignore file
     * (if any).  Return <code>null</code> for no problems, or an error
     * message.
     */
    protected String validateGolden() {

        if (golden == null) {
            return (null);
        }
        boolean ok = true;
        if (saveGolden.size() != saveResponse.size()) {
            ok = false;
        }
        if (ok) {
            for (int i = 0; i < saveGolden.size(); i++) {
                String golden = (String) saveGolden.get(i);
                String response = (String) saveResponse.get(i);
                if (!validateIgnore(golden) && !golden.equals(response)) {
                    ok = false;
                    break;
                }
            }
        }
        if (ok) {
            return (null);
        }
        System.out.println("EXPECTED: ======================================");
        for (int i = 0; i < saveGolden.size(); i++) {
            System.out.println((String) saveGolden.get(i));
        }
        System.out.println("================================================");
        if (saveIgnore.size() >= 1) {
            System.out.println("IGNORED: =======================================");
            for (int i = 0; i < saveIgnore.size(); i++) {
                System.out.println((String) saveIgnore.get(i));
            }
            System.out.println("================================================");
        }
        System.out.println("RECEIVED: ======================================");
        for (int i = 0; i < saveResponse.size(); i++) {
            System.out.println((String) saveResponse.get(i));
        }
        System.out.println("================================================");
        return ("Failed Golden File Comparison");

    }


    /**
     * Validate the saved headers against the <code>outHeaders</code>
     * property, and return an error message if there is anything missing.
     * If all of the expected headers are present, return <code>null</code>.
     */
    protected String validateHeaders() {

        // Do we have any headers to check for?
        if (outHeaders == null) {
            return (null);
        }

        // Check each specified name:value combination
        String headers = outHeaders;
        while (headers.length() > 0) {
            // Parse the next name:value combination
            int delimiter = headers.indexOf("##");
            String header = null;
            if (delimiter < 0) {
                header = headers;
                headers = "";
            } else {
                header = headers.substring(0, delimiter);
                headers = headers.substring(delimiter + 2);
            }
            int colon = header.indexOf(":");
            String name = header.substring(0, colon).trim();
            String value = header.substring(colon + 1).trim();
            // Check for the occurrence of this header
            ArrayList list = (ArrayList) saveHeaders.get(name.toLowerCase());
            if (list == null) {
                return ("Missing header name '" + name + "'");
            }
            boolean found = false;
            for (int i = 0; i < list.size(); i++) {
                if (value.equals((String) list.get(i))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return ("Missing header name '" + name + "' with value '" +
                        value + "'");
            }
        }

        // Everything was found successfully
        return (null);

    }


    /**
     * Return <code>true</code> if we should ignore this golden file line
     * because it is also in the ignore file.
     *
     * @param line Line from the golden file to be checked
     */
    protected boolean validateIgnore(String line) {

        for (int i = 0; i < saveIgnore.size(); i++) {
            String ignore = (String) saveIgnore.get(i);
            if (ignore.equals(line)) {
                return (true);
            }
        }
        return (false);

    }


    /**
     * Validate the returned response message against what we expected.
     * Return <code>null</code> for no problems, or an error message.
     *
     * @param message The returned response message
     */
    protected String validateMessage(String message) {

        if (this.message == null) {
            return (null);
        } else if (this.message.equals(message)) {
            return (null);
        } else {
            return ("Expected message='" + this.message + "', got message='" +
                    message + "'");
        }

    }


    /**
     * Validate the returned status code against what we expected.  Return
     * <code>null</code> for no problems, or an error message.
     *
     * @param status The returned status code
     */
    protected String validateStatus(int status) {

        if (this.status == 0) {
            return (null);
        }
        if (this.status == status) {
            return (null);
        } else {
            return ("Expected status=" + this.status + ", got status=" +
                    status);
        }

    }


}
