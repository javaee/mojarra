<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<head>
  <title>FruitStand.com</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
    <%@ page extends="com.sun.faces.Page" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="faces" %>

<h2><font color="#0000FF">FruitStand.com</font></h2>
<hr>

<font size="4" color="#0000FF">Open a New Account </font><body bgcolor="#FFFFFF" text="#000000"> 

  <faces:NavigationMap id="AccountNavMap" scope="session" >

        <faces:outcome commandName="Create Account" outcome="success" targetAction="forward"
                targetPath = "Login.jsp" />

        <faces:outcome commandName="Create Account" outcome="failure" targetAction="forward"
                targetPath = "createAccount.jsp" />

   </faces:NavigationMap>

  <faces:Listener id="handleCreateAccount" scope="session" className="fruitstand.CommandListenerImpl" />

  <faces:Form id="CreateAccountForm" modelReference="${UserBean}" navigationMapId="AccountNavMap" >

  <table width="450">
    <tr> 
      <td colspan='2'><font size="4" color="#0000FF">Personal Information</font></td>
    </tr>
    <tr> 
      <td>First Name</td>
      <td> 
        <faces:TextEntry_Input id="firstName" />
      </td>
    </tr>
    <tr> 
      <td>Last Name</td>
      <td> 
           <faces:TextEntry_Input id="lastName" />
      </td>
    </tr>
    <tr> 
      <td>Address</td>
      <td> 
          <faces:TextEntry_Input id="address" />
      </td>
    </tr>
    <tr> 
      <td>City</td>
      <td> 
         <faces:TextEntry_Input id="city" />
      </td>
    </tr>
    <tr> 
      <td>State</td>
      <td> 
         <faces:TextEntry_Input id="state" />
      </td>
    </tr>
    <tr> 
      <td> Country</td>
      <td> 
         <faces:TextEntry_Input id="country" />
      </td>
    </tr>
    <tr> 
      <td>Phone Number</td>
      <td> 
        <faces:TextEntry_Input id="phone" />
      </td>
    </tr>
    <tr> 
      <td colspan="2"><font size="4" color="#0000FF">Credit Card Information</font></td>
    </tr>
    <tr> 
      <td>Credit Card Type</td>
      <td> 
        <faces:SelectOne_OptionList id="creditCardType"
                   modelReference="${UserBean.items}"
                   selectedModelReference="${UserBean.creditCardType}">
          <faces:SelectOne_Option value="discover" label="Discover"/>
          <faces:SelectOne_Option value="mastercard" label="Master Card"/>
          <faces:SelectOne_Option value="visa" label="Visa"/>
        </faces:SelectOne_OptionList>
      </td>
    </tr>
    <tr> 
      <td>Card Number</td>
      <td> 
        <faces:TextEntry_Input id="creditCardNumber" />
      </td>
    </tr>
    <tr> 
      <td>Expiration Date</td>
      <td> 
        <faces:TextEntry_Input id="creditCardExpr" />
      </td>
    </tr>
    <tr> 
      <td colspan="2"> <font size="4" color="#0000FF">Username and Password</font></td>
    </tr>
    <tr> 
      <td >User Name</td>
      <td> 
        <faces:TextEntry_Input id="userName" />
      </td>
    </tr>
    <tr> 
      <td>Password</td>
      <td> 
        <faces:TextEntry_Input id="password" />
      </td>
    </tr>
    <tr> 
      <td>Confirm Password</td>
      <td> 
        <faces:TextEntry_Input id="pwdConfirm" />
      </td>
    </tr>
    <tr> 
      <td>Password Hint</td>
      <td> 
        <faces:TextEntry_Input id="pwdHint" />
      </td>
    </tr>
    <tr> 
      <td> 
        <faces:Command_Button id="createAccount" label="Create Account" 
                       commandListener="handleCreateAccount" commandName="Create Account"/>
      </td>
      <td>&nbsp;</td>
    </tr>
  </table>
</faces:Form>
<hr>
<p>Thanks for shopping with FruitStand.com. </p>
</body>
</html>
