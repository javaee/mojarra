package com.sun.faces.test.javaee8.cdi;

import static javax.faces.annotation.FacesConfig.Version.JSF_2_3;

import javax.faces.annotation.FacesConfig;

@FacesConfig(
	// Activates CDI build-in beans that provide the injection this project
    // tests for
	version = JSF_2_3 
)
public class ConfigurationBean {

}
