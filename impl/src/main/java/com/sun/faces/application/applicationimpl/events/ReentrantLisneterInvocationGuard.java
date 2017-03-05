package com.sun.faces.application.applicationimpl.events;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;

public class ReentrantLisneterInvocationGuard {

    public boolean isGuardSet(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
        Boolean result;
        Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
        result = data.get(systemEventClass);

        return null == result ? false : result;
    }

    public void setGuard(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
        Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
        data.put(systemEventClass, TRUE);

    }

    public void clearGuard(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
        Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
        data.put(systemEventClass, FALSE);

    }

    private Map<Class<? extends SystemEvent>, Boolean> getDataStructure(FacesContext ctx) {
        Map<Class<? extends SystemEvent>, Boolean> result = null;
        Map<Object, Object> ctxMap = ctx.getAttributes();
        final String IS_PROCESSING_LISTENERS_KEY = "com.sun.faces.application.ApplicationImpl.IS_PROCESSING_LISTENERS";

        if (null == (result = (Map<Class<? extends SystemEvent>, Boolean>) ctxMap.get(IS_PROCESSING_LISTENERS_KEY))) {
            result = new HashMap<>(12);
            ctxMap.put(IS_PROCESSING_LISTENERS_KEY, result);
        }

        return result;
    }

}