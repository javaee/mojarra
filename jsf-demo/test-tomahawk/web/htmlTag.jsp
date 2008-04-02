<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<%@include file="inc/head.inc" %>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<body>

<f:view>

    <t:htmlTag value="h1"><b>h1</b> tag.</t:htmlTag>
    <t:htmlTag value="h2"><b>h2</b> tag.</t:htmlTag>
    <t:htmlTag value="p"><b>p</b> tag.</t:htmlTag>
    <t:htmlTag value="i"><b>i</b> tag.</t:htmlTag>

	<t:htmlTag value="p">
    	<t:htmlTag value="b" rendered="true">This is bold, because the <i>b</i> tag is rendered</t:htmlTag>
    </t:htmlTag>
    <t:htmlTag value="p">
    	<t:htmlTag value="b" rendered="false">This should not be bold, because the <i>b</i> tag is not rendered</t:htmlTag>
    </t:htmlTag>
</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
