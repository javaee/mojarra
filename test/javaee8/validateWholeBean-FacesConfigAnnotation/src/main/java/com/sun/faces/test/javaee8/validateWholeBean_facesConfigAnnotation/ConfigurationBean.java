package com.sun.faces.test.javaee8.validateWholeBean_facesConfigAnnotation;

import static javax.faces.annotation.FacesConfig.Version.JSF_2_3;

import javax.faces.annotation.FacesConfig;

@FacesConfig(
	// Activates CDI build-in beans
	version = JSF_2_3,
        values = { FacesConfig.ContextParameter.ENABLE_VALIDATE_WHOLE_BEAN }
)
public class ConfigurationBean {

}
