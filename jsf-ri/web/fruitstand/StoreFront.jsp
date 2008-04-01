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
<form action="purchaseConfirm.html"
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

<td WIDTH="30">apple</td>

<td WIDTH="100">Crunchy and sweet</td>

<td>$0.29/lb</td>

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

<td>banana</td>

<td>Bananas smell great</td>

<td>$0.69/lb</td>

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

<td>cantaloupe</td>

<td>Honey dew melons are better</td>

<td>$0.19/lb</td>

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

<td>grapefruit</td>

<td>Don't eat with sugar</td>

<td>$0.49/lb</td>

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

<td>grapes</td>

<td>Purple grapes are all we carry</td>

<td>$0.79/lb</td>

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

<tr>
<td>
<center><img SRC="1006.jpg" height=64 width=89></center>
</td>

<td>kiwi</td>

<td>My friend Roy recommends these</td>

<td>$0.99/lb</td>

<td>
    <select name="kiwiQuantity">
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
<center><img SRC="1007.jpg" height=64 width=89></center>
</td>

<td>peach</td>

<td>Come in a can</td>

<td>$0.39/lb</td>

<td>
    <select name="peachQuantity">
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
<center><img SRC="1008.jpg" height=64 width=89></center>
</td>

<td>pear</td>

<td>Pears taste a little weird</td>

<td>$0.69/lb</td>

<td>
    <select name="pearQuantity">
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
<center><img SRC="1009.jpg" height=64 width=89></center>
</td>

<td>pineappple</td>

<td>Pineapple juice is a treat</td>

<td>$0.89/lb</td>

<td>
    <select name="pineappleQuantity">
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
<center><img SRC="1010.jpg" height=64 width=89></center>
</td>

<td>strawberries</td>

<td>Delectable and collectable</td>

<td>$0.69/lb</td>

<td>
    <select name="strawberriesQuantity">
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
<center><img SRC="1011.jpg" height=64 width=89></center>
</td>

<td>watermelon</td>

<td>The best fruit</td>

<td>$0.79/lb</td>

<td>
    <select name="watermelonQuantity">
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
