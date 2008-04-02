/*
 * $Id: MockServlet.java,v 1.4 2005/08/22 22:08:25 ofung Exp $
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
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * <p>Mock <strong>Servlet</strong> for unit tests.</p>
 */

public class MockServlet implements Servlet {


    public MockServlet() {
    }


    public MockServlet(ServletConfig config) throws ServletException {
        init(config);
    }


    private ServletConfig config;


    public void destroy() {
    }


    public ServletConfig getServletConfig() {
        return (this.config);
    }


    public String getServletInfo() {
        return ("MockServlet");
    }


    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }



    public void service(ServletRequest request, ServletResponse response)
        throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }


}
