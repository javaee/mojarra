/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.io;

import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.ObjectInputStream;

import com.sun.faces.cactus.ServletFacesTestCase;

public class TestIO extends ServletFacesTestCase {
    
    public TestIO() {
        super("TestIO");
    }


    public TestIO(String name) {
        super(name);
    }
    
    
    // ------------------------------------------------------------ Test Methods
    
    
    public void testBase64Streams() throws Exception {
        // create a string over 2048 bytes in length
        String testString = "This is a test String";
        for (int i = testString.length(); i < 6000; i++) {
            testString += 'a';
        }
        
        StringWriter writer = new StringWriter();
        Base64OutputStreamWriter sw = new Base64OutputStreamWriter(2048, writer);
        ObjectOutputStream os = new ObjectOutputStream(sw);
        os.writeObject(testString);
        os.flush();
        os.close();
        sw.finish();
        
        String encodedString = writer.toString();
        // no take the encodedString and reverse the operation
        Base64InputStream bin = new Base64InputStream(encodedString);
        ObjectInputStream input = new ObjectInputStream(bin);
        
        String result = (String) input.readObject();
        input.close();
        
        assertTrue(result != null);
        assertTrue(result.length() == testString.length());
        assertTrue(testString.equals(result));
                               
    }


} // END TestIO