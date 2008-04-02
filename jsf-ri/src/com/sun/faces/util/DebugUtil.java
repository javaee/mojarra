/*
 * $Id: DebugUtil.java,v 1.25 2005/03/10 20:29:46 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.util;

// DebugUtil.java

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.model.SelectItem;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * <B>DebugUtil</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 */

public class DebugUtil extends Object {

//
// Protected Constants
//

//
// Class Variables
//

    public static boolean keepWaiting = true;

    private static int curDepth = 0;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public DebugUtil() {
        super();
        // Util.parameterNonNull();
        this.init();
    }


    protected void init() {
        // super.init();
    }

//
// Class methods
//

    /**
     * Usage: <P>
     * <p/>
     * Place a call to this method in the earliest possible entry point of
     * your servlet app.  It will cause the app to enter into an infinite
     * loop, sleeping until the static var keepWaiting is set to false.  The
     * idea is that you attach your debugger to the servlet, then, set a
     * breakpont in this method.  When it is hit, you use the debugger to set
     * the keepWaiting class var to false.
     */

    public static void waitForDebugger() {
        while (keepWaiting) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("DebugUtil.waitForDebugger(): Exception: " +
                                   e.getMessage());
            }
        }
    }


    private static void indentPrintln(PrintStream out, String str) {
        int i = 0;

        // handle indentation
        for (i = 0; i < curDepth; i++) {
            out.print("  ");
        }
        out.print(str + "\n");
    }


    public static void printTree(UIComponent root, PrintStream out) {
        if (null == root) {
            return;
        }
        int i = 0;
        Object value = null;
    
/* PENDING
    indentPrintln(out, "===>Type:" + root.getComponentType());
*/
        indentPrintln(out, "id:" + root.getId());
        indentPrintln(out, "type:" + root.toString());

        Iterator items = null;
        SelectItem curItem = null;
        int j = 0;

        if (root instanceof javax.faces.component.UISelectOne) {
            items = Util.getSelectItems(null, root);
            indentPrintln(out, " {");
            while (items.hasNext()) {
                curItem = (SelectItem) items.next();
                indentPrintln(out, "\t value=" + curItem.getValue() +
                                   " label=" + curItem.getLabel() + " description=" +
                                   curItem.getDescription());
            }
            indentPrintln(out, " }");
        } else {
	    if (root instanceof ValueHolder) {
		value = ((ValueHolder)root).getValue();
	    }
            indentPrintln(out, "value= " + value);

            Iterator it = root.getAttributes().keySet().iterator();
            if (it != null) {
                while (it.hasNext()) {
                    String attrValue = null, attrName = (String) it.next();
                    Object attrObj = root.getAttributes().get(attrName);

                    if (!(attrValue instanceof String) && null != attrObj) {
                        // chop off the address since we don't want to print
                        // out anything that'll vary from invocation to
                        // invocation
                        attrValue = attrObj.toString();
                        int at = 0;
                        boolean doTruncate = false;
                        if (-1 == (at = attrValue.indexOf("$"))) {
                            if (-1 != (at = attrValue.indexOf("@"))) {
                                doTruncate = true;
                            }
                        } else {
                            doTruncate = true;
                        }

                        if (doTruncate) {
                            attrValue = attrValue.substring(0, at);
                        }
                    } else {
                        attrValue = (String) attrObj;
                    }

                    indentPrintln(out, "attr=" + attrName +
                                       " : " + attrValue);
                }
            }
        }

        curDepth++;
        Iterator it = root.getChildren().iterator();
	Iterator facets = root.getFacets().values().iterator();
	// print all the facets of this component
	while(facets.hasNext()) {
	    printTree((UIComponent) facets.next(), out);
	}
	// print all the children of this component
        while (it.hasNext()) {
            printTree((UIComponent) it.next(), out);
        }
        curDepth--;
    }


    public static void printTree(TreeStructure root, PrintStream out) {
        if (null == root) {
            return;
        }
        int i = 0;
        Object value = null;
    
/* PENDING
    indentPrintln(out, "===>Type:" + root.getComponentType());
*/
        indentPrintln(out, "id:" + root.id);
        indentPrintln(out, "type:" + root.className);

        Iterator items = null;
        SelectItem curItem = null;
        int j = 0;

        curDepth++;
        if (null != root.children) {
            Iterator it = root.children.iterator();
            while (it.hasNext()) {
                printTree((TreeStructure) it.next(), out);
            }
        }
        curDepth--;
    }

    public static void printTree(Object [] root, PrintStream out) {
        if (null == root) {
	    indentPrintln(out, "null");
            return;
        }
        int i = 0;
        Object value = null;
    
/* PENDING
    indentPrintln(out, "===>Type:" + root.getComponentType());
*/
	// drill down to the bottom of the first element in the array
	boolean foundBottom = false;
	Object state = null;
	Object [] myState = root;
	while (!foundBottom) {
	    state = myState[0];
	    foundBottom = !state.getClass().isArray();
	    if (!foundBottom) {
		myState = (Object []) state;
	    }
	}

        indentPrintln(out, "type:" + myState[8]);

        curDepth++;
	root = (Object []) root[1];
	for (i = 0; i < root.length; i++) {
	    printTree((Object []) root[i], out);
	}
        curDepth--;
    }
//
// General Methods
//


} // end of class DebugUtil
