/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package declarativeajax;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectMany;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>This bean has the methods that are used to illustrate
 *  the <code>JSF 2.0 declarative ajax</code>.  * <p/>
 *
 */

@ManagedBean(name="ajaxtagbean")
@SessionScoped
public class TagBean implements Serializable {
    
    private static final long serialVersionUID = 1962330230278633796L;

    private static final Logger LOGGER = Logger.getLogger("declarativeajax");
    
    //
    // Relationship Instance Variables
    //

    private Map<String,String> stateMap = null;

    private List stateOptions = null;

    // Status message to display in response to action events
    private List<StatusMessage> statusMessages= new ArrayList<StatusMessage>();

    //
    // Constructors
    //
    
    public TagBean() {
        stateMap = new HashMap<String,String>();
        stateMap.put("CA", "California");
        stateMap.put("CT", "Connecticut");
        stateMap.put("MA", "Massachusetts");
        stateMap.put("NJ", "New Jersey");
        stateMap.put("NY", "New York");

        stateOptions = new ArrayList();
        stateOptions.add(new SelectItem("CA","CA","CA"));
        stateOptions.add(new SelectItem("CT","CT","CT"));
        stateOptions.add(new SelectItem("MA","MA","MA"));
        stateOptions.add(new SelectItem("NJ","NJ","NJ"));
        stateOptions.add(new SelectItem("NY","NY","NY"));

    }

    public Collection getStateOptions() {
        return stateOptions;
    }


    public void setStateOptions(Collection newOptions) {
        stateOptions = new ArrayList(newOptions);
    }

    public void beanAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm)context.getViewRoot().findComponent("form1");
        UIInput name = (UIInput)form.findComponent("name");
        name.setValue("");
        UIInput address = (UIInput)form.findComponent("address");
        address.setValue("");
        UIInput city = (UIInput)form.findComponent("city");
        city.setValue("");
        UIInput zip = (UIInput)form.findComponent("zip");
        zip.setValue("");
    }

    public void displayState(ValueChangeEvent event) {
        String state = (String)((HtmlSelectOneMenu)event.getComponent()).getValue();
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm)context.getViewRoot().findComponent("form1");
        UIOutput output = (UIOutput)form.findComponent("stateout");
        output.setValue((String)stateMap.get(state));
    }

    public void displayText(ValueChangeEvent event) {
        String text = (String)((UIInput)event.getComponent()).getValue();
        String label = (String)((UIInput)event.getComponent()).getAttributes().get("label"); 
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm)context.getViewRoot().findComponent("form1");
        UIOutput output = null;
        if ((label!=null)&&label.equals("Name")) {
            output = (UIOutput)form.findComponent("nameout");
            output.setValue("You entered: "+text);
        } else if ((label!=null)&&label.equals("Address")) {
            output = (UIOutput)form.findComponent("addressout");
            output.setValue("You entered: "+text);
        } else if ((label!=null)&&label.equals("City")) {
            output = (UIOutput)form.findComponent("cityout");
            output.setValue("You entered: "+text);
        } else if ((label!=null)&&label.equals("Zip")) {
            output = (UIOutput)form.findComponent("zipout");
            output.setValue("You entered: "+text);
        }
    }

    public void displayRadio(ValueChangeEvent event) {
        String text = (String)((UIInput)event.getComponent()).getValue();
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm)context.getViewRoot().findComponent("form1");
        UIOutput output = null;
        if (text.equals("Java")) {
            output = (UIOutput)form.findComponent("softwareLang");
            output.setValue("Great choice!");
        } else if (text.equals("C#")) {
            output = (UIOutput)form.findComponent("softwareLang");
            output.setValue("Pretty similar to Java..");
        } else if (text.equals("C++")) {
            output = (UIOutput)form.findComponent("softwareLang");
            output.setValue("Great lower level OO language..");
        } else if (text.equals("C")) {
            output = (UIOutput)form.findComponent("softwareLang");
            output.setValue("When you need a nice device driver...");
        }
    }

    public void displayList(ValueChangeEvent event) {
        String text = (String)((UIInput)event.getComponent()).getValue();
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm)context.getViewRoot().findComponent("form1");
        UIOutput output = null;
        if (text.equals("10")) {
            output = (UIOutput)form.findComponent("out1");
            output.setValue("Really?");
        } else if (text.equals("8")) {
            output = (UIOutput)form.findComponent("out1");
            output.setValue("Are you sure?");
        } else if (text.equals("all")) {
            output = (UIOutput)form.findComponent("out1");
            output.setValue("Correct!");
        }
    }

    // Returns a Collection of ids for testing execute/render binding.
    public Collection<String> getMultipleIds() {
        return MULTIPLE_IDS;
    }

    // Used to test binding execute/render lists to String value expression
    public String getThisKeyword() {
        return "@this";
    }

    public void processBehavior(AjaxBehaviorEvent event) {
        addStatusMessage("AjaxBehaviorEvent");
    }

    public void processAction(ActionEvent event) {
        addStatusMessage("ActionEvent");
    }

    public void processValueChange(ValueChangeEvent event) {
        addStatusMessage("ValueChangeEvent");
    }

    public void addStatusMessage(String messageType) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext external = context.getExternalContext();
        Map<String, String> params = external.getRequestParameterMap();
        String source = params.get("javax.faces.source");
        String partialEvent = params.get("javax.faces.partial.event");
        String behaviorEvent = params.get("javax.faces.behavior.event");

        StringBuilder builder = new StringBuilder();
        builder.append("source='");
        builder.append(source);
        builder.append("', partial event='");
        builder.append(partialEvent);
        builder.append("', behavior event='");
        builder.append(behaviorEvent);
        builder.append("'");

        String messageDetails = builder.toString();

        String phase = context.getCurrentPhaseId().toString();

        StatusMessage message = new StatusMessage(statusMessages.size(),
                                                  messageType,
                                                  messageDetails,
                                                  phase);
        statusMessages.add(message);

        updateStatusTable(context);
    }

    public void resetStatusMessages(ActionEvent event) {

        statusMessages.clear();

        FacesContext context = FacesContext.getCurrentInstance();
        updateStatusTable(context);
    }

    public void updateStatusTable(FacesContext context) {
        PartialViewContext partial = context.getPartialViewContext();

        if (partial != null) {
            partial.getRenderIds().add("form1:statusTable");
        }
    }


    public List<StatusMessage> getStatusMessages() {
        return statusMessages;
    }

    public static class StatusMessage {

        private int count;
        private String type;
        private String details;
        private String phase;

        public StatusMessage(int count,
                             String type,
                             String details,
                             String phase) {
            this.count = count;
            this.type = type;
            this.details = details;
            this.phase = phase;
        }

        public int getCount() {
            return count;
        }

        public String getType() {
            return type;
        }

        public String getPhase() {
            return phase;
        }

        public String getDetails() {
            return details;
        }
    }

    // Some ids for testing execute/render binding
    private static Collection<String> MULTIPLE_IDS = 
        Arrays.asList("@this", "formKeyword", "thisKeyword", "noneKeyword");
}
