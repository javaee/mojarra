/*
 * $Id: EvaluatorBean.java,v 1.2.30.2 2007/04/27 21:28:11 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.systest;

import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.el.ValueBinding;

import java.util.Date;
import java.text.SimpleDateFormat;

public class EvaluatorBean extends Object {

    public EvaluatorBean() {}

    protected int reps = 30000;
    public int getReps() {
	return reps;
    }

    public void setReps(int newReps) {
	reps = newReps;
    }

    private long start = 0;
    private long end = 0;
    private SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SS");

    public String getElapsedTime() {
	long elapsedSeconds = end - start;
	end = start = 0;
	return formatter.format(new Date(elapsedSeconds));
    }

    public void doGet(ActionEvent event) {
	int i = 0;
	FacesContext context = FacesContext.getCurrentInstance();
	String id = event.getComponent().getId();
	// strip off the first character
	id = "i" + id.substring(1);
	// get the expression to evaluate
	UIOutput output = (UIOutput) context.getViewRoot().findComponent("form" + NamingContainer.SEPARATOR_CHAR +  id);
	String expression = "#{" + output.getValue() + "}";
	ValueBinding vb = 
	    context.getApplication().createValueBinding(expression);
	// if the user wants to show results
	if (showResults) {
	    // clear the buffer for the new results
	    results = new StringBuffer();
	}
	// evaluate it as a get, reps number of times 
	start = System.currentTimeMillis();
	for (i = 0; i < reps; i++) {
	    if (showResults) {
		results.append(vb.getValue(context) + "\n");
	    }
	    else {
		vb.getValue(context);
	    }
	}
	end = System.currentTimeMillis();
    }

    protected boolean showResults = false;
    public boolean isShowResults() {
	return showResults;
    }

    public void setShowResults(boolean newShowResults) {
	showResults = newShowResults;
    }

    protected String [] expressions;
    public String [] getExpressions() {
	return expressions;
    }

    public void setExpressions(String [] newExpressions) {
	expressions = newExpressions;
    }


    protected StringBuffer results = new StringBuffer();
    public String getResults() {
	return results.toString();
    }

    public void setResults(String newResults) {
	results = new StringBuffer(newResults);
    }
    

}
