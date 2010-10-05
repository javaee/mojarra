<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Managed Bean Lifecycle Annotations</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Managed Bean Lifecycle Annotations</h1>

<f:view>

  <h:form id="form">
  
  <h:outputText value="#{noneBean.appendRequestMarker}" />

  <p>requestBean PostConstruct: <h:outputText value="#{requestBean.postConstructCalled}" /></p>
  <p>requestBean PreDestroy: <h:outputText value="#{requestBean.preDestroyCalled}" /></p>

  <p>sessionBean PostConstruct: <h:outputText value="#{sessionBean.postConstructCalled}" /></p>
  <p>sessionBean PreDestroy: <h:outputText value="#{sessionBean.preDestroyCalled}" /></p>

  <p>applicationBean PostConstruct: <h:outputText value="#{applicationBean.postConstructCalled}" /></p>
  <p>applicationBean PreDestroy: <h:outputText value="#{applicationBean.preDestroyCalled}" /></p>
  
  <p>previous request status: <pre><h:outputText value="#{applicationScope.previousRequestStatus}" /></pre></p>
  
  <p><h:commandButton id="removeRequestBean" value="remove request bean" action="#{noneBean.removeRequestBean}" /></p>

  <p><h:commandButton id="removeRequestBean2" value="remove request bean2" action="#{noneBean.removeRequestBean2}" /></p>

  <p><h:commandButton id="replaceRequestBean" value="replace request bean" action="#{noneBean.replaceRequestBean}" /></p>

  <p><h:commandButton id="replaceRequestBean2" value="replace request bean2" action="#{noneBean.replaceRequestBean2}" /></p>
       
  <p><h:commandButton id="removeSessionBean" value="remove session bean" action="#{noneBean.removeSessionBean}" /></p>

  <p><h:commandButton id="removeSessionBean2" value="remove session bean2" action="#{noneBean.removeSessionBean2}" /></p>

  <p><h:commandButton id="replaceSessionBean" value="replace session bean" action="#{noneBean.replaceSessionBean}" /></p>

  <p><h:commandButton id="replaceSessionBean2" value="replace session bean2" action="#{noneBean.replaceSessionBean2}" /></p>

  <p><h:commandButton id="removeApplicationBean" value="remove application bean" action="#{noneBean.removeApplicationBean}" /></p>

  <p><h:commandButton id="removeApplicationBean2" value="remove application bean2" action="#{noneBean.removeApplicationBean2}" /></p>

  <p><h:commandButton id="replaceApplicationBean" value="replace application bean" action="#{noneBean.replaceApplicationBean}" /></p>

  <p><h:commandButton id="replaceApplicationBean2" value="replace application bean2" action="#{noneBean.replaceApplicationBean2}" /></p>
       
  <p><h:commandButton id="invalidateSession" value="invalidate session" action="#{noneBean.invalidateSession}" /></p>

  <p><h:commandButton id="clearRequestMap" value="clear request map" action="#{noneBean.clearRequestMap}" /></p>

  <p><h:commandButton id="clearRequestMapTwice" value="clear request map twice" action="#{noneBean.clearRequestMapTwice}" /></p>

  <p><h:commandButton id="clearSessionMap" value="clear session map" action="#{noneBean.clearSessionMap}" /></p>

  <p><h:commandButton id="clearSessionMapTwice" value="clear session map twice" action="#{noneBean.clearSessionMapTwice}" /></p>

  <p><h:commandButton id="clearStatusMessage" value="clear status message" action="#{noneBean.clearStatusMessage}" /></p>
  
  <p><h:commandButton id="reload" value="reload" />
  </h:form>

</f:view>
    <hr>
  </body>
</html>
