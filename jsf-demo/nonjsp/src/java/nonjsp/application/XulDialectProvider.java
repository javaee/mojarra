/*
 * $Id: XulDialectProvider.java,v 1.2 2005/08/22 22:09:22 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// XulDialectProvider.java

package nonjsp.application;


import org.apache.commons.digester.RuleSetBase;

/**
 * <B>XulDialectProvider</B> is a class that encapsulates the Xul Dialect
 * specific logic required for creating a tree of UIComponent instances
 * from an Xml file. <P>
 *
 * @version $Id: XulDialectProvider.java,v 1.2 2005/08/22 22:09:22 ofung Exp $
 */
public class XulDialectProvider extends Object implements XmlDialectProvider {

    //
    // Protected Constants
    //

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

    public XulDialectProvider() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from XmlDialectProvider
    //

    public RuleSetBase getRuleSet() {
        RuleSetBase result = null;

        result = new XmlXulRuleSet(new BuildComponentFromTagImpl());

        return result;
    }


    public String getSuffix() {
        return ".xul";
    }

} // end of class XulDialectProvider
