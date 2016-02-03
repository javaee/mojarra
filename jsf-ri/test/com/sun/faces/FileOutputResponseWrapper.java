/*
 * $Id: FileOutputResponseWrapper.java,v 1.8.38.2 2007/04/27 21:28:18 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
 * @version $Id: FileOutputResponseWrapper.java,v 1.8.38.2 2007/04/27 21:28:18 ofung Exp $
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

    public FileOutputResponseWrapper(HttpServletResponse toWrap) {
        super(toWrap);
        try {
            FileOutputResponseWriter.initializeFacesResponseRoot();
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


