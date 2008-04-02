/*
 * $Id: CarDemoApplicationHandler.java,v 1.9 2003/04/08 00:40:51 jvisvanathan Exp $
 */
/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package cardemo;

import java.util.ArrayList;
import java.util.SortedMap;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.SelectItem;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;
import javax.faces.event.FacesEvent;

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
import com.sun.faces.util.Util;

public class CarDemoApplicationHandler  {
    
    public boolean processEvent(FacesContext context, FacesEvent facesEvent) {
        
      // PENDING (visvan) commented out to get a clean compile. Will be
      // fixed as part of NavigationHandler changes.  
      /*  if (!(facesEvent instanceof FormEvent) &&
        !(facesEvent instanceof CommandEvent)) {
            return true;
        }
        
        boolean returnValue = false;
        String treeId = null;
        
        // get CurrentOptionServer
        CurrentOptionServer optServer = (CurrentOptionServer)
        (Util.getValueBinding("CurrentOptionServer")).getValue(context);
       
         if (facesEvent instanceof FormEvent) {
            
            FormEvent formEvent = (FormEvent) facesEvent;
            
            
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

            // if options were chosen form the "custom" package ...

                if (optServer.getCurrentPackage().equals("custom")) {
                    optServer.setSunRoof(optServer.getSunRoofSelected());
                    optServer.setCruiseControl(optServer.getCruiseControlSelected());
                    optServer.setKeylessEntry(optServer.getKeylessEntrySelected());
                    optServer.setSecuritySystem(optServer.getSecuritySystemSelected());
                    optServer.setSkiRack(optServer.getSkiRackSelected());
                    optServer.setTowPackage(optServer.getTowPackageSelected());
                    optServer.setGps(optServer.getGpsSelected());
                }
                treeId = "/buy.jsp";
            }
            
            else if (formEvent.getCommandName().equals("finish")) {
                treeId = "/thanks.jsp";
            }
            
            returnValue = true;
            
        }
        
        else if (facesEvent instanceof CommandEvent) {
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
            context.setTree(treeFactory.getTree(context,treeId));
        } */
        
        return false;
    }
}
