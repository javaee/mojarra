/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.test.glassfish.undeploy.undeploy2;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "countBean")
@RequestScoped
public class CountBean {

    /**
     * Get the count.
     *
     * @return the count.
     */
    public Integer getCount() {
        Integer result = 0;

        try {
            ConcurrentHashMap threadInitContext;
            Field threadMap = FacesContext.class.getDeclaredField("threadInitContext");
            threadMap.setAccessible(true);
            threadInitContext = (ConcurrentHashMap) threadMap.get(null);

            if (threadInitContext != null) {
                result += threadInitContext.size();
            }

            ConcurrentHashMap initContextServletContext;
            Field initContextMap = FacesContext.class.getDeclaredField("initContextServletContext");
            initContextMap.setAccessible(true);
            initContextServletContext = (ConcurrentHashMap) initContextMap.get(null);
            
            if (initContextServletContext != null) {
                result += initContextServletContext.size();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
