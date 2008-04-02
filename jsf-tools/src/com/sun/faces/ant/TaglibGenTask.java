/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.ant;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.taskdefs.Java;

public class TaglibGenTask extends Java {

    private Map arguments;

    public TaglibGenTask() {
	arguments = new HashMap();
    }
    
    public void setInputFile(String newInputFile) {
	arguments.put("--config", newInputFile);
    }

    public void setDestdir(String newDir) {
	arguments.put("--dir", newDir);
    }

    public void setTlddir(String newTlddir) {
	arguments.put("--tlddir", newTlddir);
    }

    public void setDtd(String newDtd) {
	arguments.put("--dtd", newDtd);
    }

    public void setTagdef(String newTagdef) {
	arguments.put("--tagdef", newTagdef);
    }

    public void setCopyright(String newCopyright) {
	arguments.put("--copyright", newCopyright);
    }
    
    public void execute() throws BuildException {
	Iterator iter = null;
	Object curObj = null;
	Commandline.Argument arg = null;

	iter = arguments.keySet().iterator();
	while (iter.hasNext()) {
	    curObj = iter.next();

	    arg = super.createArg();
	    arg.setValue(curObj.toString());

	    arg = super.createArg();
	    arg.setValue(arguments.get(curObj).toString());
	}

	super.setClassname("com.sun.faces.generate.HtmlTaglibGenerator");

	super.execute();
    }


}
