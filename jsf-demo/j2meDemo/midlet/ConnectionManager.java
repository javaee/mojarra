import java.io.IOException; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.UnsupportedEncodingException; 
import java.util.Enumeration; 
import java.util.Hashtable; 

import javax.microedition.io.Connector; 
import javax.microedition.io.HttpConnection; 
import javax.microedition.midlet.MIDlet;

/**
 * Class used to managed Http connection activity - used 
 * by MIDlets to communicate to a server.
 *
 * Thanks to web resources for much of this code.
 */

public class ConnectionManager implements Runnable { 
    private String url; 
    private String urlPrefix = "http://localhost:8080/jsf-j2meDemo/"; 
    private Hashtable request; 
    private Hashtable response; 
    private String sessionCookie; 
    private boolean busy = false; 
    private AbstractMIDlet midlet = null;

    public ConnectionManager(AbstractMIDlet midlet) {
        this.midlet = midlet;
    }

    public synchronized void run() { 
        try { 
            for (;;) { 
                while (!busy) 
                wait(); 
                try { 
                    byte[] data = post(); 
                    response = decode(data); 
                } catch (IOException ex) { 
                    ex.printStackTrace(); 
                } 
                busy = false; 
                midlet.connectionCompleted(response); 
            } 
        } catch (InterruptedException ie) {} 
    } 

    public synchronized void connect(String url, Hashtable request) { 
        this.url = url; 
        this.request = request; 
        if (busy) return; 
        busy = true; 
        notify(); 
    } 
    private void urlEncode(String s, OutputStream out) 
        throws IOException { 
        byte[] bytes = s.getBytes("UTF8"); 
        for (int i = 0; i < bytes.length; i++) { 
            byte b = bytes[i]; 
            if (b == ' ') { 
                out.write('+'); 
            } else if ('0' <= b && b <= '9' 
                || 'A' <= b && b <= 'Z' 
                || 'a' <= b && b <= 'z' 
                || "-_.!~*'(),".indexOf(b) >= 0) { 
                out.write(b); 
            } else { 
                out.write('%'); 
                int b1 = (b & 0xF0) >> 4; 
                out.write((b1 < 10 ? '0' : 'a' - 10) + b1); 
                int b2 = b & 0xF; 
                out.write((b2 < 10 ? '0' : 'a' - 10) + b2); 
            } 
        } 
    } 

    private boolean isspace(byte b) { 
        return " \n\r\t".indexOf(b) >= 0; 
    } 

    private Hashtable decode(byte[] data) { 
        if (data == null) return null; 
        Hashtable table = new Hashtable(); 
        try { 
            int start = 0; 
            for (;;) { 
                while (start < data.length && isspace(data[start])) 
                    start++; 
                if (start >= data.length) return table; 
                int end = start + 1; 
                int count = 0; 
                while (end < data.length && data[end] != '=') end++; 
                String key = new String(data, start, end - start, "ASCII"); 
                start = end + 1; 
                end = start; 
                while (end < data.length && !isspace(data[end])) { 
                    count++; 
                    if (data[end] == '%') end += 3; else end++; 
                } 
                byte[] b = new byte[count]; 
                int k = start; 
                int c = 0; 
                while (k < end) { 
                    if (data[k] == '%') { 
                        int h = data[k + 1]; 
                        if (h >= 'a') h = h - 'a' + 10; 
                        else if (h >= 'A') h = h - 'A' + 10; 
                        else h = h - '0'; 
                        int l = data[k + 2]; 
                        if (l >= 'a') l = l - 'a' + 10; 
                        else if (l >= 'A') l = l - 'A' + 10; 
                        else l = l - '0'; 
                        b[c] = (byte) ((h << 4) + l); 
                        k += 3; 
                    } else if (data[k] == '+') { 
                        b[c] = ' '; 
                        k++; 
                    } else {
                        b[c] = data[k]; 
                        k++; 
                    } 
                    c++; 
                } 
                String value = new String(b, "UTF8"); 
                table.put(key, value); 
                start = end + 1; 
            } 
        } catch (UnsupportedEncodingException ex) { 
        }
        return table; 
    } 

    private byte[] post() throws IOException { 
        HttpConnection conn = null; 
        byte[] data = null; 
        try { 
            conn = (HttpConnection) Connector.open(urlPrefix + url); 
            conn.setRequestMethod(HttpConnection.POST); 
            conn.setRequestProperty("User-Agent", 
                "Profile/MIDP-2.0 Configuration/CLDC-1.0"); 
            conn.setRequestProperty("Content-Language", "en-US"); 
            conn.setRequestProperty("Content-Type", 
                "application/x-www-form-urlencoded"); 
            if (sessionCookie != null) 
                conn.setRequestProperty("Cookie", sessionCookie); 
                OutputStream out = conn.openOutputStream(); 
                if (request != null) { 
                    Enumeration keys = request.keys(); 
                    while (keys.hasMoreElements()) { 
                        String key = (String) keys.nextElement(); 
                        String value = (String) request.get(key); 
                        urlEncode(key, out); 
                        out.write('='); 
                        urlEncode(value, out); 
                        if (keys.hasMoreElements()) out.write('&'); 
                    } 
                } 
                int rc = conn.getResponseCode(); 
                if (rc != HttpConnection.HTTP_OK) 
                    throw new IOException("HTTP response code: " + rc); 
                InputStream in = conn.openInputStream(); 
                // Read the session ID--it's the first cookie 
                String cookie = conn.getHeaderField("Set-cookie"); 
                if (cookie != null) { 
                    int semicolon = cookie.indexOf(';'); 
                    sessionCookie = cookie.substring(0, semicolon); 
                } 
                // Get the length and process the data 
                int len = (int) conn.getLength(); 
                int actual = 0; 
                int bytesread = 0 ; 
                if (len > 0) { 
                    data = new byte[len]; 
                    while ((bytesread != len) && (actual != -1)) { 
                        actual = in.read(data, bytesread, len - bytesread); 
                        if (actual != -1) bytesread += actual; 
                    } 
                } else { 
                    final int BLOCKSIZE = 1024; 
                    data = new byte[BLOCKSIZE]; 
                    while (actual != -1) { 
                        if (bytesread == data.length) { 
                            byte[] bigger = new byte[data.length + BLOCKSIZE]; 
                            System.arraycopy(data, 0, bigger, 0, data.length); 
                            data = bigger; 
                        } 
                        actual = in.read(data, bytesread, 
                        data.length - bytesread); 
                        if (actual != -1) bytesread += actual; 
                    } 
                    if (bytesread < data.length) { 
                    byte[] smaller = new byte[bytesread]; 
                    System.arraycopy(data, 0, smaller, 0, bytesread); 
                    data = smaller; 
                } 
            } 
        } catch (ClassCastException e) { 
            throw new IOException("Not an HTTP URL"); 
        } finally { 
            if (conn != null) conn.close(); 
        } 
        return data; 
    } 
} 

