
This demo is designed to show you how to create a custom component written in Java with JSF 2.

It includes the changes in web.xml needed to add the custom-taglib.xml file:

     <context-param>
        <param-name>javax.faces.FACELETS_LIBRARIES</param-name>
        <param-value>/WEB-INF/custom-taglib.xml</param-value>
    </context-param>

 The custom-taglib.xml itself is very simple, and only contains a single tag:

    <namespace>http://javaserverfaces.dev.java.net/demo/custom-taglib</namespace>
    <tag>
        <tag-name>adiv</tag-name>
        <component>
            <component-type>ajaxawarediv</component-type>
        </component>
    </tag>

 The namespace definition in the above file is referred to in the using page's namespace, like this:

 <html xmlns="http://www.w3.org/1999/xhtml"
       xmlns:h="http://java.sun.com/jsf/html"
       xmlns:f="http://java.sun.com/jsf/core"
       xmlns:cu="http://javaserverfaces.dev.java.net/demo/custom-taglib">

Then, you use that namespace to specify the tag:

            <cu:adiv id="customId">
                <f:ajax render="eventcount" listener="#{data.updateEventCount}"/>
            </cu:adiv>

Then, you need to name the Component to be the same as the component-type, above:

@FacesComponent(value = "ajaxawarediv")
public class AjaxAwareDiv extends UIComponentBase implements ClientBehaviorHolder {


We've also added the ability for the component to detect the f:ajax tag, and act on it.
Most of the code in AjaxAwareDiv.java is concerned with f:ajax detection.

