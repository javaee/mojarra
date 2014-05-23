package de.suedleasing.elease.service_requests.managedBeans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class IndexMB implements Serializable {
   
    public void dummyAction(){
        //no action
    }
    
    private boolean rendered = true;

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
  
    
}
