/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.test.javaee6web.flowtraversalcombinations;

import java.io.Serializable;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;

@Named
@FlowScoped("flow_for_issue3863")
public class Issue3863Bean implements Serializable {
    int x = 0;
    
    public void incX(){
        x++;
    }
    
    public int getX(){
        return x;
    }
}