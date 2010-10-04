/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
