<%@ page contentType="text/html"
%><%@ page import="javax.faces.component.UIInput" 
%><%@ page import="javax.faces.context.FacesContext" 
%><%@ page import="javax.faces.convert.ConverterException"
%><%@ page import="javax.faces.convert.EnumConverter"
%><%@ page import="com.sun.faces.systest.model.EnumBean"
%><%

  // Test - no targetClass Exception
  EnumConverter enumConverter = new EnumConverter();
  UIInput input = new UIInput();
  input.setId("myInput");
  String msg = null;
  try {
      Object obj = enumConverter.getAsObject(FacesContext.getCurrentInstance(), input, "foo");
  } catch (ConverterException ce) {
      msg = ce.getMessage();
  }
  if (msg.equals("myInput: 'foo' must be convertible to an enum from the enum, but no enum class provided.")) {
      out.println("/enum-converter-1.jsp PASSED");
  } else {
      out.println("/enum-converter-1.jsp FAILED");
  }
      
  try {
      String str = enumConverter.getAsString(FacesContext.getCurrentInstance(), input, "bar");
  } catch (ConverterException ce) {
      msg = ce.getMessage();
  }
  if (msg.equals("myInput: 'bar' must be convertible to an enum from the enum, but no enum class provided.")) {
      out.println("/enum-converter-1.jsp PASSED");
  } else {
      out.println("/enum-converter-1.jsp FAILED");
  }

  // Test Valid Enum member
  try {
      enumConverter = new EnumConverter(EnumBean.Simple.class);
      String str = enumConverter.getAsString(FacesContext.getCurrentInstance(), input, EnumBean.Simple.Value2);  
      out.println("/enum-converter-1.jsp PASSED");
  } catch (ConverterException ce) {
      out.println("/enum-converter-1.jsp FAILED");
  }

  // Test Invalid Enum member
  try {
      enumConverter = new EnumConverter(EnumBean.Simple.class);
      String str = enumConverter.getAsString(FacesContext.getCurrentInstance(), input, "FOO");  
      out.println("/enum-converter-1.jsp FAILED");
  } catch (ConverterException ce) {
      out.println("/enum-converter-1.jsp PASSED");
  }
%>
