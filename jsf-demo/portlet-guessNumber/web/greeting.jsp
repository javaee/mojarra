<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/portlet/components" prefix="p" %>

    <f:view>
    <%-- Embed JSF tags with in portletPage tag if you expect multiple instances
          of this portlet to exist within a portal page --%>
    <p:portletPage>
    <h:form id="helloForm" >
      Hi. My name is Duke.  I'm thinking of a number from
      <h:outputText value="#{UserNumberBean.minimum}"/> to
      <h:outputText value="#{UserNumberBean.maximum}"/>.  Can you guess
      it?

        <h:graphicImage id="waveImg" url="/wave.med.gif" />
  	<h:inputText id="userNo" value="#{UserNumberBean.userNumber}"
                      validator="#{UserNumberBean.validate}"/>          
	 <h:commandButton id="submit" action="success" value="Submit" />
         
	 <h:message style="color: red; font-family: 'New Century Schoolbook', serif; font-style: oblique; text-decoration: overline" id="errors1" for="userNo"/>

    </h:form>
    </p:portletPage>
    </f:view>
 
