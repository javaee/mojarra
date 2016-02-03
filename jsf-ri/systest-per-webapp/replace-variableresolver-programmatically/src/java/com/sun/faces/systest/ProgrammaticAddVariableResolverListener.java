/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest;

import javax.faces.application.Application;
import javax.faces.el.VariableResolver;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author edburns
 */
public class ProgrammaticAddVariableResolverListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce) {

    }

    public void contextInitialized(ServletContextEvent sce) {
        Application app = null;
        ServletContext context = sce.getServletContext();
        app = (Application) context.getAttribute("com.sun.faces.ApplicationImpl");
        VariableResolver oldVr = app.getVariableResolver();
        VariableResolver newVr = new NewVariableResolver(oldVr, context);
        app.setVariableResolver(newVr);

    }



}
