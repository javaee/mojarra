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


package carstore;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.javaee.blueprints.components.ui.components.AreaSelectedEvent;

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

    private static final Logger LOGGER = Logger.getLogger("carstore");

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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating main CarStore bean");
            LOGGER.fine("Populating locale map");
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
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Populating carModel map");
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
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Populating carCustomizers map");
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
