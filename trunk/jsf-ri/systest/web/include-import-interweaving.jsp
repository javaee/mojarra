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

<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head> <title>Hello</title> </head>
    <%@ page contentType="application/xhtml+xml" autoFlush="true"%>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>    
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <body bgcolor="white">
    <f:view>
    <h:form id="form">
    
        <p>Outer Template Text followed by 
        <h:outputText id="outerComponent" value="outer component" /></p>
	
<table border="1">	

<tr><td>
<ul><font color="blue">        

 <f:subview id="subview01">
	    <p>Subview on the outside only with jsp:include:</p>
            <jsp:include page="TCCI_subview01.jsp" />
 </f:subview>
        
</font></ul>        

	    
</td></tr>

<tr><td>
<ul><font color="green">        

	    <p>Subview on the inside only with jsp:include:</p>
            <jsp:include page="TCCI_subview02.jsp" />
        
</font></ul>        

	    
</td></tr>

<tr><td>
<ul><font color="purple">        

	    <p>Subview on the outside and inside with jsp:include:</p>
<f:subview id="subview03">
	    <p>Template Text before the include</p>
            <jsp:include page="TCCI_subview03.jsp" />
</f:subview>
        
</font></ul>        

	    
</td></tr>

<tr><td>
<ul><font color="red">        
        <f:subview id="outerSubview3">
	    <p>Subview on the outside only with c:import:</p>
            <c:import url="TCCI_subview04.jsp" />
        </f:subview>
</font></ul>        

	    
</td></tr>


<tr><td>
<ul><font color="red">        
	    <p>Subview on the inside only with c:import:</p>
            <c:import url="TCCI_subview05.jsp" />
</font></ul>        

	    
</td></tr>

<tr><td>
<ul><font color="orange">        
	    <p>Subview on the outside and inside with c:import:</p>
<f:subview id="subview05">
	    <p>Template Text on the outside</p>
            <c:import url="TCCI_subview06.jsp" />
</f:subview>
</font></ul>        

	    
</td></tr>


</table>
	 

    </h:form>
    </f:view>
    </body>
</html>  
