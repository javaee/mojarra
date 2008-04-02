/*
* Copyright 2004 The Apache Software Foundation
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package webtiersample;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

/**
 * This <code>ServletContextListener</code> will install the 
 * {@link ColorELResolver}.
 * 
 * @author Mark Roth
 */
public class InstallColorListener implements ServletContextListener {
    
    public void contextInitialized(ServletContextEvent evt) {
        
        ServletContext context = evt.getServletContext();
                      
        // Register the ColorELResolver with the JspApplicationContext.
        JspApplicationContext jspContext =
            JspFactory.getDefaultFactory().getJspApplicationContext(context);
        jspContext.addELResolver(new ColorELResolver());                

    }

    public void contextDestroyed(ServletContextEvent evt) {
    }
    
}
