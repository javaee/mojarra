<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <f:view>
            <h:form>

<p>This selectOneRadio is bound to a list of test.Payment instances.  There
is a converter-for-class registered for the test.Payment class.  This
will cause the EL coerceToType to be called to coerce the value from a
String to a test.Payment instance.  The EL uses JavaBeans PropertyEditor
instances to do this. </p>

<p>This test verifies that a custom converter-for-class converter is
called by the EL coerceToType via the ConverterPropertyEditor class in
Sun's JSF Impl.</p>

       <p>         <h:selectOneRadio value="#{testBean.payment}">
                    <f:selectItem itemLabel="cc1" itemValue="1"/>
                    <f:selectItem itemLabel="cc2" itemValue="2"/>
                </h:selectOneRadio></p>

<p>       <h:selectOneRadio value="#{testBean2.payment}">
          <f:selectItem itemLabel="cc3" itemValue="3"/>
          <f:selectItem itemLabel="cc4" itemValue="4"/>
       </h:selectOneRadio></p>

<p>Messages: <h:messages /> </p>

                <p><h:commandButton id="press" value="submit" /></p>
            </h:form>
        </f:view>
    </body>
</html>
