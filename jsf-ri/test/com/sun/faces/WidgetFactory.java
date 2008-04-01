package com.sun.faces;

import javax.faces.FacesFactory;
import javax.faces.FacesException;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.util.Map;

public class WidgetFactory extends Object implements FacesFactory 
{

public static class Widget extends Object
{
    public Widget(Object obj, int inter, boolean bool, String str) {}
}

public Object newInstance(String facesName, ServletRequest req, ServletResponse res) throws FacesException
{
    throw new FacesException("Can't create WidgetFactory instance");
}

public Object newInstance(String facesName, ServletContext ctx) throws FacesException
{
    throw new FacesException("Can't create WidgetFactory instance");

}


public Object newInstance(String facesName) throws FacesException
{
    throw new FacesException("Can't create WidgetFactory instance");

}


public Object newInstance(String facesName, Map args) throws FacesException
{

    return new Widget(args.get("paramOne"), 
		      ((Integer)args.get("paramTwo")).intValue(), 
		      ((Boolean)args.get("paramThree")).booleanValue(),
		      (String) args.get("paramFour"));
}



} // end of class WidgetFactory

