package com.tavve.webapps.nested;

import java.util.Vector;
import java.io.Serializable;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

public class TestBean implements Serializable {
    
    Vector _services = new Vector();
    
    public TestBean() {
        
        System.err.println("Constructing a TestBean");
        
        Service service1 = new Service("Service 1");
	//        service1.addPort(new Port("80"));
        
        _services.addElement(service1);
        
        Service service2 = new Service("Service 2");
        //service1.addPort(new Port("90"));
        
        _services.addElement(service2);
    }
    
    public Vector getServices() {
        return _services;
    }
    
    public void setServices(Vector services) {
        _services = services;
    }
    
    public String addService() {
        
        System.err.println("addService");
        
		_services.add(new Service("New Service"));
        
        return "OK";
    }
    
    public String deleteService() {
        
        System.err.println("deleteService");
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
		Service service = (Service)facesContext.getExternalContext().getRequestMap().get("service");
				
        _services.remove(service);
        
        return "OK";
    }

    public String addPortNumber() {
        
        System.err.println("addPortNumber");
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Service service = (Service)facesContext.getExternalContext().getRequestMap().get("service");
        
        service.addPort(new Port());

        return "OK";
    }
    
    public String deletePortNumber() {
        
        System.err.println("deletePortNumber");
        
        FacesContext facesContext = FacesContext.getCurrentInstance();				
        Service service = (Service)facesContext.getExternalContext().getRequestMap().get("service");
        Port port = (Port)facesContext.getExternalContext().getRequestMap().get("portNumber");
            
        service.deletePort(port);
        
        return "OK";
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

}
