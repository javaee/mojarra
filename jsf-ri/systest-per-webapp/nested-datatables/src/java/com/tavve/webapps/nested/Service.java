package com.tavve.webapps.nested;

import java.util.Vector;
import java.io.Serializable;

public class Service implements Serializable {
    
    String _name;
    Vector _ports = new Vector();
    
    public Service() {
        _name="";
    }
    
    public Service(String name) {
        _name = name;
    }
    
    public void setName(String name) {
        _name = name;
    }
    
    public String getName() {
        return _name;
    }
    
    public Vector getPorts() {
        return _ports;
    }
    
    public void setPorts(Vector ports) {
        _ports = ports;
    }

    public void addPort(Port port) {
        _ports.addElement(port);
    }
    
    public void deletePort(Port port) {
        _ports.remove(port);
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof Service)) {
            return false;
        }
        
        String otherName = ((Service)o).getName();
        
        return _name == null ? (otherName == null) : _name.equals(otherName);
    }
}

