<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>CarDemo</title>
</head>

<%@ page extends="com.sun.faces.Page" %>
<%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

<body bgcolor="#FFFFFF">

<faces:NavigationMap id="checkOutNavMap" scope="session" >
    <faces:outcome commandName="thanks" outcome="success" targetAction="forward"
        targetPath = "thanks.jsp" />
    <faces:outcome commandName="thanks" outcome="failure" targetAction="forward"
        targetPath = "error.jsp" />
</faces:NavigationMap>

<faces:Form id="checkOutForm" navigationMapId="checkOutNavMap" >
    <faces:Listener id="basicListener" scope="session" className="cardemo.BasicListener" />

<table BORDER=0 WIDTH="660" BGCOLOR="#E2F7DD" NOSAVE >
<tr NOSAVE>
<td WIDTH="828" NOSAVE>
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" >
<tr>
<td WIDTH="820">
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" BGCOLOR="#FFFFFF" >
<tr>
<td VALIGN=TOP WIDTH="100%"><img SRC="pictures/cardemo.jpg" BORDER=0 height=60 width=660 align=BOTTOM></td>
</tr>

<tr>
<td WIDTH="100%"><b><font face="Arial, Helvetica"><font color="#330066"><font size=+1><faces:Output_Text id="currentCarName"  modelReference="$CurrentOptionServer.carName" /></font></font></font></b>
<p><font face="Arial, Helvetica"><b><font color="#93B629">Price including
your options: </font></b><faces:Output_Text id="currentCarPrice"  modelReference="$CurrentOptionServer.currentPrice" /></font>
<p>&nbsp;</td>
</tr>

<tr>
<td WIDTH="100%" BGCOLOR="#FFFFFF"><b><font face="Arial, Helvetica"><font color="#330066"><font size=+1>Customer
Information</font></font></font></b></td>
</tr>

<tr>
<td WIDTH="100%">
<blockquote>&nbsp; <faces:Errors/>
<table COLS=2 WIDTH="60%" NOSAVE >
<tr>
<td>
<div align=right>Lastname</div>
</td>

<td><faces:TextEntry_Input /></td>
</tr>

<tr>
<td>
<div align=right>Firstname</div>
</td>

<td><faces:TextEntry_Input /></td>
</tr>

<tr>
<td>
<div align=right>Address 1</div>
</td>

<td><faces:TextEntry_Input /></td>
</tr>

<tr NOSAVE>
<td>
<div align=right>Address 2</div>
</td>

<td NOSAVE><faces:TextEntry_Input /></td>
</tr>

<tr NOSAVE>
<td NOSAVE>
<div align=right>City</div>
</td>

<td><faces:TextEntry_Input /></td>
</tr>

<tr>
<td>
<div align=right>State</div>
</td>

<td><faces:TextEntry_Input lengthMinimum="2" lengthMaximum="2" /> <font color="red">*</font> </td>
</tr>

<tr>
<td>
<div align=right>Zip</div>
</td>

<td><faces:TextEntry_Input required="false" modelType="java.lang.Integer" rangeMinimum="10000" rangeMaximum="99999" /> <font color="red">*</font></td>
</tr>

<tr>
<td>
<div align=right>Phone</div>
</td>

<td><faces:TextEntry_Input /></td>
</tr>

<tr>
<td>
<div align=right>Credit Card Number</div>
</td>

<td><faces:TextEntry_Input /></td>
</tr>

<tr>
<td>
<div align=right>Credit Card Expires</div>
</td>

<td><faces:SelectOne_OptionList >
        <faces:SelectOne_Option value="01" label="01"/>
        <faces:SelectOne_Option value="02" label="02"/>
        <faces:SelectOne_Option value="03" label="03"/>
        <faces:SelectOne_Option value="04" label="04"/>
        <faces:SelectOne_Option value="05" label="05"/>
        <faces:SelectOne_Option value="06" label="06"/>
        <faces:SelectOne_Option value="07" label="07"/>
        <faces:SelectOne_Option value="08" label="08"/>
        <faces:SelectOne_Option value="09" label="09"/>
        <faces:SelectOne_Option value="10" label="10"/>
        <faces:SelectOne_Option value="11" label="11"/>
        <faces:SelectOne_Option value="12" label="12"/>
    </faces:SelectOne_OptionList>
&nbsp;
<faces:SelectOne_OptionList >
        <faces:SelectOne_Option value="2002" label="2002"/>
        <faces:SelectOne_Option value="2003" label="2003"/>
        <faces:SelectOne_Option value="2004" label="2004"/>
        <faces:SelectOne_Option value="2005" label="2005"/>
        <faces:SelectOne_Option value="2006" label="2006"/>
        <faces:SelectOne_Option value="2007" label="2007"/>
        <faces:SelectOne_Option value="2008" label="2008"/>
    </faces:SelectOne_OptionList>
</td>
</tr>
</table>
<font face="Arial, Helvetica" color="red">* these fields will be validated</font>
<blockquote>&nbsp;<faces:Command_Button id="thanks" label="Buy" commandListener="basicListener"/></blockquote>
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
</faces:Form>
</body>
</html>
