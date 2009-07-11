package com.sun.faces.composite;

import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

@FacesComponent(value = "resourceDependencyComponent")
@ResourceDependency(name="simple.css")
public class ResourceDependencyComponent extends UINamingContainer {


}
