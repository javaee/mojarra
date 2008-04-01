<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <meta name="GENERATOR" content="Mozilla/4.75 [en] (WinNT; U) [Netscape]">
   <title>Login</title>
   <%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
</head>
<body>
<font color="#0000FF"><font size=+3>FruitStand.com</font></font>
<hr WIDTH="100%">
<p><font color="#0000FF"><font size=+2>Please Login</font></font>
<hr><form action="StoreFront.jsp"
		method='post'>
<faces:useFaces>

<table>
<tr>
<td><faces:Output_Text name='name_label' value='Name:' /></td>

<td> <faces:TextEntry_Input name="userName" /> </td>
</tr>

<tr>
<td><faces:Output_Text name='passwd_label' value='Password:' /></td>

<td> <faces:TextEntry_Secret name="password" size='10'/></td>
</tr>
</table>

<p> <faces:command_button name="Login" label="login"/> 
<p><font color="#000000">click&nbsp;</font> <a href="createAccount.jsp">here</a>
to open new account
<hr WIDTH="100%">
<p><img SRC="duke.gif" height=55 width=49 align=ABSCENTER> Thanks
for stopping by.
<br>
</form>
</faces:useFaces>
</body>
</html>
