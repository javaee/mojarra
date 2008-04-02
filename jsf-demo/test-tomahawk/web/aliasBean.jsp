<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<%@include file="inc/head.inc" %>

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

<body>

<f:view>

    <p>
        Let's suppose you have a subform you use often but with different beans.<br/>
        The aliasBean allows you to design the subform with a fictive bean
        and to include it in all the pages where you use it.
        You just need to make an alias to the real bean named after the fictive bean before invoking the fictive bean.
    </p>
    <p>
	    In this example, the customerAddress bean is a managed bean, but the address bean isn't defined anywhere.<br/>
	    After the aliasBean tag, we can use #{address.*} in place of #{custommerAddress.*}, so making it possible to have
	    generic address subforms (ok, this one is a very simple form, but you get the idea).
    </p>

    <h:form>
        <h2>aliasTest1</h2>
        <t:aliasBean alias="#{holder}" value="#{aliasTest1}">
            <f:subview id="simulatedIncludedSubform1">
                <%-- The next tags could be inserted by an %@ include or jsp:include --%>
                <h:outputLabel for="name" value="Name :"/>
                <h:inputText id="name" value="#{holder.name}"/>
            </f:subview>
        </t:aliasBean>

        <h2>aliasTest2</h2>
        <t:aliasBean alias="#{holder}" value="#{aliasTest2}">
            <f:subview id="simulatedIncludedSubform2">
                <%-- The next tags could be inserted by an %@ include or jsp:include --%>
                <h:outputLabel for="name" value="Name :"/>
                <h:inputText id="name" value="#{holder.name}"/>
                <h:commandButton value="toUpperCase" action="#{holder.toUpperCase}"/>
            </f:subview>
        </t:aliasBean>

        <h2>aliasTest with fixed string</h2>
        <t:aliasBean alias="#{holder}" value="myFixedString" >
            <f:subview id="simulatedIncludedSubform3">
                <%-- The next tags could be inserted by an %@ include or jsp:include --%>
                <h:outputLabel for="string" value="Fixed value :"/>
                <h:outputText id="string" value="#{holder}"/>
            </f:subview>
        </t:aliasBean>
            
        <h2>aliasBeansScope</h2>
        <t:aliasBeansScope>
            <t:aliasBean alias="#{holder3}" value="#{aliasTest3}"/>
            <t:aliasBean alias="#{holder4}" value="#{aliasTest4}"/>
			<t:div>
				<h:outputLabel for="name3" value="Name :"/>
				<h:inputText id="name3" value="#{holder3.name}"/>
				<h:commandButton value="toUpperCase" action="#{holder3.toUpperCase}"/>
			</t:div>
			<t:div>
				<h:outputLabel for="name4" value="Name :"/>
				<h:inputText id="name4" value="#{holder4.name}"/>
				<h:commandButton value="toUpperCase" action="#{holder4.toUpperCase}"/>
			</t:div>
        </t:aliasBeansScope>

        <br/><br/>

        <h:commandButton/>
    </h:form>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
