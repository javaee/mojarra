<%@ page contentType="text/html" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<c:forEach var="item" items="#{input1}">
    <h:outputLabel for="inputId11" value="Label for #{item.key} below"/>
    <h:message for="inputId11" styleClass="message"/>
    <h:inputText id="inputId11" value="#{input1[item.key]}" required="true"/>
</c:forEach>

<h:inputText id="Short11" value="#{forEachBean1.longProperty}"
             required="true"/>
<h:message for="Short11" styleClass="message"/>
<h:outputLabel id="Short11Label" for="Short11"
               value="Label for shortProperty above"/>