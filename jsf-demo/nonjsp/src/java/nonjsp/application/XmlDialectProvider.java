/*
 * $Id: XmlDialectProvider.java,v 1.2 2005/08/22 22:09:22 ofung Exp $
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

// XmlDialectProvider.java

package nonjsp.application;

import org.apache.commons.digester.RuleSetBase;

/**
 * <B>XmlDialectProvider</B> encapsulates the Xml Dialect specific
 * logic required for creating a tree of UIComponent instances from an
 * Xml file. <P>
 *
 * @version $Id: XmlDialectProvider.java,v 1.2 2005/08/22 22:09:22 ofung Exp $
 */
public interface XmlDialectProvider {

    /**
     * @return the Digester rule set for use in this implementation
     */

    public RuleSetBase getRuleSet();


    /**
     * @return the file suffix for files of this Xml type.  For example
     *         ".xul" or ".uiml".
     */

    public String getSuffix();

} // end of interface XmlDialectProvider
