<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
    <f:phaseListener binding="#{phaseListener.instance}"/>
    <html>
    <head>
        <title>f:phaseListener Tag</title>
    </head>

    <body>
    <h:form>

        <h2>About this test</h2>

        <p>The first time this page is visited, we'll only see output
            on the PhaseEvents below. Reason: The listener isn't added
            until the <code>phaseListener</code> tag executes, which is
            after the beforePhase event time. The outputText for the
            afterPhaseEvent executes before the afterPhase event.</p>

        <p>When the page is re-displayed any number of times by
            pressing the redisplay button below, we'll see the apply,
            process, update, invoke, and render phases on the
            beforePhaseEvent, and we'll see apply, process, update, and
            invoke on the afterPhaseEvent. The former is correct because
            it's impossible to see a restore-view event by using a view
            scoped listener. The latter is correct because we see
            everything but the after render event because the outputText's
            below render their output before the after event is sent.</p>

        <h2>Output from the PhaseListener</h2>

        <p>beforePhaseEvent: <h:outputText value="#{beforePhaseEvent}"/>.</p>

        <p>afterPhaseEvent: <h:outputText value="#{afterPhaseEvent}"/>.</p>

        <p><h:commandButton value="redisplay"/></p>

    </h:form>

    </body>
    </html>
</f:view>

