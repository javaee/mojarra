/*
 * @(#)Options2.java  1.6 03/01/17
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

package com.sun.tdk.jcov.tools;

import java.util.Hashtable;
import java.util.Vector;
import java.io.StreamTokenizer;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Extend Options to allow options to be read from a file
 * 
 * @author ed burns
 */
public class Options2 extends Options {

    public Options2() {
    }

    public Options2(String[] valid_options) {
        super(valid_options);
    }

    public Options2(OptionDescr[] valid_options) {
	super(valid_options);
    }

    /**
     * Override superclass parse to support the -option_file argument.
     * This option points to a resolvable path to a file that contains
     * one option per line.
     */
    public boolean parse(String option) throws Exception {
	String opt_name = null, opt_val = null;
	if (null != option && option.startsWith("-option_file")) {
	    int delim_ind = option.indexOf(OPT_VAL_DELIM);
	    if (delim_ind >= 0) {
		opt_name = option.substring(1, delim_ind);
		opt_val  = option.substring(delim_ind + 1);
	    }	
	    // interpret the file to contain one option per line
	    BufferedReader file = new BufferedReader(new FileReader(opt_val));
	    String curLine = null;
	    while (null != (curLine = file.readLine())) {
		if (!super.parse(curLine)) {
		    return false;
		}
	    }
	} 
	else {
	    return super.parse(option);
	}
	return true;
    }
}
