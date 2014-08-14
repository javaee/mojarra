/*
 * 
 */
package com.sun.faces.test.performance;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;

@ManagedBean(name = "issue2413Bean")
@RequestScoped
public class Issue2413Bean implements Serializable {

    private static final long serialVersionUID = 5862863998950684902L;
    private HtmlPanelGroup panelGroup;
    private ArrayList<String> data = new ArrayList<String>();

    @PostConstruct
    public void init() {
        for (int i = 0; i < 1000; i++) {
            data.add(String.valueOf(i));
        }
    }

    public HtmlPanelGroup getPanelGroup() {
        return panelGroup;
    }

    public void setPanelGroup(HtmlPanelGroup panelGroup) {
        this.panelGroup = panelGroup;
    }

    public void onAddComponent(ActionEvent event) {
        HtmlOutputText out = new HtmlOutputText();
        out.setValue("test - " + System.currentTimeMillis());
        getPanelGroup().getChildren().add(out);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
