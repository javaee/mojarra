/*
 * $Id: ConfigProperty.java,v 1.5 2005/08/22 22:08:31 ofung Exp $
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

package javax.faces.webapp;


/**
 * <p>Config Bean for an Property.</p>
 */
public class ConfigProperty extends ConfigFeature {

    private String propertyClass;
    public String getPropertyClass() {
        return (this.propertyClass);
    }
    public void setPropertyClass(String propertyClass) {
        this.propertyClass = propertyClass;
    }

    private String propertyName;
    public String getPropertyName() {
        return (this.propertyName);
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
