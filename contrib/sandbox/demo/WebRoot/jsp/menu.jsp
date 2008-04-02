<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb"%>
<html>
    <f:view>
        <risb:menu value="#{testBean.menu}" width="225px" />
        <br />
<div id="testMenu" style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1</li>
</ul>
</div>
</div>
</li>
</ul>
</div>
</div>
</li>
</ul>
</div>
</div>
</li>

<li class="yuimenuitem"><a href="http://foo.com/1">Menu Item #2<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1</li>
</ul>
</div>
</div>
</li>
</ul>
</div>
</div>
</li>
</ul>
</div>
</div>
</li>

<li class="yuimenuitem"><a href="http://foo.com/2">Menu Item #3<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1<div style="padding: 0px;" class="yuimenu">
<div class="bd">
<ul class="first-of-type">
<li class="yuimenuitem"><a href="http://foo.com/0">Menu Item #1</li>
</ul>
</div>
</div>
</li>
</ul>
</div>
</div>
</li>
</ul>
</div>
</div>
</li>
</ul>
</div>
</div>
        <script type="text/javascript">
            new YUISF.Menu("testMenu", {width: "225px", clicktohide: false, visible: true});
        </script>
    </f:view>
</html>
