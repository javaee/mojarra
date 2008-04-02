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

