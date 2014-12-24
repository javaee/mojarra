package com.sun.faces.test.servlet30.ajax;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class AjaxScriptErrorBean implements Serializable {

    private Boolean renderedScript;

    public Boolean getRenderedScript() {
        return renderedScript;
    }

    public void setRenderedScript(Boolean renderedScript) {
        this.renderedScript = renderedScript;
    }

    public String getThrower() {
        throw new RuntimeException("exception");
    }
}
