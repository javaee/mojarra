<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>CarDemo</title>
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:use_faces>
<h:form id="thanksForm" formName="thanksForm" >

<body bgcolor="#FFFFFF">

<table BORDER=0 WIDTH="660" BGCOLOR="#E2F7DD" NOSAVE >
<tr NOSAVE>
<td WIDTH="828" NOSAVE>
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" >
<tr>
<td WIDTH="820">
<TABLE BORDER="0" WIDTH="660" BGCOLOR="#4F4F72">
<tr>
<td VALIGN=TOP WIDTH="100%">
<h:graphic_image id="cardemo_img" url="/cardemo.jpg" /></td>
</tr>

<tr>
<td WIDTH="100%"><b><font face="Arial, Helvetica"><font color="#330066"><font size=+1></font></font></font></b>
<p><font face="Arial, Helvetica"><b><font color="#93B629"></font></b></font>
<p>&nbsp;</td>
</tr>

<tr>
<td WIDTH="100%" BGCOLOR="#FFFFFF"><b><font face="Arial, Helvetica"><font color="#330066" align="center"><font size=+1>
<h:output_text id="thxmsg" 
		   value="Thanks for using Cardemo! Your car will ship soon." />
</font></font></font></b></td>
</tr>

<tr>
<td WIDTH="100%">
<blockquote>&nbsp;
<table COLS=2 WIDTH="60%" NOSAVE >
<tr>
</tr>
</table>

<blockquote>&nbsp;</blockquote>
</blockquote>
</td>
</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>
</body>
</h:form>
</f:use_faces>
</html>
