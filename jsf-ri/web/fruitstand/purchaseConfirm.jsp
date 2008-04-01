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
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
<h2><font color="#0000FF"> FruitStand.com </font></h2>
<hr>
<font size="4" color="#0000FF">Here are the items you selected.</font>
<faces:Form id="purchaseConfirm" modelReference="UserBean" navigationMapId="purchaseNavMap" >
<table cellpadding="10">
  <th>Item</th>
  <th>Amount(lbs)</th>
  <th>Price/lb.($)</th>
  <th>Total($)</th>
  <tr> 
    <td><faces:Output_Text id="apple_item" value="apple" /></td>
    <td><faces:Output_Text id="apple_amount" 
                           modelReference="$UserBean.appleQuantity"/></td>
    <td><faces:Output_Text id="apple_price" value= "0.29"/></td>
    <td><faces:Output_Text id="apple_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="banana_item" value="banana" /></td>
    <td><faces:Output_Text id="banana_amount" 
                           modelReference="$UserBean.bananaQuantity"/></td>
    <td><faces:Output_Text id="banana_price" value= "0.29"/></td>
    <td><faces:Output_Text id="banana_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="cantaloupe_item" value="cantaloupe" /></td>
    <td><faces:Output_Text id="cantaloupe_amount" 
                           modelReference="$UserBean.cantaloupeQuantity"/></td>
    <td><faces:Output_Text id="cantaloupe_price" value= "0.29"/></td>
    <td><faces:Output_Text id="cantaloupe_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="grapefruit_item" value="grapefruit" /></td>
    <td><faces:Output_Text id="grapefruit_amount" 
                           modelReference="$UserBean.grapefruitQuantity"/></td>
    <td><faces:Output_Text id="grapefruit_price" value= "0.29"/></td>
    <td><faces:Output_Text id="grapefruit_total" value= "0.58"/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text id="grape_item" value="grape" /></td>
    <td><faces:Output_Text id="grape_amount" 
                           modelReference="$UserBean.grapeQuantity"/></td>
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

<faces:Output_Text id="cust_firstName" modelReference="$UserBean.firstName" /> 
<faces:Output_Text id="cust_lastName" modelReference="$UserBean.lastName" /> <br>
<faces:Output_Text id="cust_address" modelReference="$UserBean.address" /><br>
<faces:Output_Text id="cust_city" modelReference="$UserBean.city" />
<faces:Output_Text id="cust_state" modelReference="$UserBean.state" />
<br>
<faces:Output_Text id="cust_country" modelReference="$UserBean.country" />

<faces:NavigationMap id="purchaseNavMap" scope="session" >

        <faces:outcome commandName="confirm" outcome="success" targetAction="forward"
                targetPath = "purchaseAction.jsp" />

        <faces:outcome commandName="confirm" outcome="failure" targetAction="forward"
                targetPath = "StoreFront.jsp" />

</faces:NavigationMap>

<faces:Listener id="handleConfirm" scope="session" className="fruitstand.CommandListenerImpl" />


<TABLE border="2"><TR><TD>
<TABLE>
<TR>
<faces:RadioGroup id="shipType"
                  modelReference="$ShipTypeBean.shipType"
                  selectedModelReference="$ShipTypeBean.currentShipType">
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
                      commandListener="handleConfirm"/>
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

