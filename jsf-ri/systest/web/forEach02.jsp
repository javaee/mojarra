<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title></title>
  </head>

  <body>
    <h1></h1>

<%--
 * Copyright (c) 2004 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2004 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
--%>

<f:view>

<h:messages/>
<br>

<h:form               id="form">


   <table border="1" style="list-background"
        summary="Add books from the catalog to your shopping cart.">
       <tr><th>header</th>
       </tr>

       <c:forEach items="#{BooksBean.books}" var="book" varStatus="stat" >
          <tr styleClass="${(stat.index % 2) == 0 ? "list-row-event" : "list-row-odd"}">
               <td style="list-column-left">
               <h:commandLink action="null">
                       
                          <h:outputText id="bookTitle" value="#{book.title}"/>
                       
               </h:commandLink>
               </td>
          </tr>
       </c:forEach>
  </table>

</h:form>

</f:view>



    <hr>
    <address><a href="mailto:ed.burns@sun.com">Edward Burns</a></address>
<!-- Created: Tue Oct  4 13:39:02 EDT 2005 -->
<!-- hhmts start -->
Last modified: Tue Oct  4 14:02:27 EDT 2005
<!-- hhmts end -->
  </body>
</html>
