<%@ page contentType="text/html" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<jsp:useBean id="numConverter" class="javax.faces.convert.NumberConverter" scope="session" />
<%
   // configure the converter
   numConverter.setLocale(java.util.Locale.US);
   numConverter.setType("currency");
%>
<f:view>
    <h:outputText id="id1" value="#{test1.doubleProperty}" converter="#{numConverter}" />
</f:view>
