/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
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


package carstore;

import components.components.AreaSelectedEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * <p>This is the main bean for the application.  It maintains a
 * <code>Map</code> of {@link CarBean} instances, keyed by model name,
 * and a <code>Map</code> of {@link CarCustomizer} instances, keyed by
 * package name.  The <code>CarBean</code> instances in the model
 * <code>Map</code> are accessed from several pages, as described
 * below.</p>
 *
 * <p>Several pages in the application use this bean as the target of
 * method reference and value reference expressions.</p>
 *
 * <ul>
 *
 * <li><p>The "chooseLocale" page uses <code>actionListener</code>
 * attributes to point to the {@link #chooseLocaleFromMap} and {@link
 * #chooseLocaleFromLink} methods.</p></li>
 *
 * <li><p>The "storeFront" page uses value binding expressions to pull
 * information about four of the known car models in the store.</p></li>
 *
 * <li><p>The "carDetail" page uses value binding expressions to pull
 * information about the currently chosen model.  It also uses the
 * <code>action</code> attribute to convey the user's package
 * choices.</p></li>
 *
 * <li><p>The "confirmChoices" page uses value binding expressions to
 * pull the user's choices from the currently chosen model.</p></li>
 *
 * </ul>
 */

public class CarStore extends Object {

    protected static final Log log = LogFactory.getLog(CarStore.class);

    static final String CARSTORE_PREFIX = "carstore";

    static final String DEFAULT_MODEL = "Jalopy";

    static final String DEFAULT_PACKAGE = "Custom";

    static final String DEFAULT_MODEL_PROPERTIES = CARSTORE_PREFIX +
        ".bundles." + DEFAULT_MODEL;

    static final String DEFAULT_PACKAGE_PROPERTIES = CARSTORE_PREFIX +
        ".bundles." + DEFAULT_PACKAGE;

    // 
    // Relationship Instance Variables
    // 

    /**
     * <p>The locales to be selected for each hotspot, keyed by the
     * alternate text for that area.</p>
     */
    private Map locales = null;

    /**
     * <p>The currently selected car model.</p>
     */

    private String currentModelName = DEFAULT_MODEL;

    /**
     * <p>The car models we offer.</p>
     *
     * <p>Keys: Strings that happen to correspond to the name of the
     * packages.</p>
     *
     * <p>Values: CarBean instances</p>
     */
    private Map carModels = null;

    /**
     * <p>Keys: Strings that happen to correspond to the name of the
     * Properties file for the car (without the package prefix).</p>
     *
     * <p>Values: CarBeanCustomizer instances</p>
     */

    private Map carCustomizers = null;


    public CarStore() {
        if (log.isDebugEnabled()) {
            log.debug("Creating main CarStore bean");
            log.debug("Populating locale map");
        }
        locales = new HashMap();
        locales.put("NAmerica", Locale.ENGLISH);
        locales.put("SAmerica", new Locale("es", "es"));
        locales.put("Germany", Locale.GERMAN);
        locales.put("France", Locale.FRENCH);
    }

    // 
    // ActionListener handlers
    //

    public void chooseLocaleFromMap(ActionEvent actionEvent) {
        AreaSelectedEvent event = (AreaSelectedEvent) actionEvent;
        String current = event.getMapComponent().getCurrent();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale((Locale) locales.get(current));
        resetMaps();
    }


    public void chooseLocaleFromLink(ActionEvent event) {
        String current = event.getComponent().getId();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale((Locale) locales.get(current));
        resetMaps();
    }


    private void resetMaps() {
        if (null != carModels) {
            carModels.clear();
            carModels = null;
        }
        if (null != carCustomizers) {
            carCustomizers.clear();
            carCustomizers = null;
        }
    }


    public void choosePackage(ActionEvent event) {
        String packageName = event.getComponent().getId();
        choosePackage(packageName);
    }


    public void choosePackage(String packageName) {
        CarCustomizer packageCustomizer =
            (CarCustomizer) carCustomizers.get(packageName);
        packageCustomizer.customizeCar(getCurrentModel());
        getCurrentModel().getCurrentPrice();

        // HERE IS WHERE WE UPDATE THE BUTTON STYLE!
        String curName;
        Iterator iter = carCustomizers.keySet().iterator();
        // go through all the available packages and set the button
        // style accordingly.
        while (iter.hasNext()) {
            curName = (String) iter.next();
            packageCustomizer = (CarCustomizer) carCustomizers.get(curName);
            if (curName.equals(packageName)) {
                packageCustomizer.setButtonStyle("package-selected");
            } else {
                packageCustomizer.setButtonStyle("package-unselected");
            }
        }
    }

    // 
    // action handlers
    // 
    
    public String storeFrontJalopyPressed() {
        setCurrentModelName("Jalopy");
        return "carDetail";
    }


    public String storeFrontRoadsterPressed() {
        setCurrentModelName("Roadster");
        return "carDetail";
    }


    public String storeFrontLuxuryPressed() {
        setCurrentModelName("Luxury");
        return "carDetail";
    }


    public String storeFrontSUVPressed() {
        setCurrentModelName("SUV");
        return "carDetail";
    }


    public String buyCurrentCar() {
        getCurrentModel().getCurrentPrice();
        return "confirmChoices";
    }

    //
    // Accessors
    // 

    public CarBean getCurrentModel() {
        CarBean result = (CarBean) carModels.get(getCurrentModelName());
        return result;
    }


    public Map getModels() {
        if (null == carModels) {
            carModels = new HashMap();
            if (log.isDebugEnabled()) {
                log.debug("Populating carModel map");
            }
            carModels.put(DEFAULT_MODEL,
                          new CarBean(DEFAULT_MODEL_PROPERTIES));
            carModels.put("Roadster",
                          new CarBean(CARSTORE_PREFIX + ".bundles.Roadster"));
            carModels.put("Luxury", new CarBean(CARSTORE_PREFIX +
                                                ".bundles.Luxury"));
            carModels.put("SUV", new CarBean(CARSTORE_PREFIX +
                                             ".bundles.SUV"));
        }

        return carModels;
    }


    public Map getCustomizers() {
        getModels();
        if (null == carCustomizers) {
            carCustomizers = new HashMap();
            if (log.isDebugEnabled()) {
                log.debug("Populating carCustomizers map");
            }
            carCustomizers.put("Custom", new CarCustomizer(CARSTORE_PREFIX +
                                                           ".bundles.Custom"));
            carCustomizers.put("Standard",
                               new CarCustomizer(CARSTORE_PREFIX +
                                                 ".bundles.Standard"));
            carCustomizers.put("Performance",
                               new CarCustomizer(CARSTORE_PREFIX +
                                                 ".bundles.Performance"));
            carCustomizers.put("Deluxe",
                               new CarCustomizer(CARSTORE_PREFIX +
                                                 ".bundles.Deluxe"));
            choosePackage("Custom");
        }
        return carCustomizers;
    }

	

    //
    // private methods
    // 

    private String getCurrentModelName() {
        return currentModelName;
    }


    private void setCurrentModelName(String newName) {
        currentModelName = newName;
    }

    // package private util methods

    static Class loadClass(String name,
                           Object fallbackClass) throws ClassNotFoundException {
        ClassLoader loader = getCurrentLoader(fallbackClass);
        return loader.loadClass(name);
    }


    static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }


}
