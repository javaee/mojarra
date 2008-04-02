/*
 * $Id: MockServletConfig.java,v 1.4 2005/08/22 22:08:26 ofung Exp $
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

package javax.faces.mock;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


// Mock Object for ServletConfig (version 2.3)
public class MockServletConfig implements ServletConfig {


    public MockServletConfig() {
    }


    public MockServletConfig(ServletContext context) {
        setServletContext(context);
    }


    private Hashtable parameters = new Hashtable();
    private ServletContext context;


    // --------------------------------------------------------- Public Methods


    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }


    public void setServletContext(ServletContext context) {
        this.context = context;
    }


    // -------------------------------------------------- ServletConfig Methods


    public String getInitParameter(String name) {
        return ((String) parameters.get(name));
    }


    public Enumeration getInitParameterNames() {
        return (parameters.keys());
    }


    public ServletContext getServletContext() {
        return (this.context);
    }


    public String getServletName() {
        return ("MockServlet");
    }


}
