<%--
   Copyright 2003 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: verbatim_test.jsp,v 1.2 2003/09/05 18:57:12 eburns Exp $ --%>
<html>
  <head>
    <title>Test of the Verbatim Tag</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ page import="javax.faces.context.FacesContext"%>
<%

  String textToEscape = "This text<b>must be escaped</b>";
  FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("textToEscape", textToEscape);  
%>


  </head>

<f:view>

  <body>
    <h1>Test of the Verbatim Tag</h1>

<p>

    <f:verbatim>This text must be echoed verbatim <B>INCLUDING</B> any
    <I>MARKUP</I>.  The angle brackets must be un-escaped.
    </f:verbatim>

</p>

<p>

    <f:verbatim escape="false">This text must be echoed verbatim
    <B>INCLUDING</B> any <I>MARKUP</I>.  The angle brackets must be
    un-escaped.
    </f:verbatim>

</p>

<p>

    <f:verbatim escape="true">This text must be echoed verbatim
    <B>INCLUDING</B> any <I>MARKUP</I>.  The angle brackets must be
    escaped.
    </f:verbatim>

    <p><h:output_text valueRef="textToEscape"/></p>

</p>




    <hr>
<!-- Created: Thu Sep 04 14:10:57 Eastern Daylight Time 2003 -->
<!-- hhmts start -->
Last modified: Thu Sep 04 18:47:53 Eastern Daylight Time 2003
<!-- hhmts end -->
  </body>

</f:view>

</html>
