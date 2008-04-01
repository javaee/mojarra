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
<h2><font color="#0000FF"> FruitStand.com </font></h2>
<hr>
<font size="4" color="#0000FF">Here are the items you selected.</font>
<faces:UseFaces>
<table cellpadding="10">
  <th>Item</th>
  <th>Amount(lbs)</th>
  <th>Price/lb.($)</th>
  <th>Total($)</th>
  <tr> 
    <td><faces:Output_Text name="apple_item" value="apple" /></td>
    <td><faces:Output_Text name="apple_amount" 
                           model="$UserBean.appleQuantity"/></td>
    <td><faces:Output_Text name="apple_price" value= "0.29"/></td>
    <td><faces:Output_Text name="apple_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text name="banana_item" value="banana" /></td>
    <td><faces:Output_Text name="banana_amount" 
                           model="$UserBean.bananaQuantity"/></td>
    <td><faces:Output_Text name="banana_price" value= "0.29"/></td>
    <td><faces:Output_Text name="banana_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text name="cantaloupe_item" value="cantaloupe" /></td>
    <td><faces:Output_Text name="cantaloupe_amount" 
                           model="$UserBean.cantaloupeQuantity"/></td>
    <td><faces:Output_Text name="cantaloupe_price" value= "0.29"/></td>
    <td><faces:Output_Text name="cantaloupe_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text name="grapefruit_item" value="grapefruit" /></td>
    <td><faces:Output_Text name="grapefruit_amount" 
                           model="$UserBean.grapefruitQuantity"/></td>
    <td><faces:Output_Text name="grapefruit_price" value= "0.29"/></td>
    <td><faces:Output_Text name="grapefruit_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text name="grape_item" value="grape" /></td>
    <td><faces:Output_Text name="grape_amount" 
                           model="$UserBean.grapeQuantity"/></td>
    <td><faces:Output_Text name="grape_price" value= "0.29"/></td>
    <td><faces:Output_Text name="grape_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td colspan="4"><hr /></td>
    
  </tr>
  <tr>
    <td colspan="3">Total</td>
    <td> <faces:Output_Text name="total_price" value="6.14" /></td>
  </tr>
</table>
<br>
<font size="4" color="#0000FF">
The items listed above will be billed to: <br>
<br>

<faces:Output_Text name="cust_firstName" model="$UserBean.firstName" /> 
<faces:Output_Text name="cust_lastName" model="$UserBean.lastName" /> <br>
<faces:Output_Text name="cust_address" model="$UserBean.address" /><br>
<faces:Output_Text name="cust_city" model="$UserBean.city" />
<faces:Output_Text name="cust_state" model="$UserBean.state" />
<br>
<faces:Output_Text name="cust_country" model="$UserBean.country" />

<faces:Form name="purchaseConfirm" model="UserBean">
      <faces:Command name="handleConfirm" scope="session" 
                   className="fruitstand.CommandListenerImpl" 
                   onCompletion="purchaseAction.jsp" 
                   onError="StoreFront.jsp"/>

<TABLE border="2"><TR><TD>
<TABLE>
<TR>
<faces:RadioGroup name="shipType"
                  model="$ShipTypeBean.shipType"
                  selectedValueModel="$ShipTypeBean.currentShipType">
  <TABLE border="2"><TR><TD>
  <TABLE>
  <TR>
  <TD>Select Shipping</TD>
  <TD><faces:SelectOne_Radio value="nextDay" label=" Next Day"/></TD>
  <TD><faces:SelectOne_Radio checked="true" value="nextWeek" label="Next Week"/> </TD>
  <TD><faces:SelectOne_Radio value="nextMonth" label="Next Month"/> </TD>
  </TABLE>
  </TD></TR></TABLE>
</faces:RadioGroup>

</TR>
<TR>
<TD>Select Extras
</TD>
<TD><faces:SelectBoolean_Checkbox name="giftWrap" value="giftWrap" 
                                  label="Gift Wrapped"/>
</TD>
<TD><faces:SelectBoolean_Checkbox name="giftCard" value="giftCard" 
                                  label="Gift Card"/>
</TD>
<TD><faces:SelectBoolean_Checkbox name="scented" value="scented" 
                                  label="Scented"/>
</TD>
</TR>
<TR>
<TD>
  <faces:Command_Button name="confirm" label="Purchase Items Listed Above" 
                      command="handleConfirm"/>
</TD>
</TR>
<TR>
<TD colspan="4"><HR></TD>
</TR>
</TABLE>
</TD></TR></TABLE>
</faces:Form>
<hr>

</font>
<p>Thanks for shopping with FruitStand.com. </p></html>

</faces:UseFaces>
