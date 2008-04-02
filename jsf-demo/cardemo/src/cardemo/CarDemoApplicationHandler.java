/*
 * $Id: CarDemoApplicationHandler.java,v 1.1 2002/11/02 01:34:57 jball Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;

import java.util.SortedMap;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.event.FormEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.CommandEvent;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.util.Properties;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.RIConstants;

public class CarDemoApplicationHandler implements ApplicationHandler{
    
    public boolean processEvent(FacesContext context, FacesEvent facesEvent) {
        
        System.out.println("CarDemoAppHandler processEvent() reached");
        
        if (!(facesEvent instanceof FormEvent) &&
        !(facesEvent instanceof CommandEvent)) {
            return true;
        }
        
        boolean returnValue = false;
        String treeId = null;
        
        // get CurrentOptionServer
        CurrentOptionServer optServer = (CurrentOptionServer)(context.getHttpSession().getAttribute("CurrentOptionServer"));
        
        if (facesEvent instanceof FormEvent) {
            
            FormEvent formEvent = (FormEvent) facesEvent;
            System.out.println("FormEvent queued");
            
            
            if (formEvent.getCommandName().equals("more1")) {
                optServer.setCarId(1);
                treeId = "/more.jsp";
            }
            
            else if (formEvent.getCommandName().equals("more2")) {
                optServer.setCarId(2);
                treeId = "/more.jsp";
            }
            
            else if (formEvent.getCommandName().equals("more3")) {
                
                optServer.setCarId(3);
                treeId = "/more.jsp";
            }
            
            else if (formEvent.getCommandName().equals("more4")) {
                optServer.setCarId(4);
                treeId = "/more.jsp";
            }
            
            else if (formEvent.getCommandName().equals("customer")) {
                treeId = "/Customer.jsp";
            }
            
            else if (formEvent.getCommandName().equals("buy")) {
                treeId = "/buy.jsp";
            }
            
            else if (formEvent.getCommandName().equals("finish")) {
                treeId = "/thanks.jsp";
            }
            
            else if (formEvent.getCommandName().equals("more")) {
                treeId = "/more.jsp";
            }
            
            returnValue = true;
            
        }
        
        else if (facesEvent instanceof CommandEvent) {
            System.out.println("CommandEvent received");
            CommandEvent commandEvent = (CommandEvent)facesEvent;
            UIComponent c = commandEvent.getComponent();
            if (c.getAttribute("target") != null) {
                treeId = (String)c.getAttribute("target");
                returnValue = true;
            }
        }
        
        if (null != treeId) {
            TreeFactory treeFactory = (TreeFactory)
            FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            context.setResponseTree(treeFactory.getTree(context,treeId));
        }
        
        return returnValue;
    }
}
