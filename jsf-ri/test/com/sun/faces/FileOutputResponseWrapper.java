/*
 * $Id: FileOutputResponseWrapper.java,v 1.10 2005/08/22 22:11:05 ofung Exp $
 */

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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// FileOutputResponseWrapper.java

package com.sun.faces;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The sole purpose of <B>FileOutputResponseWrapper</B> is to wrap an
 * ServletResponse and change its writer object  so that
 * output can be directed to a file.  <P>
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FileOutputResponseWrapper.java,v 1.10 2005/08/22 22:11:05 ofung Exp $
 */

public class FileOutputResponseWrapper extends HttpServletResponseWrapper {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    protected PrintWriter out = null;
    public static String FACES_RESPONSE_FILENAME = "FacesResponse.txt";

// Attribute Instance Variables


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public FileOutputResponseWrapper(HttpServletResponse toWrap, 
            String testRootDir) {
        super(toWrap);
        try {
            FileOutputResponseWriter.initializeFacesResponseRoot(testRootDir);
            File file = new File(FACES_RESPONSE_FILENAME);
            FileOutputStream fs = new FileOutputStream(file);
            out = new PrintWriter(fs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void flushBuffer() throws IOException {
        out.flush();
        out.close();
    }


//
// Class methods
//

//
// Methods from ServletResponse 
//

    public PrintWriter getWriter() {
        return out;
    }


} // end of class FileOutputResponseWrapper


