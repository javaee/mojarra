<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
    <h:form id="form1">
        <table>
            <tr>
                <td>
                    <h:panelGrid id="panelGrid" styleClass="scrollPane"
                                 columns="1">
                        <h:panelGroup id="panelGroup">
                            <f:verbatim><DIV STYLE="overflow: auto; height:
                                100px;"></f:verbatim>
                            <h:outputText id="outputtext"
                                          value="An output text"/>
                            <f:verbatim></DIV></f:verbatim>
                        </h:panelGroup>
                    </h:panelGrid>
                </td>
            </tr>
            <tr>
                <td>
                    <h:commandButton id="submit" value="submit"
                                     action="success"/>
                </td>
            </tr>
        </table>
    </h:form>
</f:view>
</html>

