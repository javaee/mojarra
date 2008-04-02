<html>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<f:view>
    <head>
        <title><h:outputText id="title" value="title"/></title>
    </head>

    <body>
    <h:form id="form">
        <h:panelGrid id="panel1" columns="2" styleClass="book"
                     columnClasses="menuColumn, chapterColumn">

            <f:verbatim>
                verbatim one text here
            </f:verbatim>

            <h:panelGrid id="panel2" columns="1">
                <h:outputText id="outputheader" value="this is the header"/>
                <f:verbatim><hr/></f:verbatim>
            </h:panelGrid>

            <h:commandButton id="submit" value="submit"/>

            <f:verbatim>
                verbatim two text here
            </f:verbatim>

        </h:panelGrid>
    </h:form>
    </body>
</f:view>
</html>
