<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <meta name="GENERATOR" content="Mozilla/4.75 [en] (WinNT; U) [Netscape]">
   <title>Login</title>
    <%@ page extends="com.sun.faces.Page" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
</head>
<body>
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" BGCOLOR="#FFFFFF" >
<tr>
<td VALIGN=TOP WIDTH="100%"><img SRC="pictures/cardemo.jpg" BORDER=0 height=60 width=850
align=BOTTOM></td>
</tr>
</table>
<hr WIDTH="100%">
<p><font color="#0000FF"><font size=+2>Please Login.  Demo userid:password is <CODE>default:default</CODE></font></font>
<hr>
  <faces:DeclareBean scope="session" id="UserBean"
                     className="cardemo.UserBean"/>

  <faces:Listener id="handleLogin" scope="session" className="cardemo.CommandListenerImpl" />

<faces:NavigationMap id="LoginNavMap" scope="session">
<faces:outcome commandName="Login" outcome="success" targetAction="forward" targetPath="Storefront.jsp"/>
<faces:outcome commandName="Login" outcome="failure" targetAction="forward" targetPath="error.jsp"/>
</faces:NavigationMap>


  <faces:Form id="LoginForm" modelReference="UserBean" navigationMapId="LoginNavMap" >

    <table>
    <tr>
    <td><faces:Output_Text id="name_label" value="Name:" /></td>

    <td> <faces:TextEntry_Input id="userName" modelReference="$UserBean.userName" /> </td>
    </tr>

    <tr>
    <td><faces:Output_Text id="passwd_label" value="Password:" /></td>

    <td> <faces:TextEntry_Secret id="password" modelReference="$UserBean.password"
                          size="10"/></td>
    </tr>
    </table>
<faces:Command_Button id="MyLogin" label="Login" commandName="Login" scope="session" commandListener="handleLogin" />

<hr WIDTH="100%">
<p><img SRC="pictures/duke.gif" height=55 width=49 align=ABSCENTER> Thanks
for stopping by.
<br>
</faces:Form>
</body>
</html>

