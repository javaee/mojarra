<%@ page import="org.apache.myfaces.custom.tree.DefaultMutableTreeNode,
                 org.apache.myfaces.custom.tree.model.DefaultTreeModel"%>
<%@ page session="true" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<%@include file="inc/head.inc" %>

<body>
<%
   if (pageContext.getAttribute("treeModel", PageContext.SESSION_SCOPE) == null) {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("XY");
      DefaultMutableTreeNode a = new DefaultMutableTreeNode("A");
      root.insert(a);
      DefaultMutableTreeNode b = new DefaultMutableTreeNode("B");
      root.insert(b);
      DefaultMutableTreeNode c = new DefaultMutableTreeNode("C");
      root.insert(c);

      DefaultMutableTreeNode node = new DefaultMutableTreeNode("a1");
      a.insert(node);
      node = new DefaultMutableTreeNode("a2 ");
      a.insert(node);
      node = new DefaultMutableTreeNode("b ");
      b.insert(node);

      a = node;
      node = new DefaultMutableTreeNode("x1");
      a.insert(node);
      node = new DefaultMutableTreeNode("x2");
      a.insert(node);

      pageContext.setAttribute("treeModel", new DefaultTreeModel(root), PageContext.SESSION_SCOPE);
   }
%>
<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <h:panelGroup id="body">
        <h:form id="selection">
                <h:selectOneMenu id="selone_menu_panel" value="#{stackState.selected}" styleClass="selectOneMenu" onchange="document.forms['selection'].submit();">
                    <f:selectItem itemValue="treePanel" itemLabel="#{example_messages['treePanel']}" />
                    <f:selectItem itemValue="selectBoxPanel" itemLabel="#{example_messages['selectBoxPanel']}" />
                </h:selectOneMenu>
        </h:form>

        <t:panelStack id="stack" selectedPanel="#{stackState.selected}">
        <h:panelGroup id="treePanel">
            <t:tree id="tree" value="#{treeModel}"
                    styleClass="tree"
                    nodeClass="treenode"
                    selectedNodeClass="treenodeSelected"
                    expandRoot="true">
            </t:tree>
            <f:verbatim><br></f:verbatim>
        </h:panelGroup>
        <h:panelGroup id="treePanel">
            <t:tree id="tree" value="#{treeModel}"
                    styleClass="tree"
                    nodeClass="treenode"
                    selectedNodeClass="treenodeSelected"
                    expandRoot="true">
            </t:tree>
            <f:verbatim><br></f:verbatim>
        </h:panelGroup>

    <h:panelGroup id="selectBoxPanel">

        <t:messages id="messageList" styleClass="error" showDetail="true" summaryFormat="{0} " />

        <h:form name="formName">

            <h:panelGrid columns="2">

                <h:outputLabel for="selone_lb" value="#{example_messages['label_cars']}" />
                <h:selectOneListbox id="selone_lb" size="3" value="#{carconf.car}"
                                    validator="#{carconf.validateCar}" styleClass="selectOneListbox">
                    <f:selectItems id="selone_lb_cars" value="#{carconf.cars}" />
                </h:selectOneListbox>

                <h:outputLabel for="selone_menu_colors" value="#{example_messages['label_colors']}" />
                <h:selectOneMenu id="selone_menu_colors" value="#{carconf.color}" styleClass="selectOneMenu">
                    <f:selectItem itemValue="" itemLabel="#{example_messages['empty_selitem']}" />
                    <f:selectItems value="#{carconf.colors}" />
                </h:selectOneMenu>

                <h:outputLabel for="selone_menu_extras" value="#{example_messages['label_extras']}" />
                <h:selectManyCheckbox id="selone_menu_extras" value="#{carconf.extras}" layout="pageDirection" styleClass="selectManyCheckbox">
                    <f:selectItems value="#{carconf.extrasList}" />
                </h:selectManyCheckbox>

                <f:verbatim>&nbsp;</f:verbatim>
                <h:panelGroup >
                    <h:selectOneRadio id="r1" value="#{carconf.discount}" layout="pageDirection"  styleClass="selectOneRadio">
                        <f:selectItem itemValue="1" itemLabel="#{example_messages['discount_0']}" />
                        <f:selectItem itemValue="2" itemLabel="#{example_messages['discount_1']}"  />
                        <f:selectItem itemValue="3" itemLabel="#{example_messages['discount_2']}"  />
                    </h:selectOneRadio>
                </h:panelGroup>

                <h:panelGroup ></h:panelGroup>
                <h:panelGroup ></h:panelGroup>

                <f:verbatim>&nbsp;</f:verbatim>
                <h:panelGrid columns="1"  >
                    <%/* x:selectOneRadio is an extension of h:selectOneRadio
                           if layout="spread" selectitems wont get rendered
                           instead x:radio components are getting rendered
                    */%>
                    <t:selectOneRadio id="discount2" value="#{carconf.discount2}" layout="spread" styleClass="selectOneRadio">
                        <f:selectItem itemValue="0" itemLabel="#{example_messages['discount_2_0']}" />
                        <f:selectItem itemValue="1" itemLabel="#{example_messages['discount_2_1']}" />
                        <f:selectItem itemValue="2" itemLabel="#{example_messages['discount_2_2']}" />
                    </t:selectOneRadio>
                    <h:panelGroup>
                        <t:radio for="discount2" index="2" /><f:verbatim>&nbsp;</f:verbatim>
                        <h:inputText value="#{carconf.bandName}" />
                    </h:panelGroup>
                    <%/* x:radio is a myfaces extension. renders the selectItem at the
                         given index (starting with 0). the for attribute must be the id
                         of the corresponding x:selectOneRadio */%>
                    <t:radio for="discount2" index="0" />
                    <t:radio for="discount2" index="1" />

                </h:panelGrid>

                <h:outputLabel for="doors" value="#{example_messages['doors']}" />
                <h:selectOneMenu id="doors" value="#{carconf.doors}">
                        <f:selectItem itemValue="2" itemLabel="2" />
                        <f:selectItem itemValue="4" itemLabel="4" />
                        <f:selectItem itemValue="7" itemLabel="7" />
                </h:selectOneMenu>

                <h:outputLabel for="selboolean" value="#{example_messages['sales_tax']}" />
                <h:selectBooleanCheckbox id="selboolean" value="#{carconf.salesTax}"/>

                <f:verbatim>&nbsp;</f:verbatim>
                <h:commandButton action="#{carconf.calcPrice}" value="#{example_messages['button_calcprice']}" />

            </h:panelGrid>
        </h:form>

        <h:outputFormat value="#{example_messages['msg_price']}" >
            <f:param value="#{carconf.price}" />
        </h:outputFormat>

    </h:panelGroup>


        </t:panelStack>

    </h:panelGroup>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
