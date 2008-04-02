<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <meta name="Author" content="Roger Kitain">
   <meta name="GENERATOR" content="Mozilla/4.75 [en] (WinNT; U) [Netscape]">
   <title>StoreFront</title>
</head>
    <%@ page extends="com.sun.faces.Page" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="faces" %>
<body>
   <faces:NavigationMap id="StoreNavMap" scope="session" >

        <faces:outcome commandName="Checkout" outcome="success" targetAction="forward"
                targetPath = "purchaseConfirm.jsp" />

        <faces:outcome commandName="Checkout" outcome="failure" targetAction="redirect"
                targetPath = "StoreFront.jsp" />

   </faces:NavigationMap>

   <faces:Listener id="handleCheckout" scope="session" className="fruitstand.CommandListenerImpl" />

  <faces:Form id="purchaseForm" modelReference="${UserBean}" navigationMapId = "StoreNavMap" >

<font color="#3333FF"><font size=+2>Welcome to FruitStand.com, <faces:Output_Text id="differentName" modelReference="${UserBean.firstName}" /></font></font>
<hr WIDTH="100%">
<p><font color="#000000"><font size=+1>Please select from our fresh fruits
and vegetables.</font></font>
<br>&nbsp;
<table BORDER WIDTH="75%" >
<tr>
<td>
<center><b>Picture</b></center>
</td>

<td>
<center><b>Item</b></center>
</td>

<td>
<center><b>Description</b></center>
</td>

<td>
<center><b>Price</b></center>
</td>

<td>
<center><b>Add To Cart</b></center>
</td>
</tr>

<tr>
<td>
<center><img SRC="1001.jpg" height=64 width=89></center>
</td>

<td WIDTH="30"><faces:Output_Text id='apple_name' value='apple' /></td>

<td WIDTH="100"><faces:Output_Text id='apple_desc' value='Crunchy and sweet' /></td>

<td><faces:Output_Text id='apple_price' value='0.29' /></td>

<td>
    <faces:SelectOne_OptionList id="appleQuantity"
                   modelReference="${UserBean.items}"
                   selectedModelReference="${UserBean.appleQuantity}">
        <faces:SelectOne_Option value="0" label="0"/>
        <faces:SelectOne_Option value="1" label="1"/>
        <faces:SelectOne_Option value="2" label="2"/>
        <faces:SelectOne_Option value="3" label="3"/>
        <faces:SelectOne_Option value="4" label="4"/>
        <faces:SelectOne_Option value="5" label="5"/>
        <faces:SelectOne_Option value="6" label="6"/>
        <faces:SelectOne_Option value="7" label="7"/>
        <faces:SelectOne_Option value="8" label="8"/>
        <faces:SelectOne_Option value="9" label="9"/>
    </faces:SelectOne_OptionList>
</td>
</tr>

<tr>
<td>
<center><img SRC="1002.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text id='banana_name' value='banana' /></td>

<td><faces:Output_Text id='banana_desc' value='Bananas smell great' /></td>

<td><faces:Output_Text id='banana_price' value='$0.69/lb' /></td>

<td>
    <faces:SelectOne_OptionList id="bananaQuantity"
                   modelReference="${UserBean.items}"
                   selectedModelReference="${UserBean.bananaQuantity}">
        <faces:SelectOne_Option value="0" label="0"/>
        <faces:SelectOne_Option value="1" label="1"/>
        <faces:SelectOne_Option value="2" label="2"/>
        <faces:SelectOne_Option value="3" label="3"/>
        <faces:SelectOne_Option value="4" label="4"/>
        <faces:SelectOne_Option value="5" label="5"/>
        <faces:SelectOne_Option value="6" label="6"/>
        <faces:SelectOne_Option value="7" label="7"/>
        <faces:SelectOne_Option value="8" label="8"/>
        <faces:SelectOne_Option value="9" label="9"/>
    </faces:SelectOne_OptionList>
</td>
</tr>

<tr>
<td>
<center><img SRC="1003.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text id='cantaloupe_name' value='cantaloupe' /></td>

<td><faces:Output_Text id='cantaloupe_desc' value='Honey dew melons are better' /></td>

<td><faces:Output_Text id='cantaloupe_price' value='$0.19/lb' /></td>

<td>
    <faces:SelectOne_OptionList id="cantaloupeQuantity"
                   modelReference="${UserBean.items}"
                   selectedModelReference="${UserBean.cantaloupeQuantity}">
        <faces:SelectOne_Option value="0" label="0"/>
        <faces:SelectOne_Option value="1" label="1"/>
        <faces:SelectOne_Option value="2" label="2"/>
        <faces:SelectOne_Option value="3" label="3"/>
        <faces:SelectOne_Option value="4" label="4"/>
        <faces:SelectOne_Option value="5" label="5"/>
        <faces:SelectOne_Option value="6" label="6"/>
        <faces:SelectOne_Option value="7" label="7"/>
        <faces:SelectOne_Option value="8" label="8"/>
        <faces:SelectOne_Option value="9" label="9"/>
    </faces:SelectOne_OptionList>
</td>
</tr>

<tr>
<td>
<center><img SRC="1004.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text id='grapefruit_name' value='grapefruit' /></td>

<td><faces:Output_Text id='grapefruit_desc' value='Do not eat with sugar' /></td>

<td><faces:Output_Text id='grapefruit_price' value='$0.49/lb' /></td>

<td>
    <faces:SelectOne_OptionList id="grapefruitQuantity"
                   modelReference="${UserBean.items}"
                   selectedModelReference="${UserBean.grapefruitQuantity}">
        <faces:SelectOne_Option value="0" label="0"/>
        <faces:SelectOne_Option value="1" label="1"/>
        <faces:SelectOne_Option value="2" label="2"/>
        <faces:SelectOne_Option value="3" label="3"/>
        <faces:SelectOne_Option value="4" label="4"/>
        <faces:SelectOne_Option value="5" label="5"/>
        <faces:SelectOne_Option value="6" label="6"/>
        <faces:SelectOne_Option value="7" label="7"/>
        <faces:SelectOne_Option value="8" label="8"/>
        <faces:SelectOne_Option value="9" label="9"/>
    </faces:SelectOne_OptionList>
</td>
</tr>

<tr>
<td>
<center><img SRC="1005.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text id='grapes' value='grapes' /></td>

<td><faces:Output_Text id='grapes_desc' value='Purple grapes are all we carry' /></td>

<td><faces:Output_Text id='grapes_price' value='$0.79/lb' /></td>

<td>
    <faces:SelectOne_OptionList id="grapeQuantity"
                   modelReference="${UserBean.items}"
                   selectedModelReference="${UserBean.grapeQuantity}">
        <faces:SelectOne_Option value="0" label="0"/>
        <faces:SelectOne_Option value="1" label="1"/>
        <faces:SelectOne_Option value="2" label="2"/>
        <faces:SelectOne_Option value="3" label="3"/>
        <faces:SelectOne_Option value="4" label="4"/>
        <faces:SelectOne_Option value="5" label="5"/>
        <faces:SelectOne_Option value="6" label="6"/>
        <faces:SelectOne_Option value="7" label="7"/>
        <faces:SelectOne_Option value="8" label="8"/>
        <faces:SelectOne_Option value="9" label="9"/>
    </faces:SelectOne_OptionList>
</td>
</tr>
</table>

<br>&nbsp;
<br>
<faces:Command_Button id="checkout" label="Checkout" 
                      commandListener="handleCheckout" commandName="Checkout"/>
</faces:Form>
</body>
</html>
