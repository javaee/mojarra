<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
    <html>
    <head>
        <title>input01</title>
    </head>

    <body>

    <h:form id="form">

        <table border="1">

            <tr><td>Message: <h:message for="form:input"/></td></tr>

            <tr><td>Clear this value and press reload: <h:inputText id="input"
                                                                    value="#{test3.intProperty}"/></td>
            </tr>

        </table>

        <p><h:commandButton value="reload"/></p>

    </h:form>

    </body>
    </html>
</f:view>
