/*
 * $Id: XmlDialectProvider.java,v 1.1 2003/02/13 23:34:26 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// XmlDialectProvider.java

package nonjsp.tree;

import org.apache.commons.digester.RuleSetBase;

/**
 *  <B>XmlDialectProvider</B> encapsulates the Xml Dialect specific
 *  logic required for creating a tree of UIComponent instances from an
 *  Xml file. <P>
 *
 * Copy of com.sun.faces.tree.XmlDialectProvider in order to remove
 * demo dependancy on RI.
 *
 *
 * <B>Lifetime And Scope</B> <P>
 * Same as XmlTreeFactoryImpl.
 *
 * @version $Id: XmlDialectProvider.java,v 1.1 2003/02/13 23:34:26 horwat Exp $
 * 
 * @see	com.sun.faces.tree.XmlDialectProvider
 * @see	com.sun.faces.tree.XmlTreeFactoryImpl
 *
 */

public interface XmlDialectProvider
{

/**

* @return the Digester rule set for use in this implementation

*/

public RuleSetBase getRuleSet();

/**

* @return the file suffix for files of this Xml type.  For example
* ".xul" or ".uiml".

*/

public String getSuffix();

} // end of interface XmlDialectProvider
