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
    <td><faces:Output_Text id="apple_item" value="apple" /></td>
    <td><faces:Output_Text id="apple_amount" 
                           model="$UserBean.appleQuantity"/></td>
    <td><faces:Output_Text id="apple_price" value= "0.29"/></td>
    <td><faces:Output_Text id="apple_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="banana_item" value="banana" /></td>
    <td><faces:Output_Text id="banana_amount" 
                           model="$UserBean.bananaQuantity"/></td>
    <td><faces:Output_Text id="banana_price" value= "0.29"/></td>
    <td><faces:Output_Text id="banana_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="cantaloupe_item" value="cantaloupe" /></td>
    <td><faces:Output_Text id="cantaloupe_amount" 
                           model="$UserBean.cantaloupeQuantity"/></td>
    <td><faces:Output_Text id="cantaloupe_price" value= "0.29"/></td>
    <td><faces:Output_Text id="cantaloupe_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="grapefruit_item" value="grapefruit" /></td>
    <td><faces:Output_Text id="grapefruit_amount" 
                           model="$UserBean.grapefruitQuantity"/></td>
    <td><faces:Output_Text id="grapefruit_price" value= "0.29"/></td>
    <td><faces:Output_Text id="grapefruit_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="grape_item" value="grape" /></td>
    <td><faces:Output_Text id="grape_amount" 
                           model="$UserBean.grapeQuantity"/></td>
    <td><faces:Output_Text id="grape_price" value= "0.29"/></td>
    <td><faces:Output_Text id="grape_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td colspan="4"><hr /></td>
    
  </tr>
  <tr>
    <td colspan="3">Total</td>
    <td> <faces:Output_Text id="total_price" value="6.14" /></td>
  </tr>
</table>
<br>
<font size="4" color="#0000FF">
The items listed above will be billed to: <br>
<br>

<faces:Output_Text id="cust_firstName" model="$UserBean.firstName" /> 
<faces:Output_Text id="cust_lastName" model="$UserBean.lastName" /> <br>
<faces:Output_Text id="cust_address" model="$UserBean.address" /><br>
<faces:Output_Text id="cust_city" model="$UserBean.city" />
<faces:Output_Text id="cust_state" model="$UserBean.state" />
<br>
<faces:Output_Text id="cust_country" model="$UserBean.country" />

<faces:Form id="purchaseConfirm" model="UserBean">
      <faces:Command id="handleConfirm" scope="session" 
                   className="fruitstand.CommandListenerImpl" 
                   onCompletion="purchaseAction.jsp" 
                   onError="StoreFront.jsp"/>

<TABLE border="2"><TR><TD>
<TABLE>
<TR>
<faces:RadioGroup id="shipType"
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
<TD><faces:SelectBoolean_Checkbox id="giftWrap" value="giftWrap" 
                                  label="Gift Wrapped"/>
</TD>
<TD><faces:SelectBoolean_Checkbox id="giftCard" value="giftCard" 
                                  label="Gift Card"/>
</TD>
<TD><faces:SelectBoolean_Checkbox id="scented" value="scented" 
                                  label="Scented"/>
</TD>
</TR>
<TR>
<TD>
  <faces:Command_Button id="confirm" label="Purchase Items Listed Above" 
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
