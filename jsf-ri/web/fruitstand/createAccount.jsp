<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<head>
  <title>FruitStand.com</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

<h2><font color="#0000FF">FruitStand.com</font></h2>
<hr>

<font size="4" color="#0000FF">Open a New Account </font><body bgcolor="#FFFFFF" text="#000000"> 

<faces:UseFaces>
  <faces:Form name="CreateAccountForm" model="UserBean">

    <faces:Command name="handleCreateAccount" scope="session" 
                   className="fruitstand.CommandListenerImpl" 
                   onCompletion="Login.jsp" 
                   onError="createAccount.jsp"/>

  <table width="450">
    <tr> 
      <td colspan='2'><font size="4" color="#0000FF">Personal Information</font></td>
    </tr>
    <tr> 
      <td>First Name</td>
      <td> 
        <faces:TextEntry_Input name="firstName" />
      </td>
    </tr>
    <tr> 
      <td>Last Name</td>
      <td> 
           <faces:TextEntry_Input name="lastName" />
      </td>
    </tr>
    <tr> 
      <td>Address</td>
      <td> 
          <faces:TextEntry_Input name="address" />
      </td>
    </tr>
    <tr> 
      <td>City</td>
      <td> 
         <faces:TextEntry_Input name="city" />
      </td>
    </tr>
    <tr> 
      <td>State</td>
      <td> 
         <faces:TextEntry_Input name="state" />
      </td>
    </tr>
    <tr> 
      <td> Country</td>
      <td> 
         <faces:TextEntry_Input name="country" />
      </td>
    </tr>
    <tr> 
      <td>Phone Number</td>
      <td> 
        <faces:TextEntry_Input name="phone" />
      </td>
    </tr>
    <tr> 
      <td colspan="2"><font size="4" color="#0000FF">Credit Card Information</font></td>
    </tr>
    <tr> 
      <td>Credit Card Type</td>
      <td> 
        <faces:SelectOne_OptionList name="creditCardType"
                   model="$UserBean.items"
                   selectedValueModel="$UserBean.creditCardType">
          <faces:SelectOne_Option value="discover" label="Discover"/>
          <faces:SelectOne_Option value="mastercard" label="Master Card"/>
          <faces:SelectOne_Option value="visa" label="Visa"/>
        </faces:SelectOne_OptionList>
      </td>
    </tr>
    <tr> 
      <td>Card Number</td>
      <td> 
        <faces:TextEntry_Input name="creditCardNumber" />
      </td>
    </tr>
    <tr> 
      <td>Expiration Date</td>
      <td> 
        <faces:TextEntry_Input name="creditCardExpr" />
      </td>
    </tr>
    <tr> 
      <td colspan="2"> <font size="4" color="#0000FF">Username and Password</font></td>
    </tr>
    <tr> 
      <td >User Name</td>
      <td> 
        <faces:TextEntry_Input name="userName" />
      </td>
    </tr>
    <tr> 
      <td>Password</td>
      <td> 
        <faces:TextEntry_Input name="password" />
      </td>
    </tr>
    <tr> 
      <td>Confirm Password</td>
      <td> 
        <faces:TextEntry_Input name="pwdConfirm" />
      </td>
    </tr>
    <tr> 
      <td>Password Hint</td>
      <td> 
        <faces:TextEntry_Input name="pwdHint" />
      </td>
    </tr>
    <tr> 
      <td> 
        <faces:Command_Button name="createAccount" label="Create Account" 
                              command="handleCreateAccount"/>
      </td>
      <td>&nbsp;</td>
    </tr>
  </table>
</faces:Form>
</faces:UseFaces>
<hr>
<p>Thanks for shopping with FruitStand.com. </p>
</body>
</html>
