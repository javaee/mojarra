package com.sun.faces.regression.i_spec_758_simple_war;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


/**
 * @author <a href="http://community.jboss.org/people/bleathem)">Brian Leathem</a>
 */
@RequestScoped
@ManagedBean
public class ViewActionTestBean {
    public String action() {
        return "/result";
    }
}
