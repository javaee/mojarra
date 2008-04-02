<%@ page contentType="text/plain"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.el.ValueBinding"
%><%@ page import="com.sun.faces.systest.model.TestBean"
%><%

  // Instantiate a managed bean and validate property values #2

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // Acquire a ValueBinding for the bean to be created
  ValueBinding valueBinding = appl.createValueBinding("#{test2}");
  if (valueBinding == null) {
    out.println("/managed02.jsp FAILED - No ValueBinding returned");
    return;
  }

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/managed02.jsp FAILED - No FacesContext returned");
    return;
  }

  // Evaluate the value binding and check for bean creation
  Object result = valueBinding.getValue(facesContext);
  if (result == null) {
    out.println("/managed02.jsp FAILED - getValue() returned null");
    return;
  }
  if (!(result instanceof TestBean)) {
    out.println("/managed02.jsp FAILED - result of type " + result.getClass());
    return;
  }
  Object scoped = request.getAttribute("test2");
  if (scoped == null) {
    out.println("/managed02.jsp FAILED - not created in request scope");
    return;
  }
  if (!(result == scoped)) {
    out.println("/managed02.jsp FAILED - created bean not same as attribute");
    return;
  }

  // Verify the property values of the created bean
  TestBean bean = (TestBean) result;
  StringBuffer sb = new StringBuffer();
  if (bean.getBooleanProperty()) {
    sb.append("booleanProperty(" + bean.getBooleanProperty() + ")|");
  }
  if ((byte) 21 != bean.getByteProperty()) {
    sb.append("byteProperty(" + bean.getByteProperty() + ")|");
  }
  if (321.54 != bean.getDoubleProperty()) {
    sb.append("doubleProperty(" + bean.getDoubleProperty() + ")|");
  }
  if ((float) 21.43 != bean.getFloatProperty()) {
    sb.append("floatProperty(" + bean.getFloatProperty() + ")|");
  }
  if (321 != bean.getIntProperty()) {
    sb.append("intProperty(" + bean.getIntProperty() + ")|");
  }
  if (54321 != bean.getLongProperty()) {
    sb.append("longProperty(" + bean.getLongProperty() + ")|");
  }
  if ((short) 4321 != bean.getShortProperty()) {
    sb.append("shortProperty(" + bean.getShortProperty() + ")|");
  }
  if (!"New String Value".equals(bean.getStringProperty())) {
    sb.append("stringProperty(" + bean.getStringProperty() + ")|");
  }

  // Report any property errors
  String errors = sb.toString();
  if (errors.length() < 1) {
    out.println("/managed02.jsp PASSED");
  } else {
    out.println("/managed02.jsp FAILED - property value errors:  " + errors);
  }
%>
