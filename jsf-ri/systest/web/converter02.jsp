<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<jsp:useBean id="numConverter" class="javax.faces.convert.NumberConverter" scope="session" />
<%
   // configure the converter
   numConverter.setType("currency");
%>
<f:view>
    <h:output_text id="id1" valueRef="test1.doubleProperty" converter="<%= numConverter %>" />
</f:view>
