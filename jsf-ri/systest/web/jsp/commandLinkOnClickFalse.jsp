<HTML>
<body onLoad="initValue('form')">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<jsp:useBean id="TestBean" class="com.sun.faces.systest.model.TestBean"
             scope="session"/>
<f:view>
    <script type="text/javascript" language="Javascript">
        <!--
        function setValue(curFormName) {
            var curForm = document.forms[curFormName];
            curForm.elements['form:init'].value = "Hello";
        }
        //-->
    </script>
    <script type="text/javascript" language="Javascript">
        <!--
        function initValue(curFormName) {
            var curForm = document.forms[curFormName];
            curForm.elements['form:_idcl'].value = "Goodbye";
        }
        //-->
    </script>

    <h:form id="form">
        <table>
            <tr>
                <td><h:outputText value="Value:"/></td>
                <td><h:inputText id="init" value="initial value"/></td>
            </tr>
        </table>
        <h:commandLink id="submit" onclick="setValue('form'); return false;"
                       value="submit"/>
    </h:form>
</f:view>
</body>
</HTML>
