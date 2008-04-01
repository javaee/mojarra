<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <meta name="Author" content="Roger Kitain">
   <meta name="GENERATOR" content="Mozilla/4.75 [en] (WinNT; U) [Netscape]">
   <title>StoreFront</title>
</head>
<%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
<body>
<faces:useFaces>

<form action="purchaseConfirm.jsp"
            method='post'>
<font color="#3333FF"><font size=+2>Welcome to FruitStand.com</font></font>
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

<td WIDTH="30"><faces:Output_Text name='apple_name' value='apple' /></td>

<td WIDTH="100"><faces:Output_Text name='apple_desc' value='Crunchy and sweet' /></td>

<td><faces:Output_Text name='apple_price' value='0.29' /></td>

<td>
    <select name="appleQuantity">
        <option value="0" selected>0.00</option>
        <option value="1">1.00</option>
        <option value="2">2.00</option>
        <option value="3">3.00</option>
        <option value="4">4.00</option>
        <option value="5">5.00</option>
        <option value="6">6.00</option>
        <option value="7">7.00</option>
        <option value="8">8.00</option>
        <option value="9">9.00</option>
        <option value="10">10.00</option>
    </select>
</td>
</tr>

<tr>
<td>
<center><img SRC="1002.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text name='banana_name' value='banana' /></td>

<td><faces:Output_Text name='banana_desc' value='Bananas smell great' /></td>

<td><faces:Output_Text name='banana_price' value='$0.69/lb' /></td>

<td>
    <select name="bananaQuantity">
        <option value="0" selected>0.00</option>
        <option value="1">1.00</option>
        <option value="2">2.00</option>
        <option value="3">3.00</option>
        <option value="4">4.00</option>
        <option value="5">5.00</option>
        <option value="6">6.00</option>
        <option value="7">7.00</option>
        <option value="8">8.00</option>
        <option value="9">9.00</option>
        <option value="10">10.00</option>
    </select>
</td>
</tr>

<tr>
<td>
<center><img SRC="1003.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text name='cantaloupe_name' value='cantaloupe' /></td>

<td><faces:Output_Text name='cantaloupe_desc' value='Honey dew melons are better' /></td>

<td><faces:Output_Text name='cantaloupe_price' value='$0.19/lb' /></td>

<td>
    <select name="cantaloupeQuantity">
        <option value="0" selected>0.00</option>
        <option value="1">1.00</option>
        <option value="2">2.00</option>
        <option value="3">3.00</option>
        <option value="4">4.00</option>
        <option value="5">5.00</option>
        <option value="6">6.00</option>
        <option value="7">7.00</option>
        <option value="8">8.00</option>
        <option value="9">9.00</option>
        <option value="10">10.00</option>
    </select>
</td>
</tr>

<tr>
<td>
<center><img SRC="1004.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text name='grapefruit_name' value='grapefruit' /></td>

<td><faces:Output_Text name='grapefruit_desc' value='Do not eat with sugar' /></td>

<td><faces:Output_Text name='grapefruit_price' value='$0.49/lb' /></td>

<td>
    <select name="grapefruitQuantity">
        <option value="0" selected>0.00</option>
        <option value="1">1.00</option>
        <option value="2">2.00</option>
        <option value="3">3.00</option>
        <option value="4">4.00</option>
        <option value="5">5.00</option>
        <option value="6">6.00</option>
        <option value="7">7.00</option>
        <option value="8">8.00</option>
        <option value="9">9.00</option>
        <option value="10">10.00</option>
    </select>
</td>
</tr>

<tr>
<td>
<center><img SRC="1005.jpg" height=64 width=89></center>
</td>

<td><faces:Output_Text name='grapes' value='grapes' /></td>

<td><faces:Output_Text name='grapes_desc' value='Purple grapes are all we carry' /></td>

<td><faces:Output_Text name='grapes_price' value='$0.79/lb' /></td>

<td>
    <select name="grapeQuantity">
        <option value="0" selected>0.00</option>
        <option value="1">1.00</option>
        <option value="2">2.00</option>
        <option value="3">3.00</option>
        <option value="4">4.00</option>
        <option value="5">5.00</option>
        <option value="6">6.00</option>
        <option value="7">7.00</option>
        <option value="8">8.00</option>
        <option value="9">9.00</option>
        <option value="10">10.00</option>
    </select>
</td>
</tr>
</table>

<br>&nbsp;
<br>
<faces:command_button name="checkout"
    label="Checkout" />
</form>
</faces:useFaces>
</body>
</html>
