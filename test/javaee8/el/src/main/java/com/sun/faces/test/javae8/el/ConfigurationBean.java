package com.sun.faces.test.javae8.el;

import static javax.faces.annotation.FacesConfig.Version.JSF_2_3;

import javax.faces.annotation.FacesConfig;

@FacesConfig(
	// Activates CDI build-in beans that provide the EL resolving (naming) this project
    // tests for
	version = JSF_2_3 
)
public class ConfigurationBean {

}
