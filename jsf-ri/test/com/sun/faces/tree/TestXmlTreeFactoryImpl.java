/*
 * $Id: TestXmlTreeFactoryImpl.java,v 1.1 2002/05/30 01:42:09 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestXmlTreeFactoryImpl.java

package com.sun.faces.tree;

import com.sun.faces.util.DebugUtil;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import org.apache.cactus.ServletTestCase;

import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import java.util.Iterator;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import com.sun.faces.CompareFiles;

/**
 *
 *  <B>TestXmlTreeFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestXmlTreeFactoryImpl.java,v 1.1 2002/05/30 01:42:09 eburns Exp $
 * 
 * @see	com.sun.faces.tree.XULTreeFactoryImpl
 *
 */

public class TestXmlTreeFactoryImpl extends ServletTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";

public static final String PATH_ROOT = "./build/test/servers/tomcat40/webapps/test/";

public static final String OUTPUT_FILENAME = PATH_ROOT +
    "Faces_Basic_tree";

public static final String CORRECT_XUL_FILENAME = PATH_ROOT +
    "Faces_Basic_correct_xul_tree";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public TestXmlTreeFactoryImpl() {super("TestXmlTreeFactoryImpl");}
public TestXmlTreeFactoryImpl(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

   
public void testIds()
{
    TreeFactory factory = new XmlTreeFactoryImpl();
    Iterator treeIds = null;
    String curId = null;
    assertTrue(null != factory);

    assertTrue(null != (treeIds = 
			factory.getTreeIds(config.getServletContext())));
    while (treeIds.hasNext()) {
	curId = (String) treeIds.next();
	assertTrue(-1 != curId.indexOf(TEST_URI_XUL));
    }
}

public void testCreate()
{
    Tree tree = null;
    TreeFactory factory = new XmlTreeFactoryImpl();
    UIComponent root = null;
    PrintStream out = null;
    File file = null;
    FileOutputStream fs = null;

    tree = factory.createTree(config.getServletContext(), TEST_URI_XUL);
    assertTrue(null != tree);

    root = tree.getRoot();
    assertTrue(null != root);

    try {
        file = new File ( OUTPUT_FILENAME );
	fs = new FileOutputStream(file);
        out = new PrintStream(fs);
    } catch ( Exception e ) {
        assertTrue(false);
    }

    DebugUtil.printTree(root, out);
    out.close();
    
    try {
	CompareFiles cf = new CompareFiles();
	assertTrue(cf.filesIdentical(OUTPUT_FILENAME, CORRECT_XUL_FILENAME, 
				     null));
    } catch (Throwable e ) {
	System.out.println("Throwable: " + e.getMessage());
	assertTrue(false);
    }
    
    

}

public void testCreateNull()
{
    Tree tree = null;
    TreeFactory factory = new XmlTreeFactoryImpl();
    UIComponent root = null;

    tree = factory.createTree(config.getServletContext(), null);
    assertTrue(null != tree);

    root = tree.getRoot();
    assertTrue(null != root);

    DebugUtil.printTree(root, System.out);
}

public void testCreateBogus()
{
    Tree tree = null;
    TreeFactory factory = new XmlTreeFactoryImpl();
    UIComponent root = null;
    boolean exceptionThrown = false;

    try {
	tree = factory.createTree(config.getServletContext(), "aoeuaoeuaoeu");
    }
    catch (FacesException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
}



} // end of class TestXmlTreeFactoryImpl
