/*
 * $Id: CarDemoServletContextListener.java,v 1.12 2003/05/06 02:06:57 eburns Exp $
 */
/*
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
 */

package cardemo;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;

import com.sun.faces.context.MessageResourcesImpl;
import javax.faces.context.MessageResources;

import javax.faces.FactoryFinder;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import components.model.ImageArea;
import components.renderkit.AreaRenderer;

/**
 *
 *  <B>CarDemoServletContextListener</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *

 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CarDemoServletContextListener implements ServletContextListener
{
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

    public CarDemoServletContextListener()
    {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from ServletContextListener
    //

    public void contextInitialized(ServletContextEvent e) 
    {
        // following is  a simulation of business logic that would 
        // look up the hot spots of an image map from a 
        // map from a database, so that image maps could be rendered
        // dynamically.  In a real application, these values would probably
        // be stored in a list that is iterated over in the JSP page with
        // a JSTL forEachtag.
        e.getServletContext().setAttribute("NA", new ImageArea("poly", "NAmericas", "53,109,1,110,2,167,19,168,52,149,67,164,67,165,68,167,70,168,72,170,74,172,75,174,77,175,79,177,81,179,80,179,77,179,81,179,81,178,80,178,82,211,28,238,15,233,15,242,31,252,36,247,36,246,32,239,89,209,92,216,93,216,100,216,103,218,113,217,116,224,124,221,128,230,163,234,185,189,178,177,162,188,143,173,79,173,73,163,79,157,64,142,54,139,53,109")); 

        e.getServletContext().setAttribute("SA", new ImageArea("poly", "SAmericas", "89,217,95,228,100,234,107,239,109,240,109,237,106,231,110,234,113,240,115,246,118,250,125,254,131,256,135,255,140,257,146,263,151,269,157,271,160,273,159,279,157,287,156,294,163,315,168,324,172,329,173,338,172,351,169,368,168,379,167,388,165,399,165,408,170,415,177,420,183,420,183,415,180,408,180,405,186,400,186,395,186,391,188,388,190,384,193,382,196,379,199,377,201,374,201,371,201,366,197,362,197,358,198,354,199,351,199,347,197,344,196,340,194,337,191,335,191,332,194,332,201,332,199,326,193,317,184,310,180,309,179,311,177,314,175,312,174,308,172,305,170,304,171,302,174,298,177,296,177,290,177,288,180,287,183,287,186,287,187,284,188,280,190,279,192,278,191,282,191,286,194,288,196,288,199,286,201,285,204,285,206,285,208,285,206,280,205,279,205,276,205,274,197,270,194,267,191,265,186,262,182,262,177,261,173,261,167,261,165,265,163,266,159,265,157,263,157,260,153,253,149,251,147,251,149,247,149,244,148,240,144,238,141,238,139,241,138,244,137,245,134,246,131,245,130,242,130,238,130,236,130,233,129,230,125,225,123,221,119,221,118,223,117,220,113,219,104,217,101,215,96,215")); 

        e.getServletContext().setAttribute("gerA", new ImageArea("poly", "Germany", "324,163,323,170,322,172,320,173,320,175,321,176,321,177,321,179,321,180,319,180,318,180,317,182,316,183,315,181,314,181,313,181,312,181,311,180,310,177,310,177,307,176,307,173,307,172,308,170,309,169,309,167,309,166,311,165,311,163,311,161,312,159,314,159,316,160,316,162,318,162,319,162"));

        e.getServletContext().setAttribute("fraA", new ImageArea("poly", "France", "312,178,308,182,310,184,310,187,310,189,309,191,307,192,305,192,304,192,304,193,303,195,302,195,300,194,299,194,297,194,295,193,295,191,295,189,294,186,293,184,292,182,291,181,289,180,288,178,288,176,289,175,292,175,293,176,294,174,296,174,297,174,299,174,299,172,300,170,302,170,304,172,306,173,308,173,310,174"));  
      
    }

    public void contextDestroyed(ServletContextEvent e)
    {  
        e.getServletContext().removeAttribute("NA");
        e.getServletContext().removeAttribute("SA");
        e.getServletContext().removeAttribute("gerA");
        e.getServletContext().removeAttribute("fraA"); 
    }

} // end of class CarDemoServletContextListener
