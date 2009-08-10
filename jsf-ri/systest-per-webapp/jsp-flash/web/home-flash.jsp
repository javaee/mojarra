<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%--

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.

 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"

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

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<f:view>

<html>
  <head>
    <title>JavaServer Faces Extensions Flash</title>
  </head>

    <%@ page contentType="text/html" %>

<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" rightmargin="0" bgcolor="#ffffff" class="vaa2v0">

<a name="top"></a> 

JavaServer Faces Extensions Flash

  <p>This series of pages illustrates the usage of the flash concept
  taken from <a target="_"
  href="http://api.rubyonrails.com/classes/ActionController/Flash.html">Ruby
  On Rails</a>.</p>

  <p>In JSF, the flash is exposed naturally via the new <a
  href="http://java.sun.com/products/jsp/reference/techart/unifiedEL.html">Unified
  Expression Language in Java EE 5</a>.  It is implemented via a custom
  <code>ELResolver</code> that introduces a new implicit object called
  "flash".  I considered calling it "dhhIsMyHero" but opted for the
  simpler "flash" instead.</p>

  <p>Using the flash is simple, and semantically identical to the way it
  works in Rails.  It's a Map.  Stuff you put in the Map will be
  accessible on the "next" view shown to user.  The Map will be cleared
  when the user has been shown the "next" view.</p>



<!-- BEGIN WRAPPER TABLE, 2 COLUMN, MAIN/RIGHT -->
<table border="0" cellpadding="0" cellspacing="10" width="100%">
<tr valign="top"><td>

<!-- BEGIN MAIN COLUMN -->

  <h:form prependId="false" id="form1">

  <h:panelGrid columns="2" border="1" width="600">
      
    Put <code>fooValue</code> in the flash under key <code>foo</code>
    using <code>jsfExt:set</code>.  Note that things stored in the flash
    during <b>this</b> request are only retrievable on the <b>next</b>
    request.  If you want to store something on this request and see it
    on this one as well, use either <code>&#35;{flash.now.foo}</code> or
    <code>&#35;{requestScope.foo}</code>.  The former is simply an alias
    for the latter.

    <c:set target="${flash}" property="foo" value="fooValue" />

    <f:verbatim>
      &lt;c:set target="\${flash}" property="foo" value="fooValue" /&gt;
    </f:verbatim>

    Value of <code>&#35;{flash.foo}</code>, should be <code>null</code>.

    <h:outputText id="fooValueId" value="#{flash.foo}" />

    <h:commandButton id="reload" value="reload" />

    <h:commandButton id="next" value="next" action="next" />

   </h:panelGrid>

   <p>Type "addMessage", without the quotes, to cause a FacesMessage to
   be added <h:inputText id="inputText"
   value="#{bean.stringVal}" /></p>

   <p><h:messages id="messages"/></p>

  </h:form>


</tr>

</table>

  </body>
</html>
</f:view>
