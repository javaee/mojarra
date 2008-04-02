<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb" %>
<html>
<f:view>
    <risb:multiFileUpload maxFileSize="10240" 
          fileHolder="#{testBean.fileHolder}" destinationUrl="#{testBean.destination}"
          width="500px" height="250px" type="full" buttonText="Custom Text!"/>
</f:view>
</html>