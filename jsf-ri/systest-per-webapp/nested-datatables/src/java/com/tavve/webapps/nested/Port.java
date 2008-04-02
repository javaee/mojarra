package com.tavve.webapps.nested;

import java.io.Serializable;

public class Port implements Serializable {
    
    String _portNumber = "0";
    
    public Port() {
    }
    
    public Port(String portNumber) {
        _portNumber = portNumber;
    }
    
    public void setPortNumber(String portNumber) {
        _portNumber = portNumber;
    }
    
    public String getPortNumber() {
        return _portNumber;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Port)) {
            return false;
        }
        
        String otherPortNumber = ((Port)o).getPortNumber();
        
        return _portNumber == null ? (otherPortNumber == null) : _portNumber.equals(otherPortNumber);
    }
}

