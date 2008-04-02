/*
 * $Id: MaxMinValidatorTag.java,v 1.11 2006/12/18 18:27:28 rlubke Exp $
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

// MaxMinValidatorTag.java

package com.sun.faces.taglib.jsf_core;

/**
 * <B>MaxMinValidatorTag</B> contains ivars for maximumSet and minimumSet.
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: MaxMinValidatorTag.java,v 1.11 2006/12/18 18:27:28 rlubke Exp $
 */

public abstract class MaxMinValidatorTag extends ValidatorTag {


    /**
     * <p>Flag indicating whether a maximum limit has been set.</p>
     */
    protected boolean maximumSet = false;

    /**
     * <p>Flag indicating whether a minimum limit has been set.</p>
     */
    protected boolean minimumSet = false;


} // end of class MaxMinValidatorTag
