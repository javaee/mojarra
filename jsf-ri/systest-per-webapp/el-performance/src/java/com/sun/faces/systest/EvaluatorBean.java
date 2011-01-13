/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.component.UIComponent;
import javax.el.ValueExpression;

import java.util.Date;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.BitSet;
import java.text.SimpleDateFormat;

import java.io.PrintStream;

import com.sun.faces.util.MultiThreadTestRunner;

public class EvaluatorBean extends Object {

    public EvaluatorBean() {
	random = new Random(32714);
    }

    Random random = null;

    protected int reps = 30000;
    public int getReps() {
	return reps;
    }

    public void setReps(int newReps) {
	reps = newReps;
    }

    protected int numThreads = 1;
    public int getNumThreads() {
	return numThreads;
    }

    public void setNumThreads(int newNumThreads) {
	numThreads = newNumThreads;
    }

    protected Object [] threadOutcomes;
    public Object [] getThreadOutcomes() {
	return threadOutcomes;
    }

    public void setThreadOutcomes(Object [] newThreadOutcomes) {
	threadOutcomes = newThreadOutcomes;
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
	ValueExpression vb = 
	    context.getApplication().getExpressionFactory().
            createValueExpression(context.getELContext(),expression, Object.class);
	// if the user wants to show results
	if (showResults) {
	    // clear the buffer for the new results
	    results = new StringBuffer();
	}
	// evaluate it as a get, reps number of times 
	start = System.currentTimeMillis();
	for (i = 0; i < reps; i++) {
	    if (showResults) {
		results.append(vb.getValue(context.getELContext()) + "\n");
	    }
	    else {
		vb.getValue(context.getELContext());
	    }
	}
	end = System.currentTimeMillis();
    }

    public void doAttributeMapGet(ActionEvent event) throws Exception {
	int i = 0;
	FacesContext context = FacesContext.getCurrentInstance();

	// Create numThreads Threads passing in a new UIComponent with an
	// attribute "foo" value "bar" to the ctor.

	Thread threads[] = new Thread[numThreads];
	UIComponent curInput = null;
	threadOutcomes = new Object[numThreads];
	Runnable runnable = null;

	// initialize threads array
	for (i = 0; i < numThreads; i++) {
	    curInput = 
		context.getApplication().createComponent("javax.faces.Input");
	    curInput.getAttributes().put("foo", "bar");
	    runnable = new AttributeGetRunnable(curInput, getReps(), "foo", 
						random, i, 
						true,
						System.out,
						this);

	    threads[i] = new Thread(runnable, "TestThread" + i);
	}

	MultiThreadTestRunner runner = 
	    new MultiThreadTestRunner(threads, threadOutcomes);

	// if the user wants to show results
	if (showResults) {
	    // clear the buffer for the new results
	    results = new StringBuffer();
	}
	// evaluate it as a get, reps number of times 
	start = System.currentTimeMillis();
	boolean foundFailedThread = false;
	foundFailedThread = runner.runThreadsAndOutputResults(System.out);
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

    // Each Thread has an index assigned to it and does reps get()
    // operations on the attribute map for key, pausing for a Random
    // amount of millis between each get.  EvaluatorBean maintains an
    // Object array property threadOutcomes of length numThreads that
    // stores the outcomes of the Threads.  Each Thread writes its
    // outcome to the entry at the Thread's index into the
    // threadOutcomes array.  If an exception is thrown, it is written
    // to the array.  If the Thread executes successfully, a success
    // message is written to the threadOutcomes array.

    public static class AttributeGetRunnable extends Object implements Runnable {
	UIComponent component = null;
	int reps = 1;
	Object key = null;
	Random random = null;
	boolean sleepBetweenGets = true;
	int index = 0;
	EvaluatorBean host = null;
	PrintStream out = null;
	
	public AttributeGetRunnable(UIComponent component, int reps, 
				    Object key, Random random, int index, 
				    boolean sleepBetweenGets,
				    PrintStream out,
				    EvaluatorBean host) {
	    this.component = component;
	    this.reps = reps;
	    this.key = key;
	    this.random = random;
	    this.index = index;
	    this.sleepBetweenGets = sleepBetweenGets;
	    this.out = out;
	    this.host = host;
	}

	public void run() {
	    String name = Thread.currentThread().getName();
	    
	    for (int i = 0; i < reps; i++) {
		try {
		    component.getAttributes().get(key);
		}
		catch (Exception e) {
		    host.getThreadOutcomes()[index] = e;
		    System.out.println("index: " + index + " exception on get(): " + e.getMessage());
		    return;
		}   
		
		if (sleepBetweenGets) {
		    try {
			Thread.sleep(0L, Math.abs(random.nextInt()) % 100);
		    }
		    catch (InterruptedException e) {
			host.getThreadOutcomes()[index] = e;
			return;
		    }
		}
	    }
	    
	    host.getThreadOutcomes()[index] = name + " executed successfully.";

	}
    }

}
