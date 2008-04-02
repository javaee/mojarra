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

package com.tavve.webapps.nested;

import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

public class TestBean implements Serializable {


    Vector _services = new Vector();

    // ------------------------------------------------------------ Constructors


    public TestBean() {

        System.err.println("Constructing a TestBean");

        Service service1 = new Service("Service 1");
        //        service1.addPort(new Port("80"));

        _services.addElement(service1);

        Service service2 = new Service("Service 2");
        //service1.addPort(new Port("90"));

        _services.addElement(service2);

    }

    // ---------------------------------------------------------- Public Methods


    public String addPortNumber() {

        System.err.println("addPortNumber");

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Service service = (Service) facesContext.getExternalContext()
              .getRequestMap().get("service");

        service.addPort(new Port());

        return "OK";

    }


    public String addService() {

        System.err.println("addService");

        _services.add(new Service("New Service"));

        return "OK";

    }


    public String deletePortNumber() {

        System.err.println("deletePortNumber");

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Service service = (Service) facesContext.getExternalContext()
              .getRequestMap().get("service");
        Port port = (Port) facesContext.getExternalContext().getRequestMap()
              .get("portNumber");

        service.deletePort(port);

        return "OK";

    }


    public String deleteService() {

        System.err.println("deleteService");

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Service service = (Service) facesContext.getExternalContext()
              .getRequestMap().get("service");

        _services.remove(service);

        return "OK";

    }


    public String getCurrentStateTable() {

        StringBuffer out = new StringBuffer();
        Iterator inner, outer = _services.iterator();
        Service curService;
        Port curPort;

        out.append("<table border=\"1\">\n");
        while (outer.hasNext()) {
            curService = (Service) outer.next();
            out.append("  <tr>\n");
            inner = curService.getPorts().iterator();
            out.append("<td>service: " + curService.getName() + "</td>");
            while (inner.hasNext()) {
                curPort = (Port) inner.next();
                out.append(" <td>port: " + curPort.getPortNumber() + "</td>\n");
            }
            out.append("</tr>\n");
        }
        out.append("</table>\n");
        return out.toString();

    }


    public Vector getServices() {

        return _services;

    }


    public String printTree() {

        Iterator inner, outer = _services.iterator();
        Service curService;
        Port curPort;
        while (outer.hasNext()) {
            curService = (Service) outer.next();
            System.out.println("service: " + curService + " " +
                               curService.getName());
            inner = curService.getPorts().iterator();
            while (inner.hasNext()) {
                curPort = (Port) inner.next();
                System.out.println("\tport: " + curPort + " " +
                                   curPort.getPortNumber());
            }
        }
        return null;

    }


    public void setServices(Vector services) {

        _services = services;

    }

}
