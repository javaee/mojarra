/*
 *
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 * 
 */

package imageMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * <p>This <code>HttpSessionListener</code> is a simulation of
 * business logic that would look up the hot spots of an image
 * map from a database, so that image maps could be rendered
 * dynamically.  In a real application, these values would probably
 * be stored in a list that is iterated over in the JSP page with
 * a JSTL <code>&lt;c:forEach&gt;</code> tag.</p>
 */
public class ImageAreaSetup implements HttpSessionListener {


    public ImageAreaSetup() {
         System.out.println("Created Listener");
    }

    public void sessionCreated(HttpSessionEvent event) {

        System.out.println("session created");
        HttpSession session = event.getSession();
        session.setAttribute("NA", new ImageArea("poly", "NAmericas", "6,15,6,28,2,30,6,34,13,28,17,28,25,35,25,44,37,45,45,46,45,48,48,49,48,44,60,35,55,21,48,16,6,15"));
        session.setAttribute("SA", new ImageArea("poly", "SAmericas", "29,44,49,71,49,91,54,91,73,63,57,52,46,51,39,45"));
        session.setAttribute("finA", new ImageArea("poly", "Finland", "97,17,94,21,97,29,102,29,101,18,97,17"));
        session.setAttribute("gerA", new ImageArea("poly", "Germany", "90,28,96,28,96,39,90,39,90,28"));
        session.setAttribute("fraA", new ImageArea("poly", "France", "84,31,84,39,90,39,90,30,84,31"));

    }
    public void sessionDestroyed(HttpSessionEvent event) {
    }


}
