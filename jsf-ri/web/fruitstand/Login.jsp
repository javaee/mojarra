<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <meta name="GENERATOR" content="Mozilla/4.75 [en] (WinNT; U) [Netscape]">
   <title>Login</title>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
</head>
<body>
<font color="#0000FF"><font size=+3>FruitStand.com</font></font>
<hr WIDTH="100%">
<p><font color="#0000FF"><font size=+2>Please Login.  Demo userid:password is <CODE>default:default</CODE></font></font>
<hr>
<faces:UseFaces>
  <faces:DeclareBean scope="session" id="UserBean" 
                     className="fruitstand.UserBean"/>
  <faces:DeclareBean scope="session" id="ShipTypeBean" 
                     className="fruitstand.ShipTypeBean"/>

  <faces:Listener id="handleLogin" scope="session" className="fruitstand.CommandListenerImpl" />

  <faces:NavigationMap id="LoginNavMap" scope="session" >

        <faces:outcome commandName="Login" outcome="success" targetAction="forward"
                targetPath = "StoreFront.jsp" />

        <faces:outcome commandName="Login" outcome="failure" targetAction="forward"
                targetPath = "Login.jsp" />

   </faces:NavigationMap>

  <faces:Form id="LoginForm" model="UserBean" navigationMapId="LoginNavMap" >

    <table>
    <tr>
    <td><faces:Output_Text id="name_label" value="Name:" /></td>

    <td> <faces:TextEntry_Input id="userName" model="$UserBean.userName" /> </td>
    </tr>

    <tr>
    <td><faces:Output_Text id="passwd_label" value="Password:" /></td>

    <td> <faces:TextEntry_Secret id="password" model="$UserBean.password" 
                          size="10"/></td>
    </tr>
    </table>

<p> <faces:Command_Button id="Login" label="login" commandListener="handleLogin" /> 

<p><font color="#000000">click&nbsp;</font> <faces:Command_Hyperlink target="createAccount.jsp" text="here"/> to open new account
<hr WIDTH="100%">
<p><img SRC="duke.gif" height=55 width=49 align=ABSCENTER> Thanks
for stopping by.
<br>
</faces:Form>
</faces:UseFaces>
</body>
</html>
