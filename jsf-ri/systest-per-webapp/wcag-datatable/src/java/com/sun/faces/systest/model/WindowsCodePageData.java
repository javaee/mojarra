/*
 * WindowsCodePageData.java
 *
 * Created on September 5, 2006, 10:11 PM
 *
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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edburns
 */
public class WindowsCodePageData {
    
    /** Creates a new instance of WindowsCodePageData */
    public WindowsCodePageData() {
        codePageData = new ArrayList<WindowsCodePageDataBean>();
        WindowsCodePageDataBean bean = null;
        bean = new WindowsCodePageDataBean("1200", "Unicode (BMP of ISO 10646)",
                false, false, true, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1251", "Windows 3.1 Cyrillic",
                true, false, true, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1250", "Windows 3.1 Eastern European",
                true, false, true, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1252", "Windows 3.1 US (ANSI)",
                true, false, true, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1253", "Windows 3.1 Greek",
                true, false, true, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1254", "Windows 3.1 Turkish",
                true, false, true, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1255", "Hebrew",
                true, false, false, false, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1256", "Arabic",
                true, false, false, false, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1257", "Baltic",
                true, false, false, false, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("1361", "Korean (Johab)",
                true, false, false, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("437", "MS-DOS United States",
                false, true, true, true, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("708", "Arabic (ASMO 708)",
                false, true, false, false, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("709", "Arabic (ASMO 449+, BCON V4)",
                false, true, false, false, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("710", "Arabic (Transparent Arabic)",
                false, true, false, false, true);
        codePageData.add(bean);
        bean = new WindowsCodePageDataBean("720", "Arabic (Transparent ASMO)",
                false, true, false, false, true);
        codePageData.add(bean);
    }
    
    private List<WindowsCodePageDataBean> codePageData = null;
    
    public List<WindowsCodePageDataBean> getCodePageData() {
        return codePageData;
    }
    
}
