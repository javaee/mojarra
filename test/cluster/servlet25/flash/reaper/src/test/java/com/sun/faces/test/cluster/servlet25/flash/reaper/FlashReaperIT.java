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

package com.sun.faces.test.cluster.servlet25.flash.reaper;

import com.sun.faces.test.util.ClusterUtils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.URL;
import java.net.URLConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import org.junit.Ignore;

/**
  *
 */
public class FlashReaperIT {

    private WebClient webClient;

    @Before
    public void setUp() {
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }


    // ------------------------------------------------------------ Test Methods


    @Test
    @Ignore
    public void testFlashesAreReaped() throws Exception {
        
        doTestFlashesAreReaped(0);
        doTestFlashesAreReaped(1);
                
    }
    
    public void doTestFlashesAreReaped(int instanceNumber) throws Exception {
        
        String [] baseUrls = ClusterUtils.getBaseUrls();

        URL makeZombie = new URL(baseUrls[instanceNumber] + "faces/flashReaper.xhtml");
        URLConnection zombieConnection;
        HtmlPage page;
        int numberOfReaps = 0, numberEntriesInInnerMap = 0;
        boolean didReap = false;


        for (int i = 0; i < 50; i++) {
            zombieConnection = makeZombie.openConnection();
            zombieConnection.getContent();
            zombieConnection.getInputStream().close();
            page = webClient.getPage(baseUrls[instanceNumber] + "faces/flashReaper.xhtml");

            numberEntriesInInnerMap = Integer.parseInt(page.asText().trim());
            // When we move across instance numbers as done in this test, the
            // entries from the first instance are never reaped because 
            // we are not interleaving requests to each instance number.
            // Instead we are making N requests to instance1 and then N
            // requests to instance2.  Therefore, consider the reaping
            // boundary based on the instance number
            if (numberEntriesInInnerMap <= 
                ((1+instanceNumber)*FlashReaperBean.NUMBER_OF_ZOMBIES)) {
                didReap = true;
                numberOfReaps++;
            }
        }

        assertTrue(didReap);
        assertTrue(2 < numberOfReaps);
    }
}
